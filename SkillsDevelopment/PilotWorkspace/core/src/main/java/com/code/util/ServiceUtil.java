package com.code.util;

import com.code.enums.ConfigCodesEnum;
import com.code.enums.ErrorMessageCodesEnum;
import com.code.enums.ServiceResponseStatusesCode;
import com.code.exceptions.BusinessException;
import com.code.service.requests.BaseRequest;
import com.code.service.requests.RequestMetadata;
import com.code.service.responses.BaseResponse;
import com.code.service.responses.ResponseMetadata;

public class ServiceUtil {

    private ServiceUtil() {

    }

    public static <T extends BaseResponse> void intilaizeResponse(T response, BaseRequest request, Boolean... validatePaginatedInquiry) throws BusinessException {
	try {
	    response.setResponseMetadata(intilaizeResponseMetadata(request.getRequestMetadata()));

	    if (!BasicUtil.isNullOrEmpty(validatePaginatedInquiry) && validatePaginatedInquiry[0])
		validatePaginatedInquiry(request.getRequestMetadata());
	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e, request.getRequestMetadata().getUserId());
	}
    }

    private static ResponseMetadata intilaizeResponseMetadata(RequestMetadata requestMetadata) {
	ResponseMetadata metadata = new ResponseMetadata();
	metadata.setConsumerRequestId(requestMetadata.getConsumerRequestId());
	metadata.setProviderRequestId(requestMetadata.getProviderRequestId());
	metadata.setFirstIndex(requestMetadata.getFirstIndex());
	metadata.setPageSize(requestMetadata.getPageSize());
	metadata.setStatusCode(ServiceResponseStatusesCode.SUCCESS.toString());
	return metadata;
    }

    private static void validatePaginatedInquiry(RequestMetadata requestMetadata) throws BusinessException {
	int maxPageSize = Integer.parseInt(ConfigurationUtil.getConfigValue(ConfigCodesEnum.DAL_MAX_PAGE_SIZE));

	if (!BasicUtil.isWhole(requestMetadata.getFirstIndex()) || !BasicUtil.isPositive(requestMetadata.getPageSize())
		|| requestMetadata.getPageSize() > maxPageSize)
	    throw new BusinessException(ErrorMessageCodesEnum.INVALID_PAGINATED_INQUIRY, BasicUtil.convertObjectsToArray(maxPageSize));
    }
}
