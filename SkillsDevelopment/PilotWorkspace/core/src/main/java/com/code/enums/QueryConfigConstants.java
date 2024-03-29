package com.code.enums;

public class QueryConfigConstants {
    public static final String P_ESC_SEARCH_STR = "P_ESC_SEARCH_STR",
	    P_ESC_SEARCH_LONG = "P_ESC_SEARCH_LONG",
	    P_ESC_SEARCH_INT = "P_ESC_SEARCH_INT",
	    P_ESC_SEARCH_DOUBLE = "P_ESC_SEARCH_DOUBLE",
	    P_MODULE_ID = "P_MODULE_ID",

	    GN_Attachment_GetAttachments = "GN_Attachment_GetAttachments",
	    GN_Attachment_GetAttachments_Params = "P_ID,P_ATTACHMENTS_KEY,P_FILE_NAME,P_FILE_METADATA",

	    SP_Configuration_GetConfigurations = "SP_Configuration_GetConfigurations",
	    SP_Configuration_GetConfigurations_Params = "P_MODULE_ID",
	    SP_HijriCalendar_GetHijriCalendar = "SP_HijriCalendar_GetHijriCalendar",
	    SP_Message_GetMessages = "SP_Message_GetMessages",
	    SP_Message_GetMessages_Params = "P_MODULE_IDS",
	    SP_Module_GetModules = "SP_Module_GetModules",
	    SP_Module_GetModules_Params = "P_CODE",

	    UM_AuditLog_GetAuditLogs = "UM_AuditLog_GetAuditLogs",
	    UM_AuditLog_GetAuditLogs_Params = "P_CONTENT_ENTITY,P_CONTENT_ID,P_OPERATION,P_USER_ID,P_FROM_DATE_FLAG,P_FROM_DATE,P_TO_DATE_FLAG,P_TO_DATE,P_CONTENT",
	    UM_Group_GetGroups = "UM_Group_GetGroups",
	    UM_Group_GetGroups_Params = "P_CLASSIFICATION,P_NAME,P_USER_ID,P_URL_ID,P_URL_ACTION_ID",
	    UM_URL_GetUserURLs = "UM_URL_GetUserURLs",
	    UM_URL_GetUserURLs_Params = "P_USER_ID",
	    UM_URL_GetGroupURLs = "UM_URL_GetGroupURLs",
	    UM_URL_GetGroupURLs_Params = "P_GROUP_ID",
	    UM_URLAction_GetGroupURLActions = "UM_URLAction_GetGroupURLActions",
	    UM_URLAction_GetGroupURLActions_Params = "P_GROUP_ID",
	    UM_User_GetUsersByURL = "UM_User_GetUsersByURL",
	    UM_User_GetUsersByURL_Params = "P_URL_ID",
	    UM_User_GetUsersByAction = "UM_User_GetUsersByAction",
	    UM_User_GetUsersByAction_Params = "P_URL_ACTION_ID",
	    UM_UserURLActionData_GetUserURLActionsData = "UM_UserURLActionData_GetUserURLActionsData",
	    UM_UserURLActionData_GetUserURLActionsData_Params = "P_USER_ID",

	    WF_ProcessGroup_GetProcessGroups = "WF_ProcessGroup_GetProcessGroups",
	    WF_Process_GetProcesses = "WF_Process_GetProcesses",
	    WF_Process_GetProcesses_Params = "P_ID,P_PROCESS_GROUP_ID",
	    WF_ProcessStep_GetProcessSteps = "WF_ProcessStep_GetProcessSteps",
	    WF_ProcessStep_GetProcessSteps_Params = "P_PROCESS_ID,P_SEQ,P_ROLE",
	    WF_ProcessStepAction_GetProcessStepActions = "WF_ProcessStepAction_GetProcessStepActions",
	    WF_ProcessStepAction_GetProcessStepActions_Params = "P_STEP_ID,P_ACTION",
	    WF_Instance_GetInstanceById = "WF_Instance_GetInstanceById",
	    WF_Instance_GetInstanceById_Params = "P_ID",
	    WF_InstanceData_GetInstancesData = "WF_InstanceData_GetInstancesData",
	    WF_InstanceData_GetInstancesData_Params = "P_REQUESTER_ID,P_BENEFICIARY_ID,P_PROCESS_GROUP_ID,P_PROCESS_ID,P_SUBJECT,P_STATUSES",
	    WF_InstanceData_GetInstanceDataById = "WF_InstanceData_GetInstanceDataById",
	    WF_InstanceData_GetInstanceDataById_Params = "P_ID",
	    WF_InstanceBeneficiary_GetBeneficiariesByInstanceId = "WF_InstanceBeneficiary_GetBeneficiariesByInstanceId",
	    WF_InstanceBeneficiary_GetBeneficiariesByInstanceId_Params = "P_INSTANCE_ID",
	    WF_Task_GetInstanceTasks = "WF_Task_GetInstanceTasks",
	    WF_Task_GetInstanceTasks_Params = "P_INSTANCE_ID,P_ASSIGNEE_ROLE",
	    WF_Task_GetTaskById = "WF_Task_GetTaskById",
	    WF_Task_GetTaskById_Params = "P_ID",
	    WF_Task_GetTasksByIds = "WF_Task_GetTasksByIds",
	    WF_Task_GetTasksByIds_Params = "P_IDS",
	    WF_Task_CountInstanceTasks = "WF_Task_CountInstanceTasks",
	    WF_Task_CountInstanceTasks_Params = "P_INSTANCE_ID",
	    WF_Task_CountAssigneeTasks = "WF_Task_CountAssigneeTasks",
	    WF_Task_CountAssigneeTasks_Params = "P_ASSIGNEE_ID,P_NOTIFICATION_FLAG,P_NOTIFICATION_ROLE",
	    WF_TaskData_GetTasksData = "WF_TaskData_GetTasksData",
	    WF_TaskData_GetTasksData_Params = "P_ASSIGNEE_ID,P_REQUESTER_ID,P_BENEFICIARY_ID,P_PROCESS_GROUP_ID,P_PROCESS_ID,P_SUBJECT,P_RUNNING,P_NOTIFICATION_FLAG,P_NOTIFICATION_ROLE,P_FLAG_GROUP",
	    WF_TaskData_GetTaskDataById = "WF_TaskData_GetTaskDataById",
	    WF_TaskData_GetTaskDataById_Params = "P_ID",
	    WF_TaskData_GetInstanceTasksData = "WF_TaskData_GetInstanceTasksData",
	    WF_TaskData_GetInstanceTasksData_Params = "P_INSTANCE_ID",
	    WF_TaskData_GetInstancePreviousTasksData = "WF_TaskData_GetInstancePreviousTasksData",
	    WF_TaskData_GetInstancePreviousTasksData_Params = "P_INSTANCE_ID,P_NOTIFICATION_ROLE,P_ID,P_HLEVELS_FLAG,P_HLEVELS",
	    WF_Delegation_GetDelegateId = "WF_Delegation_GetDelegateId",
	    WF_Delegation_GetDelegateId_Params = "P_DELEGATOR_ID,P_PROCESS_ID",
	    WF_DelegationData_GetDelegationsData = "WF_DelegationData_GetDelegationsData",
	    WF_DelegationData_GetDelegationsData_Params = "P_DELEGATOR_ID,P_DELEGATE_ID,PARTIAL_FLAG,P_PROCESS_ID";

}
