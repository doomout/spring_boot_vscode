package com.zerock.spring_boot_ex.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zerock.spring_boot_ex.dto.BoardDTO;
import com.zerock.spring_boot_ex.dto.BoardListAllDTO;
//import com.zerock.spring_boot_ex.dto.BoardListReplyCountDTO;
import com.zerock.spring_boot_ex.dto.PageRequestDTO;
import com.zerock.spring_boot_ex.dto.PageResponseDTO;
import com.zerock.spring_boot_ex.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    @Value("${com.zerock.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;
    private final BoardService boardService;
    
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        //PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);

    }

    @PreAuthorize("hasRole('USER')") //특정 권한을 가진 사용자만 접근 가능하도록 지정
    @GetMapping("/register")
    public void registerGET() {

    }

    //게시물 등록을 처리하는 메서드
    //BoardDTO 객체를 매개변수로 받으며, 
    //BindingResult 객체를 통해 유효성 검사 결과를 확인하고, 
    //RedirectAttributes 객체를 사용하여 리다이렉트 시 데이터를 전달합니다.
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("board POST register.....");

        //유효성 검사 결과에 오류가 있는지 확인
        if(bindingResult.hasErrors()) {
            //오류가 있을 경우, bindingResult.getAllErrors()를 사용하여 모든 오류를 리다이렉트 속성으로 추가하고, 
            log.info("hase errors.........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register"; //"redirect:/board/register"로 리다이렉트
        }

        log.info(boardDTO);

        //게시물 등록 서비스를 호출하여 게시물을 등록하고, 반환된 게시물 번호를 저장합니다.
        Long bno = boardService.register(boardDTO);

        //리다이렉트 시 결과 데이터를 추가합니다. 게시물 번호를 "result"라는 이름으로 전달합니다.
        redirectAttributes.addFlashAttribute("result", bno);

        //"redirect:/board/list"로 리다이렉트하여 게시물 목록 페이지로 이동합니다.
        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()") //로그인한 사용자만 조회 가능
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("principal.username == #boardDTO.writer") //현재 로그인 ID 와 현재 파라미터가 수집한 writer가 일치 할 때만 적용되도록
    @PostMapping("/modify")
    public String modify (PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("board modify post...." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?"+link;
        }
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

/* 
    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }
*/
    @PreAuthorize("principal.username == #boardDTO.writer") //로그인 id 와 작성자 id 가 같아야만 작동
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno  = boardDTO.getBno();
        log.info("remove post.. " + bno);

        boardService.remove(bno);

        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }

    public void removeFiles(List<String> files){
        // 주어진 파일들을 순회하면서 삭제 작업 수행
        for (String fileName:files) {
            // 파일 리소스 생성
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            try {
                // 파일의 컨텐츠 타입 확인
                String contentType = Files.probeContentType(resource.getFile().toPath());

                // 파일 삭제
                resource.getFile().delete();

                // 섬네일 파일 삭제 (이미지 파일인 경우)
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }//end for
    }

}
