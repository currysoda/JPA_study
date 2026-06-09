package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.item.entity.Book;
import jpabook.jpashop.item.entity.Item;
import jpabook.jpashop.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
@Rollback(false) // DB에서 결과 직접 확인하고 싶을 때 주석 해제
@Commit
@DisplayName("ItemService 전체 테스트")
public class ItemServiceTest {
	
	@Autowired
	private ItemService itemService;
	
	@BeforeEach
	void setUp() {
		for (int i = 0; i < 10; i++)
		{
			Book book = Book.builder()
			                .name("book-" + i)
			                .price(10000 + (i * 1000))
			                .stockQuantity(100 + i)
			                .author("author-" + i)
			                .isbn("isbn-" + i)
			                .build();
			itemService.saveItem(book);
		}
	}
	
	@Nested
	@DisplayName("아이템 저장")
		// @Rollback // 아래 테스트 할때 주석 풀어야함
	class 아이템_저장 {
		
		@Test
		@DisplayName("아이템_저장_후_id로_조회_가능")
		public void 아이템_저장_후_id로_조회_가능() {
			// Given
			Book book = Book.builder()
			                .name("new-book")
			                .price(20000)
			                .stockQuantity(50)
			                .author("new-author")
			                .isbn("new-isbn")
			                .build();
			
			// When
			itemService.saveItem(book);
			Item found = itemService.getItem(book.getId());
			
			// Then
			assertThat(found).isNotNull();
			assertThat(found.getId()).isEqualTo(book.getId());
			assertThat(found.getName()).isEqualTo("new-book");
			assertThat(found.getPrice()).isEqualTo(20000);
			assertThat(found.getStockQuantity()).isEqualTo(50);
		}
	}
	
	@Nested
	@DisplayName("아이템 수정")
	@Rollback // 아래 테스트 할때 주석 풀어야함
	class 아이템_수정 {
		
		@Test
		@DisplayName("아이템_정보_업데이트_더티체킹으로_반영")
		public void 아이템_정보_업데이트_더티체킹으로_반영() {
			
			// Given
			Item target = itemService.getItemByName("book-0");
			Long id     = target.getId();
			
			// When - 더티 체킹으로 UPDATE 쿼리 발생
			itemService.updateItem(id, "updated-book", 99999, 999);
			Item updated = itemService.getItem(id);
			
			// Then
			assertThat(updated.getName()).isEqualTo("updated-book");
			assertThat(updated.getPrice()).isEqualTo(99999);
			assertThat(updated.getStockQuantity()).isEqualTo(999);
		}
	}
	
	@Nested
	@DisplayName("아이템 조회")
	class 아이템_조회 {
		
		@Test
		@DisplayName("전체_아이템_조회_setUp_데이터_10개_반환")
		public void 전체_아이템_조회_setUp_데이터_10개_반환() {
			// When
			List<Item> items = itemService.getItems();
			
			// Then
			assertThat(items).hasSize(10);
			log.info("전체 아이템 수={}", items.size());
		}
		
		@Test
		@DisplayName("id로_아이템_조회_성공")
		public void id로_아이템_조회_성공() {
			// Given - setUp에서 저장된 book-3 을 이름으로 먼저 조회해 id 획득
			Item saved = itemService.getItemByName("book-3");
			Long id    = saved.getId();
			
			// When
			Item found = itemService.getItem(id);
			
			// Then
			assertThat(found).isNotNull();
			assertThat(found.getName()).isEqualTo("book-3");
			assertThat(found.getPrice()).isEqualTo(13000);
		}
		
		@Test
		@DisplayName("없는_id로_조회시_null_반환")
		public void 없는_id로_조회시_null_반환() {
			// When
			Item found = itemService.getItem(999999L);
			
			// Then
			assertThat(found).isNull();
		}
		
		@Test
		@DisplayName("이름으로_아이템_조회_성공")
		public void 이름으로_아이템_조회_성공() {
			// When
			Item found = itemService.getItemByName("book-5");
			
			// Then
			assertThat(found).isNotNull();
			assertThat(found.getName()).isEqualTo("book-5");
			assertThat(found.getPrice()).isEqualTo(15000);
			assertThat(found.getStockQuantity()).isEqualTo(105);
		}
		
		@Test
		@DisplayName("없는_이름으로_조회시_null_반환")
		public void 없는_이름으로_조회시_null_반환() {
			// When
			Item found = itemService.getItemByName("존재하지않는책");
			
			// Then
			assertThat(found).isNull();
		}
	}
	
	@Nested
	@DisplayName("재고 관리")
	class 재고_관리 {
		
		@Test
		@DisplayName("restock_재고_수량_증가_확인")
		public void restock_재고_수량_증가_확인() {
			// Given
			Item target        = itemService.getItemByName("book-0");
			Long id            = target.getId();
			int  originalStock = target.getStockQuantity(); // 100
			
			// When
			itemService.restock(id, 50);
			Item restocked = itemService.getItem(id);
			
			// Then
			assertThat(restocked.getStockQuantity()).isEqualTo(originalStock + 50);
		}
		
		@Test
		@DisplayName("재고_0에서_restock_호출시_정상_증가")
		public void 재고_0에서_restock_호출시_정상_증가() {
			// Given
			Book zeroStockBook = Book.builder()
			                         .name("zero-stock-book")
			                         .price(5000)
			                         .stockQuantity(0)
			                         .author("author")
			                         .isbn("zero-isbn")
			                         .build();
			itemService.saveItem(zeroStockBook);
			Long id = zeroStockBook.getId();
			
			// When
			itemService.restock(id, 30);
			Item restocked = itemService.getItem(id);
			
			// Then
			assertThat(restocked.getStockQuantity()).isEqualTo(30);
		}
		
		@Test
		@DisplayName("재고_부족시_removeStock_NotEnoughStockException_발생")
		public void 재고_부족시_removeStock_NotEnoughStockException_발생() {
			// Given - 재고 5개짜리 아이템
			Book lowStockBook = Book.builder()
			                        .name("low-stock-book")
			                        .price(5000)
			                        .stockQuantity(5)
			                        .author("author")
			                        .isbn("low-isbn")
			                        .build();
			itemService.saveItem(lowStockBook);
			Item saved = itemService.getItem(lowStockBook.getId());
			
			// Then - 재고(5)보다 많은 수량(100) 차감 시도
			assertThatThrownBy(() -> saved.removeStock(100))
				.isInstanceOf(NotEnoughStockException.class);
		}
	}
}
