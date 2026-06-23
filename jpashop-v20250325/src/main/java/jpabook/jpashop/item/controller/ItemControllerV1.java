package jpabook.jpashop.item.controller;

import jpabook.jpashop.item.entity.Book;
import jpabook.jpashop.item.controller.dto.BookForm;
import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemControllerV1 implements ItemController {
	
	private final ItemService itemService;
	
	@Override
	@GetMapping(value = "/items/new")
	public String createForm(Model model) {
		model.addAttribute("form", BookForm.builder().build()); // 빈 폼 렌더링용 (모든 필드 null)
		return "items/createItemForm";
	}
	
	@Override
	@PostMapping(value = "/items/new")
	public String create(BookForm form) {
		Book book = Book.builder()
		                .name(form.getName())
		                .price(form.getPrice())
		                .stockQuantity(form.getStockQuantity())
		                .author(form.getAuthor())
		                .isbn(form.getIsbn())
		                .build();
		
		itemService.saveItem(book);
		return "redirect:/items";
	}
	
	@Override
	@GetMapping(value = "/items")
	public String list(Model model) {
		List<Item> items = itemService.getItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}
	
	@Override
	@GetMapping(value = "/items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Book item = (Book) itemService.getItem(itemId);
		
		BookForm form = BookForm.builder()
		                        .id(item.getId())
		                        .name(item.getName())
		                        .price(item.getPrice())
		                        .stockQuantity(item.getStockQuantity())
		                        .author(item.getAuthor())
		                        .isbn(item.getIsbn())
		                        .build();
		
		model.addAttribute("form", form);
		return "items/updateItemForm";
	}
	
	@Override
	@PostMapping(value = "/items/{itemId}/edit")
	public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {
		itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
		return "redirect:/items";
	}
}
