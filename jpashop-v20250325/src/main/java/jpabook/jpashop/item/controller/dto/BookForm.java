package jpabook.jpashop.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor // Spring 6: 생성자가 하나면 @ModelAttribute 바인딩 시 생성자 주입 방식으로 동작함
@Builder // 빌더 패턴: BookForm.builder().name("JPA").price(10000).build() 형태로 생성 가능
public class BookForm {
	
	private Long id;
	
	private String name;
	private int    price;
	private int    stockQuantity;
	
	private String author;
	private String isbn;
}