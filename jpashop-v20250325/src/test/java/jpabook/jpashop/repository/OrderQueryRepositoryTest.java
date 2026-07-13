package jpabook.jpashop.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.address.Address;
import jpabook.jpashop.item.entity.Book;
import jpabook.jpashop.item.service.ItemService;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.service.MemberService;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderStatus;
import jpabook.jpashop.order.repository.query.OrderQueryDto;
import jpabook.jpashop.order.repository.query.OrderQueryRepository;
import jpabook.jpashop.order.service.OrderService;
import jpabook.jpashop.shipping.entity.Shipping;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OrderQueryRepository 에 query 테스트
 * fetch join 으로 order 의 toOne 을 채우고
 * BatchSize 로 orderItem 정보를 불러와서 채우는 방식
 * 결론적으로 적은 쿼리로 필요한 값을 불러오는 방식이다.
 * 엔티티 대상 조회가 아닌 DTO 대상 조회
 */
@SpringBootTest
@Slf4j
@Transactional
// @Rollback(false) // DB에서 결과 직접 확인하고 싶을 때 주석 해제
// @Commit         // DB에서 결과 직접 확인하고 싶을 때 주석 해제
@DisplayName("OrderQueryRepository DTO 조회 테스트")
public class OrderQueryRepositoryTest {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private OrderQueryRepository orderQueryRepository;
	
	@Autowired
	private OrderService  orderService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ItemService   itemService;
	
	@BeforeEach
	void setUp() {
		Member member = Member.builder()
		                      .name("query-test-member")
		                      .address(new Address("서울", "테헤란로", "06234"))
		                      .build();
		memberService.join(member);
		
		itemService.saveItem(Book.builder()
		                         .name("book-A")
		                         .price(10000)
		                         .stockQuantity(20)
		                         .author("author-A")
		                         .isbn("isbn-A")
		                         .build());
		
		itemService.saveItem(Book.builder()
		                         .name("book-B")
		                         .price(15000)
		                         .stockQuantity(20)
		                         .author("author-B")
		                         .isbn("isbn-B")
		                         .build());
		
		itemService.saveItem(Book.builder()
		                         .name("book-C")
		                         .price(20000)
		                         .stockQuantity(20)
		                         .author("author-C")
		                         .isbn("isbn-C")
		                         .build());
	}
	
	@Test
	@DisplayName("배치_IN_쿼리_콘솔_확인")
	public void 배치_IN_쿼리_콘솔_확인() {
		// Given - book-A, B, C 각각 주문 3건 생성
		Member member = memberService.getMemberByMemberName("query-test-member");
		var    bookA  = itemService.getItemByName("book-A");
		var    bookB  = itemService.getItemByName("book-B");
		var    bookC  = itemService.getItemByName("book-C");
		
		orderService.createOrder(member.getId(), bookA.getId(), 1,
		                         Shipping.builder().address(member.getAddress()).build());
		orderService.createOrder(member.getId(), bookB.getId(), 2,
		                         Shipping.builder().address(member.getAddress()).build());
		orderService.createOrder(member.getId(), bookC.getId(), 3,
		                         Shipping.builder().address(member.getAddress()).build());
		
		// 1차 캐시 초기화 → orderItems 가 lazy 미초기화 상태로 변경되어 배치 IN 쿼리 발동 가능
		em.flush();
		em.clear();

		// When - Order 엔티티 로드 후 orderItems 접근 → 전역 batchsize 가 IN 쿼리 자동 생성
		List<Order> orders = orderService.getAllOrders();
		orders.forEach(o ->
			               log.info("order={}, orderItems={}", o.getId(),
			                        o.getOrderItems().size()) // 여기서 배치 IN 쿼리 발동
		              );
		
	}
}