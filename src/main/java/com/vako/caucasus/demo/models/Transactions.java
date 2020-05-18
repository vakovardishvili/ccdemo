package com.vako.caucasus.demo.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Transactions {

    @Getter
    public enum TrTypes {
        PID_IN(1),
        PID_OUT(-1),
        PHONE_IN(1),
        PHONE_OUT(-1),
        BALANCE_IN(1);
        BigDecimal direction;
        TrTypes(int direction) {
            this.direction = BigDecimal.valueOf(direction);
        }
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", amount=" + amount +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    @Enumerated(EnumType.STRING)
    TrTypes type;
    BigDecimal amount;
}
