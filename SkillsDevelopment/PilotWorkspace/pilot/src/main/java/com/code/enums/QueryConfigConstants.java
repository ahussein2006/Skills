package com.code.enums;

public class QueryConfigConstants {
    public static final String HCM_EmployeeData_GetEmployeeDataById = "HCM_EmployeeData_GetEmployeeDataById",
	    HCM_EmployeeData_GetEmployeeDataById_Params = "P_ID",

	    MSN_Mission_GetMissionById = "MSN_Mission_GetMissionById",
	    MSN_Mission_GetMissionById_Params = "P_ID",
	    MSN_Mission_GetMissions = "MSN_Mission_GetMissions",
	    MSN_Mission_GetMissions_Params = "P_LOCATION_FLAG,P_DECREE_NUMBER,P_FROM_DATE_FLAG,P_FROM_DATE,P_TO_DATE_FLAG,P_TO_DATE,P_EMPLOYEE_ID",
	    MSN_Mission_GetMissionsCount = "MSN_Mission_GetMissionsCount",
	    MSN_Mission_GetMissionsCount_Params = "P_LOCATION_FLAG,P_DECREE_NUMBER,P_FROM_DATE_FLAG,P_FROM_DATE,P_TO_DATE_FLAG,P_TO_DATE,P_EMPLOYEE_ID",
	    MSN_MissionDetail_GetMissionDetailById = "MSN_MissionDetail_GetMissionDetailById",
	    MSN_MissionDetail_GetMissionDetailById_Params = "P_ID",
	    MSN_MissionDetail_GetConsumedBalance = "MSN_MissionDetail_GetConsumedBalance",
	    MSN_MissionDetail_GetConsumedBalance_Params = "P_EMPLOYEE_ID,P_FROM_DATE,P_TO_DATE",
	    MSN_MissionDetail_GetOverlap = "MSN_MissionDetail_GetOverlap",
	    MSN_MissionDetail_GetOverlap_PARAMS = "P_EMPLOYEE_ID,P_START_DATE,P_END_DATE,P_EXCLUDED_MISSION_ID";
}
