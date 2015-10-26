package com.softserve.edu.controller.calibrator;

import com.softserve.edu.controller.calibrator.util.CalibratorTestPageDTOTransformer;
import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.dto.*;
import com.softserve.edu.dto.provider.VerificationDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.dto.provider.VerificationProviderEmployeeDTO;
import com.softserve.edu.dto.provider.VerificationReadStatusUpdateDTO;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.calibrator.BBIFileServiceFacade;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/calibrator/verifications/")
public class CalibratorVerificationController {

    private static final String contentExtensionPattern = "^.*\\.(bbi|BBI|)$";
    private static final String archiveExtensionPattern = "^.*\\.(zip|ZIP|)$";

    private final Logger logger = Logger.getLogger(CalibratorVerificationController.class);
    @Autowired
    VerificationService verificationService;

    @Autowired
    ProviderService providerService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    CalibratorEmployeeService calibratorEmployeeService;

    @Autowired
    CalibrationTestService testService;

    @Autowired
    StateVerificatorService verificatorService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    UserService userService;

    @Autowired
    BbiFileService bbiFileService;

    @Autowired
    BBIFileServiceFacade bbiFileServiceFacade;

    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearch(
            @PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder, NewVerificationsFilterSearch searchData,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfVerificationsByCalibratorIdAndCriteriaSearch(employeeUser.getOrganizationId(), pageNumber, itemsPerPage,
                searchData.getDate(),
                searchData.getEndDate(),
                searchData.getId(),
                searchData.getClient_full_name(),
                searchData.getStreet(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStatus(),
                searchData.getEmployee_last_name(),
                sortCriteria, sortOrder, calibratorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }

    /**
     * Responds a page according to input data and search value
     *
     * @param pageNumber   current page number
     * @param itemsPerPage count of elements per one page
     * @return a page of CalibrationTests with their total amount
     */
    @RequestMapping(value = "calibration-test/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<CalibrationTestDTO> pageCalibrationTestWithSearch(@PathVariable Integer pageNumber,
                                                    @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder, CalibrationTestSearch searchData) {

        ListToPageTransformer<CalibrationTest> queryResult = verificationService
                .findPageOfCalibrationTestsByVerificationId(
                        pageNumber,
                        itemsPerPage,
                        searchData.getDate(),
                        searchData.getEndDate(),
                        searchData.getName(),
                        searchData.getRegion(),
                        searchData.getDistrict(),
                        searchData.getLocality(),
                        searchData.getStreet(),
                        searchData.getId(),
                        searchData.getClientFullName(),
                        searchData.getSettingNumber(),
                        searchData.getConsumptionStatus(),
                        searchData.getProtocolId(),
                        searchData.getTestResult(),
                        searchData.getMeasurementDeviceId(),
                        searchData.getMeasurementDeviceType(),
                        sortCriteria,
                        sortOrder);

        List<CalibrationTestDTO> content = CalibratorTestPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<>(queryResult.getTotalItems(), content);

    }

    /**
     * Find page of verifications by specific criterias on main panel
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param verifDate    (optional)
     * @param verifId      (optional)
     * @param lastName     (optional)
     * @param firstName    (optional)
     * @param street       (optional)
     * @param employeeUser
     * @return PageDTO<VerificationPageDTO>
     */
    @RequestMapping(value = "new/mainpanel/{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByProviderIdAndSearchOnMainPanel(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                                                      NewVerificationsSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByCalibratorIdOnMainPanel(
                employeeUser.getOrganizationId(),
                pageNumber,
                itemsPerPage,
                searchData.getFormattedDate(),
                searchData.getIdText(),
                searchData.getClient_full_name(),
                searchData.getStreetText(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStatus(),
                searchData.getEmployee(),
                calibratorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }

    /**
     * Finds count of verifications which have read status 'UNREAD' and are
     * assigned to this organization
     *
     * @param user
     * @return Long
     */
    @RequestMapping(value = "new/count/calibrator", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByCalibratorId(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            return verificationService.findCountOfNewVerificationsByCalibratorId(user.getOrganizationId());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "new/verificators", method = RequestMethod.GET)
    public List<Organization> getMatchingVerificators(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        //todo need to find verificators by agreements(договорах)
        //todo it`s a MOCK
        /*return verificatorService.findByDistrictAndType(
                calibratorService.findById(user.getOrganizationId()).getAddress().getDistrict(), "STATE_VERIFICATOR");*/
        Set<Long> serviceAreaIds = organizationService.getOrganizationById(user.getOrganizationId()).getLocalities()
                .stream().map(locality -> locality.getId()).collect(Collectors.toSet());

        return organizationService.findByServiceAreaIdsAndOrganizationType(serviceAreaIds, OrganizationType.STATE_VERIFICATOR);
    }

    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void updateVerification(@RequestBody VerificationUpdateDTO verificationUpdateDTO) {
        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Long idCalibrator = verificationUpdateDTO.getOrganizationId();
            Organization calibrator = calibratorService.findById(idCalibrator);
            verificationService.sendVerificationTo(verificationId, calibrator, Status.SENT_TO_VERIFICATOR);
        }
    }

    /**
     * Update verification when user reads it
     *
     * @param verificationDto
     */
    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(),
                verificationDto.getReadStatus());
    }

    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getNewVerificationDetailsById(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
            return new VerificationDTO(verification.getClientData(), verification.getId(),
                    verification.getInitialDate(), verification.getExpirationDate(), verification.getStatus(),
                    verification.getCalibrator(), verification.getCalibratorEmployee(), verification.getDevice(),
                    verification.getProvider(), verification.getProviderEmployee(), verification.getStateVerificator(),
                    verification.getStateVerificatorEmployee());
        } else {
            return null;
        }
    }

