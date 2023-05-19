package com.zerock.spring_boot_ex.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.zerock.spring_boot_ex.security.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;

@Log4j2
@Configuration //애플리케이션의 구성 요소를 정의하고, 빈 객체를 생성하고 등록하는 역할
@RequiredArgsConstructor //해당 클래스의 필드를 기반으로 생성자를 자동으로 생성

//메서드 수준에서의 보안 설정을 활성화하는 데 사용
//(prePostEnabled = true) @PreAuthorize와 @PostAuthorize 어노테이션을 사용하여 메서드 실행 전후에 보안 검사를 수행할 수 있도록 설정
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class CustomSecurityConfig {
    //주입 필요
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //패스워드 암호화
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  

        log.info("------------configure-------------------");
       
        //커스텀 로그인 페이지
        http.formLogin().loginPage("/member/login");

        //CSRF 토큰 비활성화(POST/PUT/DELETE 이용하는 모든 코드를 수정해야 해서 비활성화)
        http.csrf().disable();

        http.rememberMe()
                .key("12345678") //쿠키의 값을 인코딩 하기 위한 키값
                .tokenRepository(persistentTokenRepository()) //Remember Me 토큰을 저장하기 위한 PersistentTokenRepository를 설정합니다.
                .userDetailsService(userDetailsService) //사용자 인증을 위한 UserDetailsService를 설정
                .tokenValiditySeconds(60*60*24*30); //쿠키 유지 시간

        //http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()); //403

        //http.oauth2Login().loginPage("/member/login").successHandler(authenticationSuccessHandler());

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------web configure-------------------");
        //정적 파일들 필터에서 제외
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}
