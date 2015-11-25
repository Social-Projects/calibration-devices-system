package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.OrganizationRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.CalibratorEmployeeService;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilder;
import com.softserve.edu.service.provider.buildGraphic.GraphicBuilderMainPanel;
import com.softserve.edu.service.provider.buildGraphic.MonthOfYear;
import com.softserve.edu.service.provider.buildGraphic.ProviderEmployeeGraphic;
import com.softserve.edu.service.provider.impl.ProviderEmployeeServiceImpl;
import com.softserve.edu.service.utils.EmployeeDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;


/**
 * Service for adding employees for calibrator and return data for charts.
 */

@Service
public class CalibratorEmployeeServiceImpl implements CalibratorEmployeeService{

    @Autowired
    private UserRepository calibratorEmployeeRepository;

    @Autowired
    private VerificationRepository verificationRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    Logger logger = Logger.getLogger(CalibratorEmployeeServiceImpl.class);

    /**
     * Adds Employee for calibrator. Saves encoded password and
     * gives role CALIBRATOR_EMPLOYEE for user
     *
     * @param calibratorEmployee data for creation employee
     *      */

    @Override
    @Transactional
    public void addEmployee(User calibratorEmployee) {
        // TODO Extract all passwork encoding/decoding ops to single place
        String passwordEncoded = new BCryptPasswordEncoder().encode(calibratorEmployee.getPassword());
        calibratorEmployee.setPassword(passwordEncoded);
//        calibratorEmployee.setRole(CALIBRATOR_EMPLOYEE);
        calibratorEmployeeRepository.save(calibratorEmployee);
    }

    @Override
    @Transactional
    public User oneCalibratorEmployee(String username) {
        return calibratorEmployeeRepository.findOne(username);
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAllCalibrators(List<String> role, User employee) {
        List<EmployeeDTO> calibratorListEmployee = new ArrayList<>();
        if (role.contains(UserRole.CALIBRATOR_ADMIN.name())) {
            List<User> allAvailableUsersList = calibratorEmployeeRepository.findAllAvailableUsersByRoleAndOrganizationId(
                    UserRole.CALIBRATOR_EMPLOYEE, employee.getOrganization().getId())
                    .stream()
                    .collect(Collectors.toList());
            calibratorListEmployee = EmployeeDTO.giveListOfEmployeeDTOs(allAvailableUsersList);
        } else {
            EmployeeDTO userPage = new EmployeeDTO(employee.getUsername(), employee.getFirstName(),
                    employee.getLastName(), employee.getMiddleName(), role.get(0));
            calibratorListEmployee.add(userPage);
        }
        return calibratorListEmployee;
    }
    
    
    @Override
    @Transactional
    public List<ProviderEmployeeGraphic> buidGraphicMainPanel(Date from, Date to, Long idOrganization) {    	
        Organization organization = organizationRepository.findOne(idOrganization);
        List<Verification> verifications = verificationRepository.
        		findByCalibratorAndInitialDateBetween
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
}
