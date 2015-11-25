package com.softserve.edu.service.calibrator.data.test.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.calibration.CalibrationTestData;
import com.softserve.edu.entity.verification.calibration.CalibrationTestIMG;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestIMGRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({IOUtils.class, ImageIO.class})
public class CalibrationTestServiceImplTest {
    private static final String verificationId = "123";

    private static final Date date = new Date();

    private static final Long testId = 1L;

    @InjectMocks
    private CalibrationTestServiceImpl calibrationTestService;

    @Mock
    private CalibrationTestRepository testRepository;

    @Mock
    private CalibrationTest calibrationTest;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private Verification verification;
    @Mock
    private BufferedImage buffered;

    @Mock
    private CalibrationTestData data;

    @Mock
    private CalibrationTestDataRepository dataRepository;

    @Mock
    private List<CalibrationTestData> listCalibrationTestData;

    @Mock
    private ArrayList<CalibrationTest> list;

    @Mock
    private CalibrationTestIMGRepository testIMGRepository;

    @Mock
    InputStream file;
    @Mock
    DeviceTestData deviceTestData;
    @Mock
    private Page<CalibrationTest> page;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindTestById() throws Exception {
        when(testRepository.findOne(testId)).thenReturn(calibrationTest);
        assertEquals(calibrationTest, calibrationTestService.findTestById(testId));
    }

    @Test
    public void testFindByVerificationId() throws Exception {
        when(testRepository.findByVerificationId(verificationId)).thenReturn(calibrationTest);
        assertEquals(calibrationTestService.findByVerificationId(verificationId), calibrationTest);
    }

    @Test
    public void testFindAllCalibrationTests() throws Exception {
        when(testRepository.findAll()).thenReturn(list);
        CalibrationTestList foundTests = new CalibrationTestList(list);
        assertEquals(foundTests.getCalibrationTests(), calibrationTestService.findAllCalibrationTests().getCalibrationTests());
    }


    @Test
    public void testGetCalibrationTestsBySearchAndPagination() {
        int pageNumber = 2;
        int itemsPerPage = 10;
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        when(testRepository.findAll(pageRequest)).thenReturn(page);

        calibrationTestService.getCalibrationTestsBySearchAndPagination(pageNumber, itemsPerPage, "search");
        calibrationTestService.getCalibrationTestsBySearchAndPagination(pageNumber, itemsPerPage, "null");

        verify(testRepository).findAll(any(PageRequest.class));
        verify(testRepository).findByNameLikeIgnoreCase("%" + "search" + "%", pageRequest);
    }

    @Test
    public void testDeleteTest() {
        calibrationTestService.deleteTest(testId);
        verify(testRepository).delete(testId);
    }

    @Test
    public void testCreateTestData() {
        ArgumentCaptor<Long> id = ArgumentCaptor.forClass(Long.class);
        calibrationTestService.createTestData(testId, data);
        verify(testRepository).findOne(id.capture());
        assertEquals(testId, id.getValue());
    }

    @Test
    public void testEditTest() {
        final String name = "name";
        Verification.ConsumptionStatus status = Verification.ConsumptionStatus.IN_THE_AREA;
        ArgumentCaptor<String> nameArg = ArgumentCaptor.forClass(String.class);
        when(testRepository.findOne(testId)).thenReturn(calibrationTest);
        CalibrationTest calibrationTest = calibrationTestService.editTest(testId, name, "155", 1, 1d, 1d, status,
                Verification.CalibrationTestResult.SUCCESS);
        verify(calibrationTest).setName(nameArg.capture());
        assertEquals(name, nameArg.getValue());
    }


    @Test(expected = NotAvailableException.class)
    public void testFindAllTestDataAsociatedWithTest() throws Exception {
        when(testRepository.findOne(testId)).thenReturn(null);
        calibrationTestService.findAllTestDataAsociatedWithTest(testId);
    }

    @Test
    public void testSecondFindAllTestDataAsociatedWithTest() throws Exception {
        when(testRepository.findOne(testId)).thenReturn(calibrationTest);
        when(dataRepository.findByCalibrationTestId(testId)).thenReturn(listCalibrationTestData);
        CalibrationTestDataList calibrationTestDataList = calibrationTestService.findAllTestDataAsociatedWithTest(testId);
        assertEquals(testId, calibrationTestDataList.getTestId());
        assertEquals(listCalibrationTestData, calibrationTestDataList.getListTestData());
    }

    @Test
    public void testUploadPhotos() throws Exception {
        String fileName = "./src/test/resources/photo/logo.jpg";
        CalibrationTestData calibrationTestData = mock(CalibrationTestData.class);
        InputStream file = new BufferedInputStream(new FileInputStream("./src/test/resources/photo/logo.jpg"));
        CalibrationTestIMG testIMG = new CalibrationTestIMG(calibrationTestData, fileName);

        stub(testRepository.findOne(testId)).toReturn(calibrationTest);

        calibrationTestService.uploadPhotos(file, testId, fileName);

        file.close();
    }

    /*@Test
    public void testCreateEmptyTest() throws Exception {
        calibrationTestService.createEmptyTest(verificationId);

        verify(testRepository).save(any(CalibrationTest.class));
    }*/
    @Test
    public void testCreateNewTest() throws Exception {
        when(verificationRepository.findOne(verificationId)).thenReturn(verification);
        calibrationTest = new CalibrationTest(deviceTestData.getFileName(), deviceTestData.getInstallmentNumber(),
                deviceTestData.getLatitude(), deviceTestData.getLongitude(), deviceTestData.getUnixTime(),
                deviceTestData.getCurrentCounterNumber(), verification, deviceTestData.getInitialCapacity(),
                deviceTestData.getTemperature());
        when(testRepository.save(calibrationTest)).thenReturn(calibrationTest);

        // when(ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(deviceTestData.getTestPhoto())))).thenReturn(buffered);
        // stub(calibrationTestService.createNewTest(deviceTestData, verificationId)).toReturn(testId);

    }

    @Test
    public void testCreateNewCalibrationTestData() throws Exception {
        calibrationTestService.createNewCalibrationTestData(data);
        verify(dataRepository).save(data);
    }

    @Test
    public void testCreateNewCalibrationTest() throws Exception {
        String name = "name";
        String capacity = "155";
        Integer settingNumber = 10;
        Double latitude = 24d;
        Double longitude = 48d;
        CalibrationTest expected = new CalibrationTest();

        when(testRepository.findOne(testId)).thenReturn(expected);

        CalibrationTest actual = calibrationTestService.createNewCalibrationTest(testId, name,
                capacity, settingNumber, latitude, longitude);

        assertEquals(expected, actual);
    }
}