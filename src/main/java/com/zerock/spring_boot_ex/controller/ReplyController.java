package com.zerock.spring_boot_ex.controller;

import com.zerock.spring_boot_ex.dto.ReplyDTO;

import io.swagger.annotations.ApiOperation;

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


@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {
    @ApiOperation(value="Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> register(
        @Valid @RequestBody ReplyDTO replyDTO,
        BindingResult bindingResult) throws BindException {
        
        log.info(replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resulMap = new HashMap<>();
        resulMap.put("rno", 111L);
        
        return ResponseEntity.ok(resulMap);
    }
        
}
