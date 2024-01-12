package hjs.mall.service;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.domain.Member;
import hjs.mall.exception.DuplicatedMemberIdException;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
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
                .build();

        memberRepository.save(member);
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
}
