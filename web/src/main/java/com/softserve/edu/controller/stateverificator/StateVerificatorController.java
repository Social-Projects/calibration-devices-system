package com.softserve.edu.controller.stateverificator;

import com.softserve.edu.controller.provider.util.VerificationPageDTOTransformer;
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
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.provider.ProviderService;
import com.softserve.edu.service.state.verificator.StateVerificatorEmployeeService;
import com.softserve.edu.service.state.verificator.StateVerificatorService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.user.UserService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/verificator/verifications/")
public class StateVerificatorController {

    @Autowired
    VerificationService verificationService;

    @Autowired
    UserService userService;

    @Autowired
    CalibrationTestService testService;

    @Autowired
    ProviderService providerService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CalibratorService calibratorService;

    @Autowired
    StateVerificatorEmployeeService stateVerificatorEmployeeService;

    @Autowired
    StateVerificatorService stateVerificatorService;

    @Autowired
    StateVerificatorEmployeeService employeeService;

    private final Logger logger = Logger.getLogger(StateVerificatorController.class);

    /**
     * Responds a page according to input data and search value
     *
     * @param pageNumber   current page number
     * @param itemsPerPage count of elements per one page
     * @return a page of new verifications assign on state-verificator with their total amount
     */
    @RequestMapping(value = "new/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfAllSentVerificationsByStateVerificatorIdAndSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder,
    		NewVerificationsFilterSearch searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User verificatorEmployee = stateVerificatorEmployeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfVerificationsByVerificatorIdAndCriteriaSearch(
                employeeUser.getOrganizationId(), pageNumber, itemsPerPage,
                searchData.getDate(),
                searchData.getId(),
                searchData.getClient_full_name(),
                searchData.getStreet(),
                searchData.getStatus(),
                searchData.getEmployee_last_name(),
                sortCriteria,
                sortOrder,
	verificatorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<VerificationPageDTO>(queryResult.getTotalItems(), content);
    }

    /**
     * Find providers by district which correspond state-verificator district
     *
     * @return provider
     */
    @RequestMapping(value = "new/providers", method = RequestMethod.GET)
    public Set<Organization> getMatchingVerificators(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        //todo need to fix finding verificators by agreements(договорах)
        Organization userOrganization = organizationService.getOrganizationById(user.getOrganizationId());
        return organizationService.findByIdAndTypeAndActiveAgreementDeviceType(user.getOrganizationId(), OrganizationType.PROVIDER, userOrganization.getDeviceTypes().iterator().next());
    }

    /**
     * Find calibrators by district which correspond provider district
     *
     * @return calibrator
     */
    @RequestMapping(value = "new/calibrators", method = RequestMethod.GET)
    public Set<Organization> updateVerification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        //todo need to fix finding calibrators by agreements(договорах)
        Organization userOrganization = organizationService.getOrganizationById(user.getOrganizationId());
        return organizationService.findByIdAndTypeAndActiveAgreementDeviceType(user.getOrganizationId(), OrganizationType.PROVIDER, userOrganization.getDeviceTypes().iterator().next());
    }

    /**
     * Shows count of new verifications assigned on state-verificator
     * @param user
     * @return count of new verifications
     */
    @RequestMapping(value = "new/count/verificator", method = RequestMethod.GET)
    public Long getCountOfNewVerificationsByStateVerificatorId(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        if (user != null) {
            return verificationService.findCountOfNewVerificationsByStateVerificatorId(user.getOrganizationId());
        } else {
            return null;
        }
    }

    /**
     * Updates verifications UNREAD status to READ
     * @param verificationDto
     */
    @RequestMapping(value = "new/read", method = RequestMethod.PUT)
    public void markVerificationAsRead(@RequestBody VerificationReadStatusUpdateDTO verificationDto) {
        verificationService.updateVerificationReadStatus(verificationDto.getVerificationId(), verificationDto.getReadStatus());
    }

