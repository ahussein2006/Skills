package com.code.enums;

public class QueryConfigConstants {
    public static final String WF_SimpleMission_GetMissionById = "WF_SimpleMission_GetMissionById",
	    WF_SimpleMission_GetMissionById_Params = "P_INSTANCE_ID",

	    HCM_EmployeeData_GetEmployeeDataById = "HCM_EmployeeData_GetEmployeeDataById",
	    HCM_EmployeeData_GetEmployeeDataById_Params = "P_ID",

	    MSN_Mission_GetMissionById = "MSN_Mission_GetMissionById",
	    MSN_Mission_GetMissionById_Params = "P_ID",
	    MSN_MissionDetail_GetMissionDetailById = "MSN_MissionDetail_GetMissionDetailById",
	    MSN_MissionDetail_GetMissionDetailById_Params = "P_ID",
	    MSN_MissionDetail_GetConsumedBalance = "MSN_MissionDetail_GetConsumedBalance",
	    MSN_MissionDetail_GetConsumedBalance_Params = "P_EMPLOYEE_ID,P_FROM_DATE,P_TO_DATE",
	    MSN_MissionDetail_GetOverlap = "MSN_MissionDetail_GetOverlap",
	    MSN_MissionDetail_GetOverlap_PARAMS = "P_EMPLOYEE_ID,P_START_DATE,P_END_DATE,P_EXCLUDED_MISSION_ID";
}
