package jpabook.jpashop.shipping.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.shipping.entity.Shipping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor // EntityManager 생성자 주입
public class ShippingRepositoryImpl implements ShippingRepository {

	private final EntityManager em;

	@Override
	public Shipping save(Shipping shipping) {
		em.persist(shipping);
		return shipping;
	}
}
