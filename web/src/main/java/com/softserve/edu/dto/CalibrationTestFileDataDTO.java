package com.softserve.edu.dto;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.Verification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalibrationTestFileDataDTO {

    private String fileName;

    private String counterNumber;

    private Date testDate;

    private int temperature;

    private long accumulatedVolume;

    private int counterProductionYear;

    private long installmentNumber;

    private double latitude;

    private double longitude;

    private String consumptionStatus;

    private String testPhoto;

    private Verification.CalibrationTestResult testResult;

    private List<CalibrationTestDataDTO> listTestData;

    public CalibrationTestFileDataDTO() {}

    public CalibrationTestFileDataDTO(DeviceTestData testData) {
        this.fileName = testData.getFileName();
        this.counterNumber = testData.getCurrentCounterNumber();
        this.testDate = new Date(testData.getUnixTime());
        this.temperature = testData.getTemperature();
        //this.accumulatedVolume = ; // don't have this value.
        this.counterProductionYear = testData.getCounterProductionYear();
        this.installmentNumber = testData.getInstallmentNumber();
        this.latitude = testData.getLatitude();
        this.longitude = testData.getLongitude();
        this.testPhoto = testData.getTestPhoto();

        this.listTestData = new ArrayList<>();

        for (int i = 1; i <= 6; ++i ) {
            CalibrationTestDataDTO testDataDTO = new CalibrationTestDataDTO();
            int testNumber = testData.getTestNumber(i);
            if (testNumber == 0) {
                testDataDTO.setDataAvailable(false);
                continue;
            } else {
                testDataDTO.setDataAvailable(true);
            }
            String testNumberStr = "Test " + testNumber / 10;
            Integer testRepeat = testNumber % 10;
            testNumberStr += ((testRepeat != 0) ? " Repeat " + testRepeat : "");
            testDataDTO.setTestNumber(testNumberStr);
            testDataDTO.setGivenConsumption(convertImpulsesPerSecToCubicMetersPerHour(
                    testData.getTestSpecifiedConsumption(i),
                        testData.getImpulsePricePerLitre()));
            testDataDTO.setAcceptableError(testData.getTestAllowableError(i));
            testDataDTO.setInitialValue(testData.getTestInitialCounterValue(i));
            testDataDTO.setEndValue(testData.getTestTerminalCounterValue(i));
            testDataDTO.setVolumeInDevice(round(testDataDTO.getEndValue() - testDataDTO.getInitialValue(), 2));
            testDataDTO.setTestTime(round(testData.getTestDuration(i), 1));
            testDataDTO.setVolumeOfStandard(testData.getTestSpecifiedImpulsesAmount(i) * 1.0);
            testDataDTO.setActualConsumption(convertImpulsesPerSecToCubicMetersPerHour(
                    testData.getTestCorrectedCurrentConsumption(i),
                        testData.getImpulsePricePerLitre()));
            testDataDTO.setCalculationError(countCalculationError(testDataDTO.getVolumeInDevice(),
                    testDataDTO.getVolumeOfStandard()));
            testDataDTO.setBeginPhoto(testData.getBeginPhoto(i));
            testDataDTO.setEndPhoto(testData.getEndPhoto(i));

            this.listTestData.add(testDataDTO);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(String counterNumber) {
        this.counterNumber = counterNumber;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getCounterProductionYear() {
        return counterProductionYear;
    }

    public void setCounterProductionYear(int counterProductionYear) {
        this.counterProductionYear = counterProductionYear;
    }

    public long getAccumulatedVolume() {
        return accumulatedVolume;
    }

    public void setAccumulatedVolume(long accumulatedVolume) {
        this.accumulatedVolume = accumulatedVolume;
    }

    public long getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(long installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(String consumptionStatus) {
        this.consumptionStatus = consumptionStatus;
    }

    public Verification.CalibrationTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(Verification.CalibrationTestResult testResult) {
        this.testResult = testResult;
    }

    public String getTestPhoto() {
        return testPhoto;
    }

    public void setTestPhoto(String testPhoto) {
        this.testPhoto = testPhoto;
    }

    public List<CalibrationTestDataDTO> getListTestData() {
        return listTestData;
    }

    public void setListTestData(List<CalibrationTestDataDTO> listTestData) {
        this.listTestData = listTestData;
    }

    private double round(double val, int scale) {
        return new BigDecimal(val).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private double convertImpulsesPerSecToCubicMetersPerHour(double impulses, long impLitPrice) {
        return round(3.6 * impulses / impLitPrice, 3);
    }

    private double countCalculationError(double counterVolume, double standardVolume) {
        if (standardVolume < 0.0001) {
            return 0.0;
        }
        double result = (counterVolume - standardVolume) / standardVolume * 100;
        return round(result, 2);
    }
}
