package com.homework.model;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "category")
public class Category {
@Id
@Column(name = "title")
@NotNull
    private String name;
@Column(name = "parent_title")
    private String parentName;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String parentName) {

        this.name = name;
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(parentName, category.parentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentName);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}
