create table users
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    name       varchar(20),
    email      varchar(50),
    created_at timestamp(6),
    updated_at timestamp(6)
);

create table account
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    user_id    bigint      not null,
    type      varchar(10) not null,
    account_number    varchar(30) not null,
    balance    bigint      not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    index __index_user (user_id)
);

create table external_account
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    user_id    bigint      not null,
    bank     varchar(20) not null,
    account_number    varchar(30) not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    index __index_user (user_id)
);

create table transaction
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    account_id    bigint      not null,
    transaction_type      varchar(20) not null,
    amount    bigint      not null,
    counter_party_bank    varchar(20),
    counter_party_account_id   varchar(30),
    created_at timestamp(6),
    created_date timestamp(6),
    index __index_account (account_id)
);

create table payment
(
    id         bigint AUTO_INCREMENT PRIMARY KEY,
    user_id    bigint      not null,
    partner_id    bigint      not null,
    partner_pay_key    varchar(40) not null,
    product_items   varchar(1000) not null,
    total_pay_amount    bigint      not null,
    status    varchar(20) not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    index __index_partner_pay_key (partner_pay_key)
);