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
	 */
	@Override
	@Transactional
	public void updateItem(Long id, String name, int price, int stockQuantity) {
		Item item = itemRepository.findOne(id);
		item.update(name, price, stockQuantity);
	}

	@Override
	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	@Override
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}
