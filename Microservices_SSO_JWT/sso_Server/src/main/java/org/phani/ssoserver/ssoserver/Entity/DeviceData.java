package org.phani.ssoserver.ssoserver.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceData {

    @Id
    private long DeviceId;
    private String os;
    private String browser;
    private String deviceType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId",
    referencedColumnName = "UserId")
    private UserDetail userDetail;
}
