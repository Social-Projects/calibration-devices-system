package com.softserve.edu.entity.verification;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Verification entity. Contains data about whole business process of
 * verification.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "VERIFICATION")
public class Verification {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @Enumerated(EnumType.STRING)
    private Status taskStatus;

    @ManyToOne
    @JoinColumn(name = "deviceId")
    @JsonManagedReference
    private Device device;

    @OneToMany(mappedBy = "verification")
    private Set<CalibrationTest> calibrationTests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId")
    private Organization provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerEmployeeUsername")
    private User providerEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calibratorId")
    private Organization calibrator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calibratorEmployeeUsername")
    private User calibratorEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateVerificatorId")
    private Organization stateVerificator;

    @ManyToOne
    @JoinColumn(name = "stateVerificatorEmployeeUsername")
    private User stateVerificatorEmployee;

    @Embedded
    private ClientData clientData;

    @Temporal(TemporalType.DATE)
    private Date initialDate;

    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    @Temporal(TemporalType.DATE)
    private Date sentToCalibratorDate;

    private String rejectedMessage;
    private String comment;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL ,mappedBy = "verification")
    private Set<BbiProtocol> bbiProtocols;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskId")
    private CalibrationTask task;

    @Column(columnDefinition = "boolean default false")
    private boolean isAddInfoExists;

    @Column(columnDefinition = "boolean default false")
    private boolean counterStatus;

    @Column(columnDefinition = "boolean default true")
    private boolean sealPresence;

    @OneToOne(mappedBy = "verification", cascade = CascadeType.ALL)
    private AdditionalInfo info;

    private Integer processTimeExceeding;

    public Verification(
            Date initialDate, Date expirationDate, ClientData clientData, Organization provider,
            Device device, Status status, ReadStatus readStatus
    ) {
        this(initialDate, expirationDate, clientData, provider, device, status, readStatus, null);
    }

    public Verification(Date initialDate, Date expirationDate, ClientData clientData, Organization provider,
                        Device device, Status status, ReadStatus readStatus, Organization calibrator
    ) {
        this(initialDate, expirationDate, clientData, provider, device, status, readStatus, calibrator, null);
    }

    public Verification(Date initialDate, Date expirationDate, ClientData clientData, Organization provider, Device device, Status status,
                        ReadStatus readStatus, Organization calibrator, String comment) {
        this.id = UUID.randomUUID().toString();
        this.initialDate = initialDate;
        this.expirationDate = expirationDate;
        this.clientData = clientData;
        this.provider = provider;
        this.device = device;
        this.status = status;
        this.readStatus = readStatus;
        this.calibrator = calibrator;
        this.comment = comment;
    }

    public void deleteCalibrationTest (CalibrationTest calibrationTest){
        calibrationTests.remove(calibrationTest);

    }

    public enum ReadStatus {
       READ,
       UNREAD
    }

    public enum CalibrationTestResult {
        SUCCESS,
        FAILED,
        RAW
    }

    public enum ConsumptionStatus {
         IN_THE_AREA,
         NOT_IN_THE_AREA
    }
}
