package tech.itpark.creditsmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.itpark.creditsmanager.model.Payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreditService {

    public List<Payment> getPaymentsSchedule(BigDecimal debt, int months, BigDecimal yearPercent, LocalDate createdDate, int payDay, boolean withoutFirstPayment) {

        var result = new ArrayList<Payment>();
        var mainDebt = debt;
        var firstPayment = new Payment();
        BigDecimal percent;

        BigDecimal monthPayment = getMonthPaymentByTerm(yearPercent, debt, months);

        firstPayment.setPayDate(
                Date.valueOf(
                        getNextPayDate(createdDate, payDay)
                )
        );

        percent = getPercentOfPayment(yearPercent,
                mainDebt,
                createdDate,
                getNextPayDate(createdDate, payDay)
        );

        if (withoutFirstPayment) {
//            firstPayment.setPayDate(
//                    Date.valueOf(
//                        firstPayment.getPayDate().toLocalDate().
//                            plusMonths(1))
//            );
            firstPayment.setAmount(percent);
            firstPayment.setMainDebt(BigDecimal.ZERO);
        }
        if (!withoutFirstPayment) {
            firstPayment.setAmount(monthPayment);
            firstPayment.setMainDebt(monthPayment.subtract(percent));
            mainDebt = mainDebt.subtract(firstPayment.getMainDebt());
        }
        result.add(firstPayment);

        for (int i = 1; i <= months; i++) {
            var payment = new Payment();
            var previousDate = result.get(i - 1).
                    getPayDate().
                    toLocalDate();
            payment.setPayDate(
                    Date.valueOf(
                            getNextPayDate(previousDate, payDay)
                    ));

            percent = getPercentOfPayment(
                    yearPercent,
                    mainDebt,
                    result.get(i - 1).getPayDate().toLocalDate(),
                    payment.getPayDate().toLocalDate()
            );

            if (monthPayment.subtract(percent).compareTo(mainDebt) >= 0) {
                payment.setMainDebt(mainDebt);
                payment.setAmount(mainDebt.add(percent));
                result.add(payment);
                return result;
            }

            payment.setMainDebt(monthPayment.subtract(percent));
            payment.setAmount(monthPayment);
//            payment.setId(0);
            result.add(payment);
            mainDebt = mainDebt.subtract(payment.getMainDebt());
        }
        return result;
    }

    public BigDecimal getMonthPaymentByTerm(BigDecimal yearPercent, BigDecimal debt, int months) {
        BigDecimal result;

        BigDecimal monthPercent = yearPercent.divide(new BigDecimal(12), 7, RoundingMode.DOWN);
        monthPercent = monthPercent.divide(BigDecimal.valueOf(100), 9, RoundingMode.HALF_UP);

        BigDecimal temp = monthPercent.add(BigDecimal.ONE);
        double dtemp = temp.doubleValue();
        dtemp = Math.pow(dtemp, months);
        temp = BigDecimal.valueOf(dtemp);

        BigDecimal annuityCoef = monthPercent.multiply(temp);
        annuityCoef = annuityCoef.divide(temp.subtract(BigDecimal.ONE), RoundingMode.DOWN);

        result = annuityCoef.multiply(debt);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercentOfPayment(BigDecimal yearPercent, BigDecimal debt, LocalDate previousDate, LocalDate nextDate) {
        BigDecimal result;
        var previousDateYear = previousDate.getYear();
        var nextDateYear = nextDate.getYear();
        if (previousDateYear != nextDateYear) {
            BigDecimal result1 = getPercentOfPayment(
                    yearPercent,
                    debt,
                    previousDate,
                    LocalDate.of(previousDateYear, 12, 31)
            );
            BigDecimal result2 = getPercentOfPayment(
                    yearPercent,
                    debt,
                    LocalDate.of(nextDateYear, 1, 1), nextDate.plusDays(1)
            );
            return result1.add(result2);
        }
        long per = ChronoUnit.DAYS.between(previousDate, nextDate);
        BigDecimal period = new BigDecimal(per);
        BigDecimal lengthOfYear = new BigDecimal(nextDate.lengthOfYear());

        result = debt;
        result = result.multiply(yearPercent);
        result = result.multiply(period);
        result = result.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_DOWN);
        result = result.divide(lengthOfYear, 3, RoundingMode.HALF_DOWN);

        return result
                .setScale(2, RoundingMode.HALF_DOWN);
    }

    public LocalDate getNextPayDate(LocalDate previousPaymentDate, int payDay) {
        LocalDate result = previousPaymentDate.plusMonths(1);
        if (payDay == 31) {
            return result.withDayOfMonth(result.lengthOfMonth());
        }
        try {
            return result.withDayOfMonth(payDay);
        } catch (DateTimeException e) {
            return result.withDayOfMonth(result.lengthOfMonth());
        }
    }
}
