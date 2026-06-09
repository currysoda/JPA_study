package jpabook.jpashop.order.controller;

import jpabook.jpashop.order.entity.OrderSearch;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface OrderController {

	String createForm(Model model);

	String order(@RequestParam("memberId") Long memberId,
	             @RequestParam("itemId") Long itemId,
	             @RequestParam("count") int count);

	String orderList(OrderSearch orderSearch, Model model);

	String cancelOrder(@PathVariable("orderId") Long orderId);
}
