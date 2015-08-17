package calibration.test;

import com.softserve.edu.entity.Device;
import com.softserve.edu.repository.DeviceRepository;
import com.softserve.edu.service.DeviceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vova on 12.08.15.
 */

public class DeviceServiceTest {

    private static final Long testId = 123L;

    @InjectMocks
    private DeviceService deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private Device device;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected  = NullPointerException.class)
    public void testExistsWithDeviceid() throws FileNotFoundException {
        Long id = null;
        DeviceService d = new DeviceService();
        d.existsWithDeviceid(id);
    }

    @Test(expected  = NullPointerException.class)
    public void testGetById() throws Exception {
        Long id = new Long(1);
        DeviceService d = new DeviceService();
        d.existsWithDeviceid(id);
    }

    @Test
    public void testSecondGetById() throws Exception {
        when(deviceRepository.findOne(testId )).thenReturn(device);
        deviceService.getById(testId);
        // verify(deviceRepository.findOne(testId ));
        Assert.assertEquals(device, deviceService.getById(testId));
    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testGetDevicesBySearchAndPagination() throws Exception {

    }

    @Test
    public void testGetAllByType() throws Exception {

    }
}