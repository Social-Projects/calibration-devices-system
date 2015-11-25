package com.softserve.edu.dto;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.entity.verification.Verification.ConsumptionStatus;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CalibrationTestFileDataDTO {

    private String fileName;

    private String counterNumber;

    private Date testDate;

    private String capacity;

    private String accumulatedVolume;

    private int counterProductionYear;

    private long installmentNumber;

    private double latitude;

    private double longitude;

    private ConsumptionStatus consumptionStatus;

    private String testPhoto;

    private Verification.CalibrationTestResult testResult;

    private List<CalibrationTestDataDTO> listTestData;

    private String status;

    private Integer testPosition;


    public CalibrationTestFileDataDTO() {
    }

    public CalibrationTestFileDataDTO(String fileName, Date data, String capacity, long installmentNumber,
                                      double latitude, double longitude, String testPhoto) {
        this.fileName = fileName;
        this.testDate = data;
        this.capacity = capacity;
        this.installmentNumber = installmentNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.testPhoto = testPhoto;


    }

    public CalibrationTestFileDataDTO(DeviceTestData testData) {
        this.fileName = testData.getFileName();
        this.counterNumber = testData.getCurrentCounterNumber();
        this.testDate = new Date(testData.getUnixTime());
        this.capacity = testData.getInitialCapacity();
        //this.accumulatedVolume = ; // don't have this value.
        this.counterProductionYear = testData.getCounterProductionYear();
        this.installmentNumber = testData.getInstallmentNumber();
        this.latitude = testData.getLatitude();
        this.longitude = testData.getLongitude();
        this.testPhoto = testData.getTestPhoto();

        this.listTestData = new ArrayList<>();

        for (int i = 1; i <= 6; ++i) {
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

    public CalibrationTestFileDataDTO(CalibrationTest calibrationTest,CalibrationTestService calibrationTestService) {
        this.fileName = calibrationTest.getName();
        this.counterNumber = calibrationTest.getCounterNumber().toString();
        this.testDate = calibrationTest.getDateTest();
//      this.capacity = calibrationTest.getCapacity();
       this.accumulatedVolume = calibrationTest.getCapacity();
//      this.counterProductionYear = testData.getCounterProductionYear();
        this.installmentNumber = calibrationTest.getSettingNumber();
        this.latitude = calibrationTest.getLatitude();
        this.longitude = calibrationTest.getLongitude();
        this.testPhoto = calibrationTestService.getPhotoAsString(calibrationTest.getPhotoPath(),calibrationTest);
        this.consumptionStatus = calibrationTest.getConsumptionStatus();
        this.testResult = calibrationTest.getTestResult();
        this.listTestData = new ArrayList();
        int testNumber = 1;
        List<CalibrationTestIMG> calibrationTestIMGList;
        CalibrationTestIMG calibrationTestIMG;
        this.status = calibrationTest.getVerification().getStatus().toString();
        for (CalibrationTestData calibrationTestData : calibrationTestService.getLatestTests(calibrationTest.getCalibrationTestDataList())) {
            CalibrationTestDataDTO testDataDTO = new CalibrationTestDataDTO();
            testDataDTO.setDataAvailable(true);
            testDataDTO.setTestNumber("Test" + testNumber);
            testDataDTO.setGivenConsumption(calibrationTestData.getGivenConsumption());
            testDataDTO.setAcceptableError(calibrationTestData.getAcceptableError());
            testDataDTO.setInitialValue(calibrationTestData.getInitialValue());
            testDataDTO.setEndValue(calibrationTestData.getEndValue());
            testDataDTO.setVolumeInDevice(calibrationTestData.getVolumeInDevice());
            testDataDTO.setVolumeOfStandard(calibrationTestData.getVolumeOfStandard());
            testDataDTO.setActualConsumption(calibrationTestData.getActualConsumption());
            testDataDTO.setCalculationError(calibrationTestData.getCalculationError());
            calibrationTestIMGList = calibrationTestData.getTestIMGs();
            for (int orderPhoto = 0; orderPhoto < calibrationTestIMGList.size(); orderPhoto++) {
                calibrationTestIMG = calibrationTestIMGList.get(orderPhoto);
                if (orderPhoto == 0) {
                    testDataDTO.setBeginPhoto(calibrationTestService.getPhotoAsString(calibrationTestIMG.getImgName(),calibrationTest));
                } else {
                    testDataDTO.setEndPhoto(calibrationTestService.getPhotoAsString(calibrationTestIMG.getImgName(),calibrationTest));
                }
            }
            testDataDTO.setTestPosition(calibrationTestData.getTestPosition());
            testDataDTO.setTestTime(round(calibrationTestData.getDuration(),1));
            testDataDTO.setTestResult(calibrationTestData.getTestResult());
            testDataDTO.setConsumptionStatus(calibrationTestData.getConsumptionStatus());
            listTestData.add(testDataDTO);
            testNumber++;
        }
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCapacity() {
        return capacity;
    }

    public void setTemperature(int temperature) {
        this.capacity = capacity;
    }

    public int getCounterProductionYear() {
        return counterProductionYear;
    }

    public void setCounterProductionYear(int counterProductionYear) {
        this.counterProductionYear = counterProductionYear;
    }

    public String getAccumulatedVolume() {
        return accumulatedVolume;
    }

    public void setAccumulatedVolume(String accumulatedVolume) {
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

    public ConsumptionStatus getConsumptionStatus() {
        return consumptionStatus;
    }

    public void setConsumptionStatus(ConsumptionStatus consumptionStatus) {
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
        return BigDecimal.valueOf(val).setScale(scale, RoundingMode.HALF_UP).doubleValue();
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

    public Integer getTestPosition() {
        return testPosition;
    }

    public void setTestPosition(Integer testPosition) {
        this.testPosition = testPosition;
    }
}
