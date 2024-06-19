package org.phani.ssoserver.ssoserver.Repository;

import org.phani.ssoserver.ssoserver.Entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long>{

    @Query("select u from UserDetail u where u.Email = ?1")
    UserDetail findByEmail(String Email) throws UsernameNotFoundException;
}
