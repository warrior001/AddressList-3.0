package com.example.nir.addresslist30;

/**
 * Created by Nir on 9/18/2017.
 */

public class Items {
    private String name;
    private Double price;
    private Integer quantity;

    public Items(){  }

    public Items(String name, Double price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        Double total =this.price*this.quantity;
        return this.name + "  x" + this.quantity +"         "+Math.round(total * 100.0) / 100.0+"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Items)) return false;

        Items items = (Items) o;

        return getName().equals(items.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
