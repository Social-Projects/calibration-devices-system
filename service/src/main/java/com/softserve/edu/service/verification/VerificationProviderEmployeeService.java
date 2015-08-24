package com.softserve.edu.service.verification;

import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.ReadStatus;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Maksym.Hirnyak on 12.07.2015.
 */
@Service
public class VerificationProviderEmployeeService {

    Logger logger = Logger.getLogger(VerificationProviderEmployeeService.class);

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Current method assign provider employee in verification
     * @param verificationId
     * @param providerEmployee
     */
    @Transactional
    public void assignProviderEmployee(String verificationId, User providerEmployee) {
        Verification verification = verificationRepository.findOne(verificationId);
        if (verification == null) {
            logger.error("verification haven't found");
            return;
        }
        verification.setProviderEmployee(providerEmployee);
        if (providerEmployee==null) {
            verification.setStatus(Status.SENT);
        } else {
            verification.setStatus(Status.ACCEPTED);
        }
        verification.setReadStatus(ReadStatus.READ);
        verificationRepository.save(verification);
    }



    @Transactional
    public User getProviderEmployeeById(String idVerification) {
        return verificationRepository.getProviderEmployeeById(idVerification);
    }

    /**
     * Current method return list of verification by current Provider user
     * @param username
     * @return list of Verification
     */
    @Transactional
    public List<Verification> getVerificationListbyProviderEmployee(String username) {
        return verificationRepository.findByProviderEmployeeUsernameAndStatus(username, Status.ACCEPTED);
    }

    /**
     * Current method return list of verification by current Calibrator user
     * @param username
     * @return list of Verification
     */
    @Transactional
    public List<Verification> getVerificationListbyCalibratormployee(String username) {
        return verificationRepository.findByCalibratorEmployeeUsernameAndStatus(username, Status.IN_PROGRESS);
    }

    /**
     * This method count of work in current time
     * @param username
     * @return number of work for user
     */
    @Transactional
    public Long countByProviderEmployeeTasks(String username) {
        return verificationRepository.countByProviderEmployee_usernameAndStatus(username, Status.ACCEPTED);
    }

    /**
     * This method count of work in current time
     * @param username
     * @return number of work for user
     */
    @Transactional
    public Long countByCalibratorEmployeeTasks(String username) {
        return verificationRepository.countByCalibratorEmployee_usernameAndStatus(username, Status.IN_PROGRESS);
    }

    /**
     * This method search user by username
     * @param username
     * @return user
     */
    @Transactional
    public User oneProviderEmployee(String username) {
        return userRepository.getUserByUserName(username);
    }
}
