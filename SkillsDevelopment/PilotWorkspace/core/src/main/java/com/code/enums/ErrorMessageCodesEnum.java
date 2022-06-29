package com.code.enums;

public enum ErrorMessageCodesEnum {

    SHEET_UNSUPPORTED_FILE_FORMAT("error_sheetUnsupportedFileFormat"),
    SHEET_INDEX_OUT_OF_BOUND("error_sheetIndexOutOfBound"),
    SHEET_ROW_OUT_OF_BOUND("error_sheetRowOutOfBound"),
    SHEET_COLUMN_OUT_OF_BOUND("error_sheetColumnOutOfBound"),
    SHEET_UNSUPPORTED_CELL_TYPE("error_sheetUnsupportedCellType"),

    WF_REQUESTER_OR_BENEFICIARY_MANDATORY("error_requesterOrBeneficiaryMandatory"),
    WF_REQUESTER_OR_BENEFICIARY_OR_ASSIGNEE_MANDATORY("error_requesterOrBeneficiaryOrAssigneeMandatory"),

    WF_DELEGATOR_OR_DELEGATE_MANDATORY("error_delegatorOrDelegateMandatory"),
    WF_DELEGATION_LOOP("error_wf_delegationLoop"),
    WF_DELEGATOR_CANNOT_BE_THE_SAME_AS_DELEGATE("error_wf_delegatorCannotbeTheSameAsDelegate"),
    WF_DELEGATOR_ALREADY_HAS_TOTAL_DELEGATION("error_wf_delegatorAlreadyHasTotalDelegation"),
    WF_DELEGATE_ALREADY_HAS_PARTIAL_DELEGATION("error_wf_delegateAlreadyHasPartialDelegation"),
    WF_DELEGATE_ALREADY_HAS_TOTAL_DELEGATION("error_wf_delegateAlreadyHasTotalDelegation"),
    WF_DELEGATOR_ALREADY_HAS_PARTIAL_DELEGATION("error_wf_delegatorAlreadyHasPartialDelegation"),

    WF_TASKS_MANDATORY("error_wf_selectTasksForDelegation"),
    WF_TASK_REFUSE_REASONS_MANDATORY("error_wf_refuseReasonsManadatory"),
    WF_TASK_REFUSE_REASONS_SHOULD_BE_EMPTY("error_wf_refuseReasonsShouldBeEmpty"),
    WF_TASK_NOTES_MANDATORY("error_wf_notesManadatory"),

    WF_MISSION_DESTINATION_MANDATORY("error_wf_missionDestinationMandatory"),

    MSN_MAIN_FIELDS_MNDATORY("error_msn_mainFieldsMandatory"),
    MSN_DETAIL_FIELDS_MNDATORY("error_msn_detailFieldsMandatory"),
    MSN_ONE_EMPLOYEE_MANDATORY("error_msn_oneEmployeeMandatory"),
    MSN_EMPLOYEE_REPETITION("error_msn_employeeRepetition"),
    MSN_DATES_CONFLICT("error_msn_datesConflict"),
    MSN_DATES_BEFORE_HIRING("error_msn_datesBeforeHiring"),
    MSN_NO_BALANCE("error_msn_noBalance"),
    MSN_ACTUAL_FIELDS_MNDATORY("error_msn_actuallFieldsMandatory"),
    MSN_ACTUAL_SHOULD_BE_IN_SAME_YEAR("error_msn_actualShouldbeInTheSameYear"),
    MSN_START_END_SHOULD_BE_IN_SAME_YEAR("error_msn_startEndShouldbeInTheSameYear"),
    MSN_ACTUAL_SHOULD_NOT_CHANGE_IN_ABSENCE("error_msn_actualShouldNotChangeInAbsence"),
    MSN_ABSENCE_REASONS_NOT_ALLOWED("error_msn_absenceReasonsNotAllowed"),
    MSN_JOINING_DATE_MANDATORY("error_msn_joiningDateMandatory"),
    MSN_JOINING_DATE_NOT_ALLOWED("error_msn_joiningDateNotAllowed"),
    MSN_JOINING_DATE_INVALID("error_msn_joiningDateInvalid"),

    INVALID_DATE("error_invalidDate"),
    NO_DATA_FOUND("error_noDataFound"),
    GENERAL("error_general");

    private String value;

    private ErrorMessageCodesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
