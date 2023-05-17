package com.zerock.spring_boot_ex.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.dto.BoardDTO;
import com.zerock.spring_boot_ex.dto.BoardListAllDTO;
import com.zerock.spring_boot_ex.dto.BoardListReplyCountDTO;
import com.zerock.spring_boot_ex.dto.PageRequestDTO;
import com.zerock.spring_boot_ex.dto.PageResponseDTO;
import com.zerock.spring_boot_ex.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
/*
    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }
*/
    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = dtoToEntity(boardDTO);

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        //board_image까지 조인처리 되는 findByWithImages() 이용
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        //게시글 조회
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        //게시글이 없으면 예외 발생
        Board board = result.orElseThrow();

        //조회글의 제목과 내용을 업데이트
        board.change(boardDTO.getTitle(), boardDTO.getContent());

        //기존의 첨부파일의 제거
        board.clearImages();

        //첨부 파일 이름이 있으면
        if(boardDTO.getFileNames() != null) {
            //첨부 파일 추가
            for(String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }
        //최종적으로 수정된 게시글 저장
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    //게시물만 보여줌
    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    //게시물 + 댓글 수 보여줌
    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] type = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(type, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())    
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        return null;
    }
}
