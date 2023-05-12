package com.zerock.spring_boot_ex.controller.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;


@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) { //에러발생시 json으로 응답 메세지 생성 후 전송
        log.error(e);
        Map<String, String> errorMap = new HashMap<>();
        if(e.hasErrors()) {
            BindingResult bindingResult = e.getBindingResult();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }
        return ResponseEntity.badRequest().body(errorMap);
    }


    //handleFKException()은  DataIntegrityViolationException 에러가 발생하면 
    //constraint fails를 클라이언트로 전송한다.
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e) { 
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", "" +System.currentTimeMillis());
        errorMap.put("msg", "constraint fails");
        return ResponseEntity.badRequest().body(errorMap);
    }

    //댓글 데이터가 존재하지 않는 경우 예외 처리
    @ExceptionHandler({
                    NoSuchElementException.class, 
                    EmptyResultDataAccessException.class }) //존재하지 않는 댓글 번호로 또 요청 했을 때
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleNoSuchException(Exception e) { 
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", "" +System.currentTimeMillis());
        errorMap.put("msg", "No Such Element Exception");
        return ResponseEntity.badRequest().body(errorMap);
    }
}
