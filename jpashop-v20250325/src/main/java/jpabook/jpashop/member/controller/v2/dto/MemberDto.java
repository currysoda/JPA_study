package jpabook.jpashop.member.controller.v2.dto;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.address.Address;
import jpabook.jpashop.member.entity.Member;
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
	
	public static MemberDto from(Member member) {
		
		List<Order>    tempOrders   = member.getOrders();
		List<OrderDto> tempOrderDto = new ArrayList<>();
		for (Order o : tempOrders)
		{
			tempOrderDto.add(OrderDto.from(o));
		}
		
		tempOrderDto = List.copyOf(tempOrderDto);
		
		return MemberDto.builder()
		                .id(member.getId())
		                .name(member.getName())
		                .address(member.getAddress())
		                .orders(tempOrderDto)
		                .build();
	}
	
	// compact constructor = canonical constructor (record 고유 문법)
	// new MemberDto(...) 직접 호출이든 builder().build() 든 항상 이 로직을 거침
	@Builder
	public MemberDto {
		orders = (orders == null) ? List.of() : List.copyOf(orders);  // 방어적 복사(defensive copy)
	}
	
	@Override
	public String toString() {
		return "MemberDto[id=%d, name=%s, address=%s, orderCount=%d]"
			.formatted(id, name, address, orders.size());
	}
}
