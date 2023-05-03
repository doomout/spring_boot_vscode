package com.zerock.spring_boot_ex.service;

import com.zerock.spring_boot_ex.dto.BoardDTO;

public interface BoardService {
    Long register(BoardDTO boardDTO); //등록

    BoardDTO readOne(Long bno); //조회

    void modify(BoardDTO boardDTO); //수정

    void remove(Long bno); //삭제
}
