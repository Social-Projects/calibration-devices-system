package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.AdditionInfoOrganization;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.organization.OrganizationEditHistory;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.OrganizationEditHistoryRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.catalogue.LocalityService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.utils.ArchivalOrganizationsQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger logger = Logger.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationEditHistoryRepository organizationEditHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mail;

    @Autowired
    private LocalityService localityService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Set<Organization> findAll() {return organizationRepository.findAll();}

    @Override
    @Transactional
    public void addOrganizationWithAdmin(String name, String email, String phone, List<String> types, List<String> counters,
                                         Integer employeesCapacity, Integer maxProcessTime, String firstName, String lastName,
                                         String middleName, String username, Address address, Address addressRegistered,
                                         AdditionInfoOrganization additionInfoOrganization, String adminName,
                                         Long[] localityIdList) throws UnsupportedEncodingException, MessagingException {

        Organization organization = new Organization(name, email, phone, employeesCapacity, maxProcessTime, address,
                addressRegistered, additionInfoOrganization);
        String password = RandomStringUtils.randomAlphanumeric(firstName.length());
        String passwordEncoded = new BCryptPasswordEncoder().encode(password);
        User employeeAdmin = new User(firstName, lastName, middleName, username, passwordEncoded, organization);
        employeeAdmin.setIsAvailable(true);

        for (String type : types) {
            OrganizationType organizationType = OrganizationType.valueOf(type);
            employeeAdmin.addRole(OrganizationType.getOrganizationAdminRole(organizationType));
            organization.addOrganizationType(organizationType);
            organization.addUser(employeeAdmin);
        }

        for (String counter : counters) {
            Device.DeviceType deviceType = Device.DeviceType.valueOf(counter);
            organization.addDeviceType(deviceType);
        }

        for (Long localityId : localityIdList) {
            // TODO You'd move the 'localityService.findById()' out of the loop and refactor it to something like  'localityService.findByIds'
            Locality locality = localityService.findById(localityId);
            organization.addLocality(locality);
        }

        String stringOrganizationTypes = String.join(",", types);

        Date date = new Date();
        OrganizationEditHistory organizationEditHistory = new OrganizationEditHistory(date, name, email, phone, employeesCapacity,
                maxProcessTime, stringOrganizationTypes, username, firstName, lastName, middleName, organization, address, adminName);
        organizationEditHistoryRepository.save(organizationEditHistory);
        organization.addOrganizationChangeHistory(organizationEditHistory);
        organizationRepository.save(organization);

        mail.sendOrganizationPasswordMail(email, name, username, password);
    }

    /**
     * Fetch all required organization depends on received data
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param name
     * @param email
     * @param number
     * @param type
     * @param region
     * @param district
     * @param locality
     * @param streetToSearch
     * @param sortCriteria
     * @param sortOrder
     * @return ListToPageTransformer<Organization> contains required organizations
     */
    @Override
    @Transactional(readOnly = true)
    public ListToPageTransformer<Organization> getOrganizationsBySearchAndPagination(
            int pageNumber, int itemsPerPage, String name, String email,
            String number, String type, String region, String district, String locality,
            String streetToSearch, String sortCriteria, String sortOrder
    ) {

        CriteriaQuery<Organization> criteriaQuery = ArchivalOrganizationsQueryConstructorAdmin
                .buildSearchQuery(name, email, number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager);

        Long count = entityManager.createQuery(ArchivalOrganizationsQueryConstructorAdmin
                .buildCountQuery(name, email, number, type, region, district, locality, streetToSearch, entityManager)).getSingleResult();

        TypedQuery<Organization> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<Organization> OrganizationList = typedQuery.getResultList();

        ListToPageTransformer<Organization> result = new ListToPageTransformer<>();
        result.setContent(OrganizationList);
        result.setTotalItems(count);
        return result;
    }

    /**
     * Fetch required organization by id
     *
     * @param id
     * @return organization finded by {@param id}
     */
    @Override
    @Transactional(readOnly = true)
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findOne(id);
    }

    /**
     * Edit organization data with {@param organizationId} and organization admin data
     *
     * @param organizationId
     * @param name
     * @param phone
     * @param email
     * @param types
     * @param counters
     * @param employeesCapacity
     * @param maxProcessTime
     * @param address
     * @param password
     * @param username
     * @param firstName
     * @param lastName
     * @param middleName
     * @param adminName
     * @param serviceAreas
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    @Override
    @Transactional
    public void editOrganization(Long organizationId, String name,
                                 String phone, String email, List<String> types, List<String> counters, Integer employeesCapacity,
                                 Integer maxProcessTime, Address address, String password, String username,
                                 String firstName, String lastName, String middleName, String adminName, List<Long> serviceAreas)  throws UnsupportedEncodingException, MessagingException  {

        Organization organization = organizationRepository.findOne(organizationId);

        logger.debug(organization);

        organization.setName(name);
        organization.setPhone(phone);
        organization.setEmail(email);
        organization.setEmployeesCapacity(employeesCapacity);
        organization.setMaxProcessTime(maxProcessTime);
        organization.setAddress(address);

        organization.removeOrganizationTypes();
        types.
                stream()
                .map(OrganizationType::valueOf)
                .forEach(organization::addOrganizationType);

        organization.removeDeviceType();
        counters.
                stream()
                .map(Device.DeviceType::valueOf)
                .forEach(organization::addDeviceType);

        organization.removeServiceAreas();
        //organizationRepository.save(organization);
        //serviceAreas.stream().map(localityService::findById).forEach(organization::addLocality);
        for (Long localityId : serviceAreas) {
            Locality locality = localityService.findById(localityId);
            organization.addLocality(locality);
        }

        User employeeAdmin = userRepository.findOne(username);
        employeeAdmin.setFirstName(firstName);
        employeeAdmin.setLastName(lastName);
        employeeAdmin.setMiddleName(middleName);

        employeeAdmin.setPassword(password != null && password.equals("generate") ? "generate" : employeeAdmin.getPassword());

        if (employeeAdmin.getPassword().equals("generate")) {
            String newPassword = RandomStringUtils.randomAlphanumeric(5);
            mail.sendNewPasswordMail(employeeAdmin.getEmail(), employeeAdmin.getFirstName(), newPassword);
            String passwordEncoded = new BCryptPasswordEncoder().encode(newPassword);
            employeeAdmin.setPassword(passwordEncoded);
        }

        userRepository.save(employeeAdmin);

        String stringOrganizationTypes = String.join(",", types);

        Date date = new Date();

        OrganizationEditHistory organizationEditHistory = new OrganizationEditHistory(date, name, email, phone, employeesCapacity,
                maxProcessTime, stringOrganizationTypes, username, firstName, lastName, middleName, organization, address, adminName);

        organizationEditHistoryRepository.save(organizationEditHistory);
        organization.addOrganizationChangeHistory(organizationEditHistory);
        organizationRepository.save(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getOrganizationEmployeesCapacity(Long organizationId) {
        return organizationRepository.findOne(organizationId).getEmployeesCapacity();
    }

    /**
     * Send to organization email organizations edited data
     *
     * @param organization
     * @param admin
     */
    @Override
    @Transactional
    public void sendOrganizationChanges(Organization organization, User admin) {
        mail.sendOrganizationChanges(organization, admin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationEditHistory> getHistoryByOrganizationId(Long organizationId) {
        return organizationEditHistoryRepository.findByOrganizationId(organizationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllByLocalityId(Long localityId) {
        return organizationRepository.findOrganizationByLocalityId(localityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllByLocalityIdAndTypeId(Long localityId, OrganizationType typeId) {
        return organizationRepository.findOrganizationByLocalityIdAndType(localityId, typeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<OrganizationType> findOrganizationTypesById(Long id) {
        return organizationRepository.findOrganizationTypesById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findByLocalityIdAndTypeAndDevice(Long localityId, OrganizationType orgType, Device.DeviceType deviceType) {
        return organizationRepository.findByLocalityIdAndTypeAndDevice(localityId, orgType, deviceType);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Device.DeviceType> findDeviceTypesByOrganizationId(Long organizationId) {
        return organizationRepository.findDeviceTypesByOrganizationId(organizationId);
    }

    /**
     * Find all organizations by organization types and device types
     * @param organizationType type of organization
     * @param deviceType type of device
     * @return list of organization
     */
    @Override
    @Transactional(readOnly = true)
    public List<Organization> findByOrganizationTypeAndDeviceType(OrganizationType organizationType, Device.DeviceType deviceType) {
        return organizationRepository.findByOrganizationTypeAndDeviceType(organizationType, deviceType);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Organization> findByIdAndTypeAndActiveAgreementDeviceType(Long customerId, OrganizationType organizationType, Device.DeviceType deviceType) {
        return organizationRepository.findByIdAndTypeAndActiveAgreementDeviceType(customerId, organizationType, deviceType);
    }


}
