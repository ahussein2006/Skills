package com.code.integration.requests;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbPropertyOrder;

import lombok.Data;

@Data
@JsonbNillable
@JsonbPropertyOrder({ "consumerCode", "consumerRequestId", "providerRequestId", "username", "secyirtyToken", "firstIndex", "pageSize", "preferedLang", "additionalInfo" })
public class RequestMetadata {
    private String consumerCode;
    private String consumerRequestId;
    private String providerRequestId;
    private String username;
    private String secyirtyToken;
    private Integer firstIndex;
    private Integer pageSize;
    private String preferedLang;
    private String additionalInfo;
}
