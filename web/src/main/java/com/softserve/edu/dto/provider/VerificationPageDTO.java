package com.softserve.edu.dto.provider;


import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.service.utils.ArchivalVerificationsQueryConstructorProvider;
import org.apache.log4j.Logger;

import java.util.Date;

public class VerificationPageDTO {
    static Logger logger = Logger.getLogger(ArchivalVerificationsQueryConstructorProvider.class);
    private String id;
    private Date initialDate;
    private String surname; //TODO: surname and name not needed anymore
    private String name;
    private String fullName;
    private String district;
    private String locality;
    private String phone;
    private String street;
    private String region;
    private Status status;
    private String providerEmployee;
    private String calibratorEmployee;
    private String stateVerificatorEmployee;
    private Long countOfWork;
    private Verification.ReadStatus readStatus;
    private boolean isUpload;
    private Integer processTimeExceeding;


    private Long protocolId;
    private String protocolDate;
    private String protocolStatus;

    private String measurementDeviceId;
    private String measurementDeviceType;

    private DocumentType documentType;
    private String documentTypeName;
    private String documentDate;

    private  Long  calibrationTestId;
    private String address;
    private String nameProvider;
    private String nameCalibrator;

    public VerificationPageDTO() {
    }

    public VerificationPageDTO(String id, Date initialDate, String surname, String street, String region,
                               Status status, Verification.ReadStatus readStatus, User providerEmployee, User calibratorEmployee, User stateVerificatorEmployee,
                               String name, String fullName, String district, String locality, String phone, boolean isUpload, Integer processTimeExceeding,
                               CalibrationTest calibrationTest,
                               Device device,
                               String documentType, String documentDate) {

        this.id = id;
        this.initialDate = initialDate;
        this.surname = surname;
        this.name = name;
        this.street = street;
        this.region = region;
        this.status = status;
        this.readStatus = readStatus;
        if (providerEmployee != null) {
            if (providerEmployee.getMiddleName() != null) {
                this.providerEmployee = providerEmployee.getLastName() + " " + providerEmployee.getFirstName() + " " + providerEmployee.getMiddleName();
            } else {
                this.providerEmployee = providerEmployee.getLastName() + " " + providerEmployee.getFirstName();
            }
        }
        if (calibratorEmployee != null) {
            if (calibratorEmployee.getMiddleName() != null) {
                this.calibratorEmployee = calibratorEmployee.getLastName() + " " + calibratorEmployee.getFirstName() + " " + calibratorEmployee.getMiddleName();
            } else {
                this.calibratorEmployee = calibratorEmployee.getLastName() + " " + calibratorEmployee.getFirstName();
            }
        }
        if (stateVerificatorEmployee != null) {
            if (stateVerificatorEmployee.getMiddleName() != null) {
                this.stateVerificatorEmployee = stateVerificatorEmployee.getLastName() + " " + stateVerificatorEmployee.getFirstName() + " " + stateVerificatorEmployee.getMiddleName();
            } else {
                this.stateVerificatorEmployee = stateVerificatorEmployee.getLastName() + " " + stateVerificatorEmployee.getFirstName();
            }
        }
        this.fullName = fullName;
        this.district = district;
        this.locality = locality;
        this.phone = phone;
        this.isUpload = isUpload;
        this.processTimeExceeding = processTimeExceeding;
        if (calibrationTest != null) {
            if (calibrationTest.getId() != null) {
                this.protocolId = calibrationTest.getId();
            }
            if (calibrationTest.getDateTest() != null) {
                this.protocolDate = calibrationTest.getDateTest().toString();
            }
            if (calibrationTest.getTestResult() != null) {
                this.protocolStatus = calibrationTest.getTestResult().toString();
                logger.debug("protocolStatus = " + protocolStatus);
                if (protocolStatus == Verification.CalibrationTestResult.SUCCESS.toString()) {
                    logger.debug("documentType = " +  this.documentType);
                    this.documentType = DocumentType.VERIFICATION_CERTIFICATE;
                    this.documentTypeName = "СПП";
                } else {
                    this.documentType = DocumentType.UNFITNESS_CERTIFICATE;
                    this.documentTypeName = "Довідка про непридатність";
                }
                logger.debug("documentType = " +  this.documentType);
            }
        }
        if (device != null) {
            if (device.getId() != null) {
                this.measurementDeviceId = device.getId().toString();
            }
            if (device.getDeviceType() != null) {
                this.measurementDeviceType = device.getDeviceType().toString();
            }
        }
        if (documentDate != null) {
            this.documentDate = documentDate;
        }
    }

