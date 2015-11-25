package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.verification.Verification;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double givenConsumption;
    private Long acceptableError;
    private Double volumeOfStandard;
    private Double initialValue;
    private Double endValue;
    private Double volumeInDevice;
    private Double duration;
    private Integer testPosition;
    private Double actualConsumption;
    private Double calculationError;

    @Enumerated(EnumType.STRING)
    private Verification.ConsumptionStatus consumptionStatus;
    @Enumerated(EnumType.STRING)
    private Verification.CalibrationTestResult testResult;

    @ManyToOne
    @JoinColumn(name = "calibrationTestId")
    private CalibrationTest calibrationTest;

    @OneToMany(mappedBy = "calibrationTestData")
    private List<CalibrationTestIMG> testIMGs;

    public CalibrationTestData(Double givenConsumption, Long acceptableError, Double volumeOfStandard,
                               Double initialValue, Double endValue, Double volumeInDevice,
                               Double actualConsumption, Double calculationError,
                               CalibrationTest calibrationTest, Double duration, Long lowerConsumptionLimit,
                               Long upperConsumptionLimit, Integer testPosition) {
        this.givenConsumption = givenConsumption;
        this.acceptableError = acceptableError;
        this.volumeOfStandard = volumeOfStandard;
        this.initialValue = initialValue;
        this.endValue = endValue;
        this.volumeInDevice = volumeInDevice;
        this.actualConsumption = actualConsumption;
        this.calculationError = calculationError;
        if (this.getEndValue()==0 ||this.getInitialValue() > this.getEndValue() ){
            this.testResult = Verification.CalibrationTestResult.RAW;
        }else {
            if (this.getActualConsumption() <= this.getAcceptableError()) {
                this.testResult = Verification.CalibrationTestResult.SUCCESS;
            } else {
                this.testResult = Verification.CalibrationTestResult.FAILED;
            }
        }
        if ((this.getGivenConsumption() - (this.getGivenConsumption() * lowerConsumptionLimit / 100)
                <= this.getActualConsumption()) & (this.getActualConsumption() <= (this.getGivenConsumption()
                + (this.getGivenConsumption() * upperConsumptionLimit) / 100))) {
            this.consumptionStatus = Verification.ConsumptionStatus.IN_THE_AREA;
        } else {
            this.consumptionStatus = Verification.ConsumptionStatus.NOT_IN_THE_AREA;
        }
        this.calibrationTest = calibrationTest;
        this.duration = duration;
        this.testPosition = testPosition;
    }
}
