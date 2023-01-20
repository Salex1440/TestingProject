package com.example.TestingProject.payment;

import com.example.TestingProject.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@ToString
@Entity(name = "Payment")
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "payments_seq",
            sequenceName = "payments_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "payments_customers_fk")
    )
    private Customer customer;

    @Column(
            name = "amount",
            nullable = false
    )
    private BigDecimal amount;

    @Column(
            name = "currency",
            columnDefinition = "VARCHAR(10)",
            nullable = false
    )
    private Currency currency;

    @Column(
            name = "source",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String source;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    public Payment() {
    }

    public Payment(Customer customer, BigDecimal amount, Currency currency, String source, String description) {
        this.customer = customer;
        this.amount = amount;
        this.currency = currency;
        this.source = source;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id.equals(payment.id) && customer.equals(payment.customer) && amount.equals(payment.amount) && currency == payment.currency && source.equals(payment.source) && Objects.equals(description, payment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, amount, currency, source, description);
    }
}
