package com.zerock.spring_boot_ex.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerock.spring_boot_ex.dto.upload.UploadFileDTO;
import com.zerock.spring_boot_ex.dto.upload.UploadResultDTO;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

@RestController //모든 핸들러 메서드가 HTTP 응답으로 직접 데이터를 반환함
@Log4j2 
public class UpDownController {
    @Value("${com.zerock.upload.path}") //설정 파일을 읽어서 변수 값을 사용
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록") //Swagger API 문서화를 위해 사용되는 어노테이션
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //HTTP 요청을 처리하는 핸들러 메서드

    public String upload(UploadFileDTO uploadFileDTO) {
        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null) {

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename(); //원본 이름

                log.info(originalName);

                String uuid = UUID.randomUUID().toString(); //UUID 생성

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName); //UUID(16자리)_원본이름 

                boolean image = false;

                try{
                    multipartFile.transferTo(savePath);

                    //파일이 이미지 파일이라면...
                    if(Files.probeContentType(savePath).startsWith("image")) {
                        image = true;

                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName); //작은 이미지는 s_를 붙인다.

                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200); //200 x 200 사이즈로 만든다.
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder().uuid(uuid)
                                        .fileName(originalName)
                                        .img(image)
                                        .build()
                );

            }); //end each
        }//end if
        return null;
    }
}
