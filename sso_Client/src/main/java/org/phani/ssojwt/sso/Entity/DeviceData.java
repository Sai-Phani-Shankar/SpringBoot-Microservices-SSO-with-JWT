package org.phani.ssojwt.sso.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private long DeviceId;
    @Column(name = "os")
    private String os;
    @Column(name = "browser")
    private String browser;
    @Column(name = "device_type")
    private String deviceType;

    @OneToOne(cascade = CascadeType.ALL,
    optional = false
    ,mappedBy = "deviceData")
    private UserDetail userDetail;
}
