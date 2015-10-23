package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.organization.OrganizationChangesHistory;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.OrganizationChangesHistoryRepository;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.catalogue.LocalityService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import com.softserve.edu.service.utils.ArchivalOrganizationsQueryConstructorAdmin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class OrganizationServiceImplTest {
    private final Long organizationId = 1L;
    private final Long localityId = 2L;
    private final OrganizationType orgType = OrganizationType.CALIBRATOR;
    private final DeviceType deviceType = DeviceType.WATER;
    private final List<Organization> organizationList = new ArrayList<>();
    private final Set<OrganizationType> organizationTypes = new HashSet<>();
    private final List<OrganizationChangesHistory> historyList = new ArrayList<>();
    private final int employeesCapasity = 6;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private Organization organization;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private OrganizationChangesHistoryRepository organizationChangesHistoryRepository;

    @Mock
    private LocalityService localityService;

    @Mock
    ProviderEmployeeService providerEmployeeService;

    @Mock
    private Locality locality;

    @Mock
    private MailServiceImpl mailService;

    @Mock
    EntityManager entityManager;

    @Mock
    ArchivalOrganizationsQueryConstructorAdmin archivalOrganizationsQueryConstructorAdmin;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @InjectMocks
    private OrganizationServiceImpl organizationService;


    @Before
    public void setUp() throws Exception {
        organizationService = new OrganizationServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        organizationService = null;
    }

    @Test
    public void testAddOrganizationWithAdmin() throws Exception {
        final String orgName = "name";
        final String mname = "mname";
        final String phone = "99999999";
        final String email = "email";
        final List<String> types = new ArrayList<String>();
        final List<String> counters = new ArrayList<String>();
        types.add("PROVIDER");
        counters.add("WATER");
        final Integer employeesCapacity = 13;
        final Integer maxProcessTime = 456;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final Long[] localityIdList = {1L};
        final String username = "eric123";
        final String password = "root";
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String adminName = "admin";

        stub(localityService.findById(anyLong())).toReturn(locality);
        organizationService.addOrganizationWithAdmin(orgName, email, phone, types, employeesCapacity, maxProcessTime,
                firstName, lastName, middleName, username, password, address, adminName, localityIdList);

        verify(organizationRepository, times(2)).save(new Organization(orgName, email, phone, employeesCapacity, maxProcessTime, address));
    }

    /*@Test
    public void testGetOrganizationsBySearchAndPagination() throws Exception {
        int pageNumber = 2;
        int itemsPerPage = 10;
        String name = "name";
        String email = "email";
        String number = "0000000000";
        String type = "PROVIDER";
        String region = "Lv";
        String district = "Lviv";
        String locality = "Lviv";
        String streetToSearch = "Shev";
        String sortCriteria = null;
        String sortOrder = null;

        stub(entityManager.getCriteriaBuilder()).toReturn(criteriaBuilder);
        stub(criteriaBuilder.createQuery(Organization.class)).toReturn(criteriaQuery);
        stub(archivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, number, type,
                region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager)).toReturn(criteriaQuery);

        organizationService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, name, email,
                number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder);
    }*/

    @Test
    public void testGetOrganizationById() throws Exception {
        stub(organizationRepository.findOne(organizationId)).toReturn(organization);
        Organization actual = organizationService.getOrganizationById(organizationId);

        assertEquals(organization, actual);
    }

    @Test
    public void testEditOrganization() throws Exception {

        Long organizationId = 1L;
        String name = "organization";
        String phone = "0000000000";
        String email = "email";
        List<String> types = new ArrayList<>();
        types.add("PROVIDER");
        Integer employeesCapacity = 7;
        Integer maxProcessTime = 12;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        String password = "root";
        String username = "root";
        String firstName = "fName";
        String lastName = "lName";
        String middleName = "mName";
        String adminName = "aName";

        stub(organizationRepository.findOne(organizationId)).toReturn(organization);
        stub(userRepository.findOne(username)).toReturn(user);

        organizationService.editOrganization(organizationId, name, phone, email, types,
                employeesCapacity, maxProcessTime, address, password, username,
                firstName, lastName, middleName, adminName);

        verify(organizationRepository).findOne(organizationId);
        verify(userRepository).findOne(username);
        verify(organizationRepository, times(2)).save(organization);
    }

    @Test
    public void testGetOrganizationEmployeesCapacity() throws Exception {
        stub(organization.getEmployeesCapacity()).toReturn(employeesCapasity);
        stub(organizationRepository.findOne(organizationId)).toReturn(organization);
        int actual = organizationService.getOrganizationEmployeesCapacity(organizationId);

        assertEquals(employeesCapasity, actual);
    }

    @Test
    public void testSendOrganizationChanges() throws Exception {
        organizationService.sendOrganizationChanges(organization, user);
        verify(mailService).sendOrganizationChanges(organization, user);
    }

    @Test
    public void testGetHistoryByOrganizationId() throws Exception {
        stub(organizationChangesHistoryRepository.findByOrganizationId(organizationId)).toReturn(historyList);
        List<OrganizationChangesHistory> actual = organizationService.getHistoryByOrganizationId(organizationId);

        assertEquals(historyList, actual);
    }

    @Test
    public void testFindAllByLocalityId() throws Exception {
        stub(organizationRepository.findOrganizationByLocalityId(localityId)).toReturn(organizationList);
        List<Organization> actual = organizationService.findAllByLocalityId(localityId);

        assertEquals(organizationList, actual);
    }

    @Test
    public void testFindAllByLocalityIdAndTypeId() throws Exception {
        stub(organizationRepository.findOrganizationByLocalityIdAndType(localityId, orgType)).toReturn(organizationList);
        List<Organization> actual = organizationService.findAllByLocalityIdAndTypeId(localityId, orgType);

        assertEquals(organizationList, actual);
    }

    @Test
    public void testFindOrganizationTypesById() throws Exception {
        stub(organizationRepository.findOrganizationTypesById(organizationId)).toReturn(organizationTypes);
        Set<OrganizationType> actual = organizationService.findOrganizationTypesById(organizationId);

        assertEquals(organizationTypes, actual);
    }


    @Test
    public void testFindByLocalityIdAndTypeAndDevice() throws Exception {
        stub(organizationRepository.findByLocalityIdAndTypeAndDevice(localityId, orgType, deviceType)).toReturn(organizationList);
        List<Organization> actual = organizationService.findByLocalityIdAndTypeAndDevice(localityId, orgType, deviceType);

        assertEquals(organizationList, actual);
    }
}