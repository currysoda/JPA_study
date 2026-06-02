package jpabook.jpashop.item.controller;

import jpabook.jpashop.item.entity.BookForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface ItemController {

	String createForm(Model model);

	String create(BookForm form);

	String list(Model model);

	String updateItemForm(@PathVariable("itemId") Long itemId, Model model);

	String updateItem(@PathVariable Long itemId, BookForm form);
}
