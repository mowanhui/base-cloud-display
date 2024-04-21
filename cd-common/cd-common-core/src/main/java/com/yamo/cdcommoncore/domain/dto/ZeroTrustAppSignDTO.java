package com.yamo.cdcommoncore.domain.dto;

import lombok.Data;

@Data
public class ZeroTrustAppSignDTO {
    private String appToken;
    private String callerId;
    private long callerTimestamp;
    private String callerNounce;
    private String callerSign;
}
