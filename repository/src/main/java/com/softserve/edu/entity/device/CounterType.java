package com.softserve.edu.entity.device;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "COUNTER_TYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String name;

    private String symbol;

    private String standardSize;

    private String manufacturer;

    private Integer calibrationInterval;

    private Integer yearIntroduction;

    private String gost;

    @ManyToOne
    @JoinColumn(name = "deviceId")
    private Device device;

    @OneToMany(mappedBy = "counterType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UnsuitabilityReason> unsuitabilitySet;

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = device.getDeviceName();
    }

    public CounterType(String name, String symbol, String standardSize, String manufacturer,
                       Integer calibrationInterval, Integer yearIntroduction, String gost, Device device) {
        this.name = name;
        this.symbol = symbol;
        this.standardSize = standardSize;
        this.manufacturer = manufacturer;
        this.calibrationInterval = calibrationInterval;
        this.yearIntroduction = yearIntroduction;
        this.gost = gost;
        this.device = device;
    }
}
