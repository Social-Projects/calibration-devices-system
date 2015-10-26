package com.softserve.edu.service.user;

import com.softserve.edu.config.ServiceTestingConfig;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.user.User;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Volodya NT on 08.10.2015.
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceTestingConfig.class})
public class UserServiceTest {
    @Autowired
    UserService userService;

    /**
     * find whole users with
     * @throws Exception
     */

    @Test
    public void testFindByRole() throws Exception {
        List<User> userList =  userService.findByRole(UserRole.SYS_ADMIN.name());
        userList.forEach((user) -> System.out.println("User " + user));
        final String username = "admin";
        Assert.assertEquals(userList.iterator().next().getUsername(), username);
    }

    /**
     * Get roles by user
     * @throws Exception
     */
    @Test
    public void testGetRoles() throws Exception {
        final String userRole = UserRole.PROVIDER_EMPLOYEE.name();
        Assert.assertEquals(userService.getRoles("newman").iterator().next(),
                userRole);
    }


}