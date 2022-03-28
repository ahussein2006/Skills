create table if not exists Ingredient (
  id identity,
  code varchar(4) not null,
  name varchar(25) not null,
  type varchar(10) not null
);

create table if not exists Taco (
  id identity,
  name varchar(50) not null,
  insertedAt timestamp not null,
  version bigint not null,
  UNIQUE KEY name (name)
);

create table if not exists Taco_Ingredients (
  taco bigint not null,
  ingredient bigint not null
);

alter table Taco_Ingredients
    add foreign key (taco) references Taco(id);
alter table Taco_Ingredients
    add foreign key (ingredient) references Ingredient(id);

create table if not exists Taco_Order (
	id identity,
	name varchar(50) not null,
	street varchar(50) not null,
	city varchar(50) not null,
	state varchar(2) not null,
	zip varchar(10) not null,
    placedAt timestamp not null
);

create table if not exists Taco_Order_Tacos (
	tacoOrder bigint not null,
	taco bigint not null
);

alter table Taco_Order_Tacos
    add foreign key (tacoOrder) references Taco_Order(id);
alter table Taco_Order_Tacos
    add foreign key (taco) references Taco(id);
    
create sequence hibernate_sequence;

create table if not exists UM_AUDIT_LOGS (
  ID identity,
  MODULE_ID bigint,
  USER_ID bigint,
  SYSTEM_NAME varchar(50),
  OPERATION varchar(50),
  OPERATION_GREG_DATE timestamp,
  CONTENT_ENTITY varchar(200),
  CONTENT_ID bigint,
  CONTENT varchar(1000)
);

create sequence UM_AUDIT_SEQ;
