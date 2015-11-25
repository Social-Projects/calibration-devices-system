package com.softserve.edu.controller.provider.util;

import com.softserve.edu.controller.calibrator.util.DistrictAndStreetComparator;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.Verification;

import java.time.LocalTime;
import java.util.*;

public class VerificationPageDTOTransformer {

    public static List<VerificationPageDTO> toDtoFromList(List<Verification> list){

        List<VerificationPageDTO> resultList = new ArrayList<>();

        CalibrationTest calibrationTest;
        for (Verification verification : list) {
            boolean isCalibrationTests = verification.getCalibrationTests().iterator().hasNext();
            if (isCalibrationTests) {
                calibrationTest = verification.getCalibrationTests().iterator().next();
            } else {
                calibrationTest = null;
            }
            VerificationPageDTO verificationPageDTO = new VerificationPageDTO(
                            verification.getId(),
                            verification.getInitialDate(),
                            verification.getClientData().getLastName(),
                            verification.getClientData().getClientAddress().getStreet(),
                            verification.getClientData().getClientAddress().getRegion(),
                            verification.getStatus(),
                            verification.getReadStatus(),
                            verification.getProviderEmployee(),
                            verification.getCalibratorEmployee(),
                            verification.getStateVerificatorEmployee(),
                            verification.getClientData().getFirstName(),
                            verification.getClientData().getFullName(),
                            verification.getClientData().getClientAddress().getDistrict(),
                            verification.getClientData().getClientAddress().getLocality(),
                            verification.getClientData().getPhone(),
                            verification.getBbiProtocols()==null?false:true,
                            verification.getProcessTimeExceeding(),
                            calibrationTest,
                            verification.getDevice(),
                            null,
                            null,
                            verification.getClientData().getClientAddress().getAddress());
            if(verification.getProvider()!=null){verificationPageDTO.setNameProvider(verification.getProvider().getName());}
            if(verification.getCalibrator()!=null){verificationPageDTO.setNameCalibrator(verification.getCalibrator().getName());}
            resultList.add(verificationPageDTO);
        }
        return resultList;
    }

    public static List<VerificationPlanningTaskDTO> toDoFromPageContent(List<Verification> verifications){
        List<VerificationPlanningTaskDTO> taskDTOs = new ArrayList<VerificationPlanningTaskDTO>();
        Date dateOfVerif;
        LocalTime timeFrom;
        LocalTime timeTo;
        boolean serviceability;
        Date noWaterToDate;
        for (Verification verification : verifications) {
            String counterStatus = (verification.isCounterStatus())? "Так": "Ні";
            if (verification.isAddInfoExists())
            {
                dateOfVerif = verification.getInfo().getDateOfVerif();
                timeFrom = verification.getInfo().getTimeFrom();
                timeTo = verification.getInfo().getTimeTo();
                serviceability = verification.getInfo().isServiceability();
                noWaterToDate = verification.getInfo().getNoWaterToDate();
            }
            else
            {
                dateOfVerif = null;
                timeFrom = null;
                timeTo = null;
                serviceability = true;
                noWaterToDate = null;
            }
            taskDTOs.add(new VerificationPlanningTaskDTO(verification.getSentToCalibratorDate(),
                    verification.getId(),
                    verification.getProvider().getName(),
                    verification.getClientData().getFullName(),
                    verification.getClientData().getClientAddress().getDistrict(),
                    verification.getClientData().getClientAddress().getStreet(),
                    verification.getClientData().getClientAddress().getBuilding(),
                    verification.getClientData().getClientAddress().getFlat(),
                    verification.getClientData().getPhone(),
                    verification.getClientData().getSecondPhone(),
                    dateOfVerif, timeFrom, timeTo, serviceability, noWaterToDate,
                    verification.isSealPresence()
            ));
        }
        return taskDTOs;
    }

}
