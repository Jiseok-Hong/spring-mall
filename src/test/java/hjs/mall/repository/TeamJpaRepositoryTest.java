package hjs.mall.repository;

import hjs.mall.domain.Member;
import hjs.mall.domain.Team;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

        memberJpaRepository.join(member1);

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