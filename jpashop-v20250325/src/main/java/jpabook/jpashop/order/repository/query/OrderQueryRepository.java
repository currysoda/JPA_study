package jpabook.jpashop.order.repository.query;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 엔티티가 아닌 DTO 로 직접 조회하는 쿼리 전담 리포지토리
 * - 일반 OrderRepository 와 관심사 분리
 * - 1+1 쿼리: Order flat 조회 후 OrderItem IN 쿼리로 일괄 조회 → Java 에서 매핑
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	/**
	 * Order 목록을 DTO 로 조회 (orderItems 포함)
	 *
	 * Step1: Order flat 조회 (orderItems 제외)
	 * Step2: orderIds IN 쿼리로 OrderItem 일괄 조회 → orderId 기준 Map 으로 그룹핑
	 * Step3: 각 OrderQueryDto 에 orderItems 채우기
	 */
	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> orders = findOrders();

		List<Long> orderIds = orders.stream()
		                            .map(OrderQueryDto::orderId)
		                            .collect(Collectors.toList());

		Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItems(orderIds);

		orders.forEach(o -> o.orderItems().addAll(
			orderItemMap.getOrDefault(o.orderId(), List.of())
		));

		return orders;
	}

	/**
	 * Step1 — Order flat 조회 (orderItems 컬렉션 제외)
	 */
	private List<OrderQueryDto> findOrders() {
		return em.createQuery(
			             """
				             select new jpabook.jpashop.order.repository.query.OrderQueryDto(
				                 o.id,
				                 o.orderNumber,
				                 m.id,
				                 s.id,
				                 o.orderDate,
				                 o.status
				             )
				             from Order o
				             join o.member m
				             join o.shipping s
				             """,
			             OrderQueryDto.class)
		         .getResultList();
	}

	/**
	 * Step2 — orderIds IN 쿼리로 OrderItem 일괄 조회
	 * Order N건이어도 이 쿼리는 1번만 나감
	 */
	private Map<Long, List<OrderItemQueryDto>> findOrderItems(List<Long> orderIds) {
		List<OrderItemQueryDto> orderItems = em.createQuery(
			                                           """
				                                           select new jpabook.jpashop.order.repository.query.OrderItemQueryDto(
				                                               oi.order.id,
				                                               i.name,
				                                               oi.price,
				                                               oi.quantity
				                                           )
				                                           from OrderItem oi
				                                           join oi.item i
				                                           where oi.order.id in :orderIds
				                                           """,
			                                           OrderItemQueryDto.class)
		                                       .setParameter("orderIds", orderIds)
		                                       .getResultList();

		return orderItems.stream()
		                 .collect(Collectors.groupingBy(OrderItemQueryDto::orderId));
	}
}
