package com.systemgrade.rtdr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyUpdateDTO {

    @NotBlank
    private String name;

    @NotNull
    private Map<String, Object> thresholds;

    @NotNull
    private Map<String, Object> autoBlockRules;

    @NotNull
    private Map<String, Object> escalationPaths;
}
