package com.yamo.cdcommoncore.domain.dto;

import lombok.Data;

@Data
public class ZeroTrustUserSignDTO {
    private String userToken;
    private String callerId;
    private long callerTimestamp;
    private String callerNounce;
    private String callerSign;
}
