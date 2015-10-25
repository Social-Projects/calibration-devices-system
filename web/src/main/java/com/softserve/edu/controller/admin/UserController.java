package com.softserve.edu.controller.admin;

import com.softserve.edu.controller.admin.util.UserDTOTransformer;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.UserFilterSearch;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/admin/users/")
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = Logger.getLogger(UserController.class);

    /**
     * Check whereas {@code username} is available,
     * i.e. it is possible to create new user with this {@code username}
     *
     * @param username username
     * @return {@literal true} if {@code username} available or else {@literal false}
     */
    @RequestMapping(value = "available/{username}", method = RequestMethod.GET)
    public Boolean isValidUsername(@PathVariable String username) {
        boolean isAvailable = false;
        if (username != null) {
            isAvailable = userService.existsWithUsername(username);
        }
        return isAvailable;
    }

    /**
     * The method return all employee which are registered in the system
     *
     * @param pageNumber
     * @param itemsPerPage
     * @param sortCriteria
     * @param sortOrder
     * @param search
     * @param user
     * @return
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<UsersPageItem> getPaginationUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @PathVariable String sortCriteria,
            @PathVariable String sortOrder,
            UserFilterSearch search,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        ListToPageTransformer<User> queryResult = userService.findPageOfAllEmployees(
                pageNumber, itemsPerPage, search.getUsername(), search.getRole(), search.getFirstName(), search.getLastName(), search.getOrganization(),
                search.getPhone(), sortCriteria, sortOrder);
        List<UsersPageItem> resultList = UserDTOTransformer.toDTOFromListEmployee(queryResult, userService);
        return new PageDTO<>(queryResult.getTotalItems(), resultList);
    }

    /**
     * Current method transform list of users to List DTO
     *
     * @param queryResult
     * @return full information witch connect with employees
     */


}
