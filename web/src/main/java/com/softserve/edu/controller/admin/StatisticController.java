package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.admin.CountDTO;
import com.softserve.edu.dto.admin.UsersPageItem;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.admin.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/statistics/")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @RequestMapping(value = "organizations", method = RequestMethod.GET)
    public CountDTO countOrganizations() {
        return new CountDTO(statisticService.countOrganizations());
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public CountDTO countUsers() {
        return new CountDTO(statisticService.countUsers());
    }

    @RequestMapping(value = "device-category", method = RequestMethod.GET)
    public CountDTO countDevices() {
        return new CountDTO(statisticService.countDevices());
    }

    @RequestMapping(value = "counters-type", method = RequestMethod.GET)
    public CountDTO countCounterTypes() {
        return new CountDTO(statisticService.countCounterTypes());
    }

    @RequestMapping(value = "verifications", method = RequestMethod.GET)
    public CountDTO countVerifications() {
        return new CountDTO(statisticService.countVerifications());
    }

    @RequestMapping(value = "employee", method = RequestMethod.GET)
    public UsersPageItem getEmployee(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        UsersPageItem usersPageItem = new UsersPageItem();
        User user1 =  statisticService.employeeExist(user.getUsername());
        usersPageItem.setFirstName(user1.getFirstName());
        usersPageItem.setLastName(user1.getLastName());
        usersPageItem.setUsername(user1.getUsername());
        usersPageItem.setMiddleName(user1.getMiddleName());
        return  usersPageItem;
    }

    @RequestMapping(value = "sys_admins", method = RequestMethod.GET)
    public CountDTO countSysAdmins() {
        return new CountDTO(statisticService.countSysAdmins());
    }
}
