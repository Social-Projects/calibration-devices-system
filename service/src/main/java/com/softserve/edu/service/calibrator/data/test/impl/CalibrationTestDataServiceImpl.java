package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestIMGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service that implements CRUD operations for test data.
 */
@Service
public class CalibrationTestDataServiceImpl implements CalibrationTestDataService {
    @Value("${photo.storage.local}")
    private String localStorage;
    @Autowired
    private CalibrationTestDataRepository dataRepository;
    @Autowired
    private CalibrationTestRepository testRepository;
    @Autowired
    private CalibrationTestIMGService testDataIMGService;

    @Override
    @Transactional
    public CalibrationTestData findTestData(Long id) {
        return dataRepository.findOne(id);
    }

    @Override
    @Transactional
    public CalibrationTestData deleteTestData(Long id) {
        CalibrationTestData deletedTestData = dataRepository.findOne(id);
        dataRepository.delete(id);
        return deletedTestData;
    }

    @Override
    @Transactional
    public CalibrationTestData editTestData(Long testDataId, CalibrationTestData testData) {
        CalibrationTestData updatedCalibrationTestData = dataRepository.findOne(testDataId);
        updatedCalibrationTestData.setGivenConsumption(testData.getGivenConsumption());
        updatedCalibrationTestData.setAcceptableError(testData.getAcceptableError());
        updatedCalibrationTestData.setVolumeOfStandard(testData.getVolumeOfStandard());
        updatedCalibrationTestData.setInitialValue(testData.getInitialValue());
        updatedCalibrationTestData.setEndValue(testData.getEndValue());
        updatedCalibrationTestData.setVolumeInDevice(testData.getVolumeInDevice());
        updatedCalibrationTestData.setActualConsumption(testData.getActualConsumption());
        updatedCalibrationTestData.setConsumptionStatus(testData.getConsumptionStatus());
        updatedCalibrationTestData.setCalculationError(testData.getCalculationError());
        updatedCalibrationTestData.setTestResult(testData.getTestResult());
        updatedCalibrationTestData = dataRepository.save(updatedCalibrationTestData);
        return updatedCalibrationTestData;
    }

    @Override
    public CalibrationTestData createNewTestData(Long testId, DeviceTestData deviceTestData,
                                                 int testDataId) throws IOException {

        double volumeInDevice = round(deviceTestData.getTestTerminalCounterValue(testDataId)
                - deviceTestData.getTestInitialCounterValue(testDataId), 2);
        double actualConsumption = convertImpulsesPerSecToCubicMetersPerHour(
               deviceTestData.getTestSpecifiedImpulsesAmount(testDataId),
                deviceTestData.getTestDuration(testDataId));
        double givenConsumption = convertImpulsesPerSecToCubicMetersPerHour(
                deviceTestData.getTestSpecifiedConsumption(testDataId),
                deviceTestData.getImpulsePricePerLitre());

        CalibrationTest calibrationTest = testRepository.findById(testId);
        CalibrationTestData сalibrationTestData = new CalibrationTestData(givenConsumption,
                deviceTestData.getTestAllowableError(testDataId),
                deviceTestData.getTestSpecifiedImpulsesAmount(testDataId),
                deviceTestData.getTestInitialCounterValue(testDataId),
                deviceTestData.getTestTerminalCounterValue(testDataId), volumeInDevice,
                actualConsumption,deviceTestData.getTestEstimatedError(testDataId),
                calibrationTest, deviceTestData.getTestDuration(testDataId),
                deviceTestData.getTestLowerConsumptionLimit(testDataId),
                deviceTestData.getTestUpperConsumptionLimit(testDataId),
                deviceTestData.getTestNumber(testDataId));

        dataRepository.save(сalibrationTestData);
        testDataIMGService.createTestDataIMGCalibrationTestIMGs(testDataId, deviceTestData, сalibrationTestData);
        return сalibrationTestData;
    }

    private double round(double val, int scale) {
        return BigDecimal.valueOf(val).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private double convertImpulsesPerSecToCubicMetersPerHour(double impulses, double impLitPrice) {
        return round(3.6 * impulses / impLitPrice, 3);
    }
}