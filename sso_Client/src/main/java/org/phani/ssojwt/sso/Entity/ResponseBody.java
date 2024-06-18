package org.phani.ssojwt.sso.Entity;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
public class ResponseBody {

    private String SSOToken;
    private Boolean userAndTokenValid;
}
