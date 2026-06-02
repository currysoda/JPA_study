package jpabook.jpashop.item.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

	private final EntityManager em;

	@Override
	public void save(Item item) {
		if (item.getId() == null) {
			em.persist(item);
		} else {
			em.merge(item);
		}
	}

	@Override
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}

	@Override
	public List<Item> findAll() {
		return em.createQuery("select i from Item i", Item.class).getResultList();
	}
}
