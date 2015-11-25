package com.softserve.edu.controller.admin;

import com.softserve.edu.controller.admin.util.OrganizationEditPageDTOTransformer;
import com.softserve.edu.controller.admin.util.OrganizationPageDTOTransformer;
import com.softserve.edu.dto.NewOrganizationFilterSearch;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.*;
import com.softserve.edu.dto.application.ApplicationFieldDTO;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.dto.LocalityDTO;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.AdditionInfoOrganization;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.organization.OrganizationEditHistory;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.catalogue.LocalityService;
import com.softserve.edu.service.catalogue.RegionService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.user.UserService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.OrganizationAdminDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/organization/")
public class OrganizationController {

    private final Logger logger = Logger
            .getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private UserService userService;

    /**
     * Saves organization and its administrator employee in database
     *
     * @param organizationDTO object with organization and employee admin data
     * @return a response body with http status {@literal CREATED} if everything
     * organization and employee admin successfully created or else http
     * status {@literal CONFLICT}
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addOrganization(
            @RequestBody OrganizationDTO organizationDTO,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        Address address = new Address(
                organizationDTO.getRegion(),
                organizationDTO.getDistrict(),
                organizationDTO.getLocality(),
                organizationDTO.getStreet(),
                organizationDTO.getBuilding(),
                organizationDTO.getFlat());
        Address addressRegistered = new Address(
                organizationDTO.getRegionRegistered(),
                organizationDTO.getDistrictRegistered(),
                organizationDTO.getLocalityRegistered(),
                organizationDTO.getStreetRegistered(),
                organizationDTO.getBuildingRegistered(),
                organizationDTO.getFlatRegistered());
        AdditionInfoOrganization additionInfoOrganization = new AdditionInfoOrganization(
                organizationDTO.getCodeEDRPOU(),
                organizationDTO.getSubordination(),
                organizationDTO.getCertificateNumrAuthoriz(),
                organizationDTO.getCertificateDate());
        try {
            String adminName = user.getUsername();
            organizationService.addOrganizationWithAdmin(
                    organizationDTO.getName(),
                    organizationDTO.getEmail(),
                    organizationDTO.getPhone(),
                    organizationDTO.getTypes(),
                    organizationDTO.getCounters(),
                    organizationDTO.getEmployeesCapacity(),
                    organizationDTO.getMaxProcessTime(),
                    organizationDTO.getFirstName(),
                    organizationDTO.getLastName(),
                    organizationDTO.getMiddleName(),
                    organizationDTO.getUsername(),
                    address,
                    addressRegistered,
                    additionInfoOrganization,
                    adminName,
                    organizationDTO.getServiceAreas()
            );
        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.error("Got exeption while add organization ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }

    /**
     * Fetch required data for all organization depends on  received {@param pageNumber}, {@param itemsPerPage},
     * {@param sortCriteria} {@param sortOrder} and {@param searchData} that contains fields must be filtered
     *
     * @param pageNumber   number of page to return
     * @param itemsPerPage count of items on page
     * @param sortCriteria sorting criteria
     * @param sortOrder    order of sorting
     * @param searchData   filtering data
     * @return PageDTO that contains required data about current organizations depends on received filter, sort ,pagination and items per page
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<OrganizationPageItem> pageOrganizationsWithSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                                     @PathVariable String sortCriteria, @PathVariable String sortOrder, NewOrganizationFilterSearch searchData) {
        ListToPageTransformer<Organization> queryResult = organizationService.getOrganizationsBySearchAndPagination(
                pageNumber,
                itemsPerPage,
                searchData.getName(),
                searchData.getEmail(),
                searchData.getType(),
                searchData.getPhone_number(),
                searchData.getRegion(),
                searchData.getDistrict(),
                searchData.getLocality(),
                searchData.getStreet(),
                sortCriteria,
                sortOrder
        );
        List<OrganizationPageDTO> content = OrganizationPageDTOTransformer.toDtoFromList(queryResult.getContent());
        return new PageDTO(queryResult.getTotalItems(), content);
    }

    /**
     * Responds a page according to input data.
     * <p>
     * <p>
     * Note that this uses method {@code pageOrganizationsWithSearch}, whereas
     * search values is {@literal null}
     *
     * @param pageNumber   current page number
     * @param itemsPerPage count of elements per one page
     * @return a page of organizations with their total amount
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<OrganizationPageItem> getOrganizationsPage(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage) {
        return pageOrganizationsWithSearch(pageNumber, itemsPerPage, null, null, null);
    }

    /**
     * Fetch data depends on organization with received {@param id}
     *
     * @param id id of organizaton
     * @return OrganizationDTO
     */
    @RequestMapping(value = "getOrganization/{id}")
    public OrganizationDTO getOrganization(@PathVariable("id") Long id) {
        Organization organization = organizationService.getOrganizationById(id);

        List<String> types = new ArrayList<>();
        organization.getOrganizationTypes().
                stream()
                .map(OrganizationType::name)
                .forEach(types::add);

        List<String> counters = new ArrayList<>();
        organization.getDeviceTypes().
                stream()
                .map(Device.DeviceType::name)
                .forEach(counters::add);

        return new OrganizationDTO(organization.getId(), organization.getName(), organization.getEmail(), organization.getPhone(), types, counters,
                organization.getEmployeesCapacity(), organization.getMaxProcessTime(), organization.getAddress().getRegion(), organization.getAddress().getDistrict(), organization.getAddress().getLocality(),
                organization.getAddress().getStreet(), organization.getAddress().getBuilding(), organization.getAddress().getFlat());
    }

