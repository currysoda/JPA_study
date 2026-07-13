package jpabook.jpashop.order.service;

import java.util.List;
import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.service.ItemService;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.service.MemberService;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderItem;
import jpabook.jpashop.order.repository.OrderRepository;
import jpabook.jpashop.shipping.entity.Shipping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 기본 읽기 전용, 쓰기 메서드에 @Transactional 별도 선언
@RequiredArgsConstructor // 생성자 주입
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepository;
	private final MemberService   memberService; // 다른 도메인은 Service 를 통해 접근
	private final ItemService     itemService; // 다른 도메인은 Service 를 통해 접근
	
	@Override
	@Transactional // 쓰기 트랜잭션
	public Long createOrder(Long memberId, Long itemId, int count, Shipping shipping) {
		
		// 1. 엔티티 조회 - 다른 도메인은 Service 를 통해 접근
		Member member = memberService.getMemberByMemberId(memberId);
		Item   item   = itemService.getItem(itemId);
		
		// 2. 주문 생성
		Order order = Order.builder()
		                   .member(member)
		                   .shipping(shipping)
		                   .build();
		
		// 4. 주문상품 생성 - 생성자 내부에서 order, item 양쪽에 자동 연결 (addOrderItem)
		OrderItem.builder()
		         .order(order)
		         .item(item)
		         .quantity(count)
		         .build();
		
		// 5. 저장 - CascadeType.ALL 로 OrderItem, Shipping 함께 저장
		orderRepository.save(order);
		
		return order.getId();
	}
	
	@Override
	public Order getOrder(Long id) {
		return orderRepository.findOne(id);
	}
	
	@Override
	public Order getOrderByOrderNumber(String orderNumber) {
		return orderRepository.findOneByOrderNumber(orderNumber);
	}
	
	@Override
	public List<Order> getOrderListByMember(Member member) {
		return orderRepository.findAllByMember(member);
	}
	
	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAllWithMember();
	}
}
