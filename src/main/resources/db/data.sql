DELETE FROM transaction;
DELETE FROM account;
-- DELETE FROM users;
-- DELETE FROM external_account;

-- INSERT INTO users (id, name, email, created_at, updated_at) values (1, 'nayoung', 'test@gmail.com',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
-- INSERT INTO users (id, name, email, created_at, updated_at) values (2, 'hyun', 'hyun@gmail.com',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO account (id, user_id, account_number, type, balance, created_at, updated_at) values (1, 1, '1234-2345-3456','MAIN', 10000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account (id, user_id, account_number, type, balance, created_at, updated_at) values (2, 1, '4567-5678-6789','SAVINGS', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
-- INSERT INTO external_account (id, user_id, bank, account_number, created_at, updated_at) values (1, 1, 'HUMBANK','H-5678-6789',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO account (id, user_id, account_number, type, balance, created_at, updated_at) values (3, 2, '1234-2345-3450','MAIN', 10000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO account (id, user_id, account_number, type, balance, created_at, updated_at) values (4, 2, '4567-5678-6780','SAVINGS', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
-- INSERT INTO external_account (id, user_id, bank, account_number, created_at, updated_at) values (2, 2, 'HIKBANK','K-5678-12343',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
