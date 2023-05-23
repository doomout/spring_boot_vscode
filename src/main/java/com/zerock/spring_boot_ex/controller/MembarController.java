package com.zerock.spring_boot_ex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zerock.spring_boot_ex.dto.MemberJoinDTO;
import com.zerock.spring_boot_ex.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MembarController {
 
    private final MemberService memberService;

    @GetMapping("/join")
    public void joinGET(){

        log.info("join get...");

    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){

        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO); // 회원 가입 기능을 호출하여 memberJoinDTO를 이용해 회원 가입을 처리합니다.
        } catch (MemberService.MidExistException e) {

            redirectAttributes.addFlashAttribute("error", "mid"); // "mid"라는 속성을 가지고 에러 메시지를 설정합니다.
            return "redirect:/member/join"; // 회원 가입 페이지로 다시 리다이렉트합니다.
        }

        redirectAttributes.addFlashAttribute("result", "success"); // "result"라는 속성을 가지고 성공 메시지를 설정합니다.

        return "redirect:/member/login"; // 회원 가입 후 로그인 페이지로 리다이렉트합니다.
    }


    @GetMapping("/login")
    public void loginGET(String error, String logout) {
        log.info("login get..............");
        log.info("logout: " + logout);
    }

}
