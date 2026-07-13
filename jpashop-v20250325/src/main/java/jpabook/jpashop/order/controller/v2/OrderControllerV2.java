package jpabook.jpashop.order.controller.v2;

import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.service.ItemService;
import jpabook.jpashop.member.entity.Member;
import jpabook.jpashop.member.service.MemberService;
import jpabook.jpashop.order.controller.OrderController;
import jpabook.jpashop.order.controller.dto.OrderSearch;
import jpabook.jpashop.order.controller.v2.dto.OrderDto;
import jpabook.jpashop.order.entity.Order;
import jpabook.jpashop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * v2 의 핵심은 entity 직접 import 를 제거하고 dto 로 전환하는 것이 핵심
 * fetch join 예제는 repo-service layer 에서
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/order")
public class OrderControllerV2 implements OrderController {
	
	private final OrderService  orderService;
	private final MemberService memberService;
	private final ItemService   itemService;
	
	// entity 직접 import 제거하고 dto 사용 예제
	@Override
	@GetMapping(value = "/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		
		List<OrderDto> dtoList = new ArrayList<>();
		for (Order o : orderService.getAllOrders())
		{
			dtoList.add(OrderDto.from(o));
		}
		model.addAttribute("orders", dtoList);
		return "order/orderList";
	}
	
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
	@PostMapping(value = "/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		// orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
}
