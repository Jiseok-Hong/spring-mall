package hjs.mall.repository;

import hjs.mall.domain.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
class TeamJpaRepositoryTest {

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Autowired
    MemberRepository memberJpaRepository;


    @Test
    @Rollback(value = false)
    public void joinTest() throws Exception {
        // given
        Member member1 = Member.builder()
                .userId("john")
                .userName("john")
                .build();

        memberJpaRepository.save(member1);

        Team team1 = Team.builder()
                .name("team1")
                .build();
        team1.setMember(member1);

        Long findId = teamJpaRepository.join(team1);
        // when
        Team findTeam = teamJpaRepository.findById(findId);
        // then
        assertThat(findTeam.getName()).isEqualTo("team1");
    }

}