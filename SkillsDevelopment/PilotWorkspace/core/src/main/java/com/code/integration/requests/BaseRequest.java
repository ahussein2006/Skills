package com.code.integration.requests;

import lombok.Data;

@Data
public abstract class BaseRequest {
    protected RequestMetadata requestMetadata;
}
