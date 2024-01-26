package hjs.mall.service;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.controller.dto.LoginMemberDto;
import hjs.mall.controller.dto.LoginMemberResponse;
import hjs.mall.domain.Member;
import hjs.mall.domain.Role;
import hjs.mall.exception.DuplicatedMemberIdException;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.security.JwtProvider;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberJpaRepository memberRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    EntityManager em;

    @BeforeEach
    public void createInitialMember() {
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserName("john");
        createMemberDto.setUserId("john");
        createMemberDto.setRole(Role.ADMIN);
        createMemberDto.setPassword("1234");

        // when
        memberService.join(createMemberDto);
    }

    @Test
    public void register() throws Exception{
        // given
        // register member executed in before each

        // then
        Optional<Member> findMember = memberRepository.findByUserId("john");
        findMember.ifPresent(member -> assertThat(member.getUserName()).isEqualTo("john"));
    }

    @Test
    public void signIn() throws Exception {
        // given
        // register member executed in before each

        // when
        LoginMemberDto loginMemberDto = new LoginMemberDto();
        loginMemberDto.setUserId("john");
        loginMemberDto.setPassword("1234");

        LoginMemberDto wrongUserId = new LoginMemberDto();
        wrongUserId.setUserId("john1");
        wrongUserId.setPassword("1234");

        LoginMemberDto wrongPassword = new LoginMemberDto();
        wrongUserId.setUserId("john");
        wrongUserId.setPassword("123");

        // then
        assertThatThrownBy(() -> memberService.login(wrongUserId))
                .isInstanceOf(BadCredentialsException.class);

        assertThatThrownBy(() -> memberService.login(wrongPassword))
                .isInstanceOf(BadCredentialsException.class);

        LoginMemberResponse login = memberService.login(loginMemberDto);
        assertThat(jwtProvider.validateToken("BEARER " + login.getAccessToken(), true))
                .isTrue();
        assertThat(jwtProvider.validateToken("BEARER " +login.getRefreshToken(), false))
                .isTrue();


    }

    @Test
    public void checkDuplicatedMemberId() throws Exception {
        // given
        // register member executed in before each

        //then
        CreateMemberDto duplicatedMemberDto = new CreateMemberDto();
        duplicatedMemberDto.setUserName("john");
        duplicatedMemberDto.setUserId("john");
        duplicatedMemberDto.setPassword("1234");

        assertThatThrownBy(() -> memberService.join(duplicatedMemberDto))
                .isInstanceOf(DuplicatedMemberIdException.class);
    }
    
    @Test
    public void checkRTR() throws Exception {
        // given
        LoginMemberDto loginMemberDto = new LoginMemberDto();
        loginMemberDto.setUserId("john");
        loginMemberDto.setPassword("1234");

        // when
        LoginMemberResponse login = memberService.login(loginMemberDto);

        String oldAccessToken = "BEARER " + login.getAccessToken();
        String oldRefreshToken = "BEARER " +login.getRefreshToken();

        assertThat(jwtProvider.validateToken(oldAccessToken, true))
                .isTrue();
        assertThat(jwtProvider.validateToken(oldRefreshToken, false))
                .isTrue();

        MemberService.ResponseAuthToken responseAuthToken = memberService
                .generateAccessTokenWithRefreshToken(oldRefreshToken);
        // then
        System.out.println("--------------second start--------------------");
        assertThatThrownBy(() -> memberService.generateAccessTokenWithRefreshToken(oldRefreshToken))
                .isInstanceOf(BadCredentialsException.class);


        //the new access token and refresh token are valid

        assertThat(jwtProvider.validateToken("BEARER " + responseAuthToken.getAccessToken(), true))
                .isTrue();
        assertThat(jwtProvider.validateToken("BEARER " + responseAuthToken.getRefreshToken(), false))
                .isTrue();

    }

}