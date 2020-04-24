--liquibase formatted sql
--changeset Dmitry Bychkov:1_time_zone dbms:postgresql
alter table requests
    alter column insstmp type timestamptz;
alter table requests
    alter column updstmp type timestamptz;