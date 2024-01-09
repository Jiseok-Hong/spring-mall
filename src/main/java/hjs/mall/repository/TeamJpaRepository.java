package hjs.mall.repository;

import hjs.mall.domain.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager em;

    public Long join(Team team) {
        em.persist(team);
        return team.getId();
    }

    public Team findById(Long id) {
        return em.find(Team.class, id);
    }
}
