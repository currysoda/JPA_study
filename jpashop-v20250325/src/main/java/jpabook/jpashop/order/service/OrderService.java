package jpabook.jpashop.order.service;

import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.shipping.entity.Shipping;

import java.util.List;

public interface OrderService {
	
	// memberId, itemId, count, shipping 받아서 내부에서 조립 후 저장 → 생성된 order id 반환
	Long createOrder(Long memberId, Long itemId, int count, Shipping shipping);
	
	Order getOrder(Long id);
	
	Order getOrderByOrderNumber(String orderNumber);
	
	List<Order> getOrderListByMember(Member member);
	
	List<Order> getAllOrders();
}
