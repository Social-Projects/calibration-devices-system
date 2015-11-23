package com.softserve.edu.entity.organization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionInfoOrganization {

    private String edrpou;
    private String subordination;
    private String certificateNumrAuthoriz;
    private Date certificateDate;

    public AdditionInfoOrganization(String edrpou, String subordination, String certificateNumrAuthoriz, Date certificateDate) {
        this.edrpou = edrpou;
        this.subordination = subordination;
        this.certificateNumrAuthoriz = certificateNumrAuthoriz;
        this.certificateDate = certificateDate;
    }

}
