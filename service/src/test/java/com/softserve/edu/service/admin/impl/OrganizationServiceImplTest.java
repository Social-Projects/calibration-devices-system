package com.softserve.edu.service.admin.impl;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrganizationServiceImplTest {
    @InjectMocks
    private OrganizationServiceImpl organizationsService;

    @Mock
    private Organization mockOrganization = mock(Organization.class);

    @Mock
    private User mockUser = mock(User.class);

    @Mock
    MailServiceImpl mockMail = mock(MailServiceImpl.class);

    @Mock
    private OrganizationRepository mockOrganizationRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(organizationsService.findOrganizationTypesById(1L)).thenReturn(new HashSet<>(1));
    }

    @Test
    public void testAddOrganizationWithAdmin() throws Exception {
      final String name = "Eric";
        final String email = "dot@gmail.com";
        final String phone = "0934453246";
        List<String> types = new ArrayList<>();
        types.add("PROVIDER");
        types.add("CALIBRATOR");
        final Integer employeesCapacity = 12;
        final Integer maxProcessTime = 123;
        final String username = "eric";
        final String password = "root";
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final String firstName = "firstName";
        final String lastName = "lastName";
        final String middleName = "middleName";
        final String adminName = "admin";
        final Long[] localityIdList = {1L};

//        organizationsService.addOrganizationWithAdmin(name, email, phone, types, employeesCapacity,
//                maxProcessTime, firstName, lastName, middleName,
//                username, password, address, adminName, localityIdList);

//        ArgumentCaptor<Organization> organizationArg = ArgumentCaptor.forClass(Organization.class);
//        ArgumentCaptor<User> employeeAdminArg = ArgumentCaptor.forClass(User.class);
//
//        verify(mockOrganizationRepository).save(organizationArg.capture());
//        verify(mockUserRepository).save(employeeAdminArg.capture());
//
//        assertEquals(userRole, mockUserRepository.getUserRole(anyString()));
//        assertEquals(typeTwo, mockOrganizationRepository.getOrganizationType(anyString()));
//        assertEquals(spyOrg.getName(), organizationArg.getValue().getName());
//        assertEquals(spyUser.getUsername(), employeeAdminArg.getValue().getUsername());
    }


/*    @Test
    public void testGetOrganizationsBySearchAndPagination() throws Exception {
        int pageNumber = 5;
        int itemsPerPage = 10;
        String name = "name";
        String email = "email";
        String number = "1234567890";
        String type = "type";
        String region = "Lv";
        String district = "St";
        String locality = "locality";
        String streetToSearch = "Shev";
        String sortCriteria = "sortCriteria";
        String sortOrder = "asc";
        ListToPageTransformer<Organization> page = mock(ListToPageTransformer.class);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);

        when(mockOrganizationRepository.findAll(pageRequest)).thenReturn(pageOrganizations);
        when(mockOrganizationRepository.findByNameLikeIgnoreCase("%" + search
                + "%", pageRequest)).thenReturn(pageOrganizations);
        ArgumentCaptor<PageRequest> pageRequestArg = ArgumentCaptor.forClass(PageRequest.class);


        organizationsService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, name, email, number, type, region, district,
                locality, streetToSearch, sortCriteria, sortOrder);

        verify(mockOrganizationRepository).findAll(pageRequestArg.capture());
        assertEquals(pageRequest.first(), pageRequestArg.getValue().first());
        assertEquals(pageOrganizations, organizationsService
                .getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, null));

        ArgumentCaptor<PageRequest> pageRequestArg2 = ArgumentCaptor.forClass(PageRequest.class);

        organizationsService.getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, search);

        verify(mockOrganizationRepository).findByNameLikeIgnoreCase(anyString(), pageRequestArg2.capture());
        assertEquals(pageRequest.first(), pageRequestArg.getValue().first());
        assertEquals(pageRequest.first(), pageRequestArg2.getValue().first());
        assertEquals(pageOrganizations, organizationsService
                .getOrganizationsBySearchAndPagination(pageNumber, itemsPerPage, search));

    }*/

    @Test
    public void testGetOrganizationById() throws Exception {
        final Long id = 100000L;

        when(mockOrganizationRepository.findOne(id)).thenReturn(mockOrganization);
//        ArgumentCaptor<Long> idArg = ArgumentCaptor.forClass(Long.class);

        organizationsService.getOrganizationById(id);

        assertEquals(mockOrganization, organizationsService.getOrganizationById(id));

    }

