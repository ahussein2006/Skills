package com.code.integration.responses.missions;

import java.util.List;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbPropertyOrder;

import com.code.dal.entities.missions.Mission;
import com.code.integration.responses.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonbNillable
@JsonbPropertyOrder({ "responseMetadata", "missions" })
public class MissionsInquiryResponse extends BaseResponse {

    private ResponseDetails responseDetails;

    public MissionsInquiryResponse() {
	this.responseDetails = new ResponseDetails();
    }

    @Data
    public static class ResponseDetails {
	private List<Mission> missions;
    }
}
