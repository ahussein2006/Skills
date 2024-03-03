package com.code.service.providers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.business.MissionsBusiness;
import com.code.enums.MediaTypeConstants;
import com.code.service.requests.missions.MissionsInquiryRequest;
import com.code.service.responses.missions.MissionsInquiryResponse;
import com.code.util.ExceptionUtil;
import com.code.util.ServiceUtil;

@Path("/missions")
@Consumes(MediaTypeConstants.APPLICATION_JSON)
@Produces(MediaTypeConstants.APPLICATION_JSON)
public class MissionsService {

    @Autowired
    MissionsBusiness missionsBusiness;

    @POST
    @Path("/inquiry")
    public MissionsInquiryResponse getMissions(MissionsInquiryRequest request) {
	MissionsInquiryResponse response = new MissionsInquiryResponse();
	try {
	    ServiceUtil.intilaizeResponse(response, request, true);
	    if (request.getRequestMetadata().getFirstIndex() == 0)
		response.getResponseMetadata().setRecordsCount(missionsBusiness.getMissionsCount(request.getRequestDetails().getLocationFlag(), request.getRequestDetails().getDecreeNumber(),
			request.getRequestDetails().getFromHijriDate(), request.getRequestDetails().getToHijriDate(), request.getRequestDetails().getEmployeeId()));

	    response.getResponseDetails().setMissions(missionsBusiness.getMissions(request.getRequestDetails().getLocationFlag(), request.getRequestDetails().getDecreeNumber(),
		    request.getRequestDetails().getFromHijriDate(), request.getRequestDetails().getToHijriDate(), request.getRequestDetails().getEmployeeId(),
		    request.getRequestMetadata().getPageSize(), request.getRequestMetadata().getFirstIndex()));
	    return response;
	} catch (Exception e) {
	    return ExceptionUtil.handleServiceException(e, request.getRequestMetadata().getUserId(), response, request.getRequestMetadata().getPreferedLang());
	}
    }
}
