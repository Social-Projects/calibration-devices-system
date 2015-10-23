package com.softserve.edu.service.catalogue.impl;

import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.repository.catalogue.DistrictRepository;
import junit.framework.TestCase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Created by lyubomyr on 21.10.2015.
 */
public class DistrictServiceImplTest extends TestCase {

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private DistrictServiceImpl districtServiceImpl;

    public void setUp() throws Exception {
        districtServiceImpl = new DistrictServiceImpl();

        MockitoAnnotations.initMocks(this);
    }

    public void tearDown() throws Exception {
        districtServiceImpl = null;
    }

    public void testGetDistrictsCorrespondingRegion() throws Exception {
        final long regionId = 1L;
        List<District> districtList = new ArrayList<District>();
        stub(districtRepository.findByRegionId(regionId)).toReturn(districtList);
        List<District> actual = districtServiceImpl.getDistrictsCorrespondingRegion(regionId);
        assertEquals(actual, districtList);
        verify(districtRepository).findByRegionId(regionId);
    }

    public void testFindDistrictByDesignationAndRegion() throws Exception {
        final String designation="anydestination";
        final long region = 1L;
        District district = mock(District.class);
        stub(districtRepository.findByDesignationAndRegionId(designation, region)).toReturn(district);
        District actual = districtServiceImpl.findDistrictByDesignationAndRegion(designation, region);
        assertEquals(actual, district);
        verify(districtRepository).findByDesignationAndRegionId(designation, region);
    }
}