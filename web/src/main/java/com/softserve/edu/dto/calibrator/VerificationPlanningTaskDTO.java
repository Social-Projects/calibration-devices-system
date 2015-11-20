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

    private String district;

    private String street;

    private String building;

    private String flat;

    private String telephone;

    private String secondphone;

    private String building_flat;

    private String phone;

    public VerificationPlanningTaskDTO(){}

    public VerificationPlanningTaskDTO(Date sentDate, String verificationID, String providerName, String fullName,
                                       String district, String street, String building, String flat, String telephone,
                                       String secondphone){
        this.sentToCalibrator = sentDate;
        this.verficationId = verificationID;
        this.providerName = providerName;
        this.clientFullName = fullName;
        this.district = district;
        this.street = street;
        this.building = building;
        this.flat = flat;
        this.telephone = telephone;
        this.secondphone = secondphone;
        if ((flat != null) && !flat.isEmpty())
        {
            this.building_flat = building+"  /  "+flat;
        }
        else
        {
            this.building_flat = building;
        }
        if ((secondphone != null) && !secondphone.isEmpty())
        {
            this.phone = telephone+", "+secondphone;
        }
        else
        {
            this.phone = telephone;
        }

    }

}