    /**
     * Edit organization in database
     *
     * @param organization object with organization data
     * @return a response body with http status {@literal OK} if organization
     * successfully edited or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "edit/{organizationId}", method = RequestMethod.POST)
    public ResponseEntity editOrganization(
            @RequestBody OrganizationEditDTO organization,
            @PathVariable Long organizationId,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user)
            throws UnsupportedEncodingException, MessagingException {
        HttpStatus httpStatus = HttpStatus.OK;

        Address address = new Address(
                organization.getRegion(),
                organization.getDistrict(),
                organization.getLocality(),
                organization.getStreet(),
                organization.getBuilding(),
                organization.getFlat());
        try {
            String adminName = user.getUsername();

            organizationService.editOrganization(
                    organizationId,
                    organization.getName(),
                    organization.getPhone(),
                    organization.getEmail(),
                    organization.getTypes(),
                    organization.getCounters(),
                    organization.getEmployeesCapacity(),
                    organization.getMaxProcessTime(),
                    address,
                    organization.getPassword(),
                    organization.getUsername(),
                    organization.getFirstName(),
                    organization.getLastName(),
                    organization.getMiddleName(),
                    adminName,
                    organization.getServiceAreas());
        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.error("Got exeption while editing organization ", e);
            httpStatus = HttpStatus.CONFLICT;
        }

        Organization org = organizationService
                .getOrganizationById(organizationId);

        User admin = userService.findOne(organization.getUsername());

        organizationService.sendOrganizationChanges(org, admin);

        return new ResponseEntity(httpStatus);
    }

    /**
     * Fetch organization admin data depends on organization with id {@param id}
     *
     * @param id id of organizaton
     * @return OrganizationAdminDTO
     */
    @RequestMapping(value = "getOrganizationAdmin/{id}")
    public OrganizationAdminDTO getAdmin(@PathVariable("id") Long id) {
        Organization organization = organizationService.getOrganizationById(id);
        OrganizationAdminDTO organizationAdminDTO = new OrganizationAdminDTO();
        try {
            User user = organization
                    .getUsers()
                    .stream()
                    .filter(userChecked -> userChecked.getUserRoles()
                                    .stream()
                                    .map(UserRole::name)
                                    .filter(userRole -> userRole.equals(UserRole.PROVIDER_ADMIN.name()) ||
                                                    userRole.equals(UserRole.CALIBRATOR_ADMIN.name()) || userRole.equals(UserRole.STATE_VERIFICATOR_ADMIN.name())
                                    )
                                    .collect(Collectors.toList()).size() > 0
                    )
                    .findFirst().get();
            logger.info(user);
            organizationAdminDTO = new OrganizationAdminDTO(user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getUsername());
        } catch (Exception e) {
            logger.info("========================");
            logger.info("no one admin in organization");
            logger.info("========================");
            // Attempt to fix (critical issue) (sonar: for Either log or rethrow this exception.)
            logger.info(e);
        }

        return organizationAdminDTO;
    }

    /**
     * Fetch organization edit history for organization with  {@param organizationId}
     *
     * @param organizationId id of organizaton
     * @return PageDTO<OrganizationEditHistoryPageDTO>
     */
    @RequestMapping(value = "edit/history/{organizationId}")
    public PageDTO<OrganizationEditHistoryPageDTO> getEditHistory(@PathVariable("organizationId") Long organizationId) {
        List<OrganizationEditHistory> organizationEditHistoryList = organizationService.getHistoryByOrganizationId(organizationId);

        return new PageDTO<>(OrganizationEditPageDTOTransformer.toDtoFromList(organizationEditHistoryList));
    }

    /**
     * Finds all localities by organization id
     *
     * @param organizationId id of organizaton
     * @return list of localities
     */
    @RequestMapping(value = "serviceArea/localities/{organizationId}", method = RequestMethod.GET)
    public List<LocalityDTO> getServiceAreaLocaities(@PathVariable("organizationId") Long organizationId) {
        return localityService.findLocalitiesByOrganizationId(organizationId).stream()
                .map(locality -> new LocalityDTO(locality.getId(), locality.getDesignation(), locality.getDistrict().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Find region by district id
     *
     * @param districtId id of district
     * @return region
     */
    @RequestMapping(value = "serviceArea/region/{districtId}", method = RequestMethod.GET)
    public Region getServiceAreaRegion(@PathVariable("districtId") Long districtId) {
        return regionService.findByDistrictId(districtId);
    }

    /**
     * Find all organizations by organization types and device types
     *
     * @param organizationType type of organization
     * @param deviceType       type of device
     * @return list of organization wraped in ApplicationFieldDTO
     */
    @RequestMapping(value = "getOrganization/{organizationType}/{deviceType}", method = RequestMethod.GET)
    public List<ApplicationFieldDTO> getOrganizationByOrganizationTypeAndDeviceType(@PathVariable("organizationType") String organizationType,
                                                                                    @PathVariable("deviceType") String deviceType) {
        return organizationService.findByOrganizationTypeAndDeviceType(OrganizationType.valueOf(organizationType.toUpperCase()),
                Device.DeviceType.valueOf(deviceType.toUpperCase())).stream()
                .map(organization -> new ApplicationFieldDTO(organization.getId(), organization.getName()))
                .collect(Collectors.toList());
    }
}

