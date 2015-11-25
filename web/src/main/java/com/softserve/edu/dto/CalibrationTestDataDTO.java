package com.softserve.edu.dto;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;


public class CalibrationTestDataDTO {
    private Boolean dataAvailable;
    private String testNumber;
    private Double givenConsumption;
    private Long acceptableError;
    private Double volumeOfStandard;
    private Double initialValue;
    private Double endValue;
    private Double volumeInDevice;
    private Double testTime;
    private Double actualConsumption;
    private Verification.ConsumptionStatus consumptionStatus;
    private Double calculationError;
    private String beginPhoto;
    private String endPhoto;
    private Verification.CalibrationTestResult testResult;
    private Integer testPosition;


    public Boolean getDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(Boolean dataAvailable) {
        this.dataAvailable = dataAvailable;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    public Double getGivenConsumption() {
        return givenConsumption;
    }

    public void setGivenConsumption(Double givenConsumption) {
        this.givenConsumption = givenConsumption;
    }

    public Long getAcceptableError() {
        return acceptableError;
    }

    public void setAcceptableError(Long acceptableError) {
        this.acceptableError = acceptableError;
    }

    public Double getVolumeOfStandard() {
        return volumeOfStandard;
    }

    public void setVolumeOfStandard(Double volumeOfStandard) {
        this.volumeOfStandard = volumeOfStandard;
    }

    public Double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    public Double getEndValue() {
        return endValue;
    }

    public void setEndValue(Double endValue) {
        this.endValue = endValue;
    }

    public Double getVolumeInDevice() {
        return volumeInDevice;
    }

    public void setVolumeInDevice(Double volumeInDevice) {
        this.volumeInDevice = volumeInDevice;
    }

    public Double getTestTime() {
        return testTime;
    }

    public void setTestTime(Double testTime) {
        this.testTime = testTime;
    }

    public Double getActualConsumption() {
        return actualConsumption;
    }

    public void setActualConsumption(Double actualConsumption) {
        this.actualConsumption = actualConsumption;
    }

    public Verification.ConsumptionStatus getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(Verification.ConsumptionStatus consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public Double getCalculationError() {
        return calculationError;
    }

    public void setCalculationError(Double calculationError) {
        this.calculationError = calculationError;
    }

    public String getBeginPhoto() {
        return beginPhoto;
    }

    public void setBeginPhoto(String beginPhoto) {
        this.beginPhoto = beginPhoto;
    }

    public String getEndPhoto() {
        return endPhoto;
    }

    public void setEndPhoto(String endPhoto) {
        this.endPhoto = endPhoto;
    }

    public Verification.CalibrationTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(Verification.CalibrationTestResult testResult) {
        this.testResult = testResult;
    }

    public CalibrationTestData saveTestData() {
        CalibrationTestData calibrationTestData = new CalibrationTestData();
        calibrationTestData.setGivenConsumption(givenConsumption);
        calibrationTestData.setAcceptableError(acceptableError);
        calibrationTestData.setVolumeOfStandard(volumeOfStandard);
        calibrationTestData.setInitialValue(initialValue);
        calibrationTestData.setEndValue(endValue);
        calibrationTestData.setVolumeInDevice(volumeInDevice);
        calibrationTestData.setActualConsumption(actualConsumption);
        calibrationTestData.setConsumptionStatus(consumptionStatus);
        //calibrationTestData.setCalculationError(calculationError);
        calibrationTestData.setTestResult(Verification.CalibrationTestResult.SUCCESS);
        return calibrationTestData;
    }

    public Integer getTestPosition() {
        return testPosition;
    }

    public void setTestPosition(Integer testPosition) {
        this.testPosition = testPosition;
    }
}