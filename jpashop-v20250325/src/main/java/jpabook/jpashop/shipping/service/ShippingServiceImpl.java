package jpabook.jpashop.shipping.service;

import jpabook.jpashop.shipping.entity.Shipping;
import jpabook.jpashop.shipping.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 기본 읽기 전용, 쓰기 메서드에 @Transactional 별도 선언
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private final ShippingRepository shippingRepository;

	@Override
	@Transactional // 쓰기 트랜잭션
	public Shipping register(Shipping shipping) {
		return shippingRepository.save(shipping);
	}
}
