package com.softserve.edu.service.utils;

import com.softserve.edu.entity.user.User;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Created by Misha on 10/20/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeDTOTest {

    @Mock
    private User user;

    @Test
    public void testgiveListOfEmployeeDTOs() {
        List listUser = new ArrayList();
        when(user.getUsername()).thenReturn("testName");
        when(user.getFirstName()).thenReturn("testFirstName");
        when(user.getLastName()).thenReturn("testgetLastName");
        when(user.getMiddleName()).thenReturn("testMiddleName");
        listUser = Arrays.asList(user, user);
        List<EmployeeDTO> employeeDTOList = EmployeeDTO.giveListOfEmployeeDTOs(listUser);
        assertEquals(2, employeeDTOList.size());
    }


}
