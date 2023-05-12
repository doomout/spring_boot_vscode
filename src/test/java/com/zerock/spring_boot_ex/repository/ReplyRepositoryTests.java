package com.zerock.spring_boot_ex.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.domain.Reply;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository ;

    @Test
    public void testInsert() {
        Long bno = 193L; //댓글도 연결되어 있는 게시물 번호

        Board board= Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("선플.....")
                .replyer("선플러")
                .build();

        replyRepository.save(reply); //insert
    }

    @Transactional
    @Test
    public void testBoardReplies() {
        Long bno = 193L; //댓글이 있는 게시물 번호
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }
}
