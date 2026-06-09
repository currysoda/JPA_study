package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jpabook.jpashop.address.Address;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Slf4j
@Transactional
// @Rollback(false) // DB에서 결과 직접 확인하고 싶을 때 주석 해제
// @Commit // 이것도 DB에서 결과 직접 확인하고 싶을 때 주석 해제
@DisplayName("MemberService 전체 테스트")
// @TestInstance(Lifecycle.PER_CLASS)
// @ExtendWith(MockitoExtension.class) // 가짜 객체 주입이 필요할 때 사용
public class MemberServiceTest {
	
	@Autowired
	private MemberService memberService;
	
	@BeforeEach
	void setUp() {
		
		Address[] addressArr = new Address[10];
		
		for (int i = 0; i < 10; i++)
		{
			addressArr[i] = Address.builder()
			                       .city("city-" + i)
			                       .zipcode("zipcode-" + i)
			                       .street("street-" + i)
			                       .build();
		}
		
		for (int i = 0; i < 10; i++)
		{
			Member member = Member.builder()
			                      .name("memberName-" + i)
			                      .address(addressArr[i])
			                      .build();
			
			memberService.join(member);
		}
		
	}
	
	@Nested
	@DisplayName("회원 가입")
	@Rollback
	class 회원_가입_절차 {
		
		@Test
		@DisplayName("회원_등록")
		// @Rollback
		public void 회원_등록() throws Exception {
			
			//Given
			Member member = Member.builder()
			                      .name("kim")
			                      .address(new Address("서울", "강남대로", "12345"))
			                      .build();
			
			//When
			Long saveId = memberService.join(member);
			
			//Then
			assertThat(memberService.getMemberByMemberId(saveId)).isEqualTo(member);
			log.info("[log.info] 저장된 회원 id = {}, name = {}", saveId, member.getName());
		}
		
		@Test
		@DisplayName("중복 회원 예외")
		// @Rollback
		public void 중복_회원_예외() throws Exception {
			//Given
			Member member1 = Member.builder().name("kim").build();
			Member member2 = Member.builder().name("kim").build();
			
			//When
			memberService.join(member1);
			
			//Then
			assertThatThrownBy(() -> memberService.join(member2))
				.isInstanceOf(IllegalStateException.class);
		}
	}
	
	@Nested
	@DisplayName("회원_조회_테스트")
	@Rollback // 테스트할때 주석 해제 / 재설정
	class 회원_조회 {
		
		@Test
		@DisplayName("전체_회원_조회")
		public void 전체_회원_조회() throws Exception {
			//When
			List<Member> members = memberService.getAllMembers();
			
			//Then
			assertThat(members).hasSize(10);
			log.info("전체 회원 수={}", members.size());
		}
		
		@Test
		@DisplayName("id로_회원_조회")
		public void id로_회원_조회() throws Exception {
			
			// Given - @BeforeEach 에서 삽입한 memberName-0 을 이름으로 조회해 id 획득
			Member saved = memberService.getMemberByMemberName("memberName-0");
			
			// When - 획득한 id 로 조회
			Member found = memberService.getMemberByMemberId(saved.getId());
			
			// Then - null 아니고 조회 성공
			assertThat(found).isNotNull();
			assertThat(found.getName()).isEqualTo("memberName-0");
		}
		
		@Test
		@DisplayName("이름으로_회원_조회")
		public void 이름으로_회원_조회() throws Exception {
			//When
			Member found = memberService.getMemberByMemberName("memberName-5");
			
			//Then
			assertThat(found).isNotNull();
			assertThat(found.getName()).isEqualTo("memberName-5");
			assertThat(found.getAddress().getCity()).isEqualTo("city-5");
		}
		
		@Test
		@DisplayName("존재하지_않는_이름으로_조회시_null반환")
		public void 존재하지_않는_이름으로_조회시_null반환() throws Exception {
			//When
			Member found = memberService.getMemberByMemberName("없는사람");
			
			//Then
			assertThat(found).isNull();
		}
	}
}
