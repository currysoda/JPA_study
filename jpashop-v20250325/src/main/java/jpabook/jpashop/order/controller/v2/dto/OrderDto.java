package jpabook.jpashop.order.controller.v2.dto;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.entity.OrderItem;
import jpabook.jpashop.order.entity.OrderStatus;
import lombok.Builder;

public record OrderDto(
	Long id,
	String orderNumber,
	Long memberId,
	Long ShippingId,
	LocalDateTime orderDate,
	OrderStatus orderStatus,
	List<Long> orderItems
) {
	
	public static OrderDto from(Order order) {
		
		List<OrderItem> tempOrderItems   = order.getOrderItems();
		List<Long>      tempOrderItemIds = new ArrayList<>();
		for (OrderItem oi : tempOrderItems)
		{
			tempOrderItemIds.add(oi.getId());
		}
		
		tempOrderItemIds = List.copyOf(tempOrderItemIds);
		
		return OrderDto.builder()
		               .id(order.getId())
		               .orderNumber(order.getOrderNumber())
		               .memberId(order.getMember().getId())
		               .ShippingId(order.getShipping().getId())
		               .orderDate(order.getOrderDate())
		               .orderStatus(order.getStatus())
		               .orderItems(tempOrderItemIds)
		               .build();
	}
	
	// compact constructor = canonical constructor (record 고유 문법)
	// new OrderDto(...) 직접 호출이든 builder().build() 든 항상 이 로직을 거침
	@Builder
	public OrderDto {
		orderItems = (orderItems == null) ? List.of() : List.copyOf(orderItems); // 방어적 복사(defensive copy)
	}
	
	@Override
	public String toString() {
		return "OrderDto[id=%d, orderNumber=%s, memberId=%d, shippingId=%d, orderDate=%s, orderStatus=%s, orderItemCount=%d]"
			.formatted(id, orderNumber, memberId, ShippingId, orderDate, orderStatus, orderItems.size());
	}
}
