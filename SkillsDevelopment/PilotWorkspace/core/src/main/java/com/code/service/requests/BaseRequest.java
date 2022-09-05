package com.code.service.requests;

import lombok.Data;

@Data
public abstract class BaseRequest {
    protected RequestMetadata requestMetadata;
}
