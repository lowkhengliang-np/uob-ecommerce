package com.example.demo.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message="Name cannot be blank")
    @Size(min=2, max=100, message="Name must be between 2 and 100 characters")
    @Column(nullable=false)
    private String name;

    @DecimalMin(value="0.01", message="Price must be greater than 0.01")
    @Column(nullable=false, precision = 10, scale = 2)
    private BigDecimal price;
    
    //Create the foreign key for the product
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    public Product() {
    }

    public Product(String description, String name, BigDecimal price) {
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return Id;
    }
    
    public void setId(Long id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // toString method
   @Override
   public String toString() {
       return "Product{" +
               "id=" + Id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               '}';
   }

   // equals and hashCode methods
   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       Product product = (Product) o;
       return Objects.equals(Id, product.Id) &&
               Objects.equals(name, product.name) &&
               Objects.equals(description, product.description) &&
               Objects.equals(price, product.price);
   }

   @Override
   public int hashCode() {
       return Objects.hash(Id, name, description, price);
   }
}
