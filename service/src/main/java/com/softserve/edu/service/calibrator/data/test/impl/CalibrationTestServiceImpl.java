package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestDataService;
import com.softserve.edu.service.tool.MailService;
import org.apache.commons.codec.binary.Base64;
import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import com.softserve.edu.common.Constants;

@Service
public class CalibrationTestServiceImpl implements CalibrationTestService {

    @Value("${photo.storage.local}")
    private String localStorage;

    @Autowired
    private CalibrationTestRepository testRepository;
    @Autowired
    private CalibrationTestDataService testDataService;
    @Autowired
    private CalibrationTestDataRepository dataRepository;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    MailService mailService;

    private Logger logger = Logger.getLogger(CalibrationTestServiceImpl.class);

    public long createNewTest(DeviceTestData deviceTestData, String verificationId) throws IOException {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest(deviceTestData.getFileName(),
                deviceTestData.getInstallmentNumber(), deviceTestData.getLatitude(), deviceTestData.getLongitude(),
                deviceTestData.getUnixTime(), deviceTestData.getCurrentCounterNumber(), verification,
                deviceTestData.getInitialCapacity(), deviceTestData.getTemperature());

        BufferedImage buffered = ImageIO.read(new ByteArrayInputStream(
                Base64.decodeBase64(deviceTestData.getTestPhoto())));
        String testPhoto = "mainPhoto." + Constants.IMAGE_TYPE;
        String folderPath = localStorage + File.separator + verificationId;
        String absolutePath = localStorage + File.separator + verificationId + File.separator + testPhoto;
        File file = new File(folderPath);
        file.mkdirs();
        ImageIO.write(buffered, Constants.IMAGE_TYPE, new File(absolutePath));
        calibrationTest.setPhotoPath(testPhoto);

        testRepository.save(calibrationTest);

        for (int testDataId = 1; testDataId <= 6; testDataId++) { // BBI can not contain  more than 6 tests
            if (!deviceTestData.getBeginPhoto(testDataId).equals("")) { // if there is no photo there is now test data
                CalibrationTestData сalibrationTestData = testDataService.createNewTestData(calibrationTest.getId(),
                        deviceTestData, testDataId);
                if (сalibrationTestData.getTestResult().equals(Verification.CalibrationTestResult.FAILED)) {
                    calibrationTest.setTestResult(Verification.CalibrationTestResult.FAILED);
                }
                if (сalibrationTestData.getConsumptionStatus().equals(Verification.ConsumptionStatus.NOT_IN_THE_AREA)) {
                    calibrationTest.setConsumptionStatus(Verification.ConsumptionStatus.NOT_IN_THE_AREA);
                }
            }
        }
        testRepository.save(calibrationTest);
        verification.setStatus(Status.TEST_COMPLETED);
        verificationRepository.save(verification);
        return calibrationTest.getId();
    }


    @Transactional
    public CalibrationTest findTestById(Long testId) {
        return testRepository.findOne(testId);
    }

    @Transactional
    public CalibrationTest findByVerificationId(String verifId) {
        return testRepository.findByVerificationId(verifId);
    }

    @Transactional
    public CalibrationTestList findAllCalibrationTests() {
        List<CalibrationTest> list = (ArrayList<CalibrationTest>) testRepository.findAll();
        return new CalibrationTestList(list);
    }

