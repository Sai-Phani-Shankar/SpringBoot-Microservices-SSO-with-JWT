package org.phani.ssoserver.ssoserver.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {

    @jakarta.persistence.Id
    public long UserId;
    public String Email;
    public String FirstName;
    public String LastName;
    public String password;

    @Transient
    public Boolean fetchToken;
}
