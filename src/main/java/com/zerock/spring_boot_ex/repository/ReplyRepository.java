package com.zerock.spring_boot_ex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zerock.spring_boot_ex.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.board.bno = :bno")

    /*
        메소드 파라미터의 이름이 쿼리의 매개변수 이름과 다른 경우 @Param 을 사용ㅈ
        @Repository
        public interface UserRepository extends JpaRepository<User, Long> {
            @Query("SELECT u FROM User u WHERE u.email = :emailAddress")
            User findByEmailAddress(@Param("emailAddress") String email);
}
    */

    Page<Reply> listOfBoard(@Param("bno") Long bno,@Param("pageable") Pageable pageable); //11버전 이상은 @Param 을 사용해야 한다.
     
    void deleteByBoard_Bno(@Param("bno") Long bno); //특정 게시물에 해당하는 데이터를 삭제(댓글, 첨부파일)
}