/*    @Test
    public void testEditOrganization() throws Exception {
        final Long organizationId = 1L;
        final String name = "name";
        final String phone = "phone";
        final String email = "email";
        final String[] types = {"CALIBRATOR", "PROVIDER"};
        final Integer employeesCapacity = 123;
        final Integer maxProcessTime = 456;
        final Address address = new Address("Lviv", "Leva", "123", "123", "123", "123");
        final Organization organization = mock(Organization.class);

        when(mockOrganizationRepository.findOne(organizationId)).thenReturn(organization);

        ArgumentCaptor<String> nameArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> phoneArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> employeesCapacityArg = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> maxProcessTimeArg = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Address> addressArg = ArgumentCaptor.forClass(Address.class);

        organizationsService.editOrganization(organizationId, name, phone, email, types,
                employeesCapacity, maxProcessTime, address);

        verify(organization).setName(nameArg.capture());
        verify(organization).setPhone(phoneArg.capture());
        verify(organization).setEmail(emailArg.capture());

        verify(organization).setEmployeesCapacity(employeesCapacityArg.capture());
        verify(organization).setMaxProcessTime(maxProcessTimeArg.capture());
        verify(organization).setAddress(addressArg.capture());

        assertEquals(name, nameArg.getValue());
        assertEquals(phone, phoneArg.getValue());
        assertEquals(email, emailArg.getValue());
        assertEquals(employeesCapacity, employeesCapacityArg.getValue());
        assertEquals(maxProcessTime, maxProcessTimeArg.getValue());
        assertEquals(address.getBuilding(), addressArg.getValue().getBuilding());
    }*/

    /*@Test
    public void testGetOrganizationEmployeesCapacity() throws Exception {
        final Long organizationId = 1L;
        Integer someInt = 123;
        when(organizationsService.getOrganizationEmployeesCapacity(organizationId)).thenReturn(null);

        ArgumentCaptor<Long> organizationIdArg = ArgumentCaptor.forClass(Long.class);

        organizationsService.getOrganizationEmployeesCapacity(organizationId);

        verify(organizationsService).getOrganizationEmployeesCapacity(organizationIdArg.capture());

        assertEquals(organizationId, organizationIdArg.getValue());
        assertEquals(someInt, organizationsService.getOrganizationEmployeesCapacity(organizationId));
    }*/

    @Test
    public void testSendOrganizationChanges() throws Exception {
        mockMail.sendOrganizationChanges(mockOrganization, mockUser);

    }

    @Test(expected = NullPointerException.class)
    public void testGetHistoryByOrganizationId() throws Exception {
        organizationsService.getHistoryByOrganizationId(null);
    }

    @Test
    public void testFindAllByLocalityId() throws Exception {
        Long id = 222L;

        List<Organization> list = organizationsService.findAllByLocalityId(id);
        assertNotNull(list);
    }

    @Test
    public void testFindAllByLocalityIdAndTypeId() throws Exception {
        Long id = 222L;
        OrganizationType type = OrganizationType.NO_TYPE;

        List<Organization> list = organizationsService.findAllByLocalityIdAndTypeId(id, type);
        assertNotNull(list);
    }

    @Test
    public void testFindOrganizationTypesById() throws Exception {
        Long id = 222L;

        Set<OrganizationType> set = organizationsService.findOrganizationTypesById(id);
        assertNotNull(set);
    }

    @Test
    public void testFindByLocalityIdAndTypeAndDevice() throws Exception {
        Long localityId = 1L;
        OrganizationType orgType = OrganizationType.CALIBRATOR;
        DeviceType deviceType = DeviceType.WATER;

        List<Organization> list = organizationsService.findByLocalityIdAndTypeAndDevice(localityId, orgType, deviceType);
        assertNotNull(list);
    }
}
