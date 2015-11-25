package com.softserve.edu.dto.admin;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roman on 07.11.15.
 *
 */

@Setter
@Getter
public class CalibrationModuleDTO {

    private Long moduleId;
    private Device.DeviceType deviceType;
    private String organizationCode;
    private String condDesignation;
    private String serialNumber;
    private String employeeFullName;
    private String telephone;
    private CalibrationModule.ModuleType moduleType;
    private String email;
    private String calibrationType;
    private String moduleNumber;
    private Boolean isActive;
    private Date workDate;
    private Long startDateToSearch = null;
    private Long endDateToSearch = null;

    public CalibrationModuleDTO() {
    }

    public CalibrationModuleDTO(Long moduleId, Device.DeviceType deviceType, String organizationCode,
                                String condDesignation, String serialNumber,
                                String employeeFullName, String telephone, String moduleNumber,
                                Boolean isActive, CalibrationModule.ModuleType moduleType, String email,
                                String calibrationType, Date workDate) {
        this.moduleId = moduleId;
        this.deviceType = deviceType;
        this.organizationCode = organizationCode;
        this.condDesignation = condDesignation;
        this.serialNumber = serialNumber;
        this.employeeFullName = employeeFullName;
        this.telephone = telephone;
        this.moduleNumber = moduleNumber;
        this.isActive = isActive;
        this.moduleType = moduleType;
        this.email = email;
        this.calibrationType = calibrationType;
        this.workDate = workDate;
    }

    public CalibrationModuleDTO(Long moduleId, Device.DeviceType deviceType, String organizationCode,
                                String condDesignation, String serialNumber,
                                String employeeFullName, String telephone, String moduleNumber,
                                Boolean isActive, CalibrationModule.ModuleType moduleType, String email,
                                String calibrationType, Date workDate, Long startDate, Long endDate) {
        this(moduleId, deviceType, organizationCode, condDesignation, serialNumber, employeeFullName,
                telephone, moduleNumber, isActive, moduleType, email, calibrationType, workDate);
        this.startDateToSearch = startDate;
        this.endDateToSearch = endDate;
    }

}
