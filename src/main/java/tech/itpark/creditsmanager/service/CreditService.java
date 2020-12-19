package tech.itpark.creditsmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.itpark.creditsmanager.model.Payment;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreditService {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Payment> getPaymentsSchedule(long sum, int months, int yearPercent, String createdDate, int payDay) {

        List<Payment> result = new ArrayList<>();

        long monthPayment = getMonthPaymentByTerm(yearPercent, sum, months);
        var mainDebt = sum;
        long percent;

        var firstPayment = new Payment();
        firstPayment.setPayDate((getFirstPayDate(createdDate, payDay).format(dtf)));
        firstPayment.setPaySum(monthPayment);
        percent = getPercentPayment(yearPercent, mainDebt, createdDate, getFirstPayDate(createdDate, payDay).format(dtf));
        firstPayment.setMainDebt(monthPayment - percent);

        mainDebt -= firstPayment.getMainDebt();
        result.add(firstPayment);

        for (int i = 1; i < months; i++) {
            var payment = new Payment();
            var previousDate = LocalDate.parse(result.get(i - 1).getPayDate(), dtf);

            if (previousDate.getMonthValue() == 12) {
                payment.setPayDate(LocalDate.of(
                        previousDate.getYear() + 1,
                        1,
                        previousDate.getDayOfMonth()).
                        format(dtf));
            }
            if (previousDate.getMonthValue() != 12) {

                payment.setPayDate(LocalDate.of(
                        previousDate.getYear(),
                        previousDate.getMonthValue() + 1,
                        previousDate.getDayOfMonth()).
                        format(dtf));
            }

            percent = getPercentPayment(yearPercent, mainDebt, result.get(i - 1).getPayDate(), payment.getPayDate());
            if (mainDebt < monthPayment - percent) {
                payment.setMainDebt(mainDebt);
                payment.setPaySum(mainDebt + percent);
                result.add(payment);
                return result;
            }

            payment.setMainDebt(monthPayment - percent);
            payment.setPaySum(payment.getMainDebt() + percent);
            mainDebt -= payment.getMainDebt();
            result.add(payment);
        }
        return result;
    }

    public long getMonthPaymentByTerm(int yearPercent, long sum, int months) {
        double monthPercent = yearPercent * 1.0 / 12000;
        double temp = Math.pow((1 + monthPercent), months);
        double annuityCoef = monthPercent * temp / (temp - 1);
        return (long) Math.ceil(annuityCoef * sum);
    }

    public LocalDate getFirstPayDate(String createdDate, int payDay) {
        LocalDate ldCreatedDate = LocalDate.parse(createdDate, dtf);
        return LocalDate.of(ldCreatedDate.getYear(),
                ldCreatedDate.getMonthValue() + 1,
                payDay);
    }

    public long getPercentPayment(int yearPercent, long debt, String previousDateStr, String nextDateStr) {
        LocalDate previousDate = LocalDate.parse(previousDateStr, dtf);
        LocalDate nextDate = LocalDate.parse(nextDateStr, dtf);

        if (previousDate.getYear() != nextDate.getYear()) {
            return getPercentPayment(yearPercent, debt, previousDateStr, previousDate.getYear() + "-12" + "-31") +
                    getPercentPayment(yearPercent, debt, nextDate.getYear() + "-01" + "-01", nextDateStr);
        }

        return (long) Math.ceil(1.0 * debt * yearPercent *
                previousDate.until(nextDate, ChronoUnit.DAYS) /
                Year.of(previousDate.getYear()).length()/ 1000);
    }
}
