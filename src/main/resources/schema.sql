CREATE TABLE credits
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        TEXT   NOT NULL,
    sum         BIGINT NOT NULL,
    createddate DATE,
    payday      INTEGER
);

CREATE TABLE payments
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    paysum   BIGINT,
    paydate  DATE,
    maindebt BIGINT,
    ismade   BOOLEAN,
    creditid BIGINT REFERENCES credits
);