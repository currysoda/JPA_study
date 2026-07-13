package jpabook.jpashop.order.repository.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.order.entity.OrderStatus;
import lombok.Builder;

/**
 * OrderQueryRepository 전용 DTO — 컬렉션(orderItems)은 Step2 IN 쿼리로 채움
 */
public record OrderQueryDto(
	Long orderId,
	String orderNumber,
	Long memberId,
	Long shippingId,
	LocalDateTime orderDate,
	OrderStatus orderStatus,
	List<OrderItemQueryDto> orderItems  // Step2 에서 set
) {

	// compact constructor — @Builder 로 빌더 생성, orderItems null 방어
	@Builder
	public OrderQueryDto {
		orderItems = (orderItems == null) ? new ArrayList<>() : orderItems;
	}

	// JPQL new 생성자용 — orderItems 는 빈 리스트로 시작
	public OrderQueryDto(
		Long orderId,
		String orderNumber,
		Long memberId,
		Long shippingId,
		LocalDateTime orderDate,
		OrderStatus orderStatus
	) {
		this(orderId, orderNumber, memberId, shippingId, orderDate, orderStatus, new ArrayList<>());
	}
}
