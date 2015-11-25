package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestIMGService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.softserve.edu.common.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sonka on 17.11.2015.
 */
@Service
public class CalibrationTestIMGServiceImpl implements CalibrationTestIMGService {

    @Value("${photo.storage.local}")
    private String localStorage;
    @Autowired
    private CalibrationTestIMGRepository testIMGRepository;

    @Override
    public void createTestDataIMGCalibrationTestIMGs(int testDataId, DeviceTestData deviceTestData,
                                                     CalibrationTestData calibrationTestData) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(
                deviceTestData.getBeginPhoto(testDataId))));
        String folder = deviceTestData.getTestNumber(testDataId) + "." + Constants.IMAGE_TYPE;
        String photoBegin = "beginPhoto" + folder;
        String absolutePath = localStorage + File.separator +
                calibrationTestData.getCalibrationTest().getVerification().getId() + File.separator;
        ImageIO.write(bufferedImage, Constants.IMAGE_TYPE, new File(absolutePath + photoBegin));
        CalibrationTestIMG calibrationTestIMGBegin = new CalibrationTestIMG(calibrationTestData, photoBegin);
        testIMGRepository.save(calibrationTestIMGBegin);

        bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(
                deviceTestData.getEndPhoto(testDataId))));
        String photoEnd = "endPhoto" + folder;
        ImageIO.write(bufferedImage, Constants.IMAGE_TYPE, new File(absolutePath + photoEnd));
        CalibrationTestIMG calibrationTestIMGEnd = new CalibrationTestIMG(calibrationTestData, photoEnd);

        testIMGRepository.save(calibrationTestIMGEnd);
    }
}
