package hjs.mall.service;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.domain.Member;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberJpaRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void register() throws Exception{
        // given
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setUserName("john");
        createMemberDto.setUserId("john");
        createMemberDto.setPassword("1234");

        // when
        memberService.join(createMemberDto);

        // then
        Optional<Member> findMember = memberRepository.findByUserId("john");
        findMember.ifPresent(member -> Assertions.assertThat(member.getUserName()).isEqualTo("john"));
    }

}