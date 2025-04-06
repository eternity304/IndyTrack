package com.inde.indytrack;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inde.indytrack.repository.MinorRepository;
import com.inde.indytrack.model.Minor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class MinorTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MinorRepository minorRepository;

    private final String EXISTING_MINOR_NAME = "Artificial Intelligence Engineering";
    private final String NEW_MINOR_NAME = "Systems Engineering";

    @BeforeEach
    void setUp() {
        if (minorRepository.existsByName(NEW_MINOR_NAME)) {
            minorRepository.deleteByName(NEW_MINOR_NAME);
        }
    }

    @Test
    void retrieveAllMinors() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/minors"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(EXISTING_MINOR_NAME));
    }

    @Test
    void retrieveExistingMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/minors/" + EXISTING_MINOR_NAME))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(EXISTING_MINOR_NAME));
    }

    @Test
    void retrieveNonExistingMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/minors/" + NEW_MINOR_NAME))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void createMinor() throws Exception {
        ObjectNode minorJson = objectMapper.createObjectNode();
        minorJson.put("name", NEW_MINOR_NAME);
        ArrayNode requirementsArray = objectMapper.createArrayNode();
        ObjectNode requirement1 = objectMapper.createObjectNode();
        requirement1.put("requiredCredits", 1.5);
        requirement1.putArray("courseCodes").add("MIE360H1");
        ObjectNode requirement2 = objectMapper.createObjectNode();
        requirement2.put("requiredCredits", 1.5);
        requirement2.putArray("courseCodes").add("MIE363H1");

        requirementsArray.add(requirement1);
        requirementsArray.add(requirement2);
        minorJson.set("requirements", requirementsArray);

        MockHttpServletResponse response = mockMvc.perform(post("/minors")
            .contentType("application/json")
            .content(minorJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Minor savedMinor = minorRepository.findByName(NEW_MINOR_NAME);
        assertNotNull(savedMinor);
        assertEquals(NEW_MINOR_NAME, savedMinor.getName());
    }

    @Test
    @Transactional
    void createMinorWithExistingName() throws Exception {
        ObjectNode minorJson = objectMapper.createObjectNode();
        minorJson.put("name", EXISTING_MINOR_NAME);
        ArrayNode requirementsArray = objectMapper.createArrayNode();
        ObjectNode requirement = objectMapper.createObjectNode();
        requirement.put("requiredCredits", 3.0);
        requirement.putArray("courseCodes").add("CS101");
        requirementsArray.add(requirement);
        minorJson.set("requirements", requirementsArray);

        MockHttpServletResponse response = mockMvc.perform(post("/minors")
            .contentType("application/json")
            .content(minorJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Transactional
    void updateMinor() throws Exception {
        ObjectNode minorJson = objectMapper.createObjectNode();
        minorJson.put("name", EXISTING_MINOR_NAME);
        ArrayNode requirementsArray = objectMapper.createArrayNode();
        ObjectNode requirement = objectMapper.createObjectNode();
        requirement.put("requiredCredits", 3.0);
        requirement.putArray("courseCodes").add("APS360H1");
        requirementsArray.add(requirement);
        minorJson.set("requirements", requirementsArray);
        MockHttpServletResponse response = mockMvc.perform(put("/minors/" + EXISTING_MINOR_NAME)
            .contentType("application/json")
            .content(minorJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Minor updatedMinor = minorRepository.findByName(EXISTING_MINOR_NAME);
        assertNotNull(updatedMinor);
        assertEquals(3.0, updatedMinor.getRequirements().iterator().next().getRequiredCredits());
        assertEquals("APS360H1", updatedMinor.getRequirements().iterator().next().getCourses().iterator().next().getCode());
    }

    @Test
    @Transactional
    void deleteMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/minors/" + "Nanoengineering"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNull(minorRepository.findByName("Nanoengineering"));
    }

    @Test
    @Transactional
    void getMinorRequirements() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/minors/" + EXISTING_MINOR_NAME + "/requirements"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
