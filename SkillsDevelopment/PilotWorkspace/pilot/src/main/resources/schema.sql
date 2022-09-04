DROP VIEW UM_VW_USER_URLS;
DROP VIEW UM_VW_USER_URL_ACTIONS;
DROP VIEW WF_VW_INSTANCES;
DROP VIEW WF_VW_TASKS;
DROP VIEW WF_VW_DELEGATIONS;
DROP TABLE SP_CONFIG;
DROP TABLE SP_HIJRI_CALENDAR;
DROP TABLE SP_MESSAGES;
DROP TABLE GN_ATTACHMENTS;
DROP TABLE GN_LOGS;
DROP TABLE UM_AUDIT_LOGS;
DROP TABLE UM_PRIVILEGES;
DROP TABLE UM_GROUP_DETAILS;
DROP TABLE UM_URL_ACTIONS;
DROP TABLE UM_URLS;
DROP TABLE UM_GROUPS;
DROP TABLE WF_DELEGATIONS;
DROP TABLE WF_TASKS;
DROP TABLE WF_INSTANCE_BENEFICIARIES;
DROP TABLE WF_INSTANCES;
DROP TABLE WF_PROCESS_STEP_ACTIONS;
DROP TABLE WF_PROCESS_STEPS;
DROP TABLE WF_PROCESSES;
DROP TABLE WF_PROCESS_GROUPS;
DROP TABLE UM_USERS;
DROP TABLE SP_MODULES;

DROP SEQUENCE SP_SEQ;
DROP SEQUENCE UM_SEQ;
DROP SEQUENCE UM_AUDIT_SEQ;
DROP SEQUENCE WF_SEQ;
DROP SEQUENCE WF_INSTANCE_TASK_SEQ;
DROP SEQUENCE GN_ATTACHMENT_SEQ;
DROP SEQUENCE GN_LOG_SEQ;

