package jpabook.jpashop.member.controller;

import jpabook.jpashop.address.Address;
import jpabook.jpashop.member.entity.MemberForm;
import jpabook.jpashop.member.service.MemberServiceImpl;
import jpabook.jpashop.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberControllerV1 implements MemberController {

	private final MemberServiceImpl memberService;

	@GetMapping(value = "/members/new")
	@Override
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberForm());
		return "members/createMemberForm";
	}

	@PostMapping(value = "/members/new")
	@Override
	public String create(@Valid MemberForm form, BindingResult result) {

		if (result.hasErrors()) {
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

	@GetMapping(value = "/members")
	@Override
	public String list(Model model) {
		List<Member> members = memberService.getAllMembers();
		model.addAttribute("members", members);
		return "members/memberList";
	}
}
