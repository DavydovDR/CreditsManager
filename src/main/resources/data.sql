INSERT INTO credits (id, name, sum, createddate, payday, percent, months) VALUES
(1, 'ipoteka', 240000000, '2018-01-01', 28, 920, 180),
(2, 'avto', 100000000, '2020-10-10', 20, 1420, 120);

INSERT INTO payments (id, paySum, payDate, mainDebt, creditId) VALUES
(1, 500000, '2020-01-01', 250000, 2),
(2, 500000, '2020-02-01', 250000, 2);