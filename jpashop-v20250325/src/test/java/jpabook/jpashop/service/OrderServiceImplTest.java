package jpabook.jpashop.service;

import jpabook.jpashop.address.Address;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderStatus;
import jpabook.jpashop.item.entity.Book;
import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.order.repository.OrderRepository;
import jpabook.jpashop.order.repository.OrderRepositoryImpl;
import jpabook.jpashop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class OrderServiceImplTest {
	
	private final EntityManager   em;
	private final OrderService    OrderService;
	private final OrderRepository orderRepository;
	
	@Test
	public void 상품주문() throws Exception {
		
		
	}
	
	@Test
	public void 상품주문_재고수량초과() {
		//Given
		
		//When, Then
	}
	
	@Test
	public void 주문취소() {
		//Given
		
		
		//When
		
		
		//Then
		
		
	}
	
	// private Member createMember() {
	//
	// }
	//
	// private Book createBook(String name, int price, int stockQuantity) {
	//
	// }
}