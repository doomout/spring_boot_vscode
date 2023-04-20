package com.zerock.spring_boot_ex.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 고유키 생성
    private Long bno;

    @Column(length = 500, nullable = false) //길이 500, null 허용 안함
    private String title;

    @Column(length = 2000, nullable = false) //길이 2000, null 허용 안함
    private String content;

    @Column(length = 50, nullable = false) //길이 50, null 허용 안함
    private String writer;

    public Board() {

    }

    //제목, 내용은 수정 가능 메소드
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
