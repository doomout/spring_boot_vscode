package com.zerock.spring_boot_ex.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    
    private Long replyCount; //댓글수
}
