package jpabook.jpashop.address;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

@Embeddable
@Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode
// Setter 열면 절때 안됨
public class Address {
	
	private String city;
	private String street;
	private String zipcode;
	
	@Override
	public String toString() {
		return "city : " + city + " street : " + street + " zipcode : " + zipcode + System.lineSeparator();
	}
}