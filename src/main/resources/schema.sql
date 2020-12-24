CREATE TABLE deposits
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    name    TEXT           NOT NULL UNIQUE,
    amount  DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
    percent DECIMAL(10, 2) NOT NULL CHECK (amount >= 0)
);

CREATE TABLE credits
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    name          TEXT           NOT NULL UNIQUE,
    initial_debt  DECIMAL(10, 2) NOT NULL CHECK (initial_debt >= 0),
    debt          DECIMAL(10, 2) NOT NULL CHECK (debt >= 0),
    created_date  DATE           NOT NULL,
    pay_day       INT            NOT NULL CHECK (pay_day > 0, pay_day < 32),
    percent       DECIMAL(10, 2) NOT NULL CHECK (percent >= 0),
    initial_term  INTEGER        NOT NULL CHECK (initial_term > 0),
    term          INTEGER        NOT NULL CHECK (term > 0),
    first_payment BOOLEAN        NOT NULL
);

CREATE TABLE payments
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    amount    DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    pay_date  DATE           NOT NULL,
    main_debt DECIMAL(10, 2) NOT NULL CHECK (main_debt >= 0, main_debt <= amount),
    credit_id INT REFERENCES credits (id)
);