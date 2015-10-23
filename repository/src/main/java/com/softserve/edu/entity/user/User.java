package com.softserve.edu.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.util.AddEmployeeBuilderNew;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.organization.Organization;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "username")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
public class User {

    @Id
    private String username;

    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String secondPhone;
    private Boolean isAvailable = false;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "organizationId")
    @JsonManagedReference
    private Organization organization;

    @ElementCollection
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "username"))
    @Enumerated(EnumType.STRING)
    @Column(name = "value", length = 30)
    private Set<UserRole> userRoles = new HashSet<>();

    public User(AddEmployeeBuilderNew builder) {
        username = builder.username;
        password = builder.password;
        firstName = builder.firstName;
        lastName = builder.lastName;
        middleName = builder.middleName;
        email = builder.email;
        phone = builder.phone;
        secondPhone = builder.secondPhone;
        address = builder.address;
        isAvailable = builder.isAvailable;
    }

    /**
     * Required constructor for saving employee in database. Employee cannot
     * exists without these parameters.
     *
     * @param username username
     * @param password password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Required constructor for saving employee in database. Employee cannot
     * exists without these parameters.
     *
     * @param username     username
     * @param password     password
     * @param organization its organization
     */
    public User(String username, String password, Organization organization) {
        this.username = username;
        this.password = password;
        this.organization = organization;
    }

    /**
     * Completes constructor above with optional values.
     *
     * @param firstName  first name
     * @param lastName   last name
     * @param middleName Middle name
     */
    public User(String firstName, String lastName, String middleName,
                String username, String password, Organization organization) {
        this(username, password, organization);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    /**
     * Completes constructor above with optional values.
     *
     * @param firstName first name
     * @param lastName  last name
     * @param email     email
     * @param phone     phone number
     */
    public User(String username, String password, Organization organization, String firstName, String lastName,
                String email, String phone) {
        this(username, password, organization);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public void addRole(UserRole role) {
        this.userRoles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' + '}';
    }

    /* public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    } */
}
