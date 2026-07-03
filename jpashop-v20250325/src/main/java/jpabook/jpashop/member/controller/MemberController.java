package jpabook.jpashop.member.controller;

import jpabook.jpashop.member.controller.v1.dto.MemberForm;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

public interface MemberController {
	
	String createForm(Model model);
	
	String create(@Valid MemberForm form, BindingResult result);
	
	String list(Model model);
}
