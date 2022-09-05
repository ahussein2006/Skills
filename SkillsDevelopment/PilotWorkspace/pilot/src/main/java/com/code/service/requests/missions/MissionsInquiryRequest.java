package com.code.service.requests.missions;

import java.util.Date;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.annotation.JsonbTransient;

import com.code.enums.ChronologyTypesEnum;
import com.code.service.requests.BaseRequest;
import com.code.service.requests.RequestMetadata;
import com.code.util.MultiChronologyCalendarUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonbNillable
@JsonbPropertyOrder({ "requestMetadata", "requestDetails" })
public class MissionsInquiryRequest extends BaseRequest {

    private RequestDetails requestDetails;

    public MissionsInquiryRequest(RequestMetadata requestMetadata, Integer locationFlag, String decreeNumber, Date fromHijriDate, Date toHijriDate, Long employeeId) {
	this.requestMetadata = requestMetadata;
	this.requestDetails = new RequestDetails(locationFlag, decreeNumber, fromHijriDate, toHijriDate, employeeId);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonbNillable
    public static class RequestDetails {

	private Integer locationFlag;

	private String decreeNumber;

	@JsonbTransient
	private Date fromHijriDate;

	@JsonbTransient
	private Date toHijriDate;

	private Long employeeId;

	public String getFromHijriDateString() {
	    return MultiChronologyCalendarUtil.getDateString(fromHijriDate, ChronologyTypesEnum.HIJRI);
	}

	public void setFromHijriDateString(String fromHijriDateString) {
	    this.fromHijriDate = MultiChronologyCalendarUtil.getDate(fromHijriDateString, ChronologyTypesEnum.HIJRI);
	}

	public String getToHijriDateString() {
	    return MultiChronologyCalendarUtil.getDateString(toHijriDate, ChronologyTypesEnum.HIJRI);
	}

	public void setToHijriDateString(String toHijriDateString) {
	    this.toHijriDate = MultiChronologyCalendarUtil.getDate(toHijriDateString, ChronologyTypesEnum.HIJRI);
	}

    }

}
