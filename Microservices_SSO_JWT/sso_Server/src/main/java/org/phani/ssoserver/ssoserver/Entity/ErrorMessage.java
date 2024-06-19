package org.phani.ssoserver.ssoserver.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {

    private String ErrorMessage;
    private String ErrorCode;
}
