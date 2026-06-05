package jpabook.jpashop.item.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("M")
@Getter
@Setter
public class Movie extends Item {
	
	private String director;
	private String actor;
	
	@Builder
	public Movie(String name, Integer price, Integer stockQuantity, String director, String actor) {
		super(name, price, stockQuantity);
		this.director = director;
		this.actor = actor;
	}
}