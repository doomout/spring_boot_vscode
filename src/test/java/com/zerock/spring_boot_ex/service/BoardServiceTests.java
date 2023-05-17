package com.zerock.spring_boot_ex.service;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zerock.spring_boot_ex.dto.BoardDTO;
import com.zerock.spring_boot_ex.dto.PageRequestDTO;
import com.zerock.spring_boot_ex.dto.PageResponseDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
            .title("Sample Ttitle....")
            .content("Sample Content....")
            .writer("user00")
            .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

    }
    
    @Test
    public void testRegisterWithImages() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File....Sample Title...")
                .content("Sample Content....")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
            Arrays.asList(
                UUID.randomUUID()+"_aaa.jpg",
                UUID.randomUUID()+"_bbb.jpg",
                UUID.randomUUID()+"_bbb.jpg"
            )
        );

        Long bno = boardService.register(boardDTO);
        log.info("bno: " + bno);
    }

    @Test
    public void testReadAll() {
        Long bno = 101L;
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    @Test
    public void testModify() {
        //변경에 필요한 데이터
        BoardDTO boardDTO = BoardDTO.builder()
                    .bno(101L)
                    .title("Updated......101")
                    .content("Updated content 101.....")
                    .build();
        
        //첨부 파일 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+ "_zzz.jpg"));

        boardService.modify(boardDTO);
    }

    @Test
    public void testRemoveAll() {
        Long bno = 1L; //1번 게시물에는 첨부파일 3개가 있다. 

        //게시물 조회 1번,
        //첨부 파일 조회 1번,
        //게시물 삭제 1번,
        //첨부파일 삭제 3번
        //이걸 쿼리문 한방이면 할 것을 이렇게 하네......
        boardService.remove(bno);
    }
}
