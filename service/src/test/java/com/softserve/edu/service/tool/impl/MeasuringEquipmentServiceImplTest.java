package com.softserve.edu.service.tool.impl;

import com.softserve.edu.entity.device.MeasuringEquipment;
import com.softserve.edu.repository.MeasuringEquipmentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Sonka on 21.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class MeasuringEquipmentServiceImplTest {
    private MeasuringEquipment measuringEquipment; // is used for editMeasuringEquipment
    private MeasuringEquipment measuringEquipment2; // is used for addMeasuringEquipment
    @Mock
    private MeasuringEquipmentRepository measuringEquipmentRepository;
    @Mock
    private PageRequest pageRequest;
    @Mock
    private List<MeasuringEquipment> measuringEquipments;
    @InjectMocks
    private MeasuringEquipmentServiceImpl measuringEquipmentServiceImpl;

    @Before
    public void initializeMockito() {
        MockitoAnnotations.initMocks(this);
        measuringEquipment = new MeasuringEquipment(1L,"name1","manufacturer1");
        measuringEquipmentServiceImpl.addMeasuringEquipment(measuringEquipment);
        measuringEquipment2 =new MeasuringEquipment(2L,"name2","manufacturer2");
        stub(measuringEquipmentRepository.findOne(1L)).toReturn(measuringEquipment);
        measuringEquipments.add(measuringEquipment);
        measuringEquipments.add(measuringEquipment2);
    }

    @After
    public void tearDown()  {
        measuringEquipmentServiceImpl=null;
        measuringEquipment = null;
        measuringEquipment2 = null;
        measuringEquipments = null;
    }

    @Test
    public void testGetAll() {
        when(measuringEquipmentServiceImpl.getAll()).thenReturn(measuringEquipments);
        assertEquals(measuringEquipmentServiceImpl.getAll(), measuringEquipments);
    }

    @Test
    public void testGetMeasuringEquipmentsBySearchAndPagination() {
        measuringEquipmentServiceImpl.getMeasuringEquipmentsBySearchAndPagination(2,5,null);
        verify(measuringEquipmentRepository).findAll(any(PageRequest.class));

    }
    @Test
    public void testSecondGetMeasuringEquipmentsBySearchAndPagination() {
        String search = "search";
        measuringEquipmentServiceImpl.getMeasuringEquipmentsBySearchAndPagination(2,5,search);
        verify(measuringEquipmentRepository).findByNameLikeIgnoreCase(eq("%" + search + "%"), any(PageRequest.class));
    }
    @Test
    public void testAddMeasuringEquipment()  {
        boolean result;
        try {
            measuringEquipmentServiceImpl.addMeasuringEquipment(measuringEquipment2);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(true, result);
    }

    @Test
    public void testGetMeasuringEquipmentById()  {
        MeasuringEquipment factual = measuringEquipmentServiceImpl.getMeasuringEquipmentById(1L);
        assertEquals(factual, measuringEquipment);
    }

    @Test
    public void testEditMeasuringEquipment()  {
      boolean result;
        try {
            measuringEquipmentServiceImpl.editMeasuringEquipment(1L,"name 1","deviceType 1","manufacturer 1","verificationInterval 1");
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(true, result);
    }

    @Test
    public void testDeleteMeasuringEquipment()  {
        boolean result;
        try {
            measuringEquipmentServiceImpl.deleteMeasuringEquipment(1L);
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        assertEquals(true, result);
    }
}