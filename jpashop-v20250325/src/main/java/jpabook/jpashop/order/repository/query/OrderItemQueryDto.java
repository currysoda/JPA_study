package jpabook.jpashop.order.repository.query;

import lombok.Builder;

/**
 * OrderQueryRepository 전용 DTO — 엔티티 대신 직접 DB 값을 받아냄
 *
 * @param orderId   소속 주문 ID (IN 쿼리 후 Order 에 매핑하는 키)
 * @param itemName  상품명 (item join)
 * @param price     주문 당시 가격
 * @param quantity  수량
 */
public record OrderItemQueryDto(
	Long orderId,
	String itemName,
	Integer price,
	Integer quantity
) {

	// compact constructor — @Builder 로 빌더 생성
	@Builder
	public OrderItemQueryDto {
	}
}
