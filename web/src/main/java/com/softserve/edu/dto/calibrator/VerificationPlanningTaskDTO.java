package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
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
    private String time;
    private int entrance;
    private int floor;
    private String notes;
    private Date dateOfVerif;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private boolean serviceability;
    private Date noWaterToDate;
    private boolean sealPresence;

    public VerificationPlanningTaskDTO(){}

    public VerificationPlanningTaskDTO(Date sentDate, String providerName, String district, String street,
                                       String building, String flat, String clientFullName, String telephone,
                                       AdditionalInfo additionalInfo) {
        this.sentToCalibrator = sentDate;
        this.providerName = providerName;
        this.district = district;
        this.street = street;
        this.building = building;
        this.flat = flat;
        this.clientFullName = clientFullName;
        this.telephone = telephone;
        if (additionalInfo != null) {
            this.entrance = additionalInfo.getEntrance();
            this.floor = additionalInfo.getFloor();
            if ((additionalInfo.getTimeFrom() != null) && (additionalInfo.getTimeTo() != null)) {
                this.time = additionalInfo.getTimeFrom().toString() + " - " + additionalInfo.getTimeTo().toString();
            } else {
                this.time = null;
            }
            this.notes = additionalInfo.getNotes();
        }
    }

    public VerificationPlanningTaskDTO(Date sentDate, String verificationID, String providerName, String fullName,
                                       String district, String street, String building, String flat, String telephone,
                                       String secondphone, Date dateOfVerif, LocalTime timeFrom, LocalTime timeTo,
                                       boolean serviceability, Date noWaterToDate, boolean sealPresence){
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
        this.dateOfVerif = dateOfVerif;
        this.noWaterToDate = noWaterToDate;
        this.serviceability = serviceability;
        this.sealPresence = sealPresence;

        if ((timeFrom != null) || (timeTo != null)) {
            this.time = timeFrom.toString() + " - " + timeTo.toString();
        } else {
            this.time = null;
        }

        if ((flat != null) && !flat.isEmpty()) {
            this.building_flat = building + ", " + flat;
        } else {
            this.building_flat = building;
        }

        if ((secondphone != null) && !secondphone.isEmpty()) {
            this.phone = telephone+", "+secondphone;
        } else {
            this.phone = telephone;
        }

    }

}
