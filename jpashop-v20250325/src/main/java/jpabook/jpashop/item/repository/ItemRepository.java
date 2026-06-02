package jpabook.jpashop.item.repository;

import jpabook.jpashop.item.entity.Item;

import java.util.List;

public interface ItemRepository {

	void save(Item item);

	Item findOne(Long id);

	List<Item> findAll();
}
