package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jpabook.jpashop.address.Address;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.item.entity.Book;
import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.service.ItemService;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.service.MemberService;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderStatus;
import jpabook.jpashop.order.service.OrderService;
import jpabook.jpashop.shipping.entity.Shipping;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@Rollback(false) // DB에서 결과 직접 확인하고 싶을 때 주석 해제 / 전체 테스트할때 주석 처리
@Commit         // DB에서 결과 직접 확인하고 싶을 때 주석 해제 / 전체 테스트할때 주석 처리
@DisplayName("OrderService 전체 테스트")
public class OrderServiceImplTest {
	
	@Autowired
	private OrderService  orderService;
	@Autowired
	private MemberService memberService;  // 다른 도메인은 Service 를 통해 접근
	@Autowired
	private ItemService   itemService;      // 다른 도메인은 Service 를 통해 접근
	
	// @BeforeEach 가 테스트 트랜잭션 안에서 실행되므로 @Rollback 시 setUp 데이터도 함께 롤백
	@BeforeEach
	void setUp() {
		Member member = Member.builder()
		                      .name("test-member")
		                      .address(new Address("서울", "강남대로", "12345"))
		                      .build();
		memberService.join(member);
		
		Book book = Book.builder()
		                .name("test-book")
		                .price(10000)
		                .stockQuantity(10) // 재고 10개
		                .author("test-author")
		                .isbn("test-isbn")
		                .build();
		itemService.saveItem(book);
	}
	
	@Nested
	@DisplayName("주문 생성")
		// @Rollback // 테스트 후 롤백 - DB에서 확인하고 싶으면 주석 처리
	class 주문_생성 {
		
		@Test
		@DisplayName("상품주문_정상_주문상태_ORDER")
		public void 상품주문_정상_주문상태_ORDER() {
			// Given - @BeforeEach 데이터 사용
			Member member = memberService.getMemberByMemberName("test-member");
			Item   item   = itemService.getItemByName("test-book");
			int    count  = 3;
			Shipping shipping = Shipping.builder()
			                            .address(member.getAddress()) // 회원 주소로 배송지 설정
			                            .build();
			
			// When
			Long  orderId = orderService.createOrder(member.getId(), item.getId(), count, shipping);
			Order order   = orderService.getOrder(orderId);
			
			// Then
			assertThat(order).isNotNull();
			assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER); // 주문 상태 확인
			assertThat(order.getOrderItems()).hasSize(1);               // 주문상품 1건
			assertThat(order.getOrderItems().get(0).getQuantity()).isEqualTo(count); // 수량 확인
		}
		
		@Test
		@DisplayName("상품주문_재고수량_감소_확인")
		public void 상품주문_재고수량_감소_확인() {
			// Given
			Member member        = memberService.getMemberByMemberName("test-member");
			Item   item          = itemService.getItemByName("test-book");
			int    originalStock = item.getStockQuantity(); // 10
			int    count         = 3;
			Shipping shipping = Shipping.builder()
			                            .address(member.getAddress())
			                            .build();
			
			// When
			orderService.createOrder(member.getId(), item.getId(), count, shipping);
			
			// Then - 주문 수량만큼 재고 감소
			assertThat(item.getStockQuantity()).isEqualTo(originalStock - count);
		}
		
		@Test
		@DisplayName("주문번호_자동생성_확인")
		public void 주문번호_자동생성_확인() {
			// Given
			Member member = memberService.getMemberByMemberName("test-member");
			Item   item   = itemService.getItemByName("test-book");
			Shipping shipping = Shipping.builder()
			                            .address(member.getAddress())
			                            .build();
			
			// When
			Long  orderId = orderService.createOrder(member.getId(), item.getId(), 1, shipping);
			Order order   = orderService.getOrder(orderId);
			
			// Then - 주문번호는 UUID 로 자동 생성
			assertThat(order.getOrderNumber()).isNotNull();
			assertThat(order.getOrderNumber()).isNotBlank();
		}
	}
	
	@Nested
	@DisplayName("주문 예외")
	@Rollback // 테스트 후 롤백 - DB에서 확인하고 싶으면 주석 처리
	class 주문_예외 {
		
		@Test
		@DisplayName("재고수량_초과_주문시_NotEnoughStockException_발생")
		public void 재고수량_초과_주문시_NotEnoughStockException_발생() {
			// Given
			Member member = memberService.getMemberByMemberName("test-member");
			Item   item   = itemService.getItemByName("test-book"); // 재고 10개
			Shipping shipping = Shipping.builder()
			                            .address(member.getAddress())
			                            .build();
			
			// Then - 재고(10) 보다 많은 수량(100) 주문 시 예외 발생
			assertThatThrownBy(() ->
				                   orderService.createOrder(member.getId(), item.getId(), 100, shipping))
				.isInstanceOf(NotEnoughStockException.class);
		}
	}
	
	@Nested
	@DisplayName("주문 조회")
	@Rollback // 테스트 후 롤백 - DB에서 확인하고 싶으면 주석 처리
	class 주문_조회 {
		
		@Test
		@DisplayName("id로_주문_조회_성공")
		public void id로_주문_조회_성공() {
			// Given
			Member member = memberService.getMemberByMemberName("test-member");
			Item   item   = itemService.getItemByName("test-book");
			Shipping shipping = Shipping.builder()
			                            .address(member.getAddress())
			                            .build();
			Long orderId = orderService.createOrder(member.getId(), item.getId(), 1, shipping);
			
			// When
			Order order = orderService.getOrder(orderId);
			
			// Then
			assertThat(order).isNotNull();
			assertThat(order.getId()).isEqualTo(orderId);
		}
		
		@Test
		@DisplayName("주문번호로_주문_조회_성공")
		public void 주문번호로_주문_조회_성공() {
			// Given
			Member member = memberService.getMemberByMemberName("test-member");
			Item   item   = itemService.getItemByName("test-book");
			Shipping shipping = Shipping.builder()
			                            .address(member.getAddress())
			                            .build();
			Long  orderId = orderService.createOrder(member.getId(), item.getId(), 1, shipping);
			Order created = orderService.getOrder(orderId);
			
			// When
			Order found = orderService.getOrderByOrderNumber(created.getOrderNumber());
			
			// Then
			assertThat(found).isNotNull();
			assertThat(found.getId()).isEqualTo(orderId);
		}
		
		@Test
		@DisplayName("회원으로_주문목록_조회_성공")
		public void 회원으로_주문목록_조회_성공() {
			// Given - 같은 회원으로 주문 2건 생성
			Member member = memberService.getMemberByMemberName("test-member");
			Item   item   = itemService.getItemByName("test-book");
			
			orderService.createOrder(member.getId(), item.getId(), 1,
			                         Shipping.builder().address(member.getAddress()).build());
			orderService.createOrder(member.getId(), item.getId(), 2,
			                         Shipping.builder().address(member.getAddress()).build());
			
			// When
			var orders = orderService.getOrderListByMember(member);
			
			// Then
			assertThat(orders).hasSize(2);
		}
	}
}