    public VerificationPageDTO(String id, Date initialDate, String surname, String street, String region,
                               Status status, Verification.ReadStatus readStatus, User providerEmployee, User calibratorEmployee, User stateVerificatorEmployee,
                               String name, String fullName, String district, String locality, String phone, boolean isUpload, Integer processTimeExceeding,
                               CalibrationTest calibrationTest, Long calibrationTestId,
                               Device device,
                               String documentType, String documentDate) {

        this.id = id;
        this.initialDate = initialDate;
        this.surname = surname;
        this.name = name;
        this.street = street;
        this.region = region;
        this.status = status;
        this.readStatus = readStatus;
        if (providerEmployee != null) {
            if (providerEmployee.getMiddleName() != null) {
                this.providerEmployee = providerEmployee.getLastName() + " " + providerEmployee.getFirstName() + " " + providerEmployee.getMiddleName();
            } else {
                this.providerEmployee = providerEmployee.getLastName() + " " + providerEmployee.getFirstName();
            }
        }
        if (calibratorEmployee != null) {
            if (calibratorEmployee.getMiddleName() != null) {
                this.calibratorEmployee = calibratorEmployee.getLastName() + " " + calibratorEmployee.getFirstName() + " " + calibratorEmployee.getMiddleName();
            } else {
                this.calibratorEmployee = calibratorEmployee.getLastName() + " " + calibratorEmployee.getFirstName();
            }
        }
        if (stateVerificatorEmployee != null) {
            if (stateVerificatorEmployee.getMiddleName() != null) {
                this.stateVerificatorEmployee = stateVerificatorEmployee.getLastName() + " " + stateVerificatorEmployee.getFirstName() + " " + stateVerificatorEmployee.getMiddleName();
            } else {
                this.stateVerificatorEmployee = stateVerificatorEmployee.getLastName() + " " + stateVerificatorEmployee.getFirstName();
            }
        }
        this.fullName = fullName;
        this.district = district;
        this.locality = locality;
        this.phone = phone;
        this.isUpload = isUpload;
        this.processTimeExceeding = processTimeExceeding;
        if (calibrationTest != null) {
            if (calibrationTest.getId() != null) {
                this.protocolId = calibrationTest.getId();
            }
            if (calibrationTest.getDateTest() != null) {
                this.protocolDate = calibrationTest.getDateTest().toString();
            }
            if (calibrationTest.getTestResult() != null) {
                this.protocolStatus = calibrationTest.getTestResult().toString();
                logger.debug("protocolStatus = " + protocolStatus);
                if (protocolStatus == Verification.CalibrationTestResult.SUCCESS.toString()) {
                    logger.debug("documentType = " +  this.documentType);
                    this.documentType = DocumentType.VERIFICATION_CERTIFICATE;
                    this.documentTypeName = "СПП";
                } else {
                    this.documentType = DocumentType.UNFITNESS_CERTIFICATE;
                    this.documentTypeName = "Довідка про непридатність";
                }
                logger.debug("documentType = " +  this.documentType);
            }
        }
        if (device != null) {
            if (device.getId() != null) {
                this.measurementDeviceId = device.getId().toString();
            }
            if (device.getDeviceType() != null) {
                this.measurementDeviceType = device.getDeviceType().toString();
            }
        }
        if (documentDate != null) {
            this.documentDate = documentDate;
        }
        this.calibrationTestId = calibrationTestId;
    }


    public VerificationPageDTO(Long count) {
        this.countOfWork = count;
    }


