package net.bitkap.marketplace.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppParameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int nextPipeline=1;

    private int totalPipeline = 10;

    private double allowedPaymentsPerUserAndPerDay = 20;
}
