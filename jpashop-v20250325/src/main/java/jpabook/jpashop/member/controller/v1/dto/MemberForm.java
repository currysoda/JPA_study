package jpabook.jpashop.member.controller.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor // Spring 6: 생성자가 하나면 @ModelAttribute 바인딩 시 생성자 주입 방식으로 동작함
@Builder // 빌더 패턴: MemberForm.builder().name("홍길동").city("서울").build() 형태로 생성 가능
public class MemberForm {
	
	@NotEmpty(message = "회원 이름은 필수 입니다") // Bean Validation: 빈 문자열/null 허용 안 함
	private String name;
	
	private String city;
	private String street;
	private String zipcode;
	
}