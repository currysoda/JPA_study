package jpabook.jpashop.shipping.entity;

import lombok.Getter;

@Getter
public enum ShippingStatus {
	READY(1), COMPLETE(2);
	
	private int code;
	
	private ShippingStatus(int code) {
		this.code = code;
	}
}