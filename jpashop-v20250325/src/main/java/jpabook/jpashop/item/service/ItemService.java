package jpabook.jpashop.item.service;

import jpabook.jpashop.item.entity.Item;

import java.util.List;

public interface ItemService {
	
	void saveItem(Item item);
	
	void updateItem(Long id, String name, int price, int stockQuantity);
	
	List<Item> getItems();
	
	Item getItem(Long itemId);
	
	Item getItemByName(String name);
	
	void restock(Long id, int stockQuantity);
}
