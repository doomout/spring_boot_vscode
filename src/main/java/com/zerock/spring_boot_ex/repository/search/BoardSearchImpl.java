package com.zerock.spring_boot_ex.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.zerock.spring_boot_ex.domain.Board;
import com.zerock.spring_boot_ex.domain.QBoard;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    /* 
    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board; //Q도메인  객체
        JPQLQuery<Board> query = from(board); //select.... from board
        query.where(board.title.contains("50")); //where title like....

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }
    */


    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable ) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        //검색 조건과 키워드가 있다면
        if((types != null && types.length > 0) && keyword != null ) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type: types) {
                switch(type) {
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

        //paging
        this.getQuerydsl().applyPagination(pageable, query);   
            
        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
