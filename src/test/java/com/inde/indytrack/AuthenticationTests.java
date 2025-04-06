package com.inde.indytrack;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inde.indytrack.repository.StudentRepository;
import com.inde.indytrack.repository.AdminRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthenticationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    private final String EXISTING_STUDENT_EMAIL = "tyrion.lannister@mail.univ.ca";
    private final String EXISTING_STUDENT_PASSWORD = "password1234";

    private final String EXISTING_ADMIN_EMAIL = "petyr.baelish@mail.univ.ca";
    private final String EXISTING_ADMIN_PASSWORD = "password1234";

    private final String NEW_STUDENT_EMAIL = "student@mail.univ.ca";
    private final String NEW_STUDENT_PASSWORD = "password12345";

    private final String NEW_ADMIN_EMAIL = "admin@mail.univ.ca";
    private final String NEW_ADMIN_PASSWORD = "password12345";

    @Test
    @Transactional
    void registerStudent() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Student");
        registerJson.put("email", NEW_STUDENT_EMAIL);
        registerJson.put("password", NEW_STUDENT_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/student")
            .contentType("application/json")
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(studentRepository.existsByEmail(NEW_STUDENT_EMAIL));
    }

    @Test
    @Transactional
    void registerStudentWithExistingEmail() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Student");
        registerJson.put("email", EXISTING_STUDENT_EMAIL);
        registerJson.put("password", NEW_STUDENT_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/student")
            .contentType("application/json")
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Transactional
    void registerAdmin() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Admin");
        registerJson.put("email", NEW_ADMIN_EMAIL);
        registerJson.put("password", NEW_ADMIN_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/admin")
            .contentType("application/json")
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(adminRepository.existsByEmail(NEW_ADMIN_EMAIL));
    }

    @Test
    @Transactional
    void registerAdminWithExistingEmail() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Admin");
        registerJson.put("email", EXISTING_ADMIN_EMAIL);
        registerJson.put("password", NEW_ADMIN_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/admin")
            .contentType("application/json")
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Transactional
    void studentLogin() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_STUDENT_EMAIL);
        loginJson.put("password", EXISTING_STUDENT_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/student")
            .contentType("application/json")
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_STUDENT_EMAIL, receivedJson.get("email").asText());
    }

    @Test
    @Transactional
    void studentLoginWithInvalidEmail() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", NEW_STUDENT_EMAIL);
        loginJson.put("password", EXISTING_STUDENT_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/student")
            .contentType("application/json")
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void studentLoginWithInvalidPassword() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_STUDENT_EMAIL);
        loginJson.put("password", NEW_STUDENT_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/student")
            .contentType("application/json")
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    @Transactional
    void adminLogin() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_ADMIN_EMAIL);
        loginJson.put("password", EXISTING_ADMIN_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/admin")
            .contentType("application/json")
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_ADMIN_EMAIL, receivedJson.get("email").asText());
    }

    @Test
    @Transactional
    void adminLoginWithInvalidEmail() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", "nonexistent@mail.univ.ca");
        loginJson.put("password", NEW_ADMIN_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/admin")
            .contentType("application/json")
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void adminLoginWithInvalidPassword() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_ADMIN_EMAIL);
        loginJson.put("password", "wrongpassword");

        MockHttpServletResponse response = mockMvc.perform(post("/login/admin")
            .contentType("application/json")
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
}
