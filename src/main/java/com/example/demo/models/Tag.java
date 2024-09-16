package com.example.demo.models;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name="tags")
public class Tag {
    //Primary Key
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    //mapped by is always in context of the other side of the relationship
    //product model is supposed to have a tags property
    @ManyToMany(mappedBy="tags")
    private Set<Product> products = new HashSet<>();

    public Tag(){
    }

    public Tag(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    //Helper method to facilitate the adding and removal of products
    // from a tag
    public void addProduct(Product product){
        // the unique nature of set means we dont have to check if the product exists
        this.products.add(product);
        product.getTags().add(this);
    }

    public void removeProduct(Product product){
        this.products.remove(product);
        product.getTags().remove(this);
    }

    // toString method
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    
}
