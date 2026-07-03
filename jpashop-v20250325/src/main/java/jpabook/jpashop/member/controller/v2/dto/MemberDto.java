package jpabook.jpashop.member.controller.v2.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import jpabook.jpashop.address.Address;
import jpabook.jpashop.order.entity.Order;
import lombok.Builder;
import lombok.ToString;
import lombok.ToString.Exclude;
import lombok.Value;

/**
 * builder 로 생성 권장
 */
public record MemberDto(
	@NotNull Long id, // 실무에선 public id + db id 별개 필요
	@NotNull String name,
	Address address,
	List<OrderDto> orders
) {
	
	@Builder  // 생성자(constructor) 레벨에 적용
	public MemberDto {
		orders = (orders == null) ? List.of() : List.copyOf(orders);  // 방어적 복사(defensive copy)
	}
	
	@Override
	public String toString() {
		return "MemberDto[id=%d, name=%s, address=%s, orderCount=%d]"
			.formatted(id, name, address, orders.size());
	}
}
