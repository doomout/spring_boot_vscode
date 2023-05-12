package com.zerock.spring_boot_ex.service;

import com.zerock.spring_boot_ex.dto.PageRequestDTO;
import com.zerock.spring_boot_ex.dto.PageResponseDTO;
import com.zerock.spring_boot_ex.dto.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO); //리플 등록

    ReplyDTO read(Long rno); //리플 조회
 
    void modify(ReplyDTO replyDTO); //리플 수정

    void remove(Long rno); //리플 삭제

    //댓글 페이징 처리
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
