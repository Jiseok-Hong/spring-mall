package hjs.mall.service;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.controller.dto.LoginMemberDto;
import hjs.mall.controller.dto.LoginMemberResponse;
import hjs.mall.domain.Member;
import hjs.mall.exception.DuplicatedMemberIdException;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.MemberRepository;
import hjs.mall.security.CustomUserDetail;
import hjs.mall.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberJpaRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserDetailService userDetailService;
    @Transactional
    public void join(CreateMemberDto memberDto) {

        checkDuplicatedMemberId(memberDto.getUserId());

        String salt = getSalt();
        String encryptedPassword = encryptCode(memberDto.getPassword(), salt);

        Member member = Member.builder()
                .userName(memberDto.getUserName())
                .userId(memberDto.getUserId())
                .password(encryptedPassword)
                .salt(salt)
                .role(memberDto.getRole())
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public LoginMemberResponse login(LoginMemberDto loginMemberDto){
        Member member = memberRepository.findByUserId(loginMemberDto.getUserId())
                .orElseThrow(() -> new BadCredentialsException("The User id is not existed"));


        String rawPassword = loginMemberDto.getPassword() + member.getSalt();
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BadCredentialsException("The password is wrong");
        }

        //password is correct and the user is authenticated
        String refreshToken = jwtProvider.createRefreshToken(member.getUserId(), member.getRole());

        member.changeRefreshToken(refreshToken);

        return LoginMemberResponse.builder()
                .role(member.getRole())
                .userId(member.getUserId())
                .accessToken(jwtProvider.createAccessToken(member.getUserId(), member.getRole()))
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public ResponseAcessToken generateAccessTokenWithRefreshToken(String refreshToken) {
        if (refreshToken != null && jwtProvider.validateToken(refreshToken, false)) {
            // check refresh token
            refreshToken = refreshToken.split(" ")[1].trim();
            CustomUserDetail userDetails = userDetailService.loadUserByUsername(jwtProvider.getAccount(refreshToken, false));
            String newAccessToken = jwtProvider
                    .createAccessToken(userDetails.getMember().getUserId(), userDetails.getMember().getRole());
            Authentication auth = jwtProvider.getAuthentication(newAccessToken, true);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseAcessToken(newAccessToken);
        }

        throw new BadCredentialsException("Refresh Token is invalid");
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    private void checkDuplicatedMemberId(String memberUserId) {
        Optional<Member> byUserId = memberRepository.findByUserId(memberUserId);
        if (byUserId.isPresent()) {
            throw new DuplicatedMemberIdException("The Member Id is already occupied by other member");
        }
    }

    private String encryptCode(String password, String salt) {

        return passwordEncoder.encode(password+salt);
    }

    private String getSalt() {

        //1. Random, byte object generation
        SecureRandom r = new SecureRandom ();
        byte[] salt = new byte[20];

        //2. generate random number
        r.nextBytes(salt);

        //3. byte To String
        StringBuffer sb = new StringBuffer();
        for(byte b : salt) {
            sb.append(String.format("%02x", b));
        };

        return sb.toString();
    }

    @Data
    @AllArgsConstructor
    public class ResponseAcessToken {
        private String accessToken;
    }
}
