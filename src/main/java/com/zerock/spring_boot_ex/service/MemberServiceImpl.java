package com.zerock.spring_boot_ex.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerock.spring_boot_ex.domain.Member;
import com.zerock.spring_boot_ex.domain.MemberRole;
import com.zerock.spring_boot_ex.dto.MemberJoinDTO;
import com.zerock.spring_boot_ex.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        String mid = memberJoinDTO.getMid();

        // 회원이 이미 존재하는지 확인합니다.
        boolean exist = memberRepository.existsById(mid);

        if(exist){ // 이미 회원이 존재하면 MidExistException을 던집니다.
            throw new MidExistException();
        }

        // MemberJoinDTO를 Member 엔티티로 매핑합니다.
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        
        // 비밀번호를 암호화합니다.
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));

        // 회원에게 USER 역할을 할당합니다.
        member.addRole(MemberRole.USER);

        log.info("=======================");
        log.info(member);
        log.info(member.getRoleSet());

         // 회원을 리포지토리에 저장합니다.
        memberRepository.save(member);

    }
}
