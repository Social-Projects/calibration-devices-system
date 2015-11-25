package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.device.Device.DeviceType;
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
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import com.softserve.edu.service.utils.ArchivalOrganizationsQueryConstructorAdmin;
import com.softserve.edu.service.utils.ListToPageTransformer;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ArchivalOrganizationsQueryConstructorAdmin.class})
public class OrganizationServiceImplTest {

    @InjectMocks
    private OrganizationService organizationService = new OrganizationServiceImpl();

    private final Long organizationId = 1L;
    private final Long localityId = 2L;
    private final OrganizationType orgType = OrganizationType.CALIBRATOR;
    private final DeviceType deviceType = DeviceType.WATER;
    private final List<Organization> organizationList = new ArrayList<>();
    private final Set<OrganizationType> organizationTypes = new HashSet<>();
    private final List<OrganizationEditHistory> historyList = new ArrayList<>();
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
    private OrganizationEditHistoryRepository organizationChangesHistoryRepository;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        organizationService = null;
    }

    @Test
    public void testAddOrganizationWithAdmin() throws Exception {
        final String name = "name";
        final String phone = "99999999";
        final String email = "email";
        final List<String> types = new ArrayList<String>();
        final List<String> counters = new ArrayList<String>();
        types.add("PROVIDER");
        counters.add("WATER");
        final Integer employeesCapacity = 13;
        final Integer maxProcessTime = 456;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final Address addressRegistered = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final Long[] localityIdList = {1L};
        final String username = "eric123";
        final AdditionInfoOrganization additionInfoOrganization = new AdditionInfoOrganization("123", "123", "123", 1288323623006L);
        String firstName = "firstName";
        String lastName = "lastName";
        String middleName = "middleName";
        String adminName = "admin";

        stub(localityService.findById(anyLong())).toReturn(locality);

        organizationService.addOrganizationWithAdmin(name, email, phone, types, counters, employeesCapacity,
                maxProcessTime, firstName, lastName, middleName,
                username, address, addressRegistered, additionInfoOrganization, adminName, localityIdList);

        verify(organizationRepository).save(new Organization(name, email, phone, employeesCapacity, maxProcessTime,
                address, addressRegistered, additionInfoOrganization));
    }

    @Test
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

        CriteriaQuery<Organization> organizationCriteriaQuery = mock(CriteriaQuery.class);
        CriteriaQuery<Long> longCriteriaQuery = mock(CriteriaQuery.class);
        TypedQuery<Organization> organizationTypedQuery = mock(TypedQuery.class);
        TypedQuery<Long> longTypedQuery = mock(TypedQuery.class);

        PowerMockito.mockStatic(ArchivalOrganizationsQueryConstructorAdmin.class);
        PowerMockito.when(ArchivalOrganizationsQueryConstructorAdmin.buildSearchQuery(name, email, number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder, entityManager)).thenReturn(organizationCriteriaQuery);
        PowerMockito.when(ArchivalOrganizationsQueryConstructorAdmin.buildCountQuery(name, email, number, type, region, district, locality, streetToSearch, entityManager)).thenReturn(longCriteriaQuery);

        stub(entityManager.createQuery(longCriteriaQuery)).toReturn(longTypedQuery);
        stub(entityManager.createQuery(organizationCriteriaQuery)).toReturn(organizationTypedQuery);

        List<Organization> organizationList = organizationTypedQuery.getResultList();
        Long count = entityManager.createQuery(ArchivalOrganizationsQueryConstructorAdmin
                .buildCountQuery(name, email, number, type, region, district, locality, streetToSearch, entityManager)).getSingleResult();

        ListToPageTransformer<Organization> actual = organizationService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, name, email,
                number, type, region, district, locality, streetToSearch, sortCriteria, sortOrder);

        assertEquals(organizationList, actual.getContent());
        assertEquals(count, actual.getTotalItems());
    }

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
        final List<String> counters = new ArrayList<>();
        types.add("PROVIDER");
        Integer employeesCapacity = 7;
        Integer maxProcessTime = 12;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        String password = "generate";
        String username = "root";
        String firstName = "fName";
        String lastName = "lName";
        String middleName = "mName";
        String adminName = "aName";
        List<Long> serviceAreas = new ArrayList<>();
        serviceAreas.add(1L);
        serviceAreas.add(2L);
        User employeeAdmin = mock(User.class);

        stub(organizationRepository.findOne(organizationId)).toReturn(organization);
        stub(userRepository.findOne(username)).toReturn(employeeAdmin);
        stub(localityService.findById(anyLong())).toReturn(locality);
        stub(employeeAdmin.getPassword()).toReturn(password);

        organizationService.editOrganization(organizationId, name, phone, email, types, counters,
                employeesCapacity, maxProcessTime, address, password,
                username, firstName, lastName, middleName, adminName, serviceAreas);

        verify(organizationRepository).findOne(organizationId);
        verify(userRepository).findOne(username);
        verify(organizationRepository).save(organization);
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
        List<OrganizationEditHistory> actual = organizationService.getHistoryByOrganizationId(organizationId);

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

    /*@Test
    public void testFindLocalitiesByOrganizationId() throws Exception {
        List<LocalityDTO> expected = mock(List.class);
        stub(organizationRepository.findLocalitiesByOrganizationId(organizationId)).toReturn(expected);
        List<LocalityDTO> actual = organizationService.findLocalitiesByOrganizationId(organizationId);

        assertEquals(expected, actual);
    }*/

    @Test
    public void testFindDeviceTypesByOrganizationId() throws Exception {
        Set<Device.DeviceType> expected = mock(Set.class);
        stub(organizationRepository.findDeviceTypesByOrganizationId(organizationId)).toReturn(expected);
        Set<Device.DeviceType> actual = organizationService.findDeviceTypesByOrganizationId(organizationId);

        assertEquals(expected, actual);
    }

    /*@Test
    public void testFindByServiceAreaIdsAndOrganizationType() throws Exception {
        List<Organization> list1 = new ArrayList<>();
        list1.add(new Organization("name1", "email1", "phone1"));
        list1.add(new Organization("name2", "email2", "phone2"));
        List<Organization> list2 = new ArrayList<>();
        list1.add(new Organization("name3", "email3", "phone3"));
        list1.add(new Organization("name4", "email4", "phone4"));
        List<Organization> expected = new ArrayList<>();
        expected.addAll(list1);
        expected.addAll(list2);
        Set<Long> serviceAreaId = new HashSet<>();
        serviceAreaId.add(1L);
        serviceAreaId.add(2L);
        OrganizationType type = OrganizationType.PROVIDER;
        stub(organizationRepository.findOrganizationByLocalityIdAndType(1L, type)).toReturn(list1);
        stub(organizationRepository.findOrganizationByLocalityIdAndType(2L, type)).toReturn(list2);
        List<Organization> actual = organizationService.findByServiceAreaIdsAndOrganizationType(serviceAreaId, type);

        assertEquals(expected, actual);
    }*/

    @Test
    public void testFindByOrganizationTypeAndDeviceType() throws Exception {
        stub(organizationRepository.findByOrganizationTypeAndDeviceType(orgType, deviceType)).toReturn(organizationList);
        List<Organization> actual = organizationService.findByOrganizationTypeAndDeviceType(orgType, deviceType);

        assertEquals(organizationList, actual);
    }

    @Test
    public void testFindByIdAndTypeAndActiveAgreementDeviceType() throws Exception {
        Set<Organization> expected = mock(Set.class);
        stub(organizationRepository.findByIdAndTypeAndActiveAgreementDeviceType(organizationId, orgType, deviceType)).toReturn(expected);
        Set<Organization> actual = organizationService.findByIdAndTypeAndActiveAgreementDeviceType(organizationId, orgType, deviceType);

        assertEquals(expected, actual);
    }
}