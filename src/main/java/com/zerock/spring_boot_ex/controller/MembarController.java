package com.zerock.spring_boot_ex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MembarController {
    @GetMapping("/login")
    public void loginGET(String errorCode, String logout) {
        log.info("login get..................");
        log.info("logout: " + logout);

        if(logout != null) {
            log.info("user logout........");
        }
    }

    
}
