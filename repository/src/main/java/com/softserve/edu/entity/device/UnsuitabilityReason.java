package com.softserve.edu.entity.device;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


/**
 * Created by Sonka on 20.11.2015.
 */
@Entity
@Table(name = "REASONS_UNSUITABILITY")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UnsuitabilityReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "counterTypeId")
    private CounterType counterType;

    public UnsuitabilityReason(String name, CounterType counterType) {
        this.name = name;
        this.counterType = counterType;
    }
}
