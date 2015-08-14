package calibration.test;

import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.CalibrationTestService;
import com.softserve.edu.service.exceptions.NotAvailableException;
import com.softserve.edu.service.utils.CalibrationTestDataList;
import com.softserve.edu.service.utils.CalibrationTestList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import  static org.mockito.Mockito.*;

/**
 * Created by vova on 12.08.15.
 */
public class CalibrationTestServiceTest {
    private static final String verificationId = "123";

    private static final Long testId = 123L;

    private static final Long dataTestId = 1235L;

    private  static final Date date = new Date();
    @InjectMocks
    private CalibrationTestService calibrationTestService;

    @Mock
    private CalibrationTestRepository testRepository;

    @Mock
    private CalibrationTest calibrationTest;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private Verification verification;

    @Mock
    private CalibrationTestDataList calibrationTestDataList;

    @Mock
    private CalibrationTestList calibrationTestList;

    @Mock
    private CalibrationTestDataRepository dataRepository;

    @Mock
    private List<CalibrationTestData> listCalibrationTestData;

    @Mock
    private ArrayList<CalibrationTest> list;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindTestById() throws Exception {
        when(testRepository.findOne(testId)).thenReturn(calibrationTest);
        Assert.assertEquals(calibrationTestService.findTestById(testId),calibrationTest);
    }

    @Test
    public void testFindByVerificationId() throws Exception {
        when(testRepository.findByVerificationId(verificationId)).thenReturn(calibrationTest);
        Assert.assertEquals(calibrationTestService.findByVerificationId(verificationId), calibrationTest);
    }

    @Test
    public void testFindAllCalibrationTests() throws Exception {
        when(testRepository.findAll()).thenReturn(list);
       //when(calibrationTestService.findAllCalibrationTests()).thenReturn(calibrationTestList);
        calibrationTestService.findAllCalibrationTests();
        // doNothing().when(new CalibrationTestList(list));
        verify(testRepository.findAll());
        //verify(new CalibrationTestList(list));
    }

    @Test
    public void testGetCalibrationTestsBySearchAndPagination() throws Exception {

    }

    @Test
    public void testCreateNewTest() {
        when(verificationRepository.findOne(verificationId)).thenReturn(verification);
        doNothing().when(calibrationTest).setVerification(verification);
        doNothing().when(calibrationTest).setDateTest(date);
        when(testRepository.save(calibrationTest)).thenReturn(calibrationTest);
        calibrationTestService.createNewTest(calibrationTest, date, verificationId);
        verify(verificationRepository.findOne(verificationId));

    }

    @Test
    public void testEditTest() throws Exception {

    }

    @Test
    public void testDeleteTest() throws Exception {
        when(testRepository.findOne(testId)).thenReturn(calibrationTest);
        Assert.assertEquals(calibrationTest,calibrationTestService.deleteTest(testId));
    }

    @Test
    public void testCreateTestData() throws Exception {

    }

    @Test(expected  = NotAvailableException.class)
    public void testFindAllTestDataAsociatedWithTest() throws Exception {
        when(testRepository.findOne(testId)).thenReturn(null);
        calibrationTestService.findAllTestDataAsociatedWithTest(testId);
    }

    @Test
    public void testSecondFindAllTestDataAsociatedWithTest() throws Exception {
       // when(testRepository.findOne(testId)).thenReturn(calibrationTest);
       // when(dataRepository.findByCalibrationTestId(testId)).thenReturn(listCalibrationTestData);
        //when(new CalibrationTestDataList(testId, listCalibrationTestData)).thenReturn(calibrationTestDataList);
       // CalibrationTestDataList calibrationTestDataList = spy(new CalibrationTestDataList(testId, listCalibrationTestData));
     //   when(new CalibrationTestDataList(testId , dataRepository.findByCalibrationTestId(testId))).thenReturn(calibrationTestDataList);

       // Assert.assertEquals(calibrationTestService.findAllTestDataAsociatedWithTest(testId),new CalibrationTestDataList(testId , dataRepository.findByCalibrationTestId(testId)) );
    }

    @Test
    public void testUploadPhotos() throws Exception {

    }
}