CREATE TABLE credits
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        TEXT   NOT NULL,
    sum         BIGINT NOT NULL,
    createddate TEXT,
    payday      INTEGER,
    percent     INTEGER,
    months      INTEGER
);

CREATE TABLE payments
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    paysum   BIGINT,
    paydate  TEXT,
    maindebt BIGINT,
    creditid BIGINT REFERENCES credits
);