CREATE TABLE accounts (
    id VARCHAR NOT NULL PRIMARY KEY,
    balance INT NOT NULL,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    version INT NOT NULL DEFAULT 1
);

ALTER TABLE accounts ADD CONSTRAINT non_negative_balance CHECK (balance >= 0);

INSERT INTO accounts (id, balance) VALUES ('1', 100);
INSERT INTO accounts (id, balance) VALUES ('2', 100);
INSERT INTO accounts (id, balance) VALUES ('3', 0);
INSERT INTO accounts (id, balance, is_blocked) VALUES ('4', 100, true);
INSERT INTO accounts (id, balance, is_blocked) VALUES ('5', 100, true);
