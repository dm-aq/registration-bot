--liquibase formatted sql
--changeset Dmitry Bychkov:0_init_schema dbms:postgresql
create table requests (
id bigint primary key,
user_id bigint,
full_name varchar(30),
phone varchar(20),
sex varchar(1),
room_type smallint,
dance_type varchar(20),
neighbors text
);

create sequence requests_pk_seq;

create table states(
user_id bigint primary key,
state smallint
);