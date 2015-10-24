package com.softserve.edu.entity.verification;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "fileName")
@Table(name = "BBI_PROTOCOL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbiProtocol {

    @Id
    @Setter(AccessLevel.PRIVATE)
    private String fileName;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificationId")
    private Verification verification;
    
    public BbiProtocol(String fileName, String filePath, Verification verification) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.verification = verification;
    }
}