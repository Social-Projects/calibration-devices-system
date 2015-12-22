package com.softserve.edu.controller.provider;

import com.softserve.edu.controller.calibrator.util.CounterTypeDTOTransformer;
import com.softserve.edu.controller.client.application.util.CatalogueDTOTransformer;
import com.softserve.edu.dto.DeviceLightDTO;
import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.dto.application.RejectMailDTO;
import com.softserve.edu.dto.admin.CounterTypeDTO;
import com.softserve.edu.dto.provider.OrganizationStageVerificationDTO;
import com.softserve.edu.entity.*;
import com.softserve.edu.entity.catalogue.District;
import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.dto.LocalityDTO;
import com.softserve.edu.entity.device.Counter;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.tool.DeviceService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.catalogue.DistrictService;
import com.softserve.edu.service.catalogue.LocalityService;
import com.softserve.edu.service.catalogue.RegionService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "provider/applications/")
/**
 * Used in provider UI for creating new applications
 * and sending rejection messages/notifications
 *
 * */
public class ProviderApplicationController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private CalibratorService calibratorService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MailService mail;

    /**
     * Save verification in database with calibrator id
     *
     * @param verificationDTO object with verification data
     */
    @RequestMapping(value = "send", method = RequestMethod.POST)
    public String getInitiateVerification(@RequestBody OrganizationStageVerificationDTO verificationDTO,
                                          @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        ClientData clientData = new ClientData(
                verificationDTO.getFirstName(),
                verificationDTO.getLastName(),
                verificationDTO.getMiddleName(),
                verificationDTO.getEmail(),
                verificationDTO.getPhone(),
                verificationDTO.getSecondPhone(),
                new Address(
                        verificationDTO.getRegion(),
                        verificationDTO.getDistrict(),
                        verificationDTO.getLocality(),
                        verificationDTO.getStreet(),
                        verificationDTO.getBuilding(),
                        verificationDTO.getFlat()
                )
        );
        CounterType counterType = providerService.findOneBySymbolAndStandardSize(verificationDTO.getSymbol(),
                verificationDTO.getStandardSize());
        Counter counter = new Counter(
                verificationDTO.getReleaseYear(),
                verificationDTO.getDateOfDismantled(),
                verificationDTO.getDateOfMounted(),
                verificationDTO.getNumberCounter(),
                counterType
        );
        AdditionalInfo info = new AdditionalInfo(
                verificationDTO.getEntrance(),
                verificationDTO.getDoorCode(),
                verificationDTO.getFloor(),
                verificationDTO.getDateOfVerif(),
                verificationDTO.getServiceability(),
                verificationDTO.getNoWaterToDate(),
                verificationDTO.getNotes(),
                verificationDTO.getTime()
        );

        Organization provider = providerService.findById(employeeUser.getOrganizationId());
        Organization calibrator = calibratorService.findById(verificationDTO.getCalibratorId());

        Device device = deviceService.getById(verificationDTO.getDeviceId());
        Verification verification = new Verification(new Date(), new Date(), clientData, provider, device, Status.IN_PROGRESS,
                Verification.ReadStatus.UNREAD, calibrator, info, verificationDTO.getDismantled(), counter, verificationDTO.getComment());

        verificationService.saveVerification(verification);
        String name = clientData.getFirstName() + " " + clientData.getLastName();
        mail.sendMail(clientData.getEmail(), name, verification.getId(), verification.getProvider().getName(),
                verification.getDevice().getDeviceType().toString());

        return verification.getId();
    }

    /**
     * Save verification in database without sending to calibrator (without calibrator id)
     *
     * @param verificationDTO
     * @param employeeUser
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String saveInitiateVerification(@RequestBody OrganizationStageVerificationDTO verificationDTO,
                                           @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        ClientData clientData = new ClientData(
                verificationDTO.getFirstName(),
                verificationDTO.getLastName(),
                verificationDTO.getMiddleName(),
                verificationDTO.getEmail(),
                verificationDTO.getPhone(),
                verificationDTO.getSecondPhone(),
                new Address(
                        verificationDTO.getRegion(),
                        verificationDTO.getDistrict(),
                        verificationDTO.getLocality(),
                        verificationDTO.getStreet(),
                        verificationDTO.getBuilding(),
                        verificationDTO.getFlat()
                )
        );
        CounterType counterType = providerService.findOneBySymbolAndStandardSize(verificationDTO.getSymbol(),
                verificationDTO.getStandardSize());
        Counter counter = new Counter(
                verificationDTO.getReleaseYear(),
                verificationDTO.getDateOfDismantled(),
                verificationDTO.getDateOfMounted(),
                verificationDTO.getNumberCounter(),
                counterType
        );
        AdditionalInfo info = new AdditionalInfo(
                verificationDTO.getEntrance(),
                verificationDTO.getDoorCode(),
                verificationDTO.getFloor(),
                verificationDTO.getDateOfVerif(),
                verificationDTO.getServiceability(),
                verificationDTO.getNoWaterToDate(),
                verificationDTO.getNotes(),
                verificationDTO.getTime()
        );

        Organization provider = providerService.findById(employeeUser.getOrganizationId());
        Device device = (verificationDTO.getDeviceId() != null) ? deviceService.getById(verificationDTO.getDeviceId()) : null;
        Verification verification = new Verification(new Date(), new Date(), clientData, provider, device, Status.SENT,
                Verification.ReadStatus.UNREAD, info, verificationDTO.getDismantled(), counter, verificationDTO.getComment());

        verificationService.saveVerification(verification);

        return verification.getId();
    }

    /**
     * Get verification by verificationId fot Creating form by pattern
     */
    @RequestMapping(value = "verification/{verificationId}", method = RequestMethod.GET)
    public OrganizationStageVerificationDTO getVerificationCode(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
            //logger.trace(verification.getRejectedMessage());
            return new OrganizationStageVerificationDTO(
                    verification.getClientData(),
                    verification.getClientData().getClientAddress(),
                    verification.getId(),
                    verification.getCalibrator(),
                    verification.getComment(),
                    verification.getInfo(),
                    verification.getDismantled(),
                    verification.getCounter()
            );
        } else {
            return null;
        }
    }

    /**
     * get all counter symbols from table counter_type
     */
    @RequestMapping(value = "symbols", method = RequestMethod.GET)
    public List<CounterTypeDTO> findAllSymbols() {

        return CounterTypeDTOTransformer.toDtofromList(providerService.findAllSymbols());
    }

    @RequestMapping(value = "standardSizes/{symbol}", method = RequestMethod.GET)
    public List<CounterTypeDTO> findStandardSizesBySymbol(@PathVariable String symbol) {
        return CounterTypeDTOTransformer
                .toDtofromList(providerService
                        .findStandardSizesBySymbol(symbol));
    }

    /**
     * Find region corresponding to provider service area
     *
     * @param employeeUser
     * @return
     */
    @RequestMapping(value = "region", method = RequestMethod.GET)
    public List<Region> getRegionCorrespondingProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        LocalityDTO localityDTO = localityService.findLocalitiesByOrganizationId(employeeUser.getOrganizationId()).stream()
                .map(locality -> new LocalityDTO(locality.getId(), locality.getDesignation(), locality.getDistrict().getId()))
                .collect(Collectors.toList()).get(0);
        List<Region> regions = new ArrayList<>();
        regions.add(regionService.findByDistrictId(localityDTO.getDistrictId()));
        return regions;
    }

    /**
     * Find provider devices
     *
     * @param employeeUser
     * @return
     */
    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public List<DeviceLightDTO> getDevicesCorrespondingProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        Set<Device.DeviceType> providerDeviceTypes = organizationService.findDeviceTypesByOrganizationId(employeeUser.getOrganizationId());
        return deviceService.getAll().stream()
                .filter(device -> providerDeviceTypes.contains(device.getDeviceType()))
                .map(device -> new DeviceLightDTO(device.getId(), device.getDeviceName(), device.getDeviceType().name()))
                .collect(Collectors.toList());
    }

    /**
     * Find districts corresponding to provider service area
     *
     * @param regionId
     * @param employeeUser
     * @return
     */
    @RequestMapping(value = "districts/{regionId}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getDistrictsCorrespondingProvider(@PathVariable Long regionId,
                                                                       @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        Set<Long> localityIdList = localityService.findLocalitiesByOrganizationId(employeeUser.getOrganizationId())
                .stream()
                .map(locality -> locality.getDistrict().getId())
                .collect(Collectors.toSet());

        return CatalogueDTOTransformer.toDto(districtService.getDistrictsCorrespondingRegion(regionId)
                .stream()
                .filter(district -> localityIdList.contains(district.getId()))
                .collect(Collectors.toList()));
    }

    /**
     * Find localities corresponding to provider service area
     *
     * @param districtId
     * @param employeeUser
     * @return
     */
    @RequestMapping(value = "localities/{districtId}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getLocalitiesCorrespondingProvider(@PathVariable Long districtId,
                                                                        @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        return CatalogueDTOTransformer.toDto(localityService.findByDistrictIdAndOrganizationId(districtId, employeeUser.getOrganizationId()));
    }

    /**
     * Find provider by id, finds region corresponding to provider region, finds district
     * corresponding to provider district and id
     *
     * @return ApplicationFieldDTO which contains id and designation corresponding to
     * locality id an designation
     */
    @RequestMapping(value = "localities", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getLocalityCorrespondingProvider(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        Organization provider = providerService.findById(employeeUser.getOrganizationId());
        Region region = regionService.getRegionByDesignation(provider.getAddress().getRegion());
        District district = districtService.findDistrictByDesignationAndRegion(
                provider.getAddress().getDistrict(),
                region.getId()
        );
        return CatalogueDTOTransformer.toDto(localityService.getLocalitiesCorrespondingDistrict(district.getId()));
    }

    @RequestMapping(value = "organizationType", method = RequestMethod.GET)
    public Long checkOrganizationType(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Set<String> types = providerService.getTypesById(user.getOrganizationId());
        for (String type : types) {
            if (type.equalsIgnoreCase("CALIBRATOR")) {
                return user.getOrganizationId();
            }
        }
        return (long) -1;
    }

    @RequestMapping(value = "new/mail", method = RequestMethod.POST)
    public String sendReject(@RequestBody RejectMailDTO reject) {
        Verification verification = verificationService.findById(reject.getVerifID());
        String name = verification.getClientData().getFirstName();
        String sendTo = verification.getClientData().getEmail();
        //saving rejectMessage in database if verification is rejected
        if (verification.getStatus() == Status.REJECTED) {
            verification.setRejectedMessage(reject.getMsg());
            verificationService.saveVerification(verification);
        }
        mail.sendRejectMail(sendTo, name, reject.getVerifID(), reject.getMsg(), verification.getDevice().getDeviceType().toString());
        return reject.getVerifID();
    }
}
