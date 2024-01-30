package hjs.mall;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.domain.Basket;
import hjs.mall.domain.Member;
import hjs.mall.domain.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Basket basket = new Basket();

            Member member = Member.builder()
                    .role(Role.ADMIN)
                    .userId("john")
                    .password("1234")
                    .userName("john")
                    .build();

            member.setInitialBasket(basket);

            em.persist(member);
        }

    }
}


