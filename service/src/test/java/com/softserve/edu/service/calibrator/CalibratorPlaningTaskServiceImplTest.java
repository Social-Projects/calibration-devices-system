package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationPlanningTask;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.impl.CalibratorPlaningTaskServiceImpl;
import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.star.ucb.VerificationMode;
import com.sun.star.util.DateTime;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Yurko on 18.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalibratorPlaningTaskServiceImplTest {
    /*//region fields
    @InjectMocks
    private CalibratorPlaningTaskServiceImpl calibratorPlaningTaskService;

    @Mock
    private CalibrationPlanningTaskRepository taskRepository;

    // endregion

    @org.junit.Before
    public void setUp() {
        calibratorPlaningTaskService = mock(CalibratorPlaningTaskServiceImpl.class);
        when(taskRepository.save((CalibrationPlanningTask)anyObject())).thenReturn(new CalibrationPlanningTask());
        *//*when(calibratorPlaningTaskService.addNewTask(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(Date.class),
                any(Date.class),
                anyString(),
                anyString(),
                anyInt())).;*//*
    }

    @org.junit.After
    public void tearDown() {
        calibratorPlaningTaskService = null;
    }

    *//**
     * If we pass empty parameters, we should get exception
     *
     * @throws Exception
     *//*
    @Test
    public void testAddNewTask() throws Exception {
        boolean result;
        try {
            CalibratorPlaningTaskServiceImpl cptsi= new CalibratorPlaningTaskServiceImpl();
            cptsi.addNewTask("", "", "", "", null, null, "", "", 0);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(false, result);
    }

    @Test
    public void test1AddNewTask() throws Exception {
        calibratorPlaningTaskService.addNewTask("123", "fixed_station", "", "567", null, null, "", "", 0);
        verify(taskRepository).save(new CalibrationPlanningTask());
    }

    *//**
     * If we pass empty parameters, we should get exception
     *
     * @throws Exception
     *//*
    @Test
    public void testFindVerificationsByCalibratorIdAndReadStatus() throws Exception {
        boolean result;
        try {
            CalibratorPlaningTaskServiceImpl cptsi = new CalibratorPlaningTaskServiceImpl();
            Page<Verification> p = cptsi.findVerificationsByCalibratorIdAndReadStatus("", 0, 5);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(false, result);
    }*/
}