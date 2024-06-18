package org.phani.ssojwt.sso.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public long Id;
    @Column(name = "email")
    public String Email;
    @Column(name = "first_name")
    public String FirstName;
    @Column(name = "last_name")
    public String LastName;
    @Column(name = "password")
    public String password;

    @OneToOne(cascade = CascadeType.ALL,
            optional = false)
    @JoinColumn(name = "device_id",
            referencedColumnName = "device_id")
    private DeviceData deviceData;

    @Transient
    public String token;

    @Transient
    public String os;

    @Transient
    public String browser;

    @Transient
    public String deviceType;
}
