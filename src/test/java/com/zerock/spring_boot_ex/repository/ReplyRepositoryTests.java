package com.zerock.spring_boot_ex.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.domain.Reply;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository ;

    @Test
    public void testInsert() {
        Long bno = 193L;
        Board board= Board.builder().bno(bno).build();

        Reply reply = Reply.builder().board(board).replyText("악플.....").replyer("악플러").build();

        replyRepository.save(reply);
    }
}
