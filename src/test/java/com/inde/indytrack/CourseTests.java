package com.inde.indytrack;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.CourseType;
import com.inde.indytrack.repository.CourseRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class CourseTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private CourseRepository courseRepository;

    private final String EXISTING_COURSE_CODE = "MIE236H1";
    private final String EXISTING_COURSE_NAME = "Probability";
    
    private final String NEW_COURSE_CODE = "MIE111H1";
    private final String NEW_COURSE_NAME = "Introduction to Systems Engineering";

    @BeforeEach
    void setUp() {
        if (courseRepository.existsByCode(NEW_COURSE_CODE)) {
            courseRepository.deleteByCode(NEW_COURSE_CODE);
        }
    }

    @Test
    void retrieveAllCourses() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/courses"))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ArrayNode receivedJson = (ArrayNode) objectMapper.readTree(response.getContentAsString());
        assertTrue(receivedJson.isArray());
        assertEquals(receivedJson.get(0).get("code").asText(), "APS100H1");
    }

    @Test
    void retrieveExistingCourse() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/courses/" + EXISTING_COURSE_CODE))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ObjectNode receivedJson = objectMapper.readValue(response.getContentAsString(), ObjectNode.class);
        assertEquals(EXISTING_COURSE_CODE, receivedJson.get("code").asText());
        assertEquals(EXISTING_COURSE_NAME, receivedJson.get("name").asText());
        assertEquals(0.5, receivedJson.get("creditValue").asDouble());
        assertEquals(CourseType.CORE.name(), receivedJson.get("courseType").asText());
    }

    @Test
    void retrieveNonExistingCourse() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/courses/" + NEW_COURSE_CODE))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void createCourse() throws Exception {
        ObjectNode courseJson = objectMapper.createObjectNode();
        courseJson.put("code", NEW_COURSE_CODE);
        courseJson.put("name", NEW_COURSE_NAME);
        courseJson.put("description", "This is a test course");
        courseJson.put("courseType", CourseType.CORE.name());
        courseJson.put("creditValue", 0.5);

        MockHttpServletResponse response = mockMvc.perform(post("/courses")
            .contentType("application/json")
            .content(courseJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Course savedCourse = courseRepository.findByCode(NEW_COURSE_CODE);
        assertEquals(NEW_COURSE_NAME, savedCourse.getName());
        assertEquals(0.5, savedCourse.getCreditValue(), 0.001);
        assertEquals(CourseType.CORE, savedCourse.getCourseType());
        assertEquals("This is a test course", savedCourse.getDescription());
    }

    @Test
    @Transactional
    void updateCourse() throws Exception {
        ObjectNode courseJson = objectMapper.createObjectNode();
        courseJson.put("code", EXISTING_COURSE_CODE);
        courseJson.put("name", NEW_COURSE_NAME);
        courseJson.put("description", "This is a test course");
        courseJson.put("courseType", CourseType.CORE.name());
        courseJson.put("creditValue", 0.5);

        MockHttpServletResponse response = mockMvc.perform(put("/courses/" + EXISTING_COURSE_CODE)
            .contentType("application/json")
            .content(courseJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Course updatedCourse = courseRepository.findByCode(EXISTING_COURSE_CODE);
        assertEquals(NEW_COURSE_NAME, updatedCourse.getName());
        assertEquals(0.5, updatedCourse.getCreditValue(), 0.001);
        assertEquals(CourseType.CORE, updatedCourse.getCourseType());
        assertEquals("This is a test course", updatedCourse.getDescription());
    }

    @Test
    @Transactional
    void updateCourseWithNonExistingCode() throws Exception {
        ObjectNode courseJson = objectMapper.createObjectNode();
        courseJson.put("code", NEW_COURSE_CODE);
        courseJson.put("name", NEW_COURSE_NAME);
        courseJson.put("description", "This is a test course");
        courseJson.put("courseType", CourseType.CORE.name());
        courseJson.put("creditValue", 0.5);

        MockHttpServletResponse response = mockMvc.perform(put("/courses/" + NEW_COURSE_CODE)
            .contentType("application/json")
            .content(courseJson.toString()))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Transactional
    void deleteCourse() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/courses/" + EXISTING_COURSE_CODE))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNull(courseRepository.findByCode(EXISTING_COURSE_CODE));
    }

    @Test
    @Transactional
    void deleteNonExistingCourse() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/courses/" + NEW_COURSE_CODE))
            .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    void searchCourses() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/courses/search")
            .param("code", EXISTING_COURSE_CODE)
            .param("name", EXISTING_COURSE_NAME))
            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(EXISTING_COURSE_CODE));
        assertTrue(response.getContentAsString().contains(EXISTING_COURSE_NAME));
    }
    
    
}
