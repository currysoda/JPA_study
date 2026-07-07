package jpabook.jpashop.member.controller.v2;

import java.util.ArrayList;
import jpabook.jpashop.address.Address;
import jpabook.jpashop.member.controller.MemberController;
import jpabook.jpashop.member.controller.v1.dto.MemberForm;
import jpabook.jpashop.member.controller.v2.dto.MemberDto;
import jpabook.jpashop.member.service.MemberService;
import jpabook.jpashop.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V2 는 Entity 직접 반환 하지않고 DTO 를 이용하는 것이 핵심
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/member")
public class MemberControllerV2 implements MemberController {
	
	private final MemberService memberService;
	
	// DTO 사용패턴 V2 에서 제일 중요한것 entity 직접사용X
	@GetMapping(value = "/members")
	@Override
	public String list(Model model) {
		List<MemberDto> members = new ArrayList<>();
		
		List<Member> allMembers = memberService.getAllMembers();
		
		for (Member m : allMembers)
		{
			members.add(MemberDto.from(m));
		}
		
		model.addAttribute("members", members);
		return "members/memberList";
	}
	
	@GetMapping(value = "/members/new")
	@Override
	public String createForm(Model model) {
		model.addAttribute("memberForm", MemberForm.builder().build()); // 빈 폼 렌더링용 (모든 필드 null)
		return "members/createMemberForm";
	}
	
	@PostMapping(value = "/members/new")
	@Override
	public String create(@Valid MemberForm form, BindingResult memberFormResult) {
		
		if (memberFormResult.hasErrors())
		{
			return "members/createMemberForm";
		}
		
		Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
		Member member = Member.builder()
		                      .name(form.getName())
		                      .address(address)
		                      .build();
		
		memberService.join(member);
		
		return "redirect:/";
	}
	
	
}