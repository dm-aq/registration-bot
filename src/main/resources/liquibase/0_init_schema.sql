--liquibase formatted sql
--changeset Dmitry Bychkov:0_init_schema dbms:postgresql
create table requests (
id bigint primary key,
user_id bigint,
telegram_login varchar(100),
full_name varchar(30),
email varchar(50),
phone varchar(20),
gender varchar(1),
room_type smallint,
dance_type varchar(20),
neighbors text,
state varchar(20),
insstmp timestamp not null,
updstmp timestamp not null
);

create sequence requests_pk_seq;