package com.inde.indytrack;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
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

    private final String TEST_EMAIL = "test@mail.univ.ca";
    private final String TEST_PASSWORD = "password123";
    private final String EXISTING_STUDENT_EMAIL = "tyrion.lannister@mail.univ.ca";
    private final String EXISTING_ADMIN_EMAIL = "admin@mail.univ.ca";

    @Test
    @Transactional
    void registerStudent() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Student");
        registerJson.put("email", TEST_EMAIL);
        registerJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/student")
            .contentType(MediaType.APPLICATION_JSON)
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(studentRepository.existsByEmail(TEST_EMAIL));
    }

    @Test
    @Transactional
    void registerStudentWithExistingEmail() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Student");
        registerJson.put("email", EXISTING_STUDENT_EMAIL);
        registerJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/student")
            .contentType(MediaType.APPLICATION_JSON)
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
        registerJson.put("email", TEST_EMAIL);
        registerJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(adminRepository.existsByEmail(TEST_EMAIL));
    }

    @Test
    @Transactional
    void registerAdminWithExistingEmail() throws Exception {
        ObjectNode registerJson = objectMapper.createObjectNode();
        registerJson.put("firstName", "Test");
        registerJson.put("lastName", "Admin");
        registerJson.put("email", EXISTING_ADMIN_EMAIL);
        registerJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(registerJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void studentLogin() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_STUDENT_EMAIL);
        loginJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/student")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_STUDENT_EMAIL, receivedJson.get("email").asText());
    }

    @Test
    void studentLoginWithInvalidEmail() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", "nonexistent@mail.univ.ca");
        loginJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/student")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void studentLoginWithInvalidPassword() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_STUDENT_EMAIL);
        loginJson.put("password", "wrongpassword");

        MockHttpServletResponse response = mockMvc.perform(post("/login/student")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    void adminLogin() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_ADMIN_EMAIL);
        loginJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_ADMIN_EMAIL, receivedJson.get("email").asText());
    }

    @Test
    void adminLoginWithInvalidEmail() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", "nonexistent@mail.univ.ca");
        loginJson.put("password", TEST_PASSWORD);

        MockHttpServletResponse response = mockMvc.perform(post("/login/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void adminLoginWithInvalidPassword() throws Exception {
        ObjectNode loginJson = objectMapper.createObjectNode();
        loginJson.put("email", EXISTING_ADMIN_EMAIL);
        loginJson.put("password", "wrongpassword");

        MockHttpServletResponse response = mockMvc.perform(post("/login/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }
}
