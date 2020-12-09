CREATE TABLE credits
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    name        TEXT   NOT NULL,
    sum         BIGINT NOT NULL,
    createddate DATE
);

CREATE TABLE payments
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    paysum   INTEGER,
    paydate  DATE,
    maindebt BIGINT,
    ismade   BOOLEAN,
    creditid INTEGER REFERENCES credits
);