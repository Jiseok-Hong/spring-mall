package hjs.mall.repository;


import hjs.mall.domain.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private EntityManager em;

    public void save(Item item) {
        em.persist(item);
    }

    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.of(item);
    }

    public List<Item> findAll(int offset, int limit) {
        return em.createQuery("select i from Item i", Item.class)
                .setFirstResult(offset)
                .setFirstResult(limit)
                .getResultList();
    }
}
