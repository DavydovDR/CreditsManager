CREATE TABLE credits
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL,
    sum BIGINT NOT NULL,
    opendate DATE,
    paymentday INTEGER
);

CREATE TABLE payments
(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    paysum INTEGER,
    paydate DATE,
    ismade BOOLEAN,
    creditid INTEGER REFERENCES credits
);