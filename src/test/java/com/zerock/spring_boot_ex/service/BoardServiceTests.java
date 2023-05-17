package com.zerock.spring_boot_ex.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zerock.spring_boot_ex.dto.BoardDTO;
import com.zerock.spring_boot_ex.dto.BoardImageDTO;
import com.zerock.spring_boot_ex.dto.BoardListAllDTO;
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

    @Test
    public void testListWithAll() {
        //테스트를 위해 PageRequestDTO를 생성하여 페이지 번호와 크기를 설정
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                    .page(1)
                    .size(10)
                    .build();


        //boardService.listWithAll을 호출하여 검색 결과인 responseDTO를 가져옴
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        //responseDTO에서 게시글 리스트인 dtoList를 가져옴
        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+ ":" + boardListAllDTO.getTitle());
            
            //게시글에 첨부된 이미지가 있다면 해당 이미지를 출력
            if(boardListAllDTO.getBoardImages() != null) {
                for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }
            //각 게시글과 이미지 사이에 구분선을 출력합니다.
            log.info("-----------------------------------");
        });
    }
}
