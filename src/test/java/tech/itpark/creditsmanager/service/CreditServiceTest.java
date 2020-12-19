package tech.itpark.creditsmanager.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditServiceTest {

    @Test
    void getFirstPaydate() {

        final var creditService = new CreditService();

        final var expectedFirstPayDate = LocalDate.of(2020, 2, 10);
        final var actuaFirstPayDate = creditService.getFirstPayDate("2020-01-01", 10);
        assertEquals(expectedFirstPayDate, actuaFirstPayDate);

        final var expectedMonthPayment = 104641;
        final var actualMonthPayment = creditService.getMonthPaymentByTerm(100, 10_000_00, 10);
        assertEquals(expectedMonthPayment, actualMonthPayment);

        final var expectedPercentPayment = 8470;
        final var actualPercentPayment = creditService.getPercentPayment(100, 10_000_00, "2020-01-10", "2020-02-10");
        assertEquals(expectedPercentPayment, actualPercentPayment);

    }




}