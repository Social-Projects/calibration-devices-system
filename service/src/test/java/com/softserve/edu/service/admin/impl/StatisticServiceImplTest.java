package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.DeviceRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.stub;

/**
 * Created by Sonka on 17.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceImplTest {

    private long expected;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private VerificationRepository verificationRepository;
    @Mock
    private User userExpected ;
    @InjectMocks
    private StatisticServiceImpl statisticServiceImpl;
    @Before
    public void initializeMockito() {
        statisticServiceImpl = new StatisticServiceImpl();
        MockitoAnnotations.initMocks(this);
        expected = 5L;
        userExpected = new User("Anton","0123");

    }

    @After
    public void tearDown() {
        userExpected = null;
        statisticServiceImpl = null;
    }

    @Test
    public void testCountOrganizations()  {

     stub(organizationRepository.count()).toReturn(expected);
       long factual = statisticServiceImpl.countOrganizations();
        assertEquals(factual, expected);

    }

    @Test
    public void testCountUsers(){
        stub(userRepository.count()).toReturn(expected);
        long factual = statisticServiceImpl.countUsers();
        assertEquals(factual, expected);
    }

    @Test
    public void testCountDevices()  {
        stub(deviceRepository.count()).toReturn(expected);
        long factual = statisticServiceImpl.countDevices();
        assertEquals(factual, expected);
    }

    @Test
    public void testCountVerifications()  {
        stub(verificationRepository.count()).toReturn(expected);
        long factual = statisticServiceImpl.countVerifications();
        assertEquals(factual, expected);
    }

    @Test
    public void testEmployeeExist()  {
        stub(userRepository.findOne(anyString())).toReturn(userExpected);
        User factual = statisticServiceImpl.employeeExist("Anton");
        assertEquals(factual, userExpected);
    }
}