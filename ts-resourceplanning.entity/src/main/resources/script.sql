create sequence hibernate_sequence;

create sequence session_manager_sequence;

------------------------------------------------------------------------------------------------
--------- Entites
------------------------------------------------------------------------------------------------

------------------------------------------------ AppConfiguration 
create sequence app_configuration_id_seq;

create table app_configuration
(
	id bigint not null default nextval('app_configuration_id_seq'),
	key varchar(255) not null unique,
	value varchar(255) not null,
	primary key (id)
);

-- initial values
insert into app_configuration (key, value) values ('version', '0.0.1-SNAPSHOT');
insert into app_configuration (key, value) values ('host', 'http://localhost:8080');
insert into app_configuration (key, value) values ('smtp_user', 'smtp_user');
insert into app_configuration (key, value) values ('smtp_passwd', 'smtp_passwd');
insert into app_configuration (key, value) values ('smtp_host', 'smtp_host');
insert into app_configuration (key, value) values ('smtp_port', 'smtp_port');
insert into app_configuration (key, value) values ('admin_mail', 'stefan.schulz.1976@googlemail.com');
insert into app_configuration (key, value) values ('messagesender', 'de.trispeedys.resourceplanning.messaging.FileDumpMessageSender');
insert into app_configuration (key, value) values ('dumpMessagesPath', 'C:\tmp\messages');

------------------------------------------------ Domain

create sequence domain_id_seq;
 
create table domain
(
	id bigint not null default nextval('domain_id_seq'),
	name varchar(255) not null,
	primary key (id)
); 

------------------------------------------------ Position

create sequence position_id_seq;
 
create table position
(
	id bigint not null default nextval('position_id_seq'),
	name varchar(255) not null,
	pos_key varchar(8) not null unique,
	required_helper_count int,
	domain_id bigint not null,
	minimal_age integer not null,
	hour_of_start integer null,
	hour_of_end integer null,
	event_day integer,
	primary key (id)
); 

-- fk
alter table position add constraint fk_position_domain foreign key (domain_id) references domain (id);

------------------------------------------------ Event

create sequence event_id_seq;
 
create table event
(
	id bigint not null default nextval('event_id_seq'),
	description varchar(255) not null,
	parent_event_id bigint null,
	event_state varchar(64) not null,
	primary key (id)
);

-- fk
alter table event add constraint fk_event_parent foreign key (parent_event_id) references event (id);

------------------------------------------------ EventDay

create sequence event_day_id_seq;
 
create table event_day
(
	id bigint not null default nextval('event_day_id_seq'),
	planned_date date,
	event_id bigint not null,
	index integer,
	primary key (id)
);

-- fk
alter table event_day add constraint fk_event_day_event foreign key (event_id) references event (id);

------------------------------------------------ EventPosition

create sequence event_position_id_seq;

create table event_position
(
id bigint not null default nextval('event_position_id_seq'),
	event_id bigint not null,
	position_id bigint not null,
	event_day_id bigint not null,
	hour_of_start integer not null,
	hour_of_end integer not null,
	primary key (id)
);

-- fk
alter table event_position add constraint fk_event_pos_event foreign key (event_id) references event (id);
alter table event_position add constraint fk_event_pos_position foreign key (position_id) references position (id);
alter table event_position add constraint fk_event_pos_event_day foreign key (event_day_id) references event_day (id);

------------------------------------------------ Helper

create sequence helper_id_seq;
 
create table helper
(
	id bigint not null default nextval('helper_id_seq'),
	first_name varchar(255) not null,
	last_name varchar(255) not null,
	date_of_birth date,
	helper_state varchar(64) not null,
	supervisor_id bigint,
	email varchar(255),
	code varchar(255) not null unique,
	primary key (id)
);

-- fk
alter table helper add constraint fk_helper_supervisor foreign key (supervisor_id) references helper (id);

------------------------------------------------ Assignment

create sequence assignment_id_seq;
 
create table assignment
(
	id bigint not null default nextval('assignment_id_seq'),
	helper_id bigint not null,
	event_position_id bigint not null,
	assignment_state varchar(64) not null,
	primary key (id)
); 

-- fk
alter table assignment add constraint fk_assignment_helper foreign key (helper_id) references helper (id);
alter table assignment add constraint fk_assignment_event_pos foreign key (event_position_id) references event_position (id);

------------------------------------------------ MessageQueueItem

create sequence message_queue_item_id_seq;
 
create table message_queue_item
(
	id bigint not null default nextval('message_queue_item_id_seq'),
	message_queue_type varchar(64) not null,
	to_address varchar(255) not null,
	subject varchar(1024) not null,
	body text not null,
	messaging_state varchar(64) not null,
	helper_id bigint not null,
	primary key (id)
);

-- fk
alter table message_queue_item add constraint fk_message_helper foreign key (helper_id) references helper (id);