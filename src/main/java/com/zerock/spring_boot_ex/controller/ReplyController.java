package com.zerock.spring_boot_ex.controller;

import com.zerock.spring_boot_ex.dto.PageRequestDTO;
import com.zerock.spring_boot_ex.dto.PageResponseDTO;
import com.zerock.spring_boot_ex.dto.ReplyDTO;
import com.zerock.spring_boot_ex.service.ReplyService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor //의존성 주입을 위한
public class ReplyController {
    private final ReplyService replyService;

    @ApiOperation(value="Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(
        @Valid @RequestBody ReplyDTO replyDTO, //유효성 검증
        BindingResult bindingResult) throws BindException {
        
        log.info(replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resulMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resulMap.put("rno", rno);
        
        return resulMap;
    }

    //특정 게시물의 댓글 목록
    @ApiOperation(value = "Replies of Board", notes="GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value="/list/{bno}") //게시물 번호
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;
    }
    
    //특정 댓글 조회
    @ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}") //댓글 번호
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {
        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }

    //특정 댓글 삭제
    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}") //삭제할 댓글 번호
    public Map<String, Long>remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }
}
