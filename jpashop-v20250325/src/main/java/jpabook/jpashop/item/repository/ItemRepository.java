package jpabook.jpashop.item.repository;

import jpabook.jpashop.item.entity.Item;

import java.util.List;

public interface ItemRepository {
	
	// 등록
	void save(Item item);
	
	// 찾기
	Item findOne(Long id);
	
	Item findOneByName(String name);
	
	List<Item> findAll();
}
