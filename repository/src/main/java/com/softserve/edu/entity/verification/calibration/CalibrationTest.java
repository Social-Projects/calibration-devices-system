package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.Verification;
import lombok.*;
import org.hibernate.type.IntegerType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Calibration Test entity.
 * Contains data about measurement device calibration test.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "CALIBRATION_TEST")
@NoArgsConstructor
public class CalibrationTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTest;
    private String counterNumber;
    private String capacity;
    private Integer settingNumber;
    private Double latitude;
    private Double longitude;
    private Integer temperature;

    @Enumerated(EnumType.STRING)
    private Verification.ConsumptionStatus consumptionStatus;

    @Enumerated(EnumType.STRING)
    private Verification.CalibrationTestResult testResult;

    private String photoPath;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name",
                    column = @Column(name = "documentName")),
            @AttributeOverride(name = "sign",
                    column = @Column(name = "documentSign"))
    })
    private MeteorologicalDocument meteorologicalDocument;

    @ManyToOne
    @JoinColumn(name = "verificationId")
    private Verification verification;

    @OneToMany(mappedBy = "calibrationTest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CalibrationTestData> calibrationTestDataList;

    public CalibrationTest(String name, Integer settingNumber, Double latitude,
                           Double longitude, Long unixTime, String counterNumber,
                           Verification verification, String capacity, Integer temperature) {
        this.name = name;
        this.dateTest = new Date(unixTime);
        this.capacity = capacity;
        this.settingNumber = settingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.counterNumber = counterNumber;
        this.consumptionStatus = Verification.ConsumptionStatus.IN_THE_AREA;
        this.testResult = Verification.CalibrationTestResult.SUCCESS;
        this.verification = verification;
        this.temperature = temperature;
    }
}
