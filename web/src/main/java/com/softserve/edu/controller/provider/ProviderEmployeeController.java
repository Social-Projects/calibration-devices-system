package com.softserve.edu.controller.provider;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.verification.VerificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "provider/admin/users/")
public class ProviderEmployeeController {

    Logger logger = Logger.getLogger(ProviderEmployeeController.class);

    @Autowired
    private ProviderEmployeeService providerEmployeeService;

    @Autowired
    private OrganizationService organizationsService;

    @Autowired
    private VerificationService verificationService;


    @RequestMapping(value = "graphicCapacity", method = RequestMethod.GET)
    public List<ProviderEmployeeGraphic> graphic
            (@RequestParam String fromDate, @RequestParam String toDate,
             @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
            List<ProviderEmployeeGraphic> list = null;
        try {
            Date dateFrom = providerEmployeeService.convertToDate(fromDate);
            Date dateTo = providerEmployeeService.convertToDate(toDate);
            List<User> providerEmployee= providerEmployeeService.getAllProviderEmployee(idOrganization);
            list = providerEmployeeService.buildGraphic(dateFrom, dateTo, idOrganization, providerEmployee);
        } catch (Exception e) {
            logger.error("Failed to get graphic data");
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
        }
        return list;
    }

    @RequestMapping(value = "graphicmainpanel", method = RequestMethod.GET)
    public List<ProviderEmployeeGraphic> graphicMainPanel
            (@RequestParam String fromDate, @RequestParam String toDate,
             @AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        List<ProviderEmployeeGraphic> list = null;
        try {
            Date dateFrom = providerEmployeeService.convertToDate(fromDate);
            Date dateTo = providerEmployeeService.convertToDate(toDate);

            list = providerEmployeeService.buidGraphicMainPanel(dateFrom, dateTo, idOrganization);

        } catch (Exception e) {
            logger.error("Failed to get graphic data");
            logger.error(e); // for prevent critical issue "Either log or rethrow this exception"
        }
        return list;
    }


    @RequestMapping(value = "piemainpanel", method = RequestMethod.GET)
    public Map pieMainPanel(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        Long idOrganization = user.getOrganizationId();
        Organization organization = organizationsService.getOrganizationById(idOrganization);
        Map tmp = new HashMap<>();
        tmp.put("SENT", verificationService.findCountOfAllSentVerifications(organization));
        tmp.put("ACCEPTED", verificationService.findCountOfAllAcceptedVerification(organization));
        return tmp;
    }






}
