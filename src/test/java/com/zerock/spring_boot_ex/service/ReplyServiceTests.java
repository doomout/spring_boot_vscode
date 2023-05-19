package com.zerock.spring_boot_ex.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zerock.spring_boot_ex.dto.ReplyDTO;
//import com.zerock.spring_boot_ex.service.ReplyService;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("리플 테스트 중 abc")
                .replyer("리플러abc")
                .bno(193L) //191이 리플 없음
                .build();

        log.info(replyService.register(replyDTO));
    }
}
