package jpabook.jpashop.shipping.entity;

import jpabook.jpashop.address.Address;
import jpabook.jpashop.common.BaseEntity;
import jpabook.jpashop.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Getter
@Table(name = "shipping")
public class Shipping extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shipping_id")
	private Long id;
	
	@OneToOne(mappedBy = "shipping", fetch = FetchType.LAZY)
	@Setter
	private Order order;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "city", column = @Column(name = "shippingAddress_city")),
		@AttributeOverride(name = "street", column = @Column(name = "shippingAddress_street")),
		@AttributeOverride(name = "zipcode", column = @Column(name = "shippingAddress_zipcode"))
	})
	@Setter
	private Address address;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "shipping_status")
	@Setter
	private ShippingStatus status; // ENUM [READY(준비), COMPLETE(배송)]
	
	@Builder
	public Shipping(Address address) {
		this.status = ShippingStatus.READY;
		this.address = address;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
}