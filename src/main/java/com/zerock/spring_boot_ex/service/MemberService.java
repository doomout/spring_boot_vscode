package com.zerock.spring_boot_ex.service;

import com.zerock.spring_boot_ex.dto.MemberJoinDTO;

public interface MemberService {
    static class MidExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
