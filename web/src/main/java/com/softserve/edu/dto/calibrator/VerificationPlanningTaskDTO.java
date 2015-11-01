package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.device.Device;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VerificationPlanningTaskDTO {

    private Date sentToCalibrator;

    private String verficationId;

    private String providerName;

    private String clientFullName;

    private String address;

    private String counterStatus;

    private Device.DeviceType deviceType;

    private String telephone;

    public VerificationPlanningTaskDTO(){}

    public VerificationPlanningTaskDTO(Date sentDate, String verificationID, String providerName, String fullName,
                                       String address, String counterStatus, Device.DeviceType deviceType,String telephone){
        this.sentToCalibrator = sentDate;
        this.verficationId = verificationID;
        this.providerName = providerName;
        this.clientFullName = fullName;
        this.address = address;
        this.counterStatus = counterStatus;
        this.deviceType = deviceType;
        this.telephone = telephone;
    }

}
