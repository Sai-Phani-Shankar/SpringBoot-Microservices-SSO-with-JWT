package org.phani.ssojwt.sso.Repository;

import org.phani.ssojwt.sso.Entity.DeviceData;
import org.phani.ssojwt.sso.Entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceData, Long> {

}
