package com.zerock.spring_boot_ex.domain;

import lombok.*;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;

import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
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

    //제목, 내용은 수정 가능 메소드
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @OneToMany(mappedBy = "board", 
                cascade = {CascadeType.ALL},  // board 의 모든 상태 변경이 BoardImage 에 영향을 미친다.
                fetch = FetchType.LAZY,
                orphanRemoval = true) //하위 엔티티의 참조가 없는 상태가 되면 삭제가 이루어지도록 설정
    @Builder.Default
    @BatchSize(size = 20) //비슷한 여러 쿼리문을 20개씩 모아서 사용하겠다는 것
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName) {
        //uuid, fileName 를 이용하여 BoardImage 객체 생성
        BoardImage boardImage = BoardImage.builder() 
                        .uuid(uuid)
                        .fileName(fileName)
                        .board(this)  //양방향이라 서로 일치하도록 하기 위해 this 사용
                        .ord(imageSet.size()) //이미지 갯수 
                        .build();
        imageSet.add(boardImage);  //BoardImage 객체를 imageSet에 추가
    }

    public void clearImages() { //첨부 파일 삭제
        imageSet.forEach(BoardImage -> BoardImage.changBoard(null));

        this.imageSet.clear();
    }
}
