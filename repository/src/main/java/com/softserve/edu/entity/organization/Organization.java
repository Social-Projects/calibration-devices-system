package com.softserve.edu.entity.organization;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.catalogue.Locality;
import com.softserve.edu.entity.enumeration.organization.OrganizationType;
import com.softserve.edu.entity.organization.OrganizationChangesHistory;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.enumeration.device.DeviceType;
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

    @OneToMany(mappedBy = "organization")
    private Set<OrganizationChangesHistory> organizationChangesHistorySet = new HashSet<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    @ElementCollection
    @JoinTable(name = "ORGANIZATION_TYPE", joinColumns = @JoinColumn(name = "organizationId"))
    @Column(name = "value", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<OrganizationType> organizationTypes = new HashSet<>();

    @ElementCollection
    @JoinTable(name = "DEVICE_TYPE", joinColumns = @JoinColumn(name = "organizationId"))
    @Column(name = "value", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<DeviceType> deviceTypes = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "ORGANIZATION_LOCALITY", joinColumns = @JoinColumn(name = "organizationId"),
            inverseJoinColumns = @JoinColumn(name = "localityId"))
    private Set<Locality> localities = new HashSet<>();

    public void addOrganizationChangeHistory(OrganizationChangesHistory organizationChangesHistory) {
        this.organizationChangesHistorySet.add(organizationChangesHistory);
    }

    public Organization(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Organization(String name, String email, String phone, Integer employeesCapacity, Integer maxProcessTime, Address address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.address = address;
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

    public void addDeviceType(DeviceType deviceType) {
        deviceTypes.add(deviceType);
    }

    public void addHistory(OrganizationChangesHistory history) {
        this.organizationChangesHistorySet.add(history);
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