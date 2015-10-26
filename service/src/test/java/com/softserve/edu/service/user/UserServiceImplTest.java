package com.softserve.edu.service.user;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.user.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Pavlo on 18.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    private User user;
    @Mock
    private User mockUser;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    private String username = "U";
    private String password = "pass";
    private String role = "SYS_ADMIN";


    @Before
    public void setUp() throws Exception {
        user = new User(username, password);
        when(userRepository.findOne(username)).thenReturn(user);
        when(bCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);
    }

    @Test
    public void testExistsWithUsername() {

        assertFalse(userService.existsWithUsername(username));
        assertTrue(userService.existsWithUsername(anyString()));

    }

    @Test
    public void testChangeField() {
        assertFalse("When username is null", userService.changeField(null, "", ""));
        assertFalse("When new value is null", userService.changeField("", null, ""));
        assertFalse("When field is null", userService.changeField("", "", null));
        assertFalse("When username is not right", userService.changeField("", "", ""));
        assertTrue("When changing firstName", userService.changeField(username, "", "firstName"));
        assertTrue("When changing lastName", userService.changeField(username, "", "lastName"));
        assertTrue("When changing middleName", userService.changeField(username, "", "middleName"));
        assertTrue("When changing email", userService.changeField(username, "", "email"));
        assertTrue("When changin phone", userService.changeField(username, "", "phone"));
    }

    @Test
    public void testChangePassword() {

        String username = "Petro";
        String hash_of_password = "$2a$10$59Mv7tEUrVH8iBeDsm9y7.zUcJoPHnnyOvMnC4zKRV8.wlnugQ2G2";
        String old_pasword = "pass";
        String new_pasword = "ddd";

        when(mockUser.getPassword()).thenReturn(hash_of_password);
        when(mockUser.getUsername()).thenReturn(username);
        when(userRepository.findOne(username)).thenReturn(mockUser);

        boolean isChanged = userService.changePassword(mockUser.getUsername(), old_pasword, new_pasword);
        assertTrue(isChanged);
        verify(mockUser, times(1)).setPassword(anyString());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void testChangePasswordWithIncorrectParameters() {
        assertFalse("When username is null", userService.changePassword(null, "", ""));
        assertFalse("When old password is null", userService.changePassword("", null, ""));
        assertFalse("When new password is null", userService.changePassword("", "", null));
        assertFalse("When username is incorect", userService.changePassword("", "", ""));
        assertFalse("When old password is incorrect", userService.changePassword(username, "", ""));
    }

    @Test
    public void testFindByRole() {
        List<User> usersList;
        Set<User> userSet;
        usersList = new ArrayList<>();
        usersList.add(user);
        userSet = new HashSet<>();
        userSet.add(user);
        when(userRepository.findByUserRoleAllIgnoreCase(UserRole.SYS_ADMIN)).thenReturn(userSet);
        List<User> expected = usersList;
        List<User> actual = userService.findByRole(role);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUser() {
        User expected = userRepository.findOne("username");
        User actual = userService.getUser("username");
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUser() {
        userService.updateUser(user);
        verify(userRepository).save(user);

    }

    @Test
    public void testFindOne() {
        User actual = userService.findOne(username);
        User expected = user;
        assertEquals("the users returned from the method are not equal", expected, actual);
    }

    @Test
    public void getRoles() {
        Set<UserRole> userRoles;
        userRoles = new LinkedHashSet<>();
        userRoles.add(UserRole.CALIBRATOR_ADMIN);
        when(userRepository.getRolesByUserName(username)).thenReturn(userRoles);
        List<String> actual = userService.getRoles(username);
        List<String> expected = new ArrayList<>();
        expected.add(UserRole.CALIBRATOR_ADMIN.toString());
        assertEquals(expected, actual);

    }
}