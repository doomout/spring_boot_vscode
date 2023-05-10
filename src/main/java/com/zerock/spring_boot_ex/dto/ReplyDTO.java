package com.zerock.spring_boot_ex.dto;


import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private Long rno; //리플 번호

    @NotNull
    private Long bno; //게시물 번호
    
    @NotEmpty
    private String replyText; //리플 글
    
    @NotEmpty
    private String replyer; //리플 입력자
    private LocalDateTime regDate, modDate; //등록일, 수정일
}
