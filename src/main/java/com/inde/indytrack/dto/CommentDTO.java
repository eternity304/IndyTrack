package com.inde.indytrack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    Long studentId;
    String courseId;
    String body;
}
