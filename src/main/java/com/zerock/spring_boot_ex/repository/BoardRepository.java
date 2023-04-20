package com.zerock.spring_boot_ex.repository;

import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.repository.search.BoardSearch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//JpaRepository 에는 엔티티타입, @Id 타입을 지정
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    @Query(value="select now()", nativeQuery = true)
    String getTime();
}
