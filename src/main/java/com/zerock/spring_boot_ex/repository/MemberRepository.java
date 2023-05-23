package com.zerock.spring_boot_ex.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.zerock.spring_boot_ex.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    // Member 엔티티의 roleSet 속성을 함께 로딩하기 위해 엔티티 그래프를 사용하여 회원 정보를 조회합니다.
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(@Param("mid") String mid); //11버전 이상은 @Param 을 사용해야 한다.


    // 이메일을 통해 회원 정보를 찾을 수 있도록 설정합니다.
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(@Param("email") String email); //이메일을 통해 회원 정보를 찾을 수 있도록 설정

    // 암호를 변경하기 위해 회원의 비밀번호를 업데이트합니다.
    @Modifying //DML(insert,update,delete) 처리 가능
    @Transactional
    @Query("update Member m set m.mpw =:mpw where m.mid = :mid ") //암호 변경
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);

}
