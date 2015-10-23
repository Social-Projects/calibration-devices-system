package com.softserve.edu.service.utils;

import junit.framework.TestCase;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

/**
 * Created by lyubomyr on 21.10.2015.
 */
public class OrganizationAdminDTOTest extends TestCase {
    private String username;
    private String firstName;
    private String lastName;
    private String middleName;

    @InjectMocks
    private OrganizationAdminDTO organizationAdminDTO;

    public void setUp() throws Exception {
        super.setUp();

        organizationAdminDTO = new OrganizationAdminDTO();
        MockitoAnnotations.initMocks(this);
        username="username";
        firstName="firstName";
        lastName="lastName";
        middleName="middleName";
    }

    public void tearDown() throws Exception {
        organizationAdminDTO = null;
    }

    public void testConstructor1() throws Exception {
        OrganizationAdminDTO c1 = new OrganizationAdminDTO();
        assertTrue(c1 != null);
    }

    public void testConstructor2() throws Exception {
        OrganizationAdminDTO c2 = new OrganizationAdminDTO(firstName, lastName, middleName);
        assertTrue(c2 != null);
    }

    public void testConstructor3() throws Exception {
        OrganizationAdminDTO c3 = new OrganizationAdminDTO(firstName, lastName, middleName, username);
        assertTrue(c3 != null);
    }

    public void testGetUsername() throws Exception {
        organizationAdminDTO.setUsername(username);
        assertEquals(username, organizationAdminDTO.getUsername());
    }

    public void testSetUsername() throws Exception {
        organizationAdminDTO.setUsername(username);
        assertEquals(username, organizationAdminDTO.getUsername());
    }

    public void testGetFirstName() throws Exception {
        organizationAdminDTO.setFirstName(firstName);
        assertEquals(firstName, organizationAdminDTO.getFirstName());
    }

    public void testSetFirstName() throws Exception {
        organizationAdminDTO.setFirstName(firstName);
        assertEquals(firstName, organizationAdminDTO.getFirstName());
    }

    public void testGetLastName() throws Exception {
        organizationAdminDTO.setLastName(lastName);
        assertEquals(lastName, organizationAdminDTO.getLastName());
    }

    public void testSetLastName() throws Exception {
        organizationAdminDTO.setLastName(lastName);
        assertEquals(lastName, organizationAdminDTO.getLastName());
    }

    public void testGetMiddleName() throws Exception {
        organizationAdminDTO.setMiddleName(middleName);
        assertEquals(middleName, organizationAdminDTO.getMiddleName());
    }

    public void testSetMiddleName() throws Exception {
        organizationAdminDTO.setMiddleName(middleName);
        assertEquals(middleName, organizationAdminDTO.getMiddleName());
    }
}