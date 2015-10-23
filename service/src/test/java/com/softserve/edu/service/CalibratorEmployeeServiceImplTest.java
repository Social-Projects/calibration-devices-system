package com.softserve.edu.service;

import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.impl.CalibratorEmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

/**
 * Created by Roman on 18.10.2015.
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class CalibratorEmployeeServiceImplTest {

    // private static final String ENCODED_PASSWORD = "PASS";
    private static final String PASSWORD = "pass";
    private static final String USERNAME = "usr";

    @Mock private User calibratorEmployee;
    @Mock private UserRepository calibratorEmployeeRepository;
    @Mock private VerificationRepository verificationRepository;

    @InjectMocks
    private CalibratorEmployeeServiceImpl calibratorEmployeeService;

    @Before
    public void setUp() {
        when(calibratorEmployee.getPassword()).thenReturn(PASSWORD);
        when(calibratorEmployeeRepository.findOne(USERNAME)).thenReturn(calibratorEmployee);
    }

    @Test
    public void testAddEmployee() {
        // when(calibratorEmployeeService.encodeWithBCryptPasswordEncoder(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        calibratorEmployeeService.addEmployee(calibratorEmployee);
        // verify(calibratorEmployee).setPassword(ENCODED_PASSWORD);
        verify(calibratorEmployeeRepository).save(calibratorEmployee);
    }

    @Test
    public void testOneCalibratorEmployee() {
        User actual = calibratorEmployeeService.oneCalibratorEmployee(USERNAME);
        User expected = calibratorEmployee;
        verify(calibratorEmployeeRepository).findOne(USERNAME);
        assertEquals("the users returned from the method are not equal", expected, actual);
    }
}