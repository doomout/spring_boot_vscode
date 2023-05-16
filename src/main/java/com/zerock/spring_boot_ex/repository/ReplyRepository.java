package com.zerock.spring_boot_ex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zerock.spring_boot_ex.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(@Param("bno")Long bno, Pageable pageable);
    
    void deleteByBoard_Bno(Long bno); //특정 게시물에 해당하는 데이터를 삭제(댓글, 첨부파일)
}
