DROP TABLE SP_CONFIG;
drop sequence "PILOT"."SP_SEQ";

CREATE TABLE SP_CONFIG 
(
  ID NUMBER NOT NULL 
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

CREATE SEQUENCE SP_SEQ INCREMENT BY 1 MAXVALUE 9999999999999999999999999999 MINVALUE 1 CACHE 20;