    /**
     * Current method received bbi file and save in system
     *
     * @param file
     * @param idVerification
     * @return status witch depends on loading file
     */
    @RequestMapping(value = "new/upload", method = RequestMethod.POST)
    public ResponseEntity uploadFileBbi(@RequestBody MultipartFile file, @RequestParam String idVerification) {
        ResponseEntity responseEntity;
        try {
            String originalFileFullName = file.getOriginalFilename();
            String fileType = originalFileFullName.substring(originalFileFullName.lastIndexOf('.'));
            if (Pattern.compile(contentExtensionPattern, Pattern.CASE_INSENSITIVE).matcher(fileType).matches()) {
                String originalFileName = file.getOriginalFilename();
                DeviceTestData deviceTestData = bbiFileServiceFacade.parseAndSaveBBIFile(file, idVerification, originalFileName);
                responseEntity = new ResponseEntity(new CalibrationTestFileDataDTO(deviceTestData), HttpStatus.OK);
            } else {
                logger.error("Failed to load file: pattern does not match.");
                responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to load file " + e.getMessage());
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    @RequestMapping(value = "new/upload-archive", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFileArchive(@RequestBody MultipartFile file) {
        ResponseEntity<String> httpStatus = new ResponseEntity(HttpStatus.OK);
        try {
            String originalFileFullName = file.getOriginalFilename();
            String fileType = originalFileFullName.substring(originalFileFullName.lastIndexOf('.'));
            if (Pattern.compile(archiveExtensionPattern, Pattern.CASE_INSENSITIVE).matcher(fileType).matches()) {
                bbiFileServiceFacade.parseAndSaveArchiveOfBBIfiles(file, originalFileFullName);
            } else {
                logger.error("Failed to load file ");
                httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Failed to load file " + e.getMessage());
            httpStatus = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return httpStatus;
    }


    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber,
                                                                                       @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder, ArchiveVerificationsFilterAndSort searchData,
                                                                                       @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {
        User calibratorEmployee = calibratorEmployeeService.oneCalibratorEmployee(employeeUser.getUsername());
//        System.out.println("CalibratorController searchData.getMeasurement_device_type() " + searchData.getMeasurement_device_type());
        System.out.println("prot from CalibratorController  protocol status= " + searchData.getProtocol_status());
        ListToPageTransformer<Verification> queryResult = verificationService
                .findPageOfArchiveVerificationsByCalibratorId(
                        employeeUser.getOrganizationId(),
                        pageNumber,
                        itemsPerPage,
                        searchData.getDate(),
                        searchData.getEndDate(),
                        searchData.getId(),
                        searchData.getClient_full_name(),
                        searchData.getStreet(),
                        searchData.getStatus(),
                        searchData.getEmployee_last_name(),
                        searchData.getProtocol_id(),
                        searchData.getProtocol_status(),
                        searchData.getMeasurement_device_id(),
                        searchData.getMeasurement_device_type(),
                        sortCriteria,
                        sortOrder, calibratorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
    }

    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById(@PathVariable String verificationId,
                                                              @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Verification verification = verificationService.findByIdAndCalibratorId(verificationId,
                user.getOrganizationId());

        return new VerificationDTO(
                verification.getClientData(),
                verification.getId(),
                verification.getInitialDate(),
                verification.getExpirationDate(),
                verification.getStatus(),
                verification.getCalibrator(),
                verification.getCalibratorEmployee(),
                verification.getDevice(),
                verification.getProvider(),
                verification.getProviderEmployee(),
                verification.getStateVerificator(),
                verification.getStateVerificatorEmployee());
    }

    @RequestMapping(value = "new/earliest_date/calibrator", method = RequestMethod.GET)
    public String getNewVerificationEarliestDateByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            Organization organization = organizationService.getOrganizationById(user.getOrganizationId());
            Date gottenDate = verificationService.getNewVerificationEarliestDateByCalibrator(organization);
            Date date = null;
            if (gottenDate != null) {
                date = new Date(gottenDate.getTime());
            } else {
                return null;
            }
            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDateTime localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            String isoLocalDateString = localDate.format(dbDateTimeFormatter);
            return isoLocalDateString;
        } else {
            return null;
        }
    }

    /**
     * Find date of earliest new verification
     *
     * @param user
     * @return String date
     */
    @RequestMapping(value = "archive/earliest_date/calibrator", method = RequestMethod.GET)
    public String getArchivalVerificationEarliestDateByProviderId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            Organization organization = organizationService.getOrganizationById(user.getOrganizationId());
            Date gottenDate = verificationService.getArchivalVerificationEarliestDateByCalibrator(organization);
            Date date = null;
            if (gottenDate != null) {
                date = new Date(gottenDate.getTime());
            } else {
                return null;
            }
            DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDateTime localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            String isoLocalDateString = localDate.format(dbDateTimeFormatter);
            return isoLocalDateString;
        } else {
            return null;
        }
    }

    /**
     * Current method search for file name witch user decided to delete
     *
     * @param idVerification
     * @return name of file and corresponding verification ID
     */
    @RequestMapping(value = "find/uploadFile", method = RequestMethod.GET)
    public List<String> getBbiFile(@RequestParam String idVerification) {
        List<String> data = new ArrayList();
        String fileName = calibratorService.findBbiFileByOrganizationId(idVerification);
        data = Arrays.asList(idVerification, fileName);
        return data;
    }

    /**
     * Check if current user is Employee
     *
     * @param user
     * @return true if user has role CALIBRATOR_EMPLOYEE
     * false if user has role CALIBRATOR_ADMIN
     */
    @RequestMapping(value = "calibrator/role", method = RequestMethod.GET)
    public Boolean isEmployeeCalibrator(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User checkedUser = userService.findOne(user.getUsername());
        return checkedUser.getUserRoles().contains(UserRole.CALIBRATOR_EMPLOYEE);
    }


    /**
     * get list of employees if it calibrator admin
     * or get data about employee.
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "new/calibratorEmployees", method = RequestMethod.GET)
    public List<com.softserve.edu.service.utils.EmployeeDTO> employeeVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User employee = calibratorEmployeeService.oneCalibratorEmployee(user.getUsername());
        List<String> role = userService.getRoles(user.getUsername());
        return calibratorEmployeeService
                .getAllCalibrators(role, employee);
    }

    /**
     * Assigning employee to verification
     *
     * @param verificationProviderEmployeeDTO
     */
    @RequestMapping(value = "assign/calibratorEmployee", method = RequestMethod.PUT)
    public void assignCalibratorEmployee(@RequestBody VerificationProviderEmployeeDTO verificationProviderEmployeeDTO) {
        String userNameCalibrator = verificationProviderEmployeeDTO.getEmployeeCalibrator().getUsername();
        String idVerification = verificationProviderEmployeeDTO.getIdVerification();
        User employeeCalibrator = calibratorService.oneCalibratorEmployee(userNameCalibrator);
        calibratorService.assignCalibratorEmployee(idVerification, employeeCalibrator);
    }

    /**
     * remove from verification assigned employee.
     *
     * @param verificationUpdatingDTO
     */
    @RequestMapping(value = "remove/calibratorEmployee", method = RequestMethod.PUT)
    public void removeCalibratorEmployee(@RequestBody VerificationProviderEmployeeDTO verificationUpdatingDTO) {
        String idVerification = verificationUpdatingDTO.getIdVerification();
        calibratorService.assignCalibratorEmployee(idVerification, null);
    }


    @RequestMapping(value = "/saveInfo", method = RequestMethod.POST)
    public ResponseEntity saveAddInfo(@RequestBody AdditionalInfoDTO infoDTO){
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            calibratorService.saveInfo(infoDTO.getEntrance(), infoDTO.getDoorCode(), infoDTO.getFloor(),
                    infoDTO.getDateOfVerif(), infoDTO.getTime(), infoDTO.isServiceability(), infoDTO.getNoWaterToDate(),
                    infoDTO.getNotes(), infoDTO.getVerificationId());
        } catch (Exception e) {
            logger.error("GOT EXCEPTION " + e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);

    }

    @RequestMapping(value = "/checkInfo/{verificationId}", method = RequestMethod.GET)
    public boolean checkIfAdditionalInfoExists(@PathVariable String verificationId){
        boolean exists = calibratorService.checkIfAdditionalInfoExists(verificationId);
        return exists;
    }

    @RequestMapping(value = "/findInfo/{verificationId}", method = RequestMethod.GET)
    public AdditionalInfoDTO findAdditionalInfoByVerifId(@PathVariable String verificationId){
        AdditionalInfo info = calibratorService.findAdditionalInfoByVerifId(verificationId);
        String time = ((info.getTimeFrom()==null) && (info.getTimeTo() == null)) ? "час відсутній" : (info.getTimeFrom().toString() + "-" +info.getTimeTo().toString());
        AdditionalInfoDTO infoDTO = new AdditionalInfoDTO(info.getEntrance(), info.getDoorCode(), info.getFloor(),
                info.getDateOfVerif(), time, info.isServiceability(), info.getNoWaterToDate(), info.getNotes(), info.getVerification().getId());
        return infoDTO;
    }


}
