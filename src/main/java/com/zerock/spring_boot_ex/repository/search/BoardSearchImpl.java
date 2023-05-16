package com.zerock.spring_boot_ex.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.domain.QBoard;
import com.zerock.spring_boot_ex.domain.QReply;
import com.zerock.spring_boot_ex.dto.BoardListReplyCountDTO;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    
    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board; //Q도메인  객체

        JPQLQuery<Board> query = from(board); //select.... from board

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

        booleanBuilder.or(board.title.contains("11")); // title like ...

        booleanBuilder.or(board.content.contains("11")); // content like ....

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();
        
        return null;
    }
    
 

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable ) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        //검색 조건과 키워드가 있다면
        if((types != null && types.length > 0) && keyword != null ) {

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type: types) {
                switch(type) {
                    case "t": //제목
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c": //내용
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w": //작성자
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } //end for
            query.where(booleanBuilder);
        } //end if

        //bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);   
            
        List<Board> list = query.fetch();
        long count = query.fetchCount();

        //list : 실제 목록 데이터
        //pageable : 페이지 관련 정보를 가진 객체
        //count : 전체 개수
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);

        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        //검색조건
        if((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type: types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } //end for
            query.where(booleanBuilder);
        } //end if
        //bno > 0
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(
            BoardListReplyCountDTO.class,
            board.bno,
            board.title,
            board.writer,
            board.regDate,
            reply.count().as("replyCount")
        ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();


        return new PageImpl<>(dtoList, pageable, count);
    }

    //Board와 Reply를 left join 처리 하고 쿼리를 실행해서 내용 확인
    @Override
    public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); //left join

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging

        List<Board> boardList = boardJPQLQuery.fetch();

        boardList.forEach(board1 -> {
            System.out.println(board1.getBno());
            System.out.println(board1.getImageSet());
            System.out.println("----------------");
        });

        return null;
    }
}
