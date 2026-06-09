package jpabook.jpashop.order.entity;

import jakarta.annotation.Nonnull;
import java.util.UUID;
import jpabook.jpashop.common.BaseEntity;
import jpabook.jpashop.shipping.entity.Shipping;
import jpabook.jpashop.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity(name = "Order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id; // DB 용 id
	
	@Column(name = "order_number", unique = true)
	private String orderNumber; // 고객이 볼 주문번호
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member; // 주문 회원
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "shipping_id")
	private Shipping shipping; // 배송정보
	
	@Column(name = "order_date")
	private LocalDateTime orderDate; // 주문시간
	
	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status; // 주문상태 [ORDER, CANCEL]
	
	// ==생성 메서드(Factory Method)==
	@Builder
	public Order(@Nonnull Member member, @Nonnull Shipping shipping) {
		this.member = member;
		this.status = OrderStatus.ORDER;
		this.orderDate = LocalDateTime.now();
		this.shipping = shipping;
		makeOrderNumber();
	}
	
	// 학습용이니 uuid 사용
	// 주문번호는 생성시 바로 할당되고 변경되면 안됨
	private void makeOrderNumber() {
		if (orderNumber == null)
		{
			this.orderNumber = UUID.randomUUID().toString();
		}
		else
		{
			System.out.println("이미 주문번호가 존재합니다.");
		}
	}
	
	//==연관관계 메서드==//
	// Member-Order
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	// 근데 remove 할 일이 있을까? 잘 모르겠음
	public void removeMember(Member member) {
		member.getOrders().remove(this);
		this.member = null;
	}
	
	// order-delivery
	public void setShipping(Shipping shipping) {
		shipping.setOrder(this);
		this.shipping = shipping;
	}
	
	// order-orderitem
	public void addOrderItems() {
	
	}
	
	//==비즈니스 로직==//
	// 비즈니스 로직은 파일 분리
	
	// 어떤 아이템 몇개 주문
	
	// 주문 취소
	
	// ==조회 로직(Query Logic)==
}