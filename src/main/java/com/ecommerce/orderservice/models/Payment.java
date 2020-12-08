package com.ecommerce.orderservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Item.class)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    private Item item;

    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "payment_date")
    private Date paymentDate;
    @Column(name = "payment_confirmation_number")
    private String paymentConfirmationNumber;
    @Column(name = "payment_amount")
    private Double paymentAmount;
    @Column(name = "payment_card_number")
    private String paymentCardNumber;
    @Column(name = "payment_card_cvv")
    private String paymentCardCVV;

    public Payment(String paymentMethod, Date paymentDate, String paymentConfirmationNumber, Double paymentAmount, String paymentCardNumber, String paymentCardCVV) {
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentConfirmationNumber = paymentConfirmationNumber;
        this.paymentAmount = paymentAmount;
        this.paymentCardNumber = paymentCardNumber;
        this.paymentCardCVV = paymentCardCVV;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentConfirmationNumber() {
        return paymentConfirmationNumber;
    }

    public void setPaymentConfirmationNumber(String paymentConfirmationNumber) {
        this.paymentConfirmationNumber = paymentConfirmationNumber;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCardNumber() {
        return paymentCardNumber;
    }

    public void setPaymentCardNumber(String paymentCardNumber) {
        this.paymentCardNumber = paymentCardNumber;
    }

    public String getPaymentCardCVV() {
        return paymentCardCVV;
    }

    public void setPaymentCardCVV(String paymentCardCVV) {
        this.paymentCardCVV = paymentCardCVV;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
