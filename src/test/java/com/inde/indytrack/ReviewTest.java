package com.inde.indytrack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inde.indytrack.model.Review;
import com.inde.indytrack.model.ReviewKey;
import com.inde.indytrack.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ReviewTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void testGetReviewsByCourse() throws Exception {
        String courseCode = "MIE236H1";

        MockHttpServletResponse response = mockMvc.perform(get("/reviews/courses/" + courseCode))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        JsonNode jsonArray = objectMapper.readTree(response.getContentAsString());
        assertTrue(jsonArray.isArray());
        assertEquals(2, jsonArray.size());

        JsonNode reviewNode = jsonArray.get(0);
        assertTrue(reviewNode.hasNonNull("uploadTime"), "uploadTime field should be present and non-null");

        assertTrue(reviewNode.hasNonNull("uploadTime"), "uploadTime field should be present and non-null");
        String uploadTime = reviewNode.get("uploadTime").asText();
        assertFalse(uploadTime.isEmpty(), "uploadTime should not be an empty string");

        JsonNode courseNode = reviewNode.get("course");
        assertNotNull(courseNode, "course field should not be null");
        assertEquals(courseCode, courseNode.get("code").asText());
    }

    @Test
    void testRetrieveAverageRatingForCourse() throws Exception {
        String courseCode = "MIE236H1";
        double expectedAverageRating = 4;

        MockHttpServletResponse response = mockMvc.perform(get("/reviews/" + courseCode + "/average-rating"))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        Double responseRating = objectMapper.readValue(response.getContentAsString(), Double.class);
        assertEquals(expectedAverageRating, responseRating, 0.001, "Average rating should match expected value");
    }


    @Test
    void testCreateReview() throws Exception {
        String courseCode = "MIE100H1";
        Long studentId = 1111L;
        int rating = 5;
        String comment = "Excellent course!";

        ObjectNode reviewDtoJson = objectMapper.createObjectNode();
        reviewDtoJson.put("courseCode", courseCode);
        reviewDtoJson.put("studentId", studentId);
        reviewDtoJson.put("rating", rating);
        reviewDtoJson.put("comment", comment);

        MockHttpServletResponse response = mockMvc.perform(post("/reviews/" + courseCode + "/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDtoJson)))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        JsonNode responseJson = objectMapper.readTree(response.getContentAsString());
        assertEquals(rating, responseJson.get("rating").asInt());
        assertEquals(comment, responseJson.get("comment").asText());

        JsonNode studentNode = responseJson.get("student");
        assertNotNull(studentNode);
        assertEquals(studentId, studentNode.get("id").longValue());

        JsonNode courseNode = responseJson.get("course");
        assertNotNull(courseNode);
        assertEquals(courseCode, courseNode.get("code").asText());
        assertTrue(responseJson.hasNonNull("uploadTime"));
    }

    @Test
    void testUpdateReview() throws Exception {
        String courseCode = "MIE236H1";
        Long studentId = 5555L;
        int oldRating = 4;
        int newRating = 5;
        String newComment = "Updated review comment.";

        ObjectNode reviewDtoJson = objectMapper.createObjectNode();
        reviewDtoJson.put("courseCode", courseCode);
        reviewDtoJson.put("studentId", studentId);
        reviewDtoJson.put("rating", newRating);
        reviewDtoJson.put("comment", newComment);

        MockHttpServletResponse response = mockMvc.perform(put("/reviews/" + courseCode + "/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDtoJson)))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        JsonNode responseJson = objectMapper.readTree(response.getContentAsString());
        assertEquals(newRating, responseJson.get("rating").asInt());
        assertEquals(newComment, responseJson.get("comment").asText());

        JsonNode studentNode = responseJson.get("student");
        assertNotNull(studentNode);
        assertEquals(studentId, studentNode.get("id").longValue());

        JsonNode courseNode = responseJson.get("course");
        assertNotNull(courseNode);
        assertEquals(courseCode, courseNode.get("code").asText());
        assertTrue(responseJson.hasNonNull("uploadTime"));
    }

    @Test
    void testDeleteReview() throws Exception {
        String courseCode = "MIE236H1";
        Long studentId = 5555L;
        int rating = 5;

        MockHttpServletResponse response = mockMvc.perform(delete("/reviews/" + courseCode + "/" + studentId))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        Optional<Review> checkAfterDelete = reviewRepository.findById(new ReviewKey(courseCode, studentId));
        assertFalse(checkAfterDelete.isPresent(), "Review should be deleted and no longer present in the repository");
    }
}