package com.softserve.edu.service.provider;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ConvertUserRoleToString;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.provider.impl.ProviderEmployeeServiceImpl;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import com.softserve.edu.service.utils.EmployeeDTO;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.ArgumentCaptor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProviderEmployeeServiceImplTest {

	@Mock(name = "providerEmployeeRepository")
	private UserRepository mockProviderEmployeeRepository;

	@Mock
	private VerificationRepository verificationRepository;

	@Mock
	private OrganizationRepository organizationRepository;

	@Mock(name = "mail")
	private MailServiceImpl mockMail;

	@Mock(name = "em")
	private EntityManager mockEntityManager;

	@Spy
	private User finalProviderEmployee = new User("name", "generate");

	@InjectMocks
	private ProviderEmployeeServiceImpl providerEmployeeService;

//	@Before
//	public void init() {
//		MockitoAnnotations.initMocks(this); }

	@Test
	public void testAddEmployee() {

		providerEmployeeService.addEmployee(finalProviderEmployee);

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);

		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Test
	public void testUpdateEmployeeWhenEquals() {

		ArgumentCaptor<String> passwordEncodedArg = ArgumentCaptor
				.forClass(String.class);
		String email = "finalProviderEmployee@gmail.com";
		String firstName = "User";

		when(finalProviderEmployee.getEmail()).thenReturn(email);
		when(finalProviderEmployee.getFirstName()).thenReturn(firstName);

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		verify(finalProviderEmployee, atLeast(2)).getEmail();//
		verify(mockMail).sendNewPasswordMail(eq(email), eq(firstName), anyString());
		verify(finalProviderEmployee).setPassword(passwordEncodedArg.capture());

		Assert.assertEquals(finalProviderEmployee.getPassword(),
				passwordEncodedArg.getValue());

	}

	@Test
	public void testUpdateEmployeeWhenNotEquals() {
		final String name = "name";
		final String password = "generated";
		final User finalProviderEmployee =
				spy(new User(name, password));

		verify(mockMail, never()).sendNewPasswordMail(anyString(), anyString(), anyString());

		providerEmployeeService.updateEmployee(finalProviderEmployee);

		Assert.assertNull(mockProviderEmployeeRepository
				.save(finalProviderEmployee));
	}

	@Test
	public void testOneProviderEmployeeParametersPropagation() {
		final String username = "username";
		final User mockUser = mock(User.class);

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(mockUser);
		providerEmployeeService.oneProviderEmployee(username);

		ArgumentCaptor<String> usernameArg = ArgumentCaptor
				.forClass(String.class);

		verify(mockProviderEmployeeRepository).findOne(
				usernameArg.capture());

		Assert.assertEquals(username, usernameArg.getValue());
		Assert.assertEquals(mockUser,
                providerEmployeeService.oneProviderEmployee(username));
	}
/*
	@Test
	public void testGetAllProvidersWhenContains() {

		List<EmployeeDTO> spyProviderListEmployee =
				spy(new ArrayList<>());

		final User spyEmployee = spy(new User("1", "1",
				new Organization("1", "1", "1")));

		List<User> spyUserList = spy(new ArrayList<>());
		spyUserList.add(spyEmployee);

		final EmployeeDTO finalEmpDTO = new EmployeeDTO("1", "1", "1", "1", "1");

		final List<String> spyRoleList = spy(new ArrayList<>());
		spyRoleList.add(UserRole.PROVIDER_ADMIN.name());

		when(
				mockProviderEmployeeRepository.findAllAvailableUsersByRoleAndOrganizationId(
						UserRole.CALIBRATOR_ADMIN, anyLong()).stream().collect(Collectors.toList())).thenReturn(
				spyUserList);

		spyProviderListEmployee.add(finalEmpDTO);

		verify(spyProviderListEmployee).add(finalEmpDTO);

		Assert.assertNotNull(providerEmployeeService.getAllProviders(
                spyRoleList, spyEmployee));
	}

	@Test
	public void testGetAllProvidersWhenNotContains() {

		final User spyEmployee = spy(new User("1", "1"));

		final List<String> spyRoleList = spy(new ArrayList<>());
		spyRoleList.add(UserRole.CALIBRATOR_ADMIN.name());

		EmployeeDTO userPage = mock(EmployeeDTO.class);
		List<EmployeeDTO> providerListEmployee = mock(List.class);

		Assert.assertNotNull(providerEmployeeService.getAllProviders(
                spyRoleList, spyEmployee));
	}
*/
	@Test
	public void testFindByUserame() {
		/*final String username = "userName";
		final User mockUser = mock(User.class);

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(mockUser);

		Assert.assertEquals(mockUser,
                providerEmployeeService.findByUserame(username));*/

		String username = finalProviderEmployee.getUsername();

		when(mockProviderEmployeeRepository.findOne(anyString()))
				.thenReturn(finalProviderEmployee);

		User actual = providerEmployeeService.findByUserame(username);

		ArgumentCaptor<String> usernameArg = ArgumentCaptor
				.forClass(String.class);

		verify(mockProviderEmployeeRepository).findOne(
				usernameArg.capture());

		Assert.assertEquals(username, usernameArg.getValue());
		Assert.assertEquals(finalProviderEmployee, actual);
	}

	@Test
	public void testGetRoleByUserNam() {
		//final String usernam = "usernam";
		//final String mockUser = "username";
		//List<String> mockList = Collections.singletonList(mockUser);
		//Set<String> mockSet = Collections.singleton(mockUser);

		providerEmployeeService.getRoleByUserNam(anyString());

		verify(mockProviderEmployeeRepository).getRolesByUserName(anyString());

//		when(mockProviderEmployeeRepository.getRolesByUserName(anyString())).
//				thenReturn(ConvertUserRoleToString.convertToSetUserRole(mockList));
//
//		Assert.assertEquals(mockList,
//				providerEmployeeService.getRoleByUserNam(anyString()));
	}


}