    public VerificationPageDTO(String id, Date initialDate, String surname, String street, String region,
                               Status status, Verification.ReadStatus readStatus, User providerEmployee, User calibratorEmployee, User stateVerificatorEmployee,
                               String name, String fullName, String district, String locality, String phone, boolean isUpload, Integer processTimeExceeding,
                               CalibrationTest calibrationTest,
                               Device device,
                               String documentType, String documentDate,String address) {

        this.id = id;
        this.initialDate = initialDate;
        this.surname = surname;
        this.name = name;
        this.street = street;
        this.region = region;
        this.status = status;
        this.readStatus = readStatus;
        this.address=address;
        if (providerEmployee != null) {
            if (providerEmployee.getMiddleName() != null) {
                this.providerEmployee = providerEmployee.getLastName() + " " + providerEmployee.getFirstName() + " " + providerEmployee.getMiddleName();
            } else {
                this.providerEmployee = providerEmployee.getLastName() + " " + providerEmployee.getFirstName();
            }
        }
        if (calibratorEmployee != null) {
            if (calibratorEmployee.getMiddleName() != null) {
                this.calibratorEmployee = calibratorEmployee.getLastName() + " " + calibratorEmployee.getFirstName() + " " + calibratorEmployee.getMiddleName();
            } else {
                this.calibratorEmployee = calibratorEmployee.getLastName() + " " + calibratorEmployee.getFirstName();
            }
        }
        if (stateVerificatorEmployee != null) {
            if (stateVerificatorEmployee.getMiddleName() != null) {
                this.stateVerificatorEmployee = stateVerificatorEmployee.getLastName() + " " + stateVerificatorEmployee.getFirstName() + " " + stateVerificatorEmployee.getMiddleName();
            } else {
                this.stateVerificatorEmployee = stateVerificatorEmployee.getLastName() + " " + stateVerificatorEmployee.getFirstName();
            }
        }
        this.fullName = fullName;
        this.district = district;
        this.locality = locality;
        this.phone = phone;
        this.isUpload = isUpload;
        this.processTimeExceeding = processTimeExceeding;
        if (calibrationTest != null) {
            if (calibrationTest.getId() != null) {
                this.protocolId = calibrationTest.getId();
            }
            if (calibrationTest.getDateTest() != null) {
                this.protocolDate = calibrationTest.getDateTest().toString();
            }
            if (calibrationTest.getTestResult() != null) {
                this.protocolStatus = calibrationTest.getTestResult().toString();
                logger.debug("protocolStatus = " + protocolStatus);
                if (protocolStatus == Verification.CalibrationTestResult.SUCCESS.toString()) {
                    logger.debug("documentType = " +  this.documentType);
                    this.documentType = DocumentType.VERIFICATION_CERTIFICATE;
                    this.documentTypeName = "СПП";
                } else {
                    this.documentType = DocumentType.UNFITNESS_CERTIFICATE;
                    this.documentTypeName = "Довідка про непридатність";
                }
                logger.debug("documentType = " +  this.documentType);
            }
        }
        if (device != null) {
            if (device.getId() != null) {
                this.measurementDeviceId = device.getId().toString();
            }
            if (device.getDeviceType() != null) {
                this.measurementDeviceType = device.getDeviceType().toString();
            }
        }
        if (documentDate != null) {
            this.documentDate = documentDate;
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String getProviderEmployee() {
        return providerEmployee;
    }

    public void setProviderEmployee(String providerEmployee) {
        this.providerEmployee = providerEmployee;
    }

    public Long getCountOfWork() {
        return countOfWork;
    }

    public void setCountOfWork(Long countOfWork) {
        this.countOfWork = countOfWork;
    }

    public Verification.ReadStatus getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Verification.ReadStatus readStatus) {
        this.readStatus = readStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCalibratorEmployee() {
        return calibratorEmployee;
    }

    public void setCalibratorEmployee(String calibratorEmployee) {
        this.calibratorEmployee = calibratorEmployee;
    }

    public String getStateVerificatorEmployee() {
        return stateVerificatorEmployee;
    }

    public void setStateVerificatorEmployee(String stateVerificatorEmployee) {
        this.stateVerificatorEmployee = stateVerificatorEmployee;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public Integer getProcessTimeExceeding() {
        return processTimeExceeding;
    }

    public void setProcessTimeExceeding(Integer processTimeExceeding) {
        this.processTimeExceeding = processTimeExceeding;
    }

    @Override
    public String toString() {
        return "VerificationPageDTO{" +
                "id='" + id + '\'' +
                ", initialDate=" + initialDate +
                ", surname='" + surname + '\'' +
                ", street='" + street + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", locality='" + locality + '\'' +
                ", status=" + status +
                '}';
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public String getProtocolDate() {
        return protocolDate;
    }

    public void setProtocolDate(String protocolDate) {
        this.protocolDate = protocolDate;
    }

    public String getProtocolStatus() {
        return protocolStatus;
    }

    public void setProtocolStatus(String protocolStatus) {
        this.protocolStatus = protocolStatus;
    }

    public String getMeasurementDeviceId() {
        return measurementDeviceId;
    }

    public void setMeasurementDeviceId(String measurementDeviceId) {
        this.measurementDeviceId = measurementDeviceId;
    }

    public String getMeasurementDeviceType() {
        return measurementDeviceType;
    }

    public void setMeasurementDeviceType(String measurementDeviceType) {
        this.measurementDeviceType = measurementDeviceType;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public Long getCalibrationTestId() {
        return calibrationTestId;
    }

    public void setCalibrationTestId(Long calibrationTestId) {
        this.calibrationTestId = calibrationTestId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNameProvider() {
        return nameProvider;
    }

    public void setNameProvider(String nameProvider) {
        this.nameProvider = nameProvider;
    }

    public String getNameCalibrator() {
        return nameCalibrator;
    }

    public void setNameCalibrator(String nameCalibrator) {
        this.nameCalibrator = nameCalibrator;
    }


}


