package com.softserve.edu.dto.admin;

import java.util.List;

public class OrganizationDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<String> types;
    private List<String> counters;
    private Integer employeesCapacity;
    private Integer maxProcessTime;

    private String codeEDRPOU;
    private String subordination;
    private String certificateNumrAuthoriz;
    private Long certificateDate;

    private String firstName;
    private String lastName;
    private String middleName;
    private String username;
    private String password;
    private String rePassword;

    private String region;
    private String locality;
    private String district;
    private String street;
    private String building;
    private String flat;


    private String regionRegistered;
    private String localityRegistered;
    private String districtRegistered;
    private String streetRegistered;
    private String buildingRegistered;
    private String flatRegistered;

    //todo change to list
    private Long[] serviceAreas;

    public OrganizationDTO() {
    }


    public OrganizationDTO(Long id, String name, String email, String phone, List<String> types,
                           List<String> counters, Integer employeesCapacity, Integer maxProcessTime, String region,
                           String district, String locality, String street, String building, String flat) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.types = types;
        this.counters = counters;
        this.employeesCapacity = employeesCapacity;
        this.maxProcessTime = maxProcessTime;
        this.region = region;
        this.locality = locality;
        this.district = district;
        this.street = street;
        this.building = building;
        this.flat = flat;
    }

    public OrganizationDTO(Long id, String name) {
        this(id, name, null, null, null, null, null, null, null, null, null, null, null, null);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getCounters() {
        return counters;
    }

    public void setCounters(List<String> counters) {
        this.counters = counters;
    }

    public Integer getEmployeesCapacity() {
        return employeesCapacity;
    }

    public void setEmployeesCapacity(Integer employeesCapacity) {
        this.employeesCapacity = employeesCapacity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public Integer getMaxProcessTime() {
        return maxProcessTime;
    }

    public void setMaxProcessTime(Integer maxProcessTime) {
        this.maxProcessTime = maxProcessTime;
    }

    public String getRegionRegistered() { return regionRegistered; }

    public void setRegionRegistered(String regionRegistered) { this.regionRegistered = regionRegistered; }

    public String getLocalityRegistered() { return localityRegistered; }

    public void setLocalityRegistered(String localityRegistered) { this.localityRegistered = localityRegistered; }

    public String getDistrictRegistered() { return districtRegistered; }

    public void setDistrictRegistered(String districtRegistered) { this.districtRegistered = districtRegistered; }

    public String getStreetRegistered() { return streetRegistered; }

    public void setStreetRegistered(String streetRegistered) { this.streetRegistered = streetRegistered; }

    public String getBuildingRegistered() { return buildingRegistered; }

    public void setBuildingRegistered(String buildingRegistered) { this.buildingRegistered = buildingRegistered; }

    public String getFlatRegistered() { return flatRegistered; }

    public void setFlatRegistered(String flatRegistered) { this.flatRegistered = flatRegistered; }

    public String getCodeEDRPOU() { return codeEDRPOU; }

    public void setCodeEDRPOU(String codeEDRPOU) { this.codeEDRPOU = codeEDRPOU; }

    public String getSubordination() { return subordination; }

    public void setSubordination(String subordination) { this.subordination = subordination; }

    public String getCertificateNumrAuthoriz() { return certificateNumrAuthoriz; }

    public void setCertificateNumrAuthoriz(String certificateNumrAuthoriz) {
        this.certificateNumrAuthoriz = certificateNumrAuthoriz; }

    public Long getCertificateDate() { return certificateDate; }

    public void setCertificateDate(Long certificateDate) { this.certificateDate = certificateDate; }

    /**
     * Constructor with localityIdList
     *
     * @param id
     * @param name
     * @param email
     * @param phone
     * @param types
     * @param employeesCapacity
     * @param maxProcessTime
     * @param region
     * @param locality
     * @param district
     * @param street
     * @param building
     * @param flat
     * @param serviceAreas
     */
    public OrganizationDTO(Long id, String name, String email, String phone, List<String> types, List<String> counters, Integer employeesCapacity, Integer maxProcessTime, String region, String locality, String district, String street, String building, String flat, Long[] serviceAreas) {
        this(id, email, name, phone, types, counters, employeesCapacity, maxProcessTime, region, locality, district, street, building, flat);
        this.serviceAreas = serviceAreas;
    }


    public Long[] getServiceAreas() {
        return serviceAreas;
    }

    public void setServiceAreas(Long[] serviceAreas) {
        this.serviceAreas = serviceAreas;
    }
}

