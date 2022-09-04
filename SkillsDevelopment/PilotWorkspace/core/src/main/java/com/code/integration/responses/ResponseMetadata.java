package com.code.integration.responses;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonbNillable
@JsonbPropertyOrder({ "consumerRequestId", "providerRequestId", "statusCode", "errorCode", "errorMessage", "firstIndex", "pageSize", "recordsCount" })
public class ResponseMetadata {
    private String consumerRequestId;
    private String providerRequestId;
    private String statusCode;
    private String errorCode;
    private String errorMessage;
    private Integer firstIndex;
    private Integer pageSize;
    private Long recordsCount;
}
