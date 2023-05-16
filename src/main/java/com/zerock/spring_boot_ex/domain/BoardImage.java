package com.zerock.spring_boot_ex.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{ //Comparable 인터페이스는 순번에 맞게 정렬하기 위해 사용
    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Board board;

    @Override
    public int compareTo(BoardImage other) {
        return this.ord = other.ord;
    }

    public void changBoard(Board board) {
        this.board = board;
    }
}
