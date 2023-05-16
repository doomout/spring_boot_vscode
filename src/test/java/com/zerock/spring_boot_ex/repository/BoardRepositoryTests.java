package com.zerock.spring_boot_ex.repository;

import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.domain.BoardImage;
import com.zerock.spring_boot_ex.dto.BoardDTO;
import com.zerock.spring_boot_ex.dto.BoardListAllDTO;
import com.zerock.spring_boot_ex.dto.BoardListReplyCountDTO;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired //게시판 인터페이스 의존성 주입
    private BoardRepository boardRepository;

    @Autowired //댓글 인터페이스 의존성 주입
    private ReplyRepository replyRepository;

    //insert 기능 테스트
    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title....." + i)
                    .content("content......"+i)
                    .writer("user" + (i % 10))
                    .build();
            Board result = boardRepository.save(board); //jpa에선 save가 insert 기능을 한다.
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno); //게시물 조회 기능(findById)

        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    public void testUpdate() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update....title 100", "update content 100");

        boardRepository.save(board);
    }


    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " +result.getSize());

        List<Board> todoList= result.getContent();

        todoList.forEach(board -> log.info(board));
    }

     
    @Test
    public void testSearch1() {
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }
    

     
    @Test
    public void testSearchAll() {
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }
    
    
    @Test
    public void testSearchAll2() {
        String[] types = {"t","c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        //total pages
        log.info(result.getTotalPages());
        //page size
        log.info(result.getSize());
        //pageNumber
        log.info(result.getNumber());
        //prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearchReplyCount() {
        String[] types = {"t","c","w"};
        String keyword = "한국";
        Pageable  pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());
        //page size
        log.info(result.getSize());
        //pageNumber
        log.info(result.getNumber());
        //prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());
        
        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                        .title("Image Test")
                        .content("aaa")
                        .writer("me")
                        .build();

        for(int i = 0; i<3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }

        boardRepository.save(board);
    }

    //게시글과 첨부파일 동시 읽기 테스트
    @Test
    public void testReadWithImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("----------------");
        for(BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }
    }

    //첨부 파일은 수정이 아니라 삭제 후 새로운 업로드다.
    @Transactional
    @Commit
    @Test
    public void testModifyImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        
        Board board = result.orElseThrow();

        //기존 첨부파일 삭제
        board.clearImages();

        //새로운 첨부파일들
        for(int i=0; i<2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() { //게시물이 삭제 되면, 댓글, 첨부파일도 삭제 되어야 한다.
        Long bno = 1L;
        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsetAll() {
        //게시물 100개 생성
        for(int i = 1; i<=100; i++) {
            Board board = Board.builder().title("Title.."+ i).content("Content.." + i).writer("writer.." + i).build();

            //게시물당 첨부파일 3개임
            for(int j =0; j < 3; j++) {
                if( i % 5 == 0) { //게시물 번호 5배 수는 첨부 파일이 없음
                    continue;       
                }
                board.addImage(UUID.randomUUID().toString(), i+"file"+".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //boardRepository.searchWithAll(null, null, pageable);
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("-------------------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(BoardListAllDTO -> log.info(BoardListAllDTO));
    }

   
}
