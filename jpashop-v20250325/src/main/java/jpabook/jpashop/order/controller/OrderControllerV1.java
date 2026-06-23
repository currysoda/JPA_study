package jpabook.jpashop.order.controller;

import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.service.ItemService;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.service.MemberService;
import jpabook.jpashop.order.controller.dto.OrderSearch;
import jpabook.jpashop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderControllerV1 implements OrderController {
	
	private final OrderService  orderService;
	private final MemberService memberService;
	private final ItemService   itemService;
	
	@Override
	@GetMapping(value = "/order")
	public String createForm(Model model) {
		
		List<Member> members = memberService.getAllMembers();
		List<Item>   items   = itemService.getItems();
		
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/orderForm";
	}
	
	@Override
	@PostMapping(value = "/order")
	public String order(@RequestParam("memberId") Long memberId,
	                    @RequestParam("itemId") Long itemId,
	                    @RequestParam("count") int count) {
		// orderService.order(memberId, itemId, count);
		return "redirect:/orders";
	}
	
	@Override
	@GetMapping(value = "/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		// List<Order> orders = orderService.findOrders(orderSearch);
		// model.addAttribute("orders", orders);
		return "order/orderList";
	}
	
	@Override
	@PostMapping(value = "/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		// orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
}
