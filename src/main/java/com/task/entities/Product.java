package com.task.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private long productId;

	@Column(name = "product_name", nullable = false, length = 255)
	private String productName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "price", nullable = false)
	private double price;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "description")
	private String description;

	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonIgnore
	private LocalDate createdAt;

	@Column(name = "updated_at")
	@JsonIgnore
	private LocalDate updatedAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDate.now();
	}
	
	public Product() {
		
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return String.format("Product >> {productId=%d, productName='%s', category='%s', price=%.2f, quantity=%d, description='%s', createdAt=%s, updatedAt=%s}%n",
				productId, productName, category.getCategoryName(), price, quantity, description, createdAt, updatedAt);
	}

}
