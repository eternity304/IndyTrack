package com.inde.indytrack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inde.indytrack.model.CoursePlan;
import com.inde.indytrack.model.SemesterCourses;
import com.inde.indytrack.repository.CoursePlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class CoursePlanTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getByStudent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/course-plans/students/1111"))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(response.getContentAsString());
        ObjectNode receivedJson = (ObjectNode) arrayNode.get(0); // get first object in array

        assertEquals(1L, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode, "Student node should not be null");
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());
        assertEquals("Lannister", studentNode.get("lastName").asText());
        assertEquals("tyrion.lannister@mail.univ.ca", studentNode.get("email").asText());
        assertEquals("password", studentNode.get("password").asText());

        ArrayNode semesterCourses = (ArrayNode) receivedJson.get("semesterCoursesList");
        assertEquals(3, semesterCourses.size());

        JsonNode semesterCourse1 = semesterCourses.get(0);
        assertEquals("Fall2024", semesterCourse1.get("semester").asText());
        assertTrue(semesterCourse1.get("courseCode").isNull());

        ArrayNode courses1 = (ArrayNode) semesterCourse1.get("courses");
        assertEquals(2, courses1.size());
        assertEquals("MIE236H1", courses1.get(0).asText());

        JsonNode semesterCourse2 = semesterCourses.get(1);
        assertEquals("Winter2025", semesterCourse2.get("semester").asText());
        assertTrue(semesterCourse2.get("courseCode").isNull());

        ArrayNode courses2 = (ArrayNode) semesterCourse2.get("courses");
        assertEquals(2, courses2.size());
    }

    @Test
    void getByPlanId() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/course-plans/1"))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());

        assertEquals(1L, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode, "Student node should not be null");
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());
        assertEquals("Lannister", studentNode.get("lastName").asText());
        assertEquals("tyrion.lannister@mail.univ.ca", studentNode.get("email").asText());
        assertEquals("password", studentNode.get("password").asText());

        ArrayNode semesterCourses = (ArrayNode) receivedJson.get("semesterCoursesList");
        assertEquals(3, semesterCourses.size());

        JsonNode semesterCourse1 = semesterCourses.get(0);
        assertEquals("Fall2024", semesterCourse1.get("semester").asText());
        assertTrue(semesterCourse1.get("courseCode").isNull());

        ArrayNode courses1 = (ArrayNode) semesterCourse1.get("courses");
        assertEquals(2, courses1.size());
        assertEquals("MIE236H1", courses1.get(0).asText());

        JsonNode semesterCourse2 = semesterCourses.get(1);
        assertEquals("Winter2025", semesterCourse2.get("semester").asText());
        assertTrue(semesterCourse2.get("courseCode").isNull());

        ArrayNode courses2 = (ArrayNode) semesterCourse2.get("courses");
        assertEquals(2, courses2.size());

    }

    @Test
    void createCoursePlan() throws Exception {
        // Prepare test input as JSON

        String jsonRequestBody = "{\n" +
                "  \"studentId\": 1111,\n" +
                "  \"semesterCourses\": {\n" +
                "    \"Fall2024\": [\"MIE236H1\"],\n" +
                "    \"Winter2025\": [\"MIE237H1\"]\n" +
                "  }\n" +
                "}";

        MockHttpServletResponse response = mockMvc.perform(post("/course-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertNotNull(receivedJson.get("id"));

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode);
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());

        ArrayNode semesterCourses = (ArrayNode) receivedJson.get("semesterCoursesList");
        assertEquals(2, semesterCourses.size());

        JsonNode fall = semesterCourses.get(0);
        JsonNode winter = semesterCourses.get(1);
        if (fall.get("semester").asText().equals("Winter2025")) {
            JsonNode tmp = fall;
            fall = winter;
            winter = tmp;
        }
        assertEquals("Fall2024", fall.get("semester").asText());

        ArrayNode fallCourses = (ArrayNode) fall.get("courses");
        assertEquals(1, fallCourses.size());
        assertEquals("MIE236H1", fallCourses.get(0).asText());
        assertEquals("Winter2025", winter.get("semester").asText());

        ArrayNode winterCourses = (ArrayNode) winter.get("courses");
        assertEquals(1, winterCourses.size());
        assertEquals("MIE237H1", winterCourses.get(0).asText());
    }

    @Test
    void testAddCourseToCoursePlan() throws Exception {
        Long planId = 1L;
        String semester = "Fall2024";
        String postSemester = "Fall2024";
        String courseId = "MIE567H1";

        MockHttpServletResponse response = mockMvc.perform(put("/course-plans/" + planId + "/" + postSemester + "/" + courseId))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(planId, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode);
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());

        JsonNode semesterCoursesNode = receivedJson.get("semesterCoursesList");
        Optional<JsonNode> fallCoursesOpt = StreamSupport.stream(semesterCoursesNode.spliterator(), false)
                .filter(sc -> semester.equals(sc.get("semester").asText()))
                .findFirst();
        assertTrue(fallCoursesOpt.isPresent(), "Expected semester not found in semesterCoursesList");

        JsonNode fallCourses = fallCoursesOpt.get();
        assertTrue(fallCourses.get("courses").isArray());
        assertTrue(
                StreamSupport.stream(fallCourses.get("courses").spliterator(), false)
                        .anyMatch(c -> c.asText().equals(courseId))
        );
    }

    @Test
    void testAddSemesterToCoursePlan() throws Exception {
        Long planId = 1L;
        String semester = "Summer2025";

        MockHttpServletResponse response = mockMvc.perform(put("/course-plans/" + planId + "/" + semester))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(planId, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode);
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());

        JsonNode semesterCoursesNode = receivedJson.get("semesterCoursesList");
        assertNotNull(semesterCoursesNode);

        boolean found = StreamSupport.stream(semesterCoursesNode.spliterator(), false)
                .anyMatch(sc -> semester.equals(sc.get("semester").asText()) && sc.get("courses").isArray());
        assertTrue(found, "New semester should be present with an empty course list");
    }

    @Test
    void testDeleteCourseFromSemester() throws Exception {
        Long planId = 1L;
        String semester = "Winter2025";
        String courseCode = "MIE237H1";

        MockHttpServletResponse response = mockMvc.perform(delete("/course-plans/" + planId + "/" + semester + "/" + courseCode))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(planId, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode);
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());

        JsonNode semesterCoursesNode = receivedJson.get("semesterCoursesList");
        assertNotNull(semesterCoursesNode);
        Optional<JsonNode> semesterNodeOpt = StreamSupport.stream(semesterCoursesNode.spliterator(), false)
                .filter(sc -> semester.equals(sc.get("semester").asText()))
                .findFirst();
        assertTrue(semesterNodeOpt.isPresent(), "Semester should still exist after removing only one course");

        JsonNode semesterNode = semesterNodeOpt.get();
        boolean removedCourseStillPresent = StreamSupport.stream(semesterNode.get("courses").spliterator(), false)
                .anyMatch(c -> c.asText().equals(courseCode));
        assertFalse(removedCourseStillPresent, "Removed course should no longer be in the semester course list");

    }

    @Test
    void testDeleteSemesterFromCoursePlan() throws Exception {
        Long planId = 1L;
        String semesterToRemove = "Fall2024";
        String remainingSemester = "Winter2025";

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/course-plans/" + planId + "/" + semesterToRemove))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(planId, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode);
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());

        JsonNode semesterCoursesNode = receivedJson.get("semesterCoursesList");
        assertNotNull(semesterCoursesNode);

        boolean removedSemesterExists = StreamSupport.stream(semesterCoursesNode.spliterator(), false)
                .anyMatch(sc -> semesterToRemove.equalsIgnoreCase(sc.get("semester").asText()));
        assertFalse(removedSemesterExists, "Removed semester should not be present in semesterCoursesList");

        boolean remainingSemesterExists = StreamSupport.stream(semesterCoursesNode.spliterator(), false)
                .anyMatch(sc -> remainingSemester.equalsIgnoreCase(sc.get("semester").asText()));
        assertTrue(remainingSemesterExists, "Remaining semester should still be present");
    }

    @Test
    void testClearSemesterCourses() throws Exception {
        Long planId = 1L;
        String semesterToClear = "Summer2025";

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/course-plans/" + planId + "/" + semesterToClear + "/clear"))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        ObjectNode receivedJson = (ObjectNode) objectMapper.readTree(response.getContentAsString());
        assertEquals(planId, receivedJson.get("id").longValue());

        JsonNode studentNode = receivedJson.get("student");
        assertNotNull(studentNode);
        assertEquals(1111L, studentNode.get("id").longValue());
        assertEquals("Tyrion", studentNode.get("firstName").asText());

        JsonNode semesterCoursesNode = receivedJson.get("semesterCoursesList");
        assertNotNull(semesterCoursesNode);

        Optional<JsonNode> clearedSemesterOpt = StreamSupport.stream(semesterCoursesNode.spliterator(), false)
                .filter(sc -> semesterToClear.equals(sc.get("semester").asText()))
                .findFirst();
        assertTrue(clearedSemesterOpt.isPresent(), "Semester should still exist after clearing courses");

        JsonNode clearedSemester = clearedSemesterOpt.get();
        assertTrue(clearedSemester.get("courses").isArray());
        assertEquals(0, clearedSemester.get("courses").size(), "Courses list should be empty after clearing");

    }

}