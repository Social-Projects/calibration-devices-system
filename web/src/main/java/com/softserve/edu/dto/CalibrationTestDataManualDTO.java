package com.softserve.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Misha on 12/14/2015.
 */
@Getter
@Setter
public class CalibrationTestDataManualDTO {

    private String numberCounter;
    private String statusTestFirst;
    private String statusTestSecond;
    private String statusTestThird;
    private String statusCommon;
    private String verificationId;
    private Long counterId;
    private CalibrationTestManualDTO calibrationTestManualDTO;

    public CalibrationTestDataManualDTO() {
    }

    public CalibrationTestDataManualDTO(String statusTestFirst, String statusTestSecond, String statusTestThird, String statusCommon, CalibrationTestManualDTO calibrationTestManualDTO) {
        this.statusTestFirst = statusTestFirst;
        this.statusTestSecond = statusTestSecond;
        this.statusTestThird = statusTestThird;
        this.statusCommon = statusCommon;
        this.calibrationTestManualDTO = calibrationTestManualDTO;
    }

    public CalibrationTestDataManualDTO(String numberCounter, String statusTestFirst, String statusTestSecond, String statusTestThird, String statusCommon,Long counterId, CalibrationTestManualDTO calibrationTestManualDTO) {
        this.numberCounter = numberCounter;
        this.statusTestFirst = statusTestFirst;
        this.statusTestSecond = statusTestSecond;
        this.statusTestThird = statusTestThird;
        this.statusCommon = statusCommon;
        this.counterId = counterId;
        this.calibrationTestManualDTO = calibrationTestManualDTO;
    }

}
