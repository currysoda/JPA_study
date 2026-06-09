package jpabook.jpashop.item.entity;

import javax.swing.tree.ExpandVetoException;
import jpabook.jpashop.common.BaseEntity;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Getter;

import jpabook.jpashop.category.entity.CategoryItem;
import jpabook.jpashop.order.entity.OrderItem;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity(name = "Item")
@Table(name = "item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;
	
	@Column(name = "item_name")
	private String name;
	
	@Column(name = "item_price")
	private Integer price;
	
	@Column(name = "item_stock_quantity")
	private Integer stockQuantity;
	
	// 자식 클래스용
	protected Item(String name, Integer price, Integer stockQuantity) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}
	
	// 학습용
	//==비즈니스 로직==//
	public void update(String name, Integer price, Integer stockQuantity) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}
	
	// 재고관리 메소드
	public void restock(Integer quantity) {
		stockQuantity += quantity;
	}
	
	public void removeStock(Integer quantity) {
		Integer restStock = this.stockQuantity - quantity;
		if (restStock < 0)
		{
			throw new NotEnoughStockException("재고가 부족합니다. 현재 재고: " + this.stockQuantity);
		}
		this.stockQuantity = restStock;
	}
	
	@OneToMany(mappedBy = "item")
	private List<CategoryItem> categoryItems = new ArrayList<>();
	
	@OneToMany(mappedBy = "item")
	private List<OrderItem> orderItems = new ArrayList<>();
}