package com.madhurtoppo.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Forex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String currency;
    private String rate;
    private int requestCount;

}
