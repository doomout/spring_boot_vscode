package com.zerock.spring_boot_ex.dto;


import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //댓글 등록 시간을 포멧팅 
    private LocalDateTime regDate; //댓글 등록일

    @JsonIgnore //댓글 수정일은 필요 없어서 제외
    private LocalDateTime modDate; //수정일
}
