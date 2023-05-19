package com.zerock.spring_boot_ex.domain;

import javax.persistence.Entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity {
    @Id
    private String mid; //회원 아이디

    private String mpw; //패스워드
    private String email; //이메일
    private boolean del; //탈퇴여부

    private boolean social; //소셜 로그인 자동 회원 가입 여부

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpw ){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){this.social = social;}
}
