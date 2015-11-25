package com.softserve.edu.service.provider.impl;

import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.util.ConvertSetEnumsToListString;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.service.provider.ProviderEmployeeService;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilderMainPanel;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.utils.EmployeeDTO;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilder;
import com.softserve.edu.service.provider.buildGraphic.MonthOfYear;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.utils.ListToPageTransformer;
import com.softserve.edu.service.utils.ProviderEmployeeQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProviderEmployeeServiceImpl implements ProviderEmployeeService {

    @Autowired
    private UserRepository providerEmployeeRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MailService mail;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    Logger logger = Logger.getLogger(ProviderEmployeeServiceImpl.class);

    @Override
    @Transactional
    public void addEmployee(User providerEmployee) {
        String passwordEncoded = new BCryptPasswordEncoder().encode(providerEmployee.getPassword());
        providerEmployee.setPassword(passwordEncoded);
        providerEmployeeRepository.save(providerEmployee);
    }

    @Override
    @Transactional
    public void updateEmployee(User providerEmployee) {
        if (providerEmployee.getPassword().equals("generate")) {
            String newPassword = RandomStringUtils.randomAlphanumeric(5);
            mail.sendNewPasswordMail(providerEmployee.getEmail(), providerEmployee.getFirstName(), newPassword);
            String passwordEncoded = new BCryptPasswordEncoder().encode(newPassword);
            providerEmployee.setPassword(passwordEncoded);
        }
        providerEmployeeRepository.save(providerEmployee);
    }

    @Override
    @Transactional
    public User oneProviderEmployee(String username) {
        return providerEmployeeRepository.findOne(username);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllProviders(List<String> role, User employee) {
        List<EmployeeDTO> providerListEmployee = new ArrayList<>();
        if (role.contains(UserRole.PROVIDER_ADMIN.name())) {
            List<User> allAvailableUsersList = providerEmployeeRepository.findAllAvailableUsersByRoleAndOrganizationId(
                    UserRole.PROVIDER_EMPLOYEE, employee.getOrganization().getId())
                        .stream()
                        .collect(Collectors.toList());
            providerListEmployee = EmployeeDTO.giveListOfEmployeeDTOs(allAvailableUsersList);
        } else {
            EmployeeDTO userPage = new EmployeeDTO(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(), role.get(0));
            providerListEmployee.add(userPage);
        }
        return providerListEmployee;
    }

    @Override
    @Transactional()
    public User findByUsername(String userName) {
        return providerEmployeeRepository.findOne(userName);
    }

    @Override
    @Transactional
    public List<String> getRoleByUserNam(String username) {
        return ConvertSetEnumsToListString.convertToListString(providerEmployeeRepository.getRolesByUserName(username));
    }

    @Override
    @Transactional
    public ListToPageTransformer<User>
    findPageOfAllProviderEmployeeAndCriteriaSearch(int pageNumber, int itemsPerPage, Long idOrganization, String userName,
                                                   String role, String firstName, String lastName, String organization,
                                                   String telephone, String secondTelephone, String fieldToSort) {
        CriteriaQuery<User> criteriaQuery = ProviderEmployeeQuery.buildSearchQuery(userName, role, firstName,
                lastName, organization, telephone, secondTelephone, em, idOrganization, fieldToSort);

        Long count = em.createQuery(ProviderEmployeeQuery.buildCountQuery(userName, role, firstName,
                lastName, organization, telephone, secondTelephone, idOrganization, em)).getSingleResult();

        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        List<User> providerEmployeeList = typedQuery.getResultList();

        ListToPageTransformer<User> result = new ListToPageTransformer<>();
        result.setContent(providerEmployeeList);
        result.setTotalItems(count);

        //TODO: corect showing on the pages information about current employees

        return result;
    }

    @Override
    @Transactional
    public List<ProviderEmployeeGraphic> buildGraphic(Date from, Date to, Long idOrganization, List<User> listOfEmployee) {
        Organization organization = organizationRepository.findOne(idOrganization);
        List<Verification> verifications = verificationRepository.
                findByProviderEmployeeIsNotNullAndProviderAndSentToCalibratorDateBetween(organization, from, to);
        List<ProviderEmployeeGraphic> graficData = null;
        try {
            List<MonthOfYear> monthList = GraphicBuilder.listOfMonths(from, to);
            graficData = GraphicBuilder.builderData(verifications, monthList, listOfEmployee);

        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return graficData;
    }

    @Override
    @Transactional
    public List<ProviderEmployeeGraphic> buidGraphicMainPanel(Date from, Date to, Long idOrganization) {
        Organization organization = organizationRepository.findOne(idOrganization);
        List<Verification> verifications = verificationRepository.
                findByProviderAndInitialDateBetween
                        (organization, from, to);
        List<ProviderEmployeeGraphic> graficData = null;
        try {
            List<MonthOfYear> monthList = GraphicBuilder.listOfMonths(from, to);
            graficData = GraphicBuilderMainPanel.builderData(verifications, monthList, organization);

        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return graficData;
    }

    @Override
    public Date convertToDate(String date) throws IllegalArgumentException {
        Date result = null;
        if (StringUtils.isNotBlank(date)) {
            try {
                result = DATE_FORMAT.parse(date);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("input date is not correct");
        }
        return result;
    }

    @Override
    @Transactional
    public List<User> getAllProviderEmployee(Long idOrganization) {
        return userRepository.findByUserRoleAndOrganizationId(UserRole.PROVIDER_EMPLOYEE, idOrganization)
                .stream()
                .collect(Collectors.toList());
    }
}
