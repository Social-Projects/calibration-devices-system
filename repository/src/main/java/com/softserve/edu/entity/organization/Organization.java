package com.softserve.edu.entity.organization;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "ORGANIZATION")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private Integer employeesCapacity;
    private Integer maxProcessTime;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "regionRegistered")),
            @AttributeOverride(name = "district", column = @Column(name = "districtRegistered")),
            @AttributeOverride(name = "locality", column = @Column(name = "localityRegistered")),
            @AttributeOverride(name = "street", column = @Column(name = "streetRegistered")),
            @AttributeOverride(name = "building", column = @Column(name = "buildingRegistered")),
            @AttributeOverride(name = "flat", column = @Column(name = "flatRegistered"))
    })
    private Address addressRegistered;

    @Embedded
    private AdditionInfoOrganization additionInfoOrganization;

    /**
     * Identification number of the certificate that allows this UserCalibrator
     * to perform verifications.
     */
    private String certificateNumber;

    /**
     * Identification number of the certificate that allows this calibrator to
     * perform verifications.
     */
    private Date certificateGrantedDate;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<OrganizationEditHistory> organizationEditHistorySet = new HashSet<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    /*@OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<CalibrationModule> modules = new HashSet<>();*/

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<DisassemblyTeam> disassemblyTeams = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Agreement> agreements = new HashSet<>();

    @ElementCollection
    @JoinTable(name = "ORGANIZATION_TYPE", joinColumns = @JoinColumn(name = "organizationId"))
    @Column(name = "value", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<OrganizationType> organizationTypes = new HashSet<>();

    @ElementCollection
    @JoinTable(name = "DEVICE_TYPE", joinColumns = @JoinColumn(name = "organizationId"))
    @Column(name = "value", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<Device.DeviceType> deviceTypes = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "ORGANIZATION_LOCALITY", joinColumns = @JoinColumn(name = "organizationId"),
            inverseJoinColumns = @JoinColumn(name = "localityId"))
    private Set<Locality> localities = new HashSet<>();

    public void addOrganizationChangeHistory(OrganizationEditHistory organizationEditHistory) {
        this.organizationEditHistorySet.add(organizationEditHistory);
    }

    public Organization(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Organization(String name, String email, String phone, Integer employeesCapacity, Integer maxProcessTime, Address address) {
        this(name, email, phone);
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.address = address;
    }

    public Organization(String name, String email, String phone, Integer employeesCapacity, Integer maxProcessTime,
                        Address address, Address addressRegistered, AdditionInfoOrganization additionInfoOrganization) {
        this(name, email, phone);
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.address = address;
        this.addressRegistered = addressRegistered;
        this.additionInfoOrganization = additionInfoOrganization;
    }

    public void addUser(User user) {
        user.setOrganization(this);
        users.add(user);
    }

    public void addOrganizationType(OrganizationType organizationType) {
        organizationTypes.add(organizationType);
    }

    public void removeOrganizationTypes() {
        organizationTypes.clear();
    }

    public void removeServiceAreas() {
        localities.clear();
    }

    public void addDeviceType(Device.DeviceType deviceType) {
        deviceTypes.add(deviceType);
    }

    public void removeDeviceType() {
        deviceTypes.clear();
    }

    public void addHistory(OrganizationEditHistory history) {
        this.organizationEditHistorySet.add(history);
    }

    public void addLocality(Locality locality) {
        localities.add(locality);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' + '}';
    }
}