    @Override
    @Transactional
    public Page<CalibrationTest> getCalibrationTestsBySearchAndPagination(int pageNumber, int itemsPerPage, String search) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return search.equalsIgnoreCase("null") ? testRepository.findAll(pageRequest) : testRepository.findByNameLikeIgnoreCase("%" + search + "%", pageRequest);
    }

    @Override
    @Transactional
    public CalibrationTest editTest(Long testId, String name, String capacity, Integer settingNumber,
                                    Double latitude, Double longitude, Verification.ConsumptionStatus consumptionStatus, Verification.CalibrationTestResult testResult) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = Verification.CalibrationTestResult.SUCCESS;
        calibrationTest.setName(name);
        calibrationTest.setCapacity(capacity);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(consumptionStatus);
        calibrationTest.setTestResult(testResult);
        return calibrationTest;
    }

    @Override
    @Transactional
    public void deleteTest(Long testId) {
        testRepository.delete(testId);
    }

    @Override
    @Transactional
    public void createTestData(Long testId, CalibrationTestData testData) {
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testData.setCalibrationTest(calibrationTest);
        dataRepository.save(testData);
    }

    @Override
    @Transactional
    public CalibrationTestDataList findAllTestDataAsociatedWithTest(Long calibrationTestId) {
        CalibrationTest calibrationTest = testRepository.findOne(calibrationTestId);
        if (calibrationTest == null) {
            throw new NotAvailableException("Тесту з таким ID не існує!");
        } else {
            return new CalibrationTestDataList(calibrationTestId
                    , dataRepository.findByCalibrationTestId(calibrationTestId));
        }
    }

    @Override
    public String getPhotoAsString(String photoPath, CalibrationTest calibrationTest) {
        String photo = null;
        InputStream reader = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            reader = new FileInputStream(localStorage + File.separator + calibrationTest.getVerification().getId()
                    + File.separator + photoPath);
            bufferedInputStream = new BufferedInputStream(reader);
            BufferedImage image = ImageIO.read(bufferedInputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, Constants.IMAGE_TYPE, baos);
            byte[] bytesOfImages = Base64.encodeBase64(baos.toByteArray());
            photo = new String(bytesOfImages);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
                logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
            }
        }
        return photo;
    }

    @Override
    public Set<CalibrationTestData> getLatestTests(List<CalibrationTestData> rawListOfCalibrationTestData) {
        Set<CalibrationTestData> setOfCalibrationTestData = new LinkedHashSet<>();
        Integer position;
        for (CalibrationTestData calibrationTestData : rawListOfCalibrationTestData ){
            position = calibrationTestData.getTestPosition();
            position++;
            for (CalibrationTestData calibrationTestDataSearch : rawListOfCalibrationTestData) {
                if (calibrationTestDataSearch.getTestPosition() == position) {
                    calibrationTestData = calibrationTestDataSearch;
                    position++;
                }
            }
            setOfCalibrationTestData.add(calibrationTestData);
        }
        return setOfCalibrationTestData;
    }

    @Override
    @Transactional
    public void uploadPhotos(InputStream file, Long idCalibrationTest, String originalFileFullName) throws IOException {
        String fileType = originalFileFullName.substring(originalFileFullName.lastIndexOf('.') + 1);
        byte[] bytesOfImages = IOUtils.toByteArray(file);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytesOfImages));
        ImageIO.write(bufferedImage, fileType, new File(localStorage, originalFileFullName));

        CalibrationTest calibrationTest = testRepository.findOne(idCalibrationTest);
//        CalibrationTestIMG calibrationTestIMG = new CalibrationTestIMG(calibrationTest, originalFileFullName);
//        testIMGRepository.save(calibrationTestIMG);
    }

   /* @Override
    @Transactional
    public CalibrationTest createEmptyTest(String verificationId) {
        Verification verification = verificationRepository.findOne(verificationId);
        CalibrationTest calibrationTest = new CalibrationTest();
        calibrationTest.setVerification(verification);
        testRepository.save(calibrationTest);
        return calibrationTest;
    }*/

    @Override
    @Transactional
    public void createNewCalibrationTestData(CalibrationTestData calibrationTestData) {
        dataRepository.save(calibrationTestData);
    }

    @Override
    @Transactional
    public CalibrationTest createNewCalibrationTest(Long testId, String name, String capacity, Integer settingNumber,
                                                    Double latitude, Double longitude) {
        Verification.CalibrationTestResult testResult;
        CalibrationTest calibrationTest = testRepository.findOne(testId);
        testResult = Verification.CalibrationTestResult.SUCCESS;
        Date initial = new Date();
        calibrationTest.setName(name);
        calibrationTest.setDateTest(initial);
        calibrationTest.setCapacity(capacity);
        calibrationTest.setSettingNumber(settingNumber);
        calibrationTest.setLatitude(latitude);
        calibrationTest.setLongitude(longitude);
        calibrationTest.setConsumptionStatus(Verification.ConsumptionStatus.IN_THE_AREA);
        calibrationTest.setTestResult(testResult);
        return calibrationTest;
    }

    @Override
    @Transactional
    public void updateTest(String verificationId,String status){
        Verification verification = verificationRepository.findOne(verificationId);
        String statusToSend;
        Status statusVerification = Status.valueOf(status.toUpperCase());
        if(!verification.getStatus().equals(statusVerification)) {
            if(statusVerification.equals(Status.TEST_OK)){
                statusToSend = "придатний";
            }else {
                statusToSend = "непридатний";
            }
            verification.setStatus(statusVerification);
            String emailCustomer = verification.getClientData().getEmail();
            String emailProvider = verification.getProviderEmployee().getEmail();
            mailService.sendPassedTestMail(emailCustomer, verificationId, statusToSend);
            mailService.sendPassedTestMail(emailProvider, verificationId, statusToSend);
            verificationRepository.save(verification);
        }
    }


}