    /**
     * Updates status of verification to TEST_OK and sent it to provider
     * @param verificationUpdateDTO
     */
    @RequestMapping(value = "new/update", method = RequestMethod.PUT)
    public void sendVerification(@RequestBody VerificationUpdateDTO verificationUpdateDTO) {
        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Long idProvider = verificationUpdateDTO.getOrganizationId();
            Organization provider = providerService.findById(idProvider);
            verificationService.sendVerificationTo(verificationId, provider, Status.TEST_OK);
        }
    }

    /**
     * Updates status of verification to TEST_NOK and sent it to provider
     * @param verificationUpdateDTO
     */
    @RequestMapping(value = "new/notOk", method = RequestMethod.PUT)
    public void sendWithNotOkStatus(@RequestBody VerificationUpdateDTO verificationUpdateDTO) {
        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Long idProvider = verificationUpdateDTO.getOrganizationId();
            Organization provider = providerService.findById(idProvider);
            verificationService.sendVerificationTo(verificationId, provider, Status.TEST_NOK);
        }
    }

    /**
     * Updates status of verification to IN_PROGRESS and sent it to calibrator
     * @param verificationUpdateDTO
     */
    @RequestMapping(value = "new/reject", method = RequestMethod.PUT)
    public void rejectVerification(@RequestBody VerificationUpdateDTO verificationUpdateDTO) {
        for (String verificationId : verificationUpdateDTO.getIdsOfVerifications()) {
            Verification verification = verificationService.findById(verificationId);
            verification.setRejectedMessage(verificationUpdateDTO.getMessage());
            verificationService.saveVerification(verification);
            Organization calibrator = calibratorService.findById(verification.getCalibrator().getId());
            verificationService.sendVerificationTo(verificationId, calibrator, Status.IN_PROGRESS);
        }
    }


    /**
     * Displays details about verification in modal window
     * @param verificationId
     * @return
     */
    @RequestMapping(value = "new/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getNewVerificationDetailsById(@PathVariable String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        if (verification != null) {
            return new VerificationDTO(verification.getClientData(), verification.getId(), verification.getInitialDate(),
                    verification.getExpirationDate(), verification.getStatus(), verification.getCalibrator(), verification.getCalibratorEmployee(),
                    verification.getDevice(), verification.getProvider(), verification.getProviderEmployee(), verification.getStateVerificator(),
                    verification.getStateVerificatorEmployee());
        } else {
            return null;
        }
    }

    /**
     * Displays details about calibration-test by verification ID
     * @param verificationId
     * @return calibration-test
     */
    @RequestMapping(value = "show/{verificationId}", method = RequestMethod.GET)
    public CalibrationTestDTO getCalibraionTestDetails(@PathVariable String verificationId) {
        CalibrationTest calibrationTest = testService.findByVerificationId(verificationId);
        return new CalibrationTestDTO(calibrationTest);

    }

    /**
     * Displays an archive with verifications of all statuses
     * @param pageNumber
     * @param itemsPerPage
     * @param searchData
     * @param employeeUser
     * @return
     */
    @RequestMapping(value = "archive/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<VerificationPageDTO> getPageOfArchivalVerificationsByOrganizationId(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage, @PathVariable String sortCriteria, @PathVariable String sortOrder,
    		ArchiveVerificationsFilterAndSort searchData, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails employeeUser) {

        User verificatorEmployee = employeeService.oneProviderEmployee(employeeUser.getUsername());
        ListToPageTransformer<Verification> queryResult = verificationService.findPageOfArchiveVerificationsByVerificatorId(
                employeeUser.getOrganizationId(), pageNumber, itemsPerPage,
                searchData.getDate(),
                searchData.getId(),
                searchData.getClient_full_name(),
                searchData.getStreet(),
                searchData.getStatus(),
                searchData.getEmployee_last_name(),
                sortCriteria,
                sortOrder,
                verificatorEmployee);
        List<VerificationPageDTO> content = VerificationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO<>(queryResult.getTotalItems(), content);
    }

    /**
     * Displays details about verification in archive
     * @param verificationId
     * @return verification
     */
    @RequestMapping(value = "archive/{verificationId}", method = RequestMethod.GET)
    public VerificationDTO getArchivalVerificationDetailsById(@PathVariable String verificationId, @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Verification verification = verificationService.findByIdAndStateVerificatorId(verificationId, user.getOrganizationId());
        return new VerificationDTO(verification.getClientData(), verification.getId(), verification.getInitialDate(),
                verification.getExpirationDate(), verification.getStatus(), verification.getCalibrator(),
                verification.getCalibratorEmployee(), verification.getDevice(), verification.getProvider(),
                verification.getProviderEmployee(), verification.getStateVerificator(),
                verification.getStateVerificatorEmployee(), verification.getRejectedMessage()
        );
    }


    /**
     * Check if current user is Employee
     * @param user
     * @return true if user has role STATE_VERIFICATOR_EMPLOYEE
     *         false if user has role STATE_VERIFICATOR_ADMIN
     */
    @RequestMapping(value = "verificator/role", method = RequestMethod.GET)
    public Boolean isEmployeeStateVerificator(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User checkedUser = userService.findOne(user.getUsername());
        return checkedUser.getUserRoles().contains(UserRole.STATE_VERIFICATOR_EMPLOYEE);
    }


    /**
     * return All Verificator Employee
     * using for add Employee to verification
     * @param user
     * @return
     */

    @RequestMapping(value = "new/verificatorEmployees", method = RequestMethod.GET)
    public List<com.softserve.edu.service.utils.EmployeeDTO> employeeStateVerificatorVerification(
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        User employee = stateVerificatorEmployeeService.oneProviderEmployee(user.getUsername());
        List<String> role = userService.getRoles(user.getUsername());
        return stateVerificatorService.getAllVerificatorEmployee(role, employee);
    }


    /**
     * set employee on a current verification,
     * and than this guy will have opportunity to
     * check this verification.
     * @param verificationProviderEmployeeDTO
     */
    @RequestMapping(value = "assign/verificatorEmployee", method = RequestMethod.PUT)
    public void assignVerificatorEmployee(@RequestBody VerificationProviderEmployeeDTO verificationProviderEmployeeDTO) {
        String usernameVerificator = verificationProviderEmployeeDTO.getEmployeeCalibrator().getUsername();
        String idVerification = verificationProviderEmployeeDTO.getIdVerification();
        User employeeCalibrator = stateVerificatorEmployeeService.oneProviderEmployee(usernameVerificator);
        stateVerificatorService.assignVerificatorEmployee(idVerification, employeeCalibrator);
    }

    /**
     * remove assigned employee on a current verification
     * @param verificationUpdatingDTO
     */
    @RequestMapping(value = "remove/verificatorEmployee", method = RequestMethod.PUT)
    public void removeVerificatorEmployee(@RequestBody VerificationProviderEmployeeDTO verificationUpdatingDTO) {
        String idVerification = verificationUpdatingDTO.getIdVerification();
        stateVerificatorService.assignVerificatorEmployee(idVerification, null);
    }
}
