package jpabook.jpashop.member.controller.v2.dto;

import java.time.LocalDateTime;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderStatus;
import lombok.Builder;

/**
 * memberDto 에 orders 컬렉션에 쓸 dto
 * order id 만 가지고 필요하면 id 로 조회하기
 */
@Builder
public record OrderDto(
	Long id
) {
	
	public static OrderDto from(Order order) {
		return OrderDto.builder()
		               .id(order.getId())
		               .build();
	}
}
