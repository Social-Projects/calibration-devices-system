package com.softserve.edu.service.calibrator.data.test;


import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface CalibrationTestService {

     CalibrationTest findTestById(Long testId);

     CalibrationTest findByVerificationId(String verifId);

     CalibrationTestList findAllCalibrationTests();

     Page<CalibrationTest> getCalibrationTestsBySearchAndPagination(int pageNumber, int itemsPerPage, String search);


     long createNewTest(DeviceTestData deviceTestData,  String verificationId) throws IOException;

     CalibrationTest editTest(Long testId, String name, String capacity, Integer settingNumber,
                                    Double latitude, Double longitude, Verification.ConsumptionStatus consumptionStatus, Verification.CalibrationTestResult testResult);

     void deleteTest(Long testId);

     void createTestData(Long testId, CalibrationTestData testData);

     CalibrationTestDataList findAllTestDataAsociatedWithTest(Long calibrationTestId);

     void uploadPhotos(InputStream file, Long idCalibrationTest, String originalFileFullName) throws IOException;

     //CalibrationTest createEmptyTest(String verificationId);

     void createNewCalibrationTestData(CalibrationTestData calibrationTestData);

     CalibrationTest createNewCalibrationTest(Long testId, String name, String capacity, Integer settingNumber,
                                                    Double latitude, Double longitude);
     String getPhotoAsString(String photoPath,CalibrationTest calibrationTest);

     void updateTest(String verificationId,String status);

     Set<CalibrationTestData> getLatestTests(List<CalibrationTestData> rawListOfCalibrationTestData);

}
