package com.softserve.edu.service;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.calibrator.impl.CalibratorServiceImpl;
import com.softserve.edu.service.utils.EmployeeDTO;
import javafx.beans.binding.Bindings;
import jdk.nashorn.internal.ir.annotations.Ignore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Misha on 10/18/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CalibratorServiceImplTest {

    private CalibratorServiceImpl calibratorService;

    @Mock
    private OrganizationRepository calibratorRepository;

    @Mock
    private List listOfRole;

    @Mock
    private InputStream in;

    @Spy
    private User user;

    @Mock
    Organization organization;

    @Before
    public void setUp() {
        calibratorService = new CalibratorServiceImpl();
        user = new User("gag", "gag", organization);
    }


    @Test
    public void testgetAllCalibratorEmployeeRoleCalibratorAdmin() {
        when(listOfRole.contains(UserRole.CALIBRATOR_ADMIN.name())).thenReturn(true);
        when(organization.getId()).thenReturn(11L);
        List<EmployeeDTO> list = calibratorService.getAllCalibratorEmployee(listOfRole, user);
        assertNotEquals(0, list.size());
    }


    @Test
    public void testgetAllCalibratorEmployeeRoleCalibratorAdminNotNullList() {
        when(listOfRole.contains(UserRole.CALIBRATOR_ADMIN.name())).thenReturn(true);
        when(organization.getId()).thenReturn(11L);
        List<EmployeeDTO> list = calibratorService.getAllCalibratorEmployee(listOfRole, user);
        assertNotNull(list);
    }


    @Test
    public void testgetAllCalibratorEmployeeNotCalibratorAdmin() {
        when(listOfRole.contains(anyString())).thenReturn(false);
        List<EmployeeDTO> list = calibratorService.getAllCalibratorEmployee(listOfRole, user);
        when(user.getUsername()).thenReturn("gag");
        when(user.getFirstName()).thenReturn("gag");
        when(user.getLastName()).thenReturn("gag");
        when(user.getMiddleName()).thenReturn("gag");
        when(listOfRole.get(0)).thenReturn("CALIBRATOR_EMPLOYEE");
        List<EmployeeDTO> listEmployee = calibratorService.getAllCalibratorEmployee(listOfRole, user);
        assertEquals(1, listEmployee.size());
    }


}
