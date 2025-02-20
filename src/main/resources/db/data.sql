INSERT INTO users (id, name, email, created_at, updated_at) values (1, 'nayoung', 'test@gmail.com',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO account (id, user_id, type, balance, created_at, updated_at) values (1, 1, 'MAIN', 10000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account (id, user_id, type, balance, created_at, updated_at) values (2, 1, 'SAVINGS', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO transaction (id, account_id, transaction_type, amount, created_at, created_date) values (1, 1, 'WITHDRAW', 1000, CURRENT_TIMESTAMP(), current_date());