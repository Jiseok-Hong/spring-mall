package hjs.mall.repository;

import hjs.mall.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {

    private final EntityManager em;

    public String save(Member member) {
        em.persist(member);
        return member.getUserName();
    }

}
