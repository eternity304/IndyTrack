package com.inde.indytrack;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.inde.indytrack.repository.MinorRepository;
import com.inde.indytrack.repository.StudentRepository;
import com.inde.indytrack.dto.MinorProgressDTO;
import com.inde.indytrack.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class StudentTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MinorRepository minorRepository;

    private final Long EXISTING_STUDENT_ID = 1111L; // Tyrion Lannister
    private final String EXISTING_MINOR_NAME_1 = "Artificial Intelligence Engineering";
    private final String EXISTING_MINOR_NAME_2 = "Engineering Business";
    private final String EXISTING_MINOR_NAME_3 = "Nanoengineering";

    private final Long NEW_STUDENT_ID = 9999L;
    private final String NEW_MINOR_NAME = "Systems Engineering";

    @BeforeEach
    void setUp() {
        if (studentRepository.existsById(NEW_STUDENT_ID)) {
            studentRepository.deleteById(NEW_STUDENT_ID);
        }
        if (minorRepository.existsByName(NEW_MINOR_NAME)) {
            minorRepository.deleteByName(NEW_MINOR_NAME);
        }
    }

    @Test
    void retrieveAllStudents() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ArrayNode receivedJson = (ArrayNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(receivedJson.get(0).get("id").asLong(), EXISTING_STUDENT_ID);
    }

    @Test
    void retrieveExistingStudent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(EXISTING_STUDENT_ID, receivedJson.get("id").asLong());
        assertEquals("Tyrion", receivedJson.get("firstName").asText());
        assertEquals("Lannister", receivedJson.get("lastName").asText());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson.get("email").asText());
    }

    @Test
    void retrieveNonExistingStudent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + NEW_STUDENT_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void createStudent() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("firstName", "John");
        studentJson.put("lastName", "Doe");
        studentJson.put("email", "john.doe@mail.univ.ca");
        studentJson.put("password", "password1234");
        MockHttpServletResponse response = mockMvc.perform(post("/students")
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Student student = studentRepository.findByEmail("john.doe@mail.univ.ca");
        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john.doe@mail.univ.ca", student.getEmail());
    }

    @Test
    @Transactional
    void createStudentWithExistingEmail() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("firstName", "John");
        studentJson.put("lastName", "Doe");
        studentJson.put("email", "tyrion.lannister@mail.univ.ca");
        studentJson.put("password", "password1234");

        MockHttpServletResponse response = mockMvc.perform(post("/students")
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Transactional
    void updateStudent() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("firstName", "Updated");
        studentJson.put("lastName", "Name");
        studentJson.put("email", "updated.name@mail.univ.ca");

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID)
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertEquals("Updated", studentRepository.findById(EXISTING_STUDENT_ID).get().getFirstName());
        assertEquals("Name", studentRepository.findById(EXISTING_STUDENT_ID).get().getLastName());
        assertEquals("updated.name@mail.univ.ca", studentRepository.findById(EXISTING_STUDENT_ID).get().getEmail());
    }

    @Test
    @Transactional
    void updateNonExistingStudent() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", 9999L);
        studentJson.put("firstName", "Updated");
        studentJson.put("lastName", "Name");
        studentJson.put("email", "updated.name@mail.univ.ca");

        MockHttpServletResponse response = mockMvc.perform(put("/students/9999")
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void deleteStudent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/students/" + EXISTING_STUDENT_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertFalse(studentRepository.existsById(EXISTING_STUDENT_ID));
    }

    @Test
    @Transactional
    void deleteNonExistingStudent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/students/" + NEW_STUDENT_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
    
    @Test
    @Transactional
    void retrieveStudentsByName() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/names/Tyrion"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ArrayNode receivedJson = (ArrayNode) objectMapper.readTree(response.getContentAsString());

        Student student = studentRepository.findById(receivedJson.get(0).get("id").asLong()).get();
        assertEquals(EXISTING_STUDENT_ID, student.getId());
        assertEquals("Tyrion", student.getFirstName());
        assertEquals("Lannister", student.getLastName());
        assertEquals("tyrion.lannister@mail.univ.ca", student.getEmail());
    }

    @Test
    @Transactional
    void getIntendedMinors() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID + "/intended-minors"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ArrayNode receivedJson = (ArrayNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(2, receivedJson.size());
        assertEquals(EXISTING_MINOR_NAME_1, receivedJson.get(0).get("name").asText());
        assertEquals(EXISTING_MINOR_NAME_2, receivedJson.get(1).get("name").asText());
    }

    @Test
    @Transactional
    void addIntendedMinor() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", EXISTING_MINOR_NAME_3);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + EXISTING_MINOR_NAME_3))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Student student = studentRepository.findById(EXISTING_STUDENT_ID).get();
        assertEquals(3, student.getIntendedMinors().size());
        assertTrue(student.getIntendedMinors().stream().anyMatch(minor -> minor.getName().equals(EXISTING_MINOR_NAME_3)));
    }

    @Test
    @Transactional
    void addIntendedMinorWithNonExistingMinor() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", NEW_MINOR_NAME);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + NEW_MINOR_NAME))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void removeIntendedMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + EXISTING_MINOR_NAME_2))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Student student = studentRepository.findById(EXISTING_STUDENT_ID).get();
        assertEquals(1, student.getIntendedMinors().size());
        assertFalse(student.getIntendedMinors().stream().anyMatch(minor -> minor.getName().equals(EXISTING_MINOR_NAME_2))); 
    }

    @Test
    @Transactional
    void removeIntendedMinorWithNonExistingMinor() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", NEW_MINOR_NAME);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + NEW_MINOR_NAME))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void removeIntendedMinorWithNotEnrolledMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + EXISTING_MINOR_NAME_3))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void updateIntendedMinors() throws Exception {
        ArrayNode intendedMinors = objectMapper.createArrayNode();
        intendedMinors.add(EXISTING_MINOR_NAME_3);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors")
            .contentType("application/json")
            .content(intendedMinors.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Student student = studentRepository.findById(EXISTING_STUDENT_ID).get();
        assertEquals(1, student.getIntendedMinors().size());
        assertTrue(student.getIntendedMinors().stream().anyMatch(minor -> minor.getName().equals(EXISTING_MINOR_NAME_3)));
    }

    @Test
    @Transactional
    void updateIntendedMinorsWithNonExistingMinor() throws Exception {
        ArrayNode intendedMinors = objectMapper.createArrayNode();
        intendedMinors.add(NEW_MINOR_NAME);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors")
            .contentType("application/json")
            .content(intendedMinors.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void retrieveAllIntendedMinorsProgress() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID + "/intended-minors/progress"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<MinorProgressDTO> minorProgressDTOs = studentRepository.findIntendedMinorsProgress(EXISTING_STUDENT_ID);
        assertEquals(2, minorProgressDTOs.size());
        assertEquals(EXISTING_MINOR_NAME_1, minorProgressDTOs.get(0).getMinorName());
        assertEquals(2.0, minorProgressDTOs.get(0).getCreditsEarned());
        assertEquals(66.67, minorProgressDTOs.get(0).getPercentageCompleted());
        assertEquals(EXISTING_MINOR_NAME_2, minorProgressDTOs.get(1).getMinorName());
        assertEquals(0.0, minorProgressDTOs.get(1).getCreditsEarned());
        assertEquals(0.0, minorProgressDTOs.get(1).getPercentageCompleted());
    }

    @Test
    @Transactional
    void retrieveIntendedMinorProgress() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + EXISTING_MINOR_NAME_1 + "/progress"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        JsonNode minorProgressDTO = objectMapper.readTree(response.getContentAsString());
        assertEquals(EXISTING_MINOR_NAME_1, minorProgressDTO.get("minorName").asText());
        assertEquals(2.0, minorProgressDTO.get("creditsEarned").asDouble());
        assertEquals(66.67, minorProgressDTO.get("percentageCompleted").asDouble());
    }

    @Test
    @Transactional
    void retrieveIntendedMinorProgressWithNonExistingMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + NEW_MINOR_NAME + "/progress"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
