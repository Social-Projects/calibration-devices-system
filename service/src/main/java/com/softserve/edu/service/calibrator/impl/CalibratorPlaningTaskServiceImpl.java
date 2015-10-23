package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.verification.ReadStatus;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationPlanningTask;
import com.softserve.edu.repository.CalibrationPlanningTaskRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.repository.VerificationPlanningTaskRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CalibratorPlaningTaskServiceImpl implements CalibratorPlanningTaskService {

    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private VerificationPlanningTaskRepository planningTaskRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @Override
    @Transactional
    public void addNewTask(String verifiedId, String placeOfCalibration, String counterStatus, String counterNumber,
                           Date dateOfVisit, Date dateOfVisitTo, String installationNumber, String notes, int floor) {
        Verification verification = verificationRepository.findOne(verifiedId);
        if (verification == null) {
            logger.error("verification haven't found");
        } else {
            if ((placeOfCalibration == null) || (counterStatus == null)
                    || (installationNumber == null)) {
                throw new IllegalArgumentException();
            }
            CalibrationPlanningTask task = new CalibrationPlanningTask();
            task.setVerification(verification);
            task.setPlaceOfCalibration(placeOfCalibration);
            task.setRemoveStatus(counterStatus);
            task.setSerialNumberOfMeasuringInstallation(installationNumber);
            task.setSerialNumberOfCounter(counterNumber);
            task.setNotes(notes);
            if (placeOfCalibration == "fixed_station" && counterStatus == "removed") { //TODO: WHY??!!!
                task.setDateOfVisit(null);
                task.setDateOfVisitTo(null);
            } else {
                if ((placeOfCalibration == "fixed_station" && counterStatus == "not_removed") || //TODO: WHY??!!!
                        (placeOfCalibration == "fixed_station")) {
                    if (dateOfVisit == null && dateOfVisitTo == null) {
                        throw new IllegalArgumentException();
                    } else {
                        task.setDateOfVisit(dateOfVisit);
                        task.setDateOfVisitTo(dateOfVisitTo);
                    }
                }
            }
            task.setFloor(floor);
            taskRepository.save(task);
        }
    }

    @Override
    public Page<Verification> findVerificationsByCalibratorIdAndReadStatus (String userName, int pageNumber, int itemsPerPage) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
        }
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage);
        return planningTaskRepository.findByCalibratorIdAndReadStatus(user.getOrganization().getId(), ReadStatus.READ, pageRequest);
    }
}
