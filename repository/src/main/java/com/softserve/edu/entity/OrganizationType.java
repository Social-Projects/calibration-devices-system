package com.softserve.edu.entity;

import javax.persistence.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ORGANIZATION_TYPE")
public class OrganizationType {
	@Id
	@GeneratedValue
	private Integer id;
	private String type;


	@ManyToMany(mappedBy = "organizationTypes")
	private Set<Organization> organizations = new HashSet<Organization>();

	public void addOrganization(Organization organization) {
		this.organizations.add(organization);
	}

	public OrganizationType() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}