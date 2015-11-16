package com.softserve.edu.dto.admin;

import com.softserve.edu.entity.device.Device;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by roman on 07.11.15.
 *
 */

@Setter
@Getter
public class CalibrationModuleDTO {

    private Long moduleId;
    private String deviceType;
    private String organizationCode;
    private String condDesignation;
    private String serialNumber;
    private String employeeFullName;
    private String telephone;
    private String moduleType;
    private String email;
    private String calibrationType;
    private String moduleNumber;
    private Boolean isActive;
    private Date workDate;

    public CalibrationModuleDTO() {}

    public CalibrationModuleDTO(Long moduleId, String deviceType, String organizationCode,
                             String condDesignation, String serialNumber,
                             String employeeFullName, String telephone, String moduleNumber,
                             String moduleType, String email, String calibrationType, Date workDate) {
        this.moduleId = moduleId;
        this.deviceType = deviceType;
        this.organizationCode = organizationCode;
        this.condDesignation = condDesignation;
        this.serialNumber = serialNumber;
        this.employeeFullName = employeeFullName;
        this.telephone = telephone;
        this.moduleNumber = moduleNumber;
        this.moduleType = moduleType;
        this.email = email;
        this.calibrationType = calibrationType;
        this.workDate = workDate;
    }

    public CalibrationModuleDTO(Long moduleId, Device.DeviceType deviceType, String organizationCode,
                                String condDesignation, String serialNumber,
                                String employeeFullName, String telephone, String moduleNumber,
                                String moduleType, String email, String calibrationType,
                                Date workDate) {
        this(moduleId, deviceType.name(), organizationCode, condDesignation, serialNumber, employeeFullName,
                telephone, moduleNumber, moduleType, email, calibrationType, workDate);
    }

}
