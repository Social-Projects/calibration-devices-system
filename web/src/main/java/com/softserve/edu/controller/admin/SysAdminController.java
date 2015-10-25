package com.softserve.edu.controller.admin;


import com.softserve.edu.controller.admin.util.UserDTOTransformer;
import com.softserve.edu.controller.provider.util.UserDTO;
import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.SysAdminDTO;
import com.softserve.edu.dto.admin.UserFilterSearch;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.admin.UserService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/sysadmins/")
public class SysAdminController {

    @Autowired
    private UserService userService;

    Logger logger = Logger.getLogger(SysAdminController.class);


    @RequestMapping(value = "/{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<SysAdminDTO> getPaginationUsers(
            @PathVariable Integer pageNumber,
            @PathVariable Integer itemsPerPage,
            @PathVariable String sortCriteria,
            @PathVariable String sortOrder,
            UserFilterSearch search,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {

        ListToPageTransformer<User> queryResult = userService.findAllSysAdmins();
        List<SysAdminDTO> resultList = UserDTOTransformer.toDTOFromListSysAdmin(queryResult);
        return new PageDTO<>(queryResult.getTotalItems(), resultList);
    }


    /**
     * Add new employee
     *
     * @param sysAdmin
     * @param user
     * @return status
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> addSysAdmin(
            @RequestBody UserDTO sysAdmin,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            userService.addSysAdmin(sysAdmin.getUsername(), sysAdmin.getPassword(), sysAdmin.getFirstName(), sysAdmin.getLastName(), sysAdmin.getMiddleName(), sysAdmin.getPhone(),
                    sysAdmin.getEmail(), sysAdmin.getAddress());
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }


        return new ResponseEntity<>(httpStatus);
    }

    /**
     * Add new employee
     *
     */
    @RequestMapping(value = "get_sys_admin/{username}", method = RequestMethod.GET)
    public SysAdminDTO findSysAdminByUsername(@PathVariable("username") String username) {
        User sysAdmin = userService.findOne(username);
        SysAdminDTO SysAdminDTO = new SysAdminDTO(
                sysAdmin.getUsername(),
                sysAdmin.getFirstName(),
                sysAdmin.getLastName(),
                sysAdmin.getMiddleName(),
                sysAdmin.getEmail(),
                sysAdmin.getPhone(),
                sysAdmin.getAddress().getRegion(),
                sysAdmin.getAddress().getDistrict(),
                sysAdmin.getAddress().getLocality(),
                sysAdmin.getAddress().getStreet(),
                sysAdmin.getAddress().getBuilding(),
                sysAdmin.getAddress().getFlat()
        );
        return SysAdminDTO;
    }

    /**
     *
     * Delete sys admin with current username
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "delete/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteSysAdmin(
            @PathVariable String username) {
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            userService.deleteSysAdmin(username);
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     *
     * Edit sys admin with current username
     *
     * @return
     */
    @RequestMapping(value = "edit/{username}", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> editSysAdmin(
            @RequestBody UserDTO sysAdmin,
            @PathVariable String username,
            @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            userService.editSysAdmin(username, sysAdmin.getPassword(), sysAdmin.getFirstName(), sysAdmin.getLastName(), sysAdmin.getMiddleName(), sysAdmin.getPhone(),
                    sysAdmin.getEmail(), sysAdmin.getAddress());
        } catch (Exception e) {
            // TODO
            logger.error("GOT EXCEPTION ", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(httpStatus);
    }
}
