package jpabook.jpashop.shipping.repository;


import jpabook.jpashop.shipping.entity.Shipping;

public interface ShippingRepository {
	
	// create
	Shipping save(Shipping shipping);
}
