create schema if not exists public;

create table if not exists dq_rule
(
    id serial
        constraint dq_rule_pk
            primary key
);

create table if not exists log
(
    date  timestamp,
    count numeric
);