------------------------------------------------------------------------------------------------
CREATE TABLE SP_MODULES 
(
  ID NUMBER NOT NULL 
, CODE VARCHAR2(50 CHAR) NOT NULL 
, DESCRIPTION VARCHAR2(200 CHAR) NOT NULL 
, CONSTRAINT PK_SP_MODULES PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE SP_MODULES
ADD CONSTRAINT U_SP_MODULES_CODE UNIQUE 
(
  CODE 
)
ENABLE;

ALTER TABLE SP_MODULES
ADD CONSTRAINT U_SP_MODULES_DESC UNIQUE 
(
  DESCRIPTION 
)
ENABLE;

CREATE TABLE SP_CONFIG 
(
  ID NUMBER NOT NULL
, MODULE_ID NUMBER NOT NULL
, CODE VARCHAR2(50 CHAR) NOT NULL 
, CONFIG_VALUE VARCHAR2(200 CHAR) NOT NULL 
, DESCRIPTION VARCHAR2(200 CHAR) NOT NULL 
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_SP_CONFIG PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE SP_CONFIG
ADD CONSTRAINT U_SP_CONFIG_MODULE_ID_CODE UNIQUE 
(
  MODULE_ID
, CODE 
)
ENABLE;

ALTER TABLE SP_CONFIG
ADD CONSTRAINT FK_SP_CONFIG_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

CREATE TABLE SP_HIJRI_CALENDAR 
(
  ID NUMBER NOT NULL 
, HIJRI_YEAR NUMBER(4) NOT NULL 
, HIJRI_MONTH NUMBER(2) NOT NULL 
, HIJRI_MONTH_LENGTH NUMBER(2) NOT NULL 
, HIJRI_MONTH_END_GREGORIAN_DATE DATE NOT NULL 
, CONSTRAINT PK_SP_HIJRI_CALENDAR PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE SP_HIJRI_CALENDAR
ADD CONSTRAINT U_SP_HIJRI_CALENDAR_YEAR_MONTH UNIQUE 
(
  HIJRI_YEAR, HIJRI_MONTH 
)
ENABLE;

ALTER TABLE SP_HIJRI_CALENDAR
ADD CONSTRAINT U_SP_HIJRI_CALENDAR_END_GREGORIAN_DATE UNIQUE 
(
  HIJRI_MONTH_END_GREGORIAN_DATE 
)
ENABLE;

CREATE TABLE SP_MESSAGES 
(
  ID NUMBER NOT NULL
, MODULE_IDS VARCHAR2(50 CHAR) NOT NULL
, MESSAGE_TYPE VARCHAR2(50 CHAR) NOT NULL 
, MESSAGE_KEY VARCHAR2(100 CHAR) NOT NULL 
, AR_VALUE VARCHAR2(500 CHAR) NOT NULL 
, EN_VALUE VARCHAR2(500 CHAR) NOT NULL  
, CONSTRAINT PK_SP_MESSAGES PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE SP_MESSAGES
ADD CONSTRAINT U_SP_MESSAGES_MODULE_IDS_MESSAGE_KEY UNIQUE 
(
   MODULE_IDS
,  MESSAGE_KEY 
)
ENABLE;

------------------------------------------------------------------------------------------------
CREATE TABLE UM_USERS 
(
  ID NUMBER NOT NULL 
, NAME VARCHAR2(200 CHAR) NOT NULL 
, USERNAME VARCHAR2(50 CHAR) NOT NULL 
, SYSTEM_FLAG NUMBER(1) NOT NULL
, TEMP_KEY VARCHAR2(50 CHAR) NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE
, CONSTRAINT PK_UM_USERS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE UM_USERS
ADD CONSTRAINT U_UM_USERS_USERNAME UNIQUE 
(
  USERNAME 
)
ENABLE;

CREATE TABLE UM_URLS 
(
  ID NUMBER NOT NULL 
, CODE VARCHAR2(100 CHAR) 
, URL VARCHAR2(200 CHAR) 
, MODULE_ID NUMBER NOT NULL 
, NAME_KEY VARCHAR2(200 CHAR) NOT NULL 
, PARENT_ID NUMBER 
, ORDER_BY NUMBER NOT NULL 
, CLASSIFICATION NUMBER(1) NOT NULL 
, ACTIVE_FLAG NUMBER(1) NOT NULL 
, CONSTRAINT PK_UM_URLS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE UM_URLS
ADD CONSTRAINT U_UM_URLS_MODULE_ID_CODE UNIQUE 
(
  MODULE_ID 
, CODE 
)
USING INDEX 
(
    CREATE UNIQUE INDEX UM_URLS ON UM_URLS (MODULE_ID ASC, CODE ASC) 
)
 ENABLE;

ALTER TABLE UM_URLS
ADD CONSTRAINT U_UM_URLS_MODULE_ID_URL UNIQUE 
(
  MODULE_ID 
, URL 
)
ENABLE;

ALTER TABLE UM_URLS
ADD CONSTRAINT FK_UM_URLS_PARENT_ID FOREIGN KEY
(
  PARENT_ID 
)
REFERENCES UM_URLS
(
  ID 
)
ENABLE;

ALTER TABLE UM_URLS
ADD CONSTRAINT FK_UM_URLS_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

COMMENT ON COLUMN UM_URLS.CLASSIFICATION IS '0: Non Menu Screen, 1: Menu Screen, 2: Service URL ';

CREATE TABLE UM_URL_ACTIONS 
(
  ID NUMBER NOT NULL 
, ACTION VARCHAR2(100 CHAR) NOT NULL
, URL_ID NUMBER NOT NULL
, DESCRIPTION VARCHAR2(200 CHAR) NOT NULL 
, CONSTRAINT PK_UM_URL_ACTIONS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE UM_URL_ACTIONS
ADD CONSTRAINT U_UM_URL_ACTIONS_URL_ID_ACTION UNIQUE 
(
  URL_ID 
, ACTION 
)
 ENABLE;

ALTER TABLE UM_URL_ACTIONS
ADD CONSTRAINT FK_UM_URL_ACTIONS_UM_URLS_URL_ID FOREIGN KEY
(
  URL_ID 
)
REFERENCES UM_URLS
(
  ID 
)
ENABLE;

CREATE TABLE UM_GROUPS
(
  ID NUMBER NOT NULL 
, NAME VARCHAR2(200 CHAR) NOT NULL
, MODULE_ID NUMBER NOT NULL
, CLASSIFICATION NUMBER(1) NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE
, CONSTRAINT PK_UM_GROUPS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE UM_GROUPS
ADD CONSTRAINT FK_UM_GROUPS_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

COMMENT ON COLUMN UM_GROUPS.CLASSIFICATION IS '1: Users Group, 2: URLs Group, 3: URL Actions Group ';

CREATE TABLE UM_GROUP_DETAILS
(
  ID NUMBER NOT NULL 
, GROUP_ID NUMBER NOT NULL
, USER_ID NUMBER
, URL_ID NUMBER
, URL_ACTION_ID NUMBER
, DATA_DIMENSION NUMBER
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE
, CONSTRAINT PK_UM_GROUP_DETAILS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT U_UM_GROUP_DETAILS_GROUP_ID_USER_ID UNIQUE 
(
  GROUP_ID 
, USER_ID
, URL_ID
, URL_ACTION_ID
, DATA_DIMENSION
)
ENABLE;

/*ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT U_UM_GROUP_DETAILS_GROUP_ID_URL_ID UNIQUE 
(
  GROUP_ID 
, URL_ID 
)
ENABLE;

ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT U_UM_GROUP_DETAILS_GROUP_ID_URL_ACTION_ID UNIQUE 
(
  GROUP_ID 
, URL_ACTION_ID 
)
ENABLE;

ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT U_UM_GROUP_DETAILS_GROUP_ID_DATA_DIMENSION UNIQUE 
(
  GROUP_ID 
, DATA_DIMENSION 
)
ENABLE;
*/
ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT FK_UM_GROUP_DETAILS_UM_GROUPS_GROUP_ID FOREIGN KEY
(
  GROUP_ID 
)
REFERENCES UM_GROUPS
(
  ID 
)
ENABLE;

ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT FK_UM_GROUP_DETAILS_UM_USERS_USER_ID FOREIGN KEY
(
  USER_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT FK_UM_GROUP_DETAILS_UM_URLS_URL_ID FOREIGN KEY
(
  URL_ID 
)
REFERENCES UM_URLS
(
  ID 
)
ENABLE;

ALTER TABLE UM_GROUP_DETAILS
ADD CONSTRAINT FK_UM_GROUP_DETAILS_UM_URL_ACTIONS_URL_ACTION_ID FOREIGN KEY
(
  URL_ACTION_ID 
)
REFERENCES UM_URL_ACTIONS
(
  ID 
)
ENABLE;


CREATE TABLE UM_PRIVILEGES
(
  ID NUMBER NOT NULL 
, USER_GROUP_ID NUMBER NOT NULL
, PRIVILEGE_GROUP_ID NUMBER NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE
, CONSTRAINT PK_UM_PRIVILEGES PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE UM_PRIVILEGES
ADD CONSTRAINT U_UM_PRIVILEGES_USER_GROUP_ID_PRIVILEGE_GROUP_ID UNIQUE 
(
  USER_GROUP_ID 
, PRIVILEGE_GROUP_ID 
)
ENABLE;

ALTER TABLE UM_PRIVILEGES
ADD CONSTRAINT FK_UM_PRIVILEGES_UM_GROUPS_USER_GROUP_ID FOREIGN KEY
(
  USER_GROUP_ID 
)
REFERENCES UM_GROUPS
(
  ID 
)
ENABLE;

ALTER TABLE UM_PRIVILEGES
ADD CONSTRAINT FK_UM_PRIVILEGES_UM_GROUPS_PRIVILEGE_GROUP_ID FOREIGN KEY
(
  PRIVILEGE_GROUP_ID 
)
REFERENCES UM_GROUPS
(
  ID 
)
ENABLE;

CREATE TABLE UM_AUDIT_LOGS
(
  ID NUMBER NOT NULL 
, MODULE_ID NUMBER NOT NULL
, USER_ID NUMBER NOT NULL
, OPERATION VARCHAR2(10 CHAR) NOT NULL
, OPERATION_DATE DATE NOT NULL
, CONTENT_ENTITY VARCHAR2(200 CHAR) NOT NULL
, CONTENT_ID VARCHAR2(100 CHAR) NOT NULL
, CONTENT VARCHAR2(2000 CHAR) NOT NULL
, CONSTRAINT PK_UM_AUDIT_LOGS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);


ALTER TABLE UM_AUDIT_LOGS
ADD CONSTRAINT FK_UM_AUDIT_LOGS_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

ALTER TABLE UM_AUDIT_LOGS
ADD CONSTRAINT FK_UM_AUDIT_LOGS_UM_USERS_USER_ID FOREIGN KEY
(
  USER_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

CREATE OR REPLACE VIEW UM_VW_USER_URLS ("USER_ID", "URL_ID") AS
SELECT DISTINCT USERGD.USER_ID, URLGD.URL_ID
FROM UM_PRIVILEGES P, UM_GROUPS USERG, UM_GROUPS URLG, UM_GROUP_DETAILS USERGD, UM_GROUP_DETAILS URLGD
WHERE
	P.USER_GROUP_ID = USERG.ID
AND P.PRIVILEGE_GROUP_ID = URLG.ID
AND USERGD.GROUP_ID = USERG.ID
AND URLGD.GROUP_ID = URLG.ID
AND URLG.CLASSIFICATION = 2;

CREATE OR REPLACE VIEW UM_VW_USER_URL_ACTIONS ("ID", "URL_ID", "USER_ID", "URL_CODE", "ACTION") AS
SELECT DISTINCT  A.ID, A.URL_ID, USERGD.USER_ID,  URL.CODE, A.ACTION
FROM UM_PRIVILEGES P, UM_GROUPS USERG, UM_GROUPS AG, UM_GROUP_DETAILS USERGD, UM_GROUP_DETAILS AGD, UM_URL_ACTIONS A, UM_URLS URL
WHERE
	P.USER_GROUP_ID = USERG.ID
AND P.PRIVILEGE_GROUP_ID = AG.ID
AND USERGD.GROUP_ID = USERG.ID
AND AGD.GROUP_ID = AG.ID
AND AGD.URL_ACTION_ID = A.ID
AND A.URL_ID = URL.ID 
AND AG.CLASSIFICATION = 3;

------------------------------------------------------------------------------------------------
CREATE TABLE GN_ATTACHMENTS 
(
  ID NUMBER NOT NULL 
, ATTACHMENTS_KEY VARCHAR2(100 CHAR) NOT NULL 
, FILE_ID VARCHAR2(100 CHAR) NOT NULL 
, FILE_NAME VARCHAR2(100 CHAR) 
, FILE_METADATA VARCHAR2(200 CHAR)
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_GN_ATTACHMENTS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

CREATE TABLE GN_LOGS 
(
  ID NUMBER NOT NULL
, MODULE_ID NUMBER NOT NULL
, LOG_TYPE VARCHAR2(25 CHAR) NOT NULL 
, LOG_DATE DATE NOT NULL 
, LOG_DATA CLOB NOT NULL 
, USER_ID NUMBER
, INTEG_REQUEST_ID VARCHAR2(36 CHAR)
, CONSTRAINT PK_GN_LOGS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE GN_LOGS
ADD CONSTRAINT FK_GN_LOGS_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

ALTER TABLE GN_LOGS
ADD CONSTRAINT FK_GN_LOGS_UM_USERS_USER_ID FOREIGN KEY
(
  USER_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

------------------------------------------------------------------------------------------------
CREATE TABLE WF_PROCESS_GROUPS 
(
  ID NUMBER NOT NULL 
, MODULE_ID NUMBER NOT NULL 
, NAME VARCHAR2(200 CHAR) NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_WF_PROCESS_GROUPS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_PROCESS_GROUPS
ADD CONSTRAINT U_WF_PROCESS_GROUPS_MODULE_ID_NAME UNIQUE 
(
  MODULE_ID, NAME 
)
ENABLE;

ALTER TABLE WF_PROCESS_GROUPS
ADD CONSTRAINT FK_WF_PROCESS_GROUPS_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

CREATE TABLE WF_PROCESSES 
(
  ID NUMBER NOT NULL 
, NAME VARCHAR2(200 CHAR) NOT NULL 
, PROCESS_GROUP_ID NUMBER NOT NULL
, PRE_INIT VARCHAR2(200 CHAR)
, FIRST_STEP VARCHAR2(200 CHAR) NOT NULL
, POST_INIT VARCHAR2(200 CHAR)
, URL VARCHAR2(200 CHAR) NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_WF_PROCESSES PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_PROCESSES
ADD CONSTRAINT U_WF_PROCESSES_GROUP_ID_NAME UNIQUE 
(
  PROCESS_GROUP_ID, NAME 
)
ENABLE;

ALTER TABLE WF_PROCESSES
ADD CONSTRAINT FK_WF_PROCESSES_WF_PROCESS_GROUPS_GROUP_ID FOREIGN KEY
(
  PROCESS_GROUP_ID 
)
REFERENCES WF_PROCESS_GROUPS
(
  ID 
)
ENABLE;

CREATE TABLE WF_PROCESS_STEPS 
(
  ID NUMBER NOT NULL 
, PROCESS_ID NUMBER NOT NULL 
, ROLE VARCHAR2(100 CHAR) NOT NULL
, SEQ NUMBER NOT NULL 
, ASSIGNEES VARCHAR2(300 CHAR) NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT WF_PROCESS_STEPS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_PROCESS_STEPS
ADD CONSTRAINT U_WF_PROCESS_STEPS_PROCESS_ID_SEQ UNIQUE 
(
  PROCESS_ID, SEQ 
)
ENABLE;

ALTER TABLE WF_PROCESS_STEPS
ADD CONSTRAINT FK_WF_PROCESS_STEPS_WF_PROCESSES_PROCESS_ID FOREIGN KEY
(
  PROCESS_ID 
)
REFERENCES WF_PROCESSES
(
  ID 
)
ENABLE;

CREATE TABLE WF_PROCESS_STEP_ACTIONS 
(
  ID NUMBER NOT NULL 
, STEP_ID NUMBER NOT NULL 
, ACTION VARCHAR2(100 CHAR) NOT NULL
, PRE_METHOD VARCHAR2(200 CHAR)
, NEXT_STEP VARCHAR2(300 CHAR) NOT NULL
, POST_METHOD VARCHAR2(200 CHAR)
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT WF_PROCESS_STEP_ACTIONS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_PROCESS_STEP_ACTIONS
ADD CONSTRAINT U_WF_PROCESS_STEP_ACTIONS_STEP_ID_ACTION UNIQUE 
(
  STEP_ID, ACTION 
)
ENABLE;

ALTER TABLE WF_PROCESS_STEP_ACTIONS
ADD CONSTRAINT FK_WF_PROCESS_STEP_ACTIONS_WF_PROCESS_STEPS_STEP_ID FOREIGN KEY
(
  STEP_ID 
)
REFERENCES WF_PROCESS_STEPS
(
  ID 
)
ENABLE;

CREATE TABLE WF_INSTANCES 
(
  ID NUMBER NOT NULL 
, PROCESS_ID NUMBER NOT NULL 
, REQUESTER_ID NUMBER NOT NULL 
, SUBJECT VARCHAR2(1000 CHAR) NOT NULL
, REQUEST_DATE DATE NOT NULL
, REQUEST_HIJRI_DATE DATE NOT NULL
, STATUS NUMBER(1) NOT NULL
, LAST_ACTION VARCHAR2(100 CHAR)
, ATTACHMENTS_KEY VARCHAR2(100 CHAR)
, CONSTRAINT PK_WF_INSTANCES PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_INSTANCES
ADD CONSTRAINT FK_WF_INSTANCES_WF_PROCESSES_PROCESS_ID FOREIGN KEY
(
  PROCESS_ID 
)
REFERENCES WF_PROCESSES
(
  ID 
)
ENABLE;

ALTER TABLE WF_INSTANCES
ADD CONSTRAINT FK_WF_INSTANCES_UM_USERS_REQUESTER_ID FOREIGN KEY
(
  REQUESTER_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

CREATE OR REPLACE VIEW WF_VW_INSTANCES ("ID", "PROCESS_ID", "PROCESS_NAME", "PROCESS_GROUP_ID", "MODULE_ID", "REQUESTER_ID", "REQUESTER_NAME", "SUBJECT", "REQUEST_DATE", "REQUEST_HIJRI_DATE", "STATUS", "LAST_ACTION", "ATTACHMENTS_KEY") AS
SELECT I.ID, I.PROCESS_ID, P.NAME, P.PROCESS_GROUP_ID, PG.MODULE_ID, I.REQUESTER_ID, U.NAME, I.SUBJECT, I.REQUEST_DATE, I.REQUEST_HIJRI_DATE, I.STATUS, I.LAST_ACTION, I.ATTACHMENTS_KEY
FROM WF_INSTANCES I, WF_PROCESSES P, WF_PROCESS_GROUPS PG, UM_USERS U
WHERE
	I.PROCESS_ID = P.ID
AND P.PROCESS_GROUP_ID = PG.ID
AND I.REQUESTER_ID = U.ID;

CREATE TABLE WF_INSTANCE_BENEFICIARIES 
(
  INSTANCE_ID NUMBER NOT NULL 
, BENEFICIARY_ID NUMBER NOT NULL 
, CONSTRAINT PK_WF_INSTANCE_BENEFICIARIES PRIMARY KEY 
  (
    INSTANCE_ID, BENEFICIARY_ID
  )
  ENABLE 
);

ALTER TABLE WF_INSTANCE_BENEFICIARIES
ADD CONSTRAINT FK_WF_INSTANCE_BENEFICIARIES_WF_INSTANCES_INSTANCE_ID FOREIGN KEY
(
  INSTANCE_ID 
)
REFERENCES WF_INSTANCES
(
  ID 
)
ENABLE;

ALTER TABLE WF_INSTANCE_BENEFICIARIES
ADD CONSTRAINT FK_WF_INSTANCE_BENEFICIARIES_UM_USERS_BENEFICIARY_ID FOREIGN KEY
(
  BENEFICIARY_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

CREATE TABLE WF_TASKS 
(
  ID NUMBER NOT NULL 
, INSTANCE_ID NUMBER NOT NULL 
, ORGINAL_ID NUMBER NOT NULL 
, ASSIGNEE_ID NUMBER NOT NULL 
, ASSIGNMENT_DATE DATE NOT NULL
, ASSIGNMENT_HIJRI_DATE DATE NOT NULL
, ASSIGNEE_ROLE VARCHAR2(100 CHAR) NOT NULL
, URL VARCHAR2(200 CHAR) NOT NULL
, ACTION VARCHAR2(100 CHAR)
, ACTION_DATE DATE
, ACTION_HIJRI_DATE DATE
, NOTES VARCHAR2(500 CHAR)
, REFUSE_REASONS VARCHAR2(500 CHAR)
, FIRST_FLEX_FIELD VARCHAR2(200 CHAR)
, SECOND_FLEX_FIELD VARCHAR2(200 CHAR)
, THIRD_FLEX_FIELD VARCHAR2(200 CHAR)
, ATTACHMENTS_KEY VARCHAR2(100 CHAR)
, FLAG_GROUP VARCHAR2(50 CHAR)
, HLEVEL VARCHAR2(50 CHAR) NOT NULL
, VERSION NUMBER NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_WF_TASKS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_TASKS
ADD CONSTRAINT FK_WF_TASKS_WF_INSTANCES_INSTANCE_ID FOREIGN KEY
(
  INSTANCE_ID 
)
REFERENCES WF_INSTANCES
(
  ID 
)
ENABLE;

ALTER TABLE WF_TASKS
ADD CONSTRAINT FK_WF_TASKS_UM_USERS_ORGINAL_ID FOREIGN KEY
(
  ORGINAL_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

ALTER TABLE WF_TASKS
ADD CONSTRAINT FK_WF_TASKS_UM_USERS_ASSIGNEE_ID FOREIGN KEY
(
  ASSIGNEE_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

CREATE OR REPLACE VIEW WF_VW_TASKS ("ID", "INSTANCE_ID", "PROCESS_ID", "PROCESS_NAME", "PROCESS_GROUP_ID", "MODULE_ID", "REQUESTER_ID", "REQUESTER_NAME", "SUBJECT", "ORGINAL_ID", "ORGINAL_NAME"
, "ASSIGNEE_ID", "ASSIGNMENT_DATE", "ASSIGNMENT_HIJRI_DATE", "ASSIGNEE_ROLE", "URL", "ACTION", "ACTION_DATE", "ACTION_HIJRI_DATE", "NOTES", "REFUSE_REASONS", "FIRST_FLEX_FIELD", "SECOND_FLEX_FIELD"
, "THIRD_FLEX_FIELD", "ATTACHMENTS_KEY", "FLAG_GROUP", "HLEVEL") AS
SELECT T.ID, T.INSTANCE_ID, I.PROCESS_ID, P.NAME, P.PROCESS_GROUP_ID, PG.MODULE_ID, I.REQUESTER_ID, RU.NAME, I.SUBJECT, T.ORGINAL_ID, OU.NAME, T.ASSIGNEE_ID, T.ASSIGNMENT_DATE,
T.ASSIGNMENT_HIJRI_DATE, T.ASSIGNEE_ROLE, T.URL, T.ACTION, T.ACTION_DATE, T.ACTION_HIJRI_DATE, T.NOTES, T.REFUSE_REASONS, T.FIRST_FLEX_FIELD, T.SECOND_FLEX_FIELD, T.THIRD_FLEX_FIELD,
T.ATTACHMENTS_KEY, T.FLAG_GROUP, T.HLEVEL

FROM WF_TASKS T, WF_INSTANCES I, WF_PROCESSES P, WF_PROCESS_GROUPS PG, UM_USERS RU, UM_USERS OU
WHERE T.INSTANCE_ID = I.ID
AND I.PROCESS_ID = P.ID
AND P.PROCESS_GROUP_ID = PG.ID
AND I.REQUESTER_ID = RU.ID
AND T.ORGINAL_ID = OU.ID;

CREATE TABLE WF_DELEGATIONS 
(
  ID NUMBER NOT NULL 
, MODULE_ID NUMBER NOT NULL 
, DELEGATOR_ID NUMBER NOT NULL 
, DELEGATE_ID NUMBER NOT NULL 
, PROCESS_ID NUMBER
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_WF_DELEGATIONS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

ALTER TABLE WF_DELEGATIONS
ADD CONSTRAINT U_WF_DELEGATIONS_MODULE_DELEGATOR_DELEGATE_PROCESS UNIQUE 
(
  MODULE_ID, DELEGATOR_ID, DELEGATE_ID, PROCESS_ID
)
ENABLE;

ALTER TABLE WF_DELEGATIONS
ADD CONSTRAINT FK_WF_DELEGATIONS_SP_MODULES_MODULE_ID FOREIGN KEY
(
  MODULE_ID 
)
REFERENCES SP_MODULES
(
  ID 
)
ENABLE;

ALTER TABLE WF_DELEGATIONS
ADD CONSTRAINT FK_WF_DELEGATIONS_UM_USERS_DELEGATOR_ID FOREIGN KEY
(
  DELEGATOR_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

ALTER TABLE WF_DELEGATIONS
ADD CONSTRAINT FK_WF_DELEGATIONS_UM_USERS_DELEGATE_ID FOREIGN KEY
(
  DELEGATE_ID 
)
REFERENCES UM_USERS
(
  ID 
)
ENABLE;

ALTER TABLE WF_DELEGATIONS
ADD CONSTRAINT FK_WF_DELEGATIONS_WF_PROCESSES_PROCESS_ID FOREIGN KEY
(
  PROCESS_ID 
)
REFERENCES WF_PROCESSES
(
  ID 
)
ENABLE;

CREATE OR REPLACE VIEW WF_VW_DELEGATIONS ("ID", "MODULE_ID", "DELEGATOR_ID", "DELEGATOR_NAME", "DELEGATE_ID", "DELEGATE_NAME", "PROCESS_ID", "PROCESS_NAME") AS
SELECT D.ID, D.MODULE_ID, D.DELEGATOR_ID, FU.NAME, D.DELEGATE_ID, TU.NAME, D.PROCESS_ID, P.NAME
FROM WF_DELEGATIONS D, WF_PROCESSES P, UM_USERS FU, UM_USERS TU
WHERE D.PROCESS_ID = P.ID (+)
AND D.DELEGATOR_ID = FU.ID
AND D.DELEGATE_ID = TU.ID;

------------------------------------------------------------------------------------------------
CREATE SEQUENCE SP_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
CREATE SEQUENCE UM_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
CREATE SEQUENCE UM_AUDIT_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
CREATE SEQUENCE WF_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
CREATE SEQUENCE WF_INSTANCE_TASK_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
CREATE SEQUENCE GN_ATTACHMENT_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
CREATE SEQUENCE GN_LOG_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;

------------------------------------------------------------------------------------------------
DROP VIEW WF_VW_MISSION_DETAILS;
DROP VIEW HCM_VW_MSN_DETAILS;
DROP TABLE WF_MISSION_DETAILS;
DROP TABLE WF_MISSIONS;
DROP TABLE HCM_MSN_DETAILS;
DROP TABLE HCM_MSN_TRANSACTIONS;
DROP TABLE HCM_EMPLOYEES;

DROP SEQUENCE HCM_MSN_SEQ;
------------------------------------------------------------------------------------------------
CREATE TABLE HCM_EMPLOYEES 
(
  ID NUMBER NOT NULL
, NAME VARCHAR2(200 CHAR) NOT NULL 
, SOCIAL_NUMBER VARCHAR2(10 CHAR) NOT NULL 
, CATEGORY_CODE VARCHAR2(1 CHAR) NOT NULL
, GRADE_CODE VARCHAR2(2 CHAR) NOT NULL
, GRADE_NAME VARCHAR2(200 CHAR) NOT NULL
, POSITION_CODE VARCHAR2(10 CHAR)
, POSITION_TITLE_NAME VARCHAR2(200 CHAR)
, PHYSICAL_UNIT_TYPE_CODE VARCHAR2(3 CHAR)
, PHYSICAL_UNIT_FULL_NAME VARCHAR2(500 CHAR)
, PHYSICAL_UNIT_HKEY VARCHAR2(30 CHAR)
, PHYSICAL_REGION_CODE VARCHAR2(2 CHAR)
, MANAGER_FLAG NUMBER(1) NOT NULL
, MANAGER_ID NUMBER
, HIRING_HIJRI_DATE DATE
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_HCM_EMPLOYEES PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

CREATE TABLE HCM_MSN_TRANSACTIONS 
(
  ID NUMBER NOT NULL
, LOCATION_FLAG NUMBER(1) NOT NULL 
, LOCATION VARCHAR2(200 CHAR) NOT NULL 
, DESTINATION VARCHAR2(200 CHAR) NOT NULL
, PURPOSE VARCHAR2(200 CHAR) NOT NULL
, PERIOD NUMBER NOT NULL
, ROAD_PERIOD NUMBER NOT NULL
, START_HIJRI_DATE DATE NOT NULL 
, END_HIJRI_DATE DATE NOT NULL 
, MINISTERY_APPROVAL_NUMBER VARCHAR2(100 CHAR)
, MINISTERY_APPROVAL_HIJRI_DATE DATE
, ROAD_LINE VARCHAR2(200 CHAR)
, DOUBLE_BALANCE_FLAG NUMBER(1) NOT NULL
, HAJJ_FLAG NUMBER(1) NOT NULL
, REMARKS VARCHAR2(200 CHAR)
, DECREE_NUMBER VARCHAR2(100 CHAR) NOT NULL
, DECREE_HIJRI_DATE DATE NOT NULL 
, ATTACHMENTS_KEY VARCHAR2(100 CHAR)
, INTERNAL_COPIES VARCHAR2(500 CHAR)
, EXTERNAL_COPIES VARCHAR2(500 CHAR)
, APPROVED_ID NUMBER
, DECREE_APPROVED_ID NUMBER
, ORIGINAL_DECREE_APPROVED_ID NUMBER
, DECREE_REGION_CODE VARCHAR2(2 CHAR)
, DIRECTED_TO_POSITION_TITLE VARCHAR2(200 CHAR)
, CATEGORY_CODE VARCHAR2(1 CHAR)
, EFLAG NUMBER(1) NOT NULL
, MIG_FLAG NUMBER(1) NOT NULL
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_HCM_MSN_TRANSACTIONS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

CREATE TABLE HCM_MSN_DETAILS 
(
  ID NUMBER NOT NULL
, MISSION_ID NUMBER NOT NULL 
, EMPLOYEE_ID NUMBER NOT NULL 
, DECREE_EMPLOYEE_UNIT_FULL_NAME VARCHAR2(500 CHAR)
, DECREE_EMPLOYEE_POSITION_CODE VARCHAR2(10 CHAR)
, DECREE_EMPLOYEE_POSITION_TITLE_NAME VARCHAR2(200 CHAR)
, DECREE_EMPLOYEE_GRADE_NAME VARCHAR2(200 CHAR)
, BALANCE NUMBER
, PERIOD NUMBER NOT NULL
, ROAD_PERIOD NUMBER NOT NULL
, START_HIJRI_DATE DATE NOT NULL 
, END_HIJRI_DATE DATE NOT NULL 
, ACTUAL_PERIOD NUMBER
, ACTUAL_START_HIJRI_DATE DATE 
, ACTUAL_END_HIJRI_DATE DATE
, EXCEPTIONAL_APPROVAL_NUMBER VARCHAR2(100 CHAR)
, EXCEPTIONAL_APPROVAL_HIJRI_DATE DATE
, ROAD_LINE VARCHAR2(200 CHAR)
, ABSENCE_FLAG NUMBER(1) NOT NULL
, ABSENCE_REASONS VARCHAR2(200 CHAR)
, JOINING_HIJRI_DATE DATE
, REMARKS VARCHAR2(200 CHAR)
, CLOSING_ATTACHMENTS_KEY VARCHAR2(100 CHAR)
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_HCM_MSN_DETAILS PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

CREATE OR REPLACE VIEW HCM_VW_MSN_DETAILS
("ID", "MISSION_ID", "MISSION_LOCATION_FLAG", "MISSION_DESTINATION", "MISSION_PURPOSE", "MISSION_DECREE_NUMBER", "MISSION_DECREE_HIJRI_DATE", "MISSION_ATTACHMENTS_KEY", "MISSION_EFLAG",
"EMPLOYEE_ID", "EMPLOYEE_NAME", "EMPLOYEE_GRADE_CODE", "EMPLOYEE_PHYSICAL_REGION_CODE", "EMPLOYEE_HIRING_HIJRI_DATE",
"DECREE_EMPLOYEE_UNIT_FULL_NAME", "DECREE_EMPLOYEE_POSITION_CODE", "DECREE_EMPLOYEE_POSITION_TITLE_NAME", "DECREE_EMPLOYEE_GRADE_NAME",
"BALANCE", "PERIOD", "ROAD_PERIOD", "START_HIJRI_DATE", "END_HIJRI_DATE", "ACTUAL_PERIOD", "ACTUAL_START_HIJRI_DATE", "ACTUAL_END_HIJRI_DATE", "EXCEPTIONAL_APPROVAL_NUMBER",
"EXCEPTIONAL_APPROVAL_HIJRI_DATE", "ROAD_LINE", "ABSENCE_FLAG", "ABSENCE_REASONS", "JOINING_HIJRI_DATE", "REMARKS", "CLOSING_ATTACHMENTS_KEY"
) AS
SELECT D.ID, D.MISSION_ID, M.LOCATION_FLAG, M.DESTINATION, M.PURPOSE, M.DECREE_NUMBER, M.DECREE_HIJRI_DATE, M.ATTACHMENTS_KEY, M.EFLAG,
D.EMPLOYEE_ID, E.NAME, E.GRADE_CODE, E.PHYSICAL_REGION_CODE, E.HIRING_HIJRI_DATE,
D.DECREE_EMPLOYEE_UNIT_FULL_NAME, D.DECREE_EMPLOYEE_POSITION_CODE, D.DECREE_EMPLOYEE_POSITION_TITLE_NAME,
D.DECREE_EMPLOYEE_GRADE_NAME, D.BALANCE, D.PERIOD, D.ROAD_PERIOD, D.START_HIJRI_DATE, D.END_HIJRI_DATE, D.ACTUAL_PERIOD, D.ACTUAL_START_HIJRI_DATE, 
D.ACTUAL_END_HIJRI_DATE, D.EXCEPTIONAL_APPROVAL_NUMBER, D.EXCEPTIONAL_APPROVAL_HIJRI_DATE, D.ROAD_LINE, D.ABSENCE_FLAG, D.ABSENCE_REASONS, D.JOINING_HIJRI_DATE,
D.REMARKS, D.CLOSING_ATTACHMENTS_KEY
FROM HCM_MSN_DETAILS D, HCM_MSN_TRANSACTIONS M, HCM_EMPLOYEES E
WHERE D.MISSION_ID = M.ID
AND D.EMPLOYEE_ID = E.ID;

CREATE TABLE WF_MISSIONS 
(
  INSTANCE_ID NUMBER NOT NULL
, LOCATION_FLAG NUMBER(1) NOT NULL 
, LOCATION VARCHAR2(200 CHAR) NOT NULL 
, DESTINATION VARCHAR2(200 CHAR) NOT NULL
, PURPOSE VARCHAR2(200 CHAR) NOT NULL
, PERIOD NUMBER NOT NULL
, ROAD_PERIOD NUMBER NOT NULL
, START_HIJRI_DATE DATE NOT NULL 
, END_HIJRI_DATE DATE NOT NULL 
, MINISTERY_APPROVAL_NUMBER VARCHAR2(100 CHAR)
, MINISTERY_APPROVAL_HIJRI_DATE DATE
, ROAD_LINE VARCHAR2(200 CHAR)
, DOUBLE_BALANCE_FLAG NUMBER(1) NOT NULL
, HAJJ_FLAG NUMBER(1) NOT NULL
, REMARKS VARCHAR2(200 CHAR)
, INTERNAL_COPIES VARCHAR2(500 CHAR)
, EXTERNAL_COPIES VARCHAR2(500 CHAR)
, APPROVED_ID NUMBER
, DECREE_APPROVED_ID NUMBER
, ORIGINAL_DECREE_APPROVED_ID NUMBER
, DECREE_REGION_CODE VARCHAR2(2 CHAR)
, DIRECTED_TO_POSITION_TITLE VARCHAR2(200 CHAR)
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_WF_MISSIONS PRIMARY KEY 
  (
    INSTANCE_ID 
  )
  ENABLE 
);

CREATE TABLE WF_MISSION_DETAILS 
(
  INSTANCE_ID NUMBER NOT NULL
, EMPLOYEE_ID NUMBER NOT NULL 
, BALANCE NUMBER
, PERIOD NUMBER NOT NULL
, ROAD_PERIOD NUMBER NOT NULL
, START_HIJRI_DATE DATE NOT NULL 
, END_HIJRI_DATE DATE NOT NULL 
, EXCEPTIONAL_APPROVAL_NUMBER VARCHAR2(100 CHAR)
, EXCEPTIONAL_APPROVAL_HIJRI_DATE DATE
, ROAD_LINE VARCHAR2(200 CHAR)
, REMARKS VARCHAR2(200 CHAR)
, INSERTION_DATE DATE NOT NULL 
, LAST_UPDATE_DATE DATE 
, CONSTRAINT PK_WF_MISSION_DETAILS PRIMARY KEY 
  (
    INSTANCE_ID, EMPLOYEE_ID 
  )
  ENABLE 
);

CREATE OR REPLACE VIEW WF_VW_MISSION_DETAILS
("INSTANCE_ID", "EMPLOYEE_ID", "EMPLOYEE_NAME", "EMPLOYEE_GRADE_CODE", "EMPLOYEE_GRADE_NAME", "EMPLOYEE_POSITION_CODE", "EMPLOYEE_POSITION_TITLE_NAME",
"EMPLOYEE_PHYSICAL_UNIT_FULL_NAME", "EMPLOYEE_PHYSICAL_UNIT_HKEY", "EMPLOYEE_PHYSICAL_REGION_CODE", "EMPLOYEE_HIRING_HIJRI_DATE",
"BALANCE", "PERIOD", "ROAD_PERIOD", "START_HIJRI_DATE", "END_HIJRI_DATE", "EXCEPTIONAL_APPROVAL_NUMBER", "EXCEPTIONAL_APPROVAL_HIJRI_DATE",
"ROAD_LINE", "REMARKS"
) AS
SELECT D.INSTANCE_ID, D.EMPLOYEE_ID, E.NAME, E.GRADE_CODE, E.GRADE_NAME, E.POSITION_CODE, E.POSITION_TITLE_NAME,
E.PHYSICAL_UNIT_FULL_NAME, E.PHYSICAL_UNIT_HKEY, E.PHYSICAL_REGION_CODE, E.HIRING_HIJRI_DATE,
D.BALANCE, D.PERIOD, D.ROAD_PERIOD, D.START_HIJRI_DATE, D.END_HIJRI_DATE, D.EXCEPTIONAL_APPROVAL_NUMBER, D.EXCEPTIONAL_APPROVAL_HIJRI_DATE,
D.ROAD_LINE, D.REMARKS
FROM WF_MISSION_DETAILS D, HCM_EMPLOYEES E
WHERE D.EMPLOYEE_ID = E.ID;

------------------------------------------------------------------------------------------------
CREATE SEQUENCE HCM_MSN_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;

