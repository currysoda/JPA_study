package jpabook.jpashop.item.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item {
	
	private String artist;
	private String etc;
	
	@Builder
	public Album(String name, Integer price, Integer stockQuantity, String artist, String etc) {
		super(name, price, stockQuantity);
		this.artist = artist;
		this.etc = etc;
	}
}