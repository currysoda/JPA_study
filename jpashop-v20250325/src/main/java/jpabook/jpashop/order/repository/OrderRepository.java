package jpabook.jpashop.order.repository;

import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderSearch;

import java.util.List;

public interface OrderRepository {
	
	void save(Order order);
	
	Order findOne(Long id);
	
	List<Order> findAllByString(OrderSearch orderSearch);
	
	List<Order> findAllByCriteria(OrderSearch orderSearch);
	
	List<Order> findOneByOrderNumber(String orderNumber);
}
