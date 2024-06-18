package org.phani.ssoserver.ssoserver.Entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ResponseBody {

    private String SSOToken;
    private Boolean userAndTokenValid;
}
