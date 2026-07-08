package jpabook.jpashop.order.repository;

import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.controller.dto.OrderSearch;

import java.util.List;

public interface OrderRepository {
	
	void save(Order order);
	
	Order findOne(Long id);
	
	List<Order> findAllByString(OrderSearch orderSearch);
	
	List<Order> findAllByCriteria(OrderSearch orderSearch);
	
	Order findOneByOrderNumber(String orderNumber);
	
	List<Order> findAllByMember(Member member);
	
	List<Order> findAllWithOrderItems();
}
