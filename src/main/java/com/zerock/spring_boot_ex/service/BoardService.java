package com.zerock.spring_boot_ex.service;

import java.util.List;
import java.util.stream.Collectors;

import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.domain.BoardImage;
import com.zerock.spring_boot_ex.dto.BoardDTO;
import com.zerock.spring_boot_ex.dto.BoardListAllDTO;
import com.zerock.spring_boot_ex.dto.BoardListReplyCountDTO;
import com.zerock.spring_boot_ex.dto.PageRequestDTO;
import com.zerock.spring_boot_ex.dto.PageResponseDTO;

public interface BoardService {
    Long register(BoardDTO boardDTO); //등록

    BoardDTO readOne(Long bno); //조회

    void modify(BoardDTO boardDTO); //수정

    void remove(Long bno); //삭제

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO); //목록, 검색기능

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);


    /**
     * DTO 객체를 Entity 객체로 변환하는 메서드입니다.
     * @param boardDTO DTO 객체
     * @return 변환된 Entity 객체
     */
    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();
         // DTO 객체의 fileNames에서 파일명을 추출하여 Entity의 imageSet에 추가합니다.
        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    /**
     * Entity 객체를 DTO로 변환하는 메서드입니다.
     * @param board Entity 객체
     * @return 변환된 DTO 객체
     */
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                        .bno(board.getBno())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .writer(board.getWriter())
                        .regDate(board.getRegDate())
                        .modDate(board.getModDate())
                        .build();

        // Entity 객체의 imageSet에서 파일명을 추출하여 DTO의 fileNames에 설정합니다.
        List<String> fileName = board.getImageSet().stream()
                            .sorted().map(BoardImage -> 
                            BoardImage.getUuid()+ "_" +BoardImage.getFileName()
                            ).collect(Collectors.toList());

        boardDTO.setFileNames(fileName);
        return boardDTO;
    }
}
