package com.systemgrade.rtdr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlertStreamDTO {

    private UUID incidentId;
    private String threatType;
    private String severity;
    private String status;
    private Integer riskScore;
    private String sourceIp;
    private Map<String, Object> rawPayload;
}
