package jpabook.jpashop.order.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import static jpabook.jpashop.item.entity.QItem.item;
import static jpabook.jpashop.member.entity.QMember.member;
import static jpabook.jpashop.order.entity.QOrder.order;
import static jpabook.jpashop.order.entity.QOrderItem.orderItem;
import static jpabook.jpashop.shipping.entity.QShipping.shipping;

/**
 * OrderQueryRepository 와 동일한 로직 — JPQL 대신 QueryDSL 사용
 * Q클래스는 빌드 후 build/generated/sources/annotationProcessor 에 생성됨
 */
@Repository
public class OrderQueryDslRepository {

	private final JPAQueryFactory queryFactory;

	public OrderQueryDslRepository(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	/**
	 * Order 목록을 DTO 로 조회 (orderItems 포함)
	 *
	 * Step1: Order flat 조회
	 * Step2: orderIds IN 쿼리로 OrderItem 일괄 조회 → orderId 기준 Map 그룹핑
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
	 * Step1 — Order flat 조회
	 * Projections.constructor : DTO 생성자 파라미터 타입·순서로 매핑
	 */
	private List<OrderQueryDto> findOrders() {
		return queryFactory
			.select(Projections.constructor(OrderQueryDto.class,
				order.id,
				order.orderNumber,
				member.id,
				shipping.id,
				order.orderDate,
				order.status
			))
			.from(order)
			.join(order.member, member)
			.join(order.shipping, shipping)
			.fetch();
	}

	/**
	 * Step2 — orderIds IN 쿼리로 OrderItem 일괄 조회
	 * .where(orderItem.order.id.in(orderIds)) : QueryDSL IN 절
	 */
	private Map<Long, List<OrderItemQueryDto>> findOrderItems(List<Long> orderIds) {
		List<OrderItemQueryDto> orderItems = queryFactory
			.select(Projections.constructor(OrderItemQueryDto.class,
				orderItem.order.id,
				item.name,
				orderItem.price,
				orderItem.quantity
			))
			.from(orderItem)
			.join(orderItem.item, item)
			.where(orderItem.order.id.in(orderIds))
			.fetch();

		return orderItems.stream()
		                 .collect(Collectors.groupingBy(OrderItemQueryDto::orderId));
	}
}