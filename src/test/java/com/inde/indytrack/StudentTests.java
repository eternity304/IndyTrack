package com.inde.indytrack;

import org.junit.jupiter.api.AfterEach;
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
import com.inde.indytrack.repository.MinorRepository;
import com.inde.indytrack.repository.StudentRepository;
import com.inde.indytrack.dto.MinorProgressDTO;
import com.inde.indytrack.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        studentRepository.deleteById(NEW_STUDENT_ID);
        minorRepository.deleteByName(NEW_MINOR_NAME);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteById(NEW_STUDENT_ID);
        minorRepository.deleteByName(NEW_MINOR_NAME);
    }

    @Test
    void retrieveAllStudents() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertTrue(receivedJson.size() >= 5);
    }

    @Test
    void retrieveExistingStudent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
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
        studentJson.put("id", NEW_STUDENT_ID);
        studentJson.put("firstName", "John");
        studentJson.put("lastName", "Doe");
        studentJson.put("email", "john.doe@mail.univ.ca");

        MockHttpServletResponse response = mockMvc.perform(post("/students")
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertTrue(studentRepository.existsById(NEW_STUDENT_ID));
        assertEquals(NEW_STUDENT_ID, studentRepository.findById(NEW_STUDENT_ID).get().getId());
        assertEquals("John", studentRepository.findById(NEW_STUDENT_ID).get().getFirstName());
        assertEquals("Doe", studentRepository.findById(NEW_STUDENT_ID).get().getLastName());
        assertEquals("john.doe@mail.univ.ca", studentRepository.findById(NEW_STUDENT_ID).get().getEmail());
    }

    @Test
    @Transactional
    void createStudentWithExistingEmail() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", NEW_STUDENT_ID);
        studentJson.put("firstName", "John");
        studentJson.put("lastName", "Doe");
        studentJson.put("email", "tyrion.lannister@mail.univ.ca");

        MockHttpServletResponse response = mockMvc.perform(post("/students")
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Email is already in use", response.getContentAsString());
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
    void retrieveStudentByName() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/names/Tyrion"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_STUDENT_ID, receivedJson.get("id").asLong());
        assertEquals("Tyrion", receivedJson.get("firstName").asText());
        assertEquals("Lannister", receivedJson.get("lastName").asText());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson.get("email").asText());
    }

    @Test
    @Transactional
    void getIntendedMinors() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID + "/intended-minors"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_STUDENT_ID, receivedJson.get("id").asLong());
        assertEquals("Tyrion", receivedJson.get("firstName").asText());
        assertEquals("Lannister", receivedJson.get("lastName").asText());
        assertEquals("tyrion.lannister@mail.univ.ca", receivedJson.get("email").asText());
        assertTrue(receivedJson.get("intendedMinors").isArray());
        assertEquals(2, receivedJson.get("intendedMinors").size());
        assertEquals(EXISTING_MINOR_NAME_1, receivedJson.get("intendedMinors").get(0).asText());
        assertEquals(EXISTING_MINOR_NAME_2, receivedJson.get("intendedMinors").get(1).asText());
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
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", EXISTING_MINOR_NAME_2);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + EXISTING_MINOR_NAME_2))
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
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", EXISTING_MINOR_NAME_3);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + EXISTING_MINOR_NAME_3))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void updateIntendedMinors() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", EXISTING_MINOR_NAME_3);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors")
            .contentType("application/json")
            .content(studentJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Student student = studentRepository.findById(EXISTING_STUDENT_ID).get();
        assertEquals(1, student.getIntendedMinors().size());
        assertTrue(student.getIntendedMinors().stream().anyMatch(minor -> minor.getName().equals(EXISTING_MINOR_NAME_3)));
    }

    @Test
    @Transactional
    void updateIntendedMinorsWithNonExistingMinor() throws Exception {
        ObjectNode studentJson = objectMapper.createObjectNode();
        studentJson.put("id", EXISTING_STUDENT_ID);
        studentJson.put("intendedMinors", NEW_MINOR_NAME);

        MockHttpServletResponse response = mockMvc.perform(put("/students/" + EXISTING_STUDENT_ID + "/intended-minors")
            .contentType("application/json")
            .content(studentJson.toString()))
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

        MinorProgressDTO minorProgressDTO = objectMapper.readValue(response.getContentAsString(), MinorProgressDTO.class);
        assertEquals(EXISTING_MINOR_NAME_1, minorProgressDTO.getMinorName());
        assertEquals(2.0, minorProgressDTO.getCreditsEarned());
        assertEquals(66.67, minorProgressDTO.getPercentageCompleted());
    }

    @Test
    @Transactional
    void retrieveIntendedMinorProgressWithNonExistingMinor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID + "/intended-minors/" + NEW_MINOR_NAME + "/progress"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
