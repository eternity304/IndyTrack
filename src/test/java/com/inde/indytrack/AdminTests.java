package com.inde.indytrack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inde.indytrack.model.Admin;
import com.inde.indytrack.repository.AdminRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AdminTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;
    
     // Constants from data.sql
     private final Long EXISTING_ADMIN_ID = 7777L; // Petyr Baelish
 
     // ID for the admin to be created/deleted in tests
     private final Long NEW_ADMIN_ID = 8888L;

     @BeforeEach
     void setUp() {
         // Ensure the test admin does not exist before each test that might create it
         if (adminRepository.existsById(NEW_ADMIN_ID)) {
            adminRepository.deleteById(NEW_ADMIN_ID);
         }
     }

     @Test
     void retrieveAllAdmins() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/admins"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ArrayNode receivedJson = (ArrayNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(receivedJson.get(1).get("id").asLong(), EXISTING_ADMIN_ID);
     }


     @Test
     void retrieveExistingAdmin() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/admins/" + EXISTING_ADMIN_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(EXISTING_ADMIN_ID, receivedJson.get("id").longValue());
        assertEquals("Petyr", receivedJson.get("firstName").textValue());
        assertEquals("Baelish", receivedJson.get("lastName").textValue());
        assertEquals("petyr.baelish@mail.univ.ca", receivedJson.get("email").textValue());
     }

     @Test
     void retrieveNonExistingAdmin() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/admins/9999"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
     }
     
     @Test
     @Transactional // Roll back the admin creation after the test
     void createAdmin() throws Exception {
        ObjectNode adminJson = objectMapper.createObjectNode();
        adminJson.put("firstName", "John");
        adminJson.put("lastName", "Doe");
        adminJson.put("email", "john.doe@mail.univ.ca");
        adminJson.put("password", "password1234");

        MockHttpServletResponse response = mockMvc.perform(post("/admins")
            .contentType("application/json")
            .content(adminJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        // Verify the admin was created in the database
        Admin admin = adminRepository.findByEmail("john.doe@mail.univ.ca");
        assertNotNull(admin);
        assertEquals("John", admin.getFirstName());
        assertEquals("Doe", admin.getLastName());
        assertEquals("john.doe@mail.univ.ca", admin.getEmail());
     }
    
     @Test
     @Transactional // Roll back the admin creation after the test
     void createAdminWithExistingEmail() throws Exception {
        ObjectNode adminJson = objectMapper.createObjectNode();
        adminJson.put("firstName", "John");
        adminJson.put("lastName", "Doe");
        adminJson.put("email", "petyr.baelish@mail.univ.ca");
        adminJson.put("password", "password1234");
        MockHttpServletResponse response = mockMvc.perform(post("/admins")
            .contentType("application/json")
            .content(adminJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
     }

     @Test
     @Transactional
     void updateAdmin() throws Exception {
        ObjectNode adminJson = objectMapper.createObjectNode();
        adminJson.put("id", EXISTING_ADMIN_ID);
        adminJson.put("firstName", "Updated");
        adminJson.put("lastName", "Name");
        adminJson.put("email", "updated.name@mail.univ.ca");

        MockHttpServletResponse response = mockMvc.perform(put("/admins/" + EXISTING_ADMIN_ID)
            .contentType("application/json")
            .content(adminJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        // Verify the admin was updated in the database
        assertEquals("Updated", adminRepository.findById(EXISTING_ADMIN_ID).get().getFirstName());
        assertEquals("Name", adminRepository.findById(EXISTING_ADMIN_ID).get().getLastName());
        assertEquals("updated.name@mail.univ.ca", adminRepository.findById(EXISTING_ADMIN_ID).get().getEmail());
     }

     @Test
     @Transactional
     void updateNonExistingAdmin() throws Exception {
        ObjectNode adminJson = objectMapper.createObjectNode();
        adminJson.put("id", 9999L);
        adminJson.put("firstName", "Updated");
        adminJson.put("lastName", "Name");
        adminJson.put("email", "updated.name@mail.univ.ca");

        MockHttpServletResponse response = mockMvc.perform(put("/admins/9999")
            .contentType("application/json")
            .content(adminJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
     }

     @Test
     @Transactional
     void deleteAdmin() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/admins/" + EXISTING_ADMIN_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertFalse(adminRepository.existsById(EXISTING_ADMIN_ID));
     }

     @Test
     @Transactional
     void deleteNonExistingAdmin() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/admins/9999"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
     }
}
