package tech.itpark.creditsmanager.service;

import org.junit.jupiter.api.Test;
import tech.itpark.creditsmanager.model.Payment;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreditServiceTest {

    CreditService creditService = new CreditService();

    @Test
    void getPaymentsSchedule() {
        Payment expectedPayment = new Payment(
                0,
                BigDecimal.valueOf(10210.41),
                Date.valueOf("2024-12-14"),
                BigDecimal.valueOf(10107.68),
                0
        );
        List<Payment> actualSchedule = creditService.getPaymentsSchedule(
                new BigDecimal("386371"),
                48,
                new BigDecimal("12.4"),
                LocalDate.of(2020, 11, 15),
                14,
                true
        );
        Payment actualPayment = actualSchedule.get(actualSchedule.size()-1);
        assertEquals(expectedPayment, actualPayment);

        expectedPayment = new Payment(
                0,
                BigDecimal.valueOf(23059.14),
                Date.valueOf("2033-09-14"),
                BigDecimal.valueOf(22880.36),
                0
        );
        actualSchedule = creditService.getPaymentsSchedule(
                new BigDecimal("2330000"),
                180,
                new BigDecimal("9.2"),
                LocalDate.of(2018, 9, 14),
                14,
                false
        );
        actualPayment = actualSchedule.get(actualSchedule.size()-1);
        assertEquals(expectedPayment, actualPayment);
    }

    @Test
    void getMonthPaymentByTerm() {
        BigDecimal expectedPayment = new BigDecimal("10250.68");
        BigDecimal actualPayment = creditService.getMonthPaymentByTerm(
                new BigDecimal("12.4"),
                new BigDecimal("386371"),
                48
        );
        assertEquals(expectedPayment, actualPayment);

        expectedPayment = new BigDecimal("23910.43");
        actualPayment = creditService.getMonthPaymentByTerm(
                new BigDecimal("9.2"),
                new BigDecimal("2330000"),
                180
        );
        assertEquals(expectedPayment, actualPayment);

        expectedPayment = new BigDecimal("16903.28");
        actualPayment = creditService.getMonthPaymentByTerm(
                new BigDecimal("8.8"),
                new BigDecimal("1900000"),
                238
        );
        assertEquals(expectedPayment, actualPayment);
    }

    @Test
    void getPercentOfPayment() {
        BigDecimal expectedPercent = new BigDecimal("3796.15");
        BigDecimal actualPercent = creditService.getPercentOfPayment(
                new BigDecimal("12.4"),
                new BigDecimal("386371"),
                LocalDate.of(2020, 11, 15),
                LocalDate.of(2020, 12, 14)
        );
        assertEquals(expectedPercent, actualPercent);

        expectedPercent = new BigDecimal("4062.97");
        actualPercent = creditService.getPercentOfPayment(
                new BigDecimal("12.4"),
                new BigDecimal("386371"),
                LocalDate.of(2020, 12, 14),
                LocalDate.of(2021, 1, 14)
        );
        assertEquals(expectedPercent, actualPercent);

        expectedPercent = new BigDecimal("3339.57");
        actualPercent = creditService.getPercentOfPayment(
                new BigDecimal("12.4"),
                new BigDecimal("327673.38"),
                LocalDate.of(2021, 9, 14),
                LocalDate.of(2021, 10, 14)
        );
        assertEquals(expectedPercent, actualPercent);

        expectedPercent = new BigDecimal("3231.46");
        actualPercent = creditService.getPercentOfPayment(
                new BigDecimal("12.4"),
                new BigDecimal("306838.12"),
                LocalDate.of(2021, 12, 14),
                LocalDate.of(2022, 1, 14)
        );
        assertEquals(expectedPercent, actualPercent);

        expectedPercent = new BigDecimal("13907.35");
        actualPercent = creditService.getPercentOfPayment(
                new BigDecimal("8.8"),
                new BigDecimal("1865869.24"),
                LocalDate.of(2020, 11, 30),
                LocalDate.of(2020, 12, 31)
        );
        assertEquals(expectedPercent, actualPercent);
    }

    @Test
    void getNextPayDate() {
        LocalDate expectedDate = LocalDate.of(2020, 1, 31);
        LocalDate actualDate = creditService.getNextPayDate(LocalDate.of(2019, 12, 1), 31);
        assertEquals(expectedDate, actualDate);

        expectedDate = LocalDate.of(2020, 1, 1);
        actualDate = creditService.getNextPayDate(LocalDate.of(2019, 12, 31), 1);
        assertEquals(expectedDate, actualDate);

        expectedDate = LocalDate.of(2020, 2, 29);
        actualDate = creditService.getNextPayDate(LocalDate.of(2020, 1, 31), 31);
        assertEquals(expectedDate, actualDate);

        expectedDate = LocalDate.of(2020, 12, 31);
        actualDate = creditService.getNextPayDate(LocalDate.of(2020, 11, 30), 31);
        assertEquals(expectedDate, actualDate);
    }
}