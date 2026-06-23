package jpabook.jpashop.item.service;

import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	
	private final ItemRepository itemRepository;
	
	@Override
	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	
	/**
	 * 영속성 컨텍스트가 자동 변경 (더티 체킹)
	 * 병합(merge) 대신 변경 감지(id 로 조회후 변경값 바꾸기)
	 */
	@Override
	@Transactional
	public void updateItem(Long id, String name, int price, int stockQuantity) {
		Item item = itemRepository.findOne(id);
		item.update(name, price, stockQuantity);
	}
	
	@Override
	public List<Item> getItems() {
		return itemRepository.findAll();
	}
	
	@Override
	public Item getItem(Long itemId) {
		return itemRepository.findOne(itemId);
	}
	
	@Override
	public Item getItemByName(String name) {
		return itemRepository.findOneByName(name);
	}
	
	@Override
	@Transactional
	public void restock(Long id, int stockQuantity) {
		Item item = itemRepository.findOne(id);
		item.restock(stockQuantity);
	}
}
