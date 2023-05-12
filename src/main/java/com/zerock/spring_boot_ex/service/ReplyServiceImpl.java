package com.zerock.spring_boot_ex.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.zerock.spring_boot_ex.domain.Reply;
import com.zerock.spring_boot_ex.dto.ReplyDTO;
import com.zerock.spring_boot_ex.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;

    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = modelMapper.map(replyDTO, Reply.class);

        Long rno = replyRepository.save(reply).getRno();

        return rno;
    }
}
