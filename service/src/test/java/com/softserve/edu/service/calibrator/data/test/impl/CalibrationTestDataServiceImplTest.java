package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalibrationTestDataServiceImplTest {

    private static final Long testId = 123L;

    @InjectMocks
    private CalibrationTestDataServiceImpl calibrationTestDataService;

    @Mock
    private CalibrationTestDataRepository dataRepository;

    @Mock
    private CalibrationTestData calibrationTestData;

    @Mock
    private CalibrationTestData updatedCalibrationTestData;

    @Mock
    private CalibrationTestData returnCalibrationTestData;
    @Mock
    private VerificationRepository verificationRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindTestData() throws Exception {
        when(dataRepository.findOne(testId)).thenReturn(calibrationTestData);
        Assert.assertEquals(calibrationTestDataService.findTestData(testId), dataRepository.findOne(testId));
    }

    @Test
    public void testDeleteTestData() throws Exception {
        when(dataRepository.findOne(testId)).thenReturn(calibrationTestData);
        Assert.assertEquals(dataRepository.findOne(testId), calibrationTestDataService.deleteTestData(testId));
        verify(dataRepository).delete(testId);
    }

    @Test
    public void testEditTestData() throws Exception {
        when(dataRepository.findOne(testId)).thenReturn(updatedCalibrationTestData);
        Assert.assertEquals(dataRepository.save(updatedCalibrationTestData), calibrationTestDataService.editTestData(testId, calibrationTestData));
        verify(updatedCalibrationTestData).setGivenConsumption(calibrationTestData.getGivenConsumption());
        verify(updatedCalibrationTestData).setAcceptableError(calibrationTestData.getAcceptableError());
        verify(updatedCalibrationTestData).setVolumeOfStandard(calibrationTestData.getVolumeOfStandard());
        verify(updatedCalibrationTestData).setInitialValue(calibrationTestData.getInitialValue());
        verify(updatedCalibrationTestData).setEndValue(calibrationTestData.getEndValue());
        verify(updatedCalibrationTestData).setVolumeInDevice(calibrationTestData.getVolumeInDevice());
        verify(updatedCalibrationTestData).setActualConsumption(calibrationTestData.getActualConsumption());
        verify(updatedCalibrationTestData).setConsumptionStatus(calibrationTestData.getConsumptionStatus());
        verify(updatedCalibrationTestData).setCalculationError(calibrationTestData.getCalculationError());
        verify(updatedCalibrationTestData).setTestResult(calibrationTestData.getTestResult());
        when(dataRepository.save(updatedCalibrationTestData)).thenReturn(returnCalibrationTestData);
    }
}