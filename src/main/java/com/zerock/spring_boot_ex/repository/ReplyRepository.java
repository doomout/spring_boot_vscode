package com.zerock.spring_boot_ex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerock.spring_boot_ex.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
}
