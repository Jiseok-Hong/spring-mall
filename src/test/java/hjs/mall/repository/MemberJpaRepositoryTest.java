package hjs.mall.repository;

import hjs.mall.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberRepository memberJpaRepository;

    @Test
    @Rollback(false)
    public void joinTest() throws Exception {
        // given
        Member member1 = Member.builder()
                .userId("john")
                .userName("john")
                .build();
        // when
        String joinedMember = memberJpaRepository.join(member1);
        // then
        Assertions.assertThat(joinedMember).isEqualTo("john");
    }
}