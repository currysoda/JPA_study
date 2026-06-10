package jpabook.jpashop.order.entity;

import jpabook.jpashop.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jpabook.jpashop.item.entity.Item;
import jakarta.persistence.*;

@Entity(name = "OrderItem")
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;      // 상품
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;    // 주문
	
	@Column(name = "order_item_price")
	private Integer price; // 주문 당시 가격
	
	@Column
	private Integer quantity; // 주문 갯수
	
	//==생성 메서드==//
	@Builder
	public OrderItem(Order order, Item item, Integer quantity) {
		this.order = order;
		this.item = item;
		this.price = item.getPrice(); // 할인 정책은 없다고 가정함
		this.quantity = quantity;
		item.removeStock(quantity);
		setOrderItem(order, item);
	}
	
	// 연관관계 메소드
	public void setOrderItem(Order order, Item item) {
		item.getOrderItems().add(this);
		order.getOrderItems().add(this);
	}
}