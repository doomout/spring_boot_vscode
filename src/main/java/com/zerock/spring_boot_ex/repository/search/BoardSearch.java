package com.zerock.spring_boot_ex.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.dto.BoardListReplyCountDTO;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);

    //검색 조건들과 키워드로 조회
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);
}
