package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.client.application.util.CatalogueDTOTransformer;
import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.dto.provider.ProviderStageVerificationDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.CalibratorService;
import com.softserve.edu.service.SecurityUserDetailsService;
import com.softserve.edu.service.catalogue.*;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/provider/applications/")
public class ProviderApplicationController {
    @Autowired
    private RegionService regionService;
    @Autowired
    VerificationService verificationService;

    @Autowired
    ProviderService providerService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    DistrictService districtService;

    @Autowired
    LocalityService localityService;

    @Autowired
    StreetService streetService;

    @Autowired
    BuildingService buildingService;

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public void getInitiateVerification(
            @RequestBody ProviderStageVerificationDTO verificationDTO,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Provider provider = providerService.findById(employeeUser.getOrganizationId());

        Verification verification = new Verification(
                new Date(),
                new ClientData(
                        verificationDTO.getName(),
                        verificationDTO.getSurname(),
                        verificationDTO.getMiddleName(),
                        verificationDTO.getPhone(),
                        new Address(
                                provider.getAddress().getRegion(),
                                provider.getAddress().getDistrict(),
                                verificationDTO.getLocality(),
                                verificationDTO.getStreet(),
                                verificationDTO.getBuilding(),
                                verificationDTO.getFlat())),
                provider,
                Status.SENT,verificationDTO.getCalibrator());
        verificationService.saveVerification(verification);
    }
    @RequestMapping(value = "localities", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getLocalityCorrespondingProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Provider provider = providerService.findById(employeeUser.getOrganizationId());
        Region region = regionService.getRegionByDesignation(provider.getAddress().getRegion());
        District district = districtService.findDistrictByDesignationAndRegion(
                provider.getAddress().getDistrict(),
                region.getId()
        );
        return CatalogueDTOTransformer.toDto(localityService.getLocalitiesCorrespondingDistrict(district.getId()));
    }

    @RequestMapping(value = "streets/{localityId}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getStreetsCorrespondingLocality(@PathVariable Long localityId) {
        return CatalogueDTOTransformer.toDto(streetService.getStreetsCorrespondingLocality(localityId));
    }

    @RequestMapping(value = "buildings/{streetId}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getBuildingsCorrespondingStreet(@PathVariable Long streetId) {
        return CatalogueDTOTransformer.toDto(buildingService.getBuildingsCorrespondingStreet(streetId));
    }

    @RequestMapping(value = "calibrators", method = RequestMethod.GET)
    public List<Calibrator> getCalibrators(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        return calibratorService.findByDistrict(
                providerService
                        .findById(employeeUser.getOrganizationId())
                        .getAddress()
                        .getDistrict()
        );
    }
}