package com.onlinebookstore.bookstore.model.entity;


import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private BookOrder bookOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private boolean isSuccessful;
}
