DELETE FROM transaction;
DELETE FROM account;
DELETE FROM users;

INSERT INTO users (id, name, email, created_at, updated_at) values (1, 'nayoung', 'test@gmail.com',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO users (id, name, email, created_at, updated_at) values (2, 'hyun', 'hyun@gmail.com',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO account (id, user_id, type, balance, created_at, updated_at) values (1, 1, 'MAIN', 10000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account (id, user_id, type, balance, created_at, updated_at) values (2, 1, 'SAVINGS', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO account (id, user_id, type, balance, created_at, updated_at) values (3, 2, 'MAIN', 10000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account (id, user_id, type, balance, created_at, updated_at) values (4, 2, 'SAVINGS', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());