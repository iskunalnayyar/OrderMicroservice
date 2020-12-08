package com.ecommerce.orderservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Order.class)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_qty")
    private int itemQty;
    @Column(name = "order_shipping_charges")
    private double orderShippingCharges;
    @Column(name = "order_subtotal")
    private double orderSubtotal;
    @Column(name = "order_total")
    private double orderTotal;
    @Column(name = "order_tax")
    private double orderTax;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToOne(mappedBy = "item")
    private BillingDetails billingDetails;

    @OneToOne(mappedBy = "item")
    private ShippingDetails shippingDetails;

    public Item() {
    }

    public Item(String itemName, int itemQty, double orderShippingCharges, double orderSubtotal, double orderTotal, double orderTax) {
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.orderShippingCharges = orderShippingCharges;
        this.orderSubtotal = orderSubtotal;
        this.orderTotal = orderTotal;
        this.orderTax = orderTax;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public double getOrderShippingCharges() {
        return orderShippingCharges;
    }

    public void setOrderShippingCharges(double orderShippingCharges) {
        this.orderShippingCharges = orderShippingCharges;
    }

    public double getOrderSubtotal() {
        return orderSubtotal;
    }

    public void setOrderSubtotal(double orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public double getOrderTax() {
        return orderTax;
    }

    public void setOrderTax(double orderTax) {
        this.orderTax = orderTax;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public BillingDetails getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(BillingDetails billingDetails) {
        this.billingDetails = billingDetails;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
