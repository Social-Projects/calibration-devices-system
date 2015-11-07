package calibration.test;

import com.softserve.edu.entity.CalibrationTestData;
import com.softserve.edu.repository.CalibrationTestDataRepository;
import com.softserve.edu.service.CalibrationTestDataService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vova on 17.08.15.
 */
public class CalibrationTestDataServiceTest {

    private static final Long testId = 123L;

    @InjectMocks
    private CalibrationTestDataService calibrationTestDataService;

    @Mock
    private CalibrationTestDataRepository dataRepository;

    @Mock
    private CalibrationTestData calibrationTestData;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindTestData() throws Exception {
        when(dataRepository.findOne(testId)).thenReturn(calibrationTestData);
        Assert.assertEquals(calibrationTestDataService.findTestData(testId),dataRepository.findOne(testId) );
    }

    @Test
    public void testDeleteTestData() throws Exception {
        when(dataRepository.findOne(testId)).thenReturn(calibrationTestData);
        Assert.assertEquals(dataRepository.findOne(testId),calibrationTestDataService.deleteTestData(testId));
        verify(dataRepository).delete(testId);
    }

    @Test
    public void testEditTestData() throws Exception {

    }

}
