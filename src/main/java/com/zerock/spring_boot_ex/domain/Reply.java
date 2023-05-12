package com.zerock.spring_boot_ex.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "Reply", indexes = {
    @Index(name = "idx_reply_board_bno", columnList = "board_bno") //인덱스 생성
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString (exclude = "board") //해당 객체를 사용하지 않도록 제한
public class Reply  extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY) //n:1 관계
    private Board board;

    private String replyText;
    
    private String replyer;

    //리플은 내용만 수정 가능
    public void changeText(String text) {
        this.replyText = text;
    }
}
