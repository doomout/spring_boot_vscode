package com.zerock.spring_boot_ex.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.zerock.spring_boot_ex.domain.Board;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
}
