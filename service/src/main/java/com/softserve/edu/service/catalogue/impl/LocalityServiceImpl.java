package com.softserve.edu.service.catalogue.impl;

import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.repository.catalogue.LocalityRepository;
import com.softserve.edu.service.catalogue.LocalityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LocalityServiceImpl implements LocalityService {
    Logger logger = Logger.getLogger(LocalityServiceImpl.class);

    @Autowired
    private LocalityRepository localityRepository;

    @Override
    public List<Locality> getLocalitiesCorrespondingDistrict(Long districtId) {
        return localityRepository.findDistinctByDistrictId(districtId);
    }

    @Override
    public List<String> getMailIndexForLocality(String designation, Long districtId) {
        return localityRepository.findMailIndexByDesignationAndDistrictId(designation, districtId);
    }

    @Override
    public Locality findById(Long id) {
        return localityRepository.findOne(id);
    }
}
