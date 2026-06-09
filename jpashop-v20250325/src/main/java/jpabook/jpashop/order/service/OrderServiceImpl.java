package jpabook.jpashop.order.service;

import java.util.List;
import jpabook.jpashop.item.repository.ItemRepository;
import jpabook.jpashop.member.repository.MemberRepository;
import jpabook.jpashop.member.repository.MemberRepositoryImpl;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderSearch;
import jpabook.jpashop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	private final MemberRepository memberRepositoryImpl;
	private final OrderRepository  orderRepository;
	private final ItemRepository   itemRepository;
	
	/**
	 * 주문
	 */
	// @Transactional
	// public Long order(Long memberId, Long itemId, int count) {
	//
	// 	//엔티티 조회
	// 	Member member = memberRepository.findOneByMemberId(memberId);
	// 	Item   item   = itemRepository.findOne(itemId);
	//
	// 	//배송정보 생성
	// 	Delivery delivery = new Delivery();
	// 	delivery.setAddress(member.getAddress());
	// 	delivery.setStatus(DeliveryStatus.READY);
	//
	// 	//주문상품 생성
	// 	OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
	// 	//주문 생성
	//
	//
	// 	//주문 저장
	// 	orderRepository.save(order);
	// 	return order.getId();
	// }
	
	/**
	 * 주문 취소
	 */
	@Transactional
	public void cancelOrder(Long orderId) {
		
		//주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		//주문 취소
		
	}
	
	/**
	 * 주문 검색
	 */
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByString(orderSearch);
	}
}