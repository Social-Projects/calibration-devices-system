package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.enumeration.verification.CalibrationTestResult;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * Calibration Test Data entity.
 * Contains information about measurement device calibration test data.
 */
@Entity
@Table(name = "CALIBRATION_TEST_DATA")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class CalibrationTestData {

    @Id
    @GeneratedValue
    private Long id;

    private Double givenConsumption;
    private Integer acceptableError;
    private Integer volumeOfStandard;
    private Double initialValue;
    private Double endValue;
    private Double volumeInDevice;

    @Temporal(TemporalType.TIMESTAMP)

    private Date testTime;
    private Double actualConsumption;
    private String consumptionStatus;
    private Double calculationError;
    private CalibrationTestResult testResult;

    @ManyToOne
    @JoinColumn(name = "calibrationTestId")
    private CalibrationTest calibrationTest;

    public CalibrationTestData(
            Double givenConsumption, Integer acceptableError, Integer volumeOfStandard, Double initialValue,
            Double endValue, Double volumeInDevice, Double actualConsumption, String consumptionStatus,
            Double calculationError, CalibrationTestResult testResult, CalibrationTest calibrationTest
    ) {
        this.givenConsumption = givenConsumption;
        this.acceptableError = acceptableError;
        this.volumeOfStandard = volumeOfStandard;
        this.initialValue = initialValue;
        this.endValue = endValue;
        this.volumeInDevice = volumeInDevice;
        this.testTime = new Date();
        this.actualConsumption = actualConsumption;
        this.consumptionStatus = consumptionStatus;
        this.calculationError = calculationError;
        this.testResult = testResult;
        this.calibrationTest = calibrationTest;
    }
}