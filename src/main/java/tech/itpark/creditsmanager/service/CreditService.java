package tech.itpark.creditsmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.itpark.creditsmanager.manager.CreditManager;
import tech.itpark.creditsmanager.manager.PaymentManager;
import tech.itpark.creditsmanager.model.Credit;
import tech.itpark.creditsmanager.model.Payment;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditManager creditManager;
    private final PaymentManager paymentManager;
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public long getMonthPaymentByTerm(long creditId, int months) {
        Credit credit = creditManager.getById(creditId);
        double monthPercent = credit.getPercent()/12000;
        double annuityCoef = monthPercent / (1 -(1 + 1/Math.pow(monthPercent, months)));
        return (long) Math.ceil(credit.getSum() * annuityCoef);
    }

    public LocalDate getFirstPayDate(long creditId) {
        Credit credit = creditManager.getById(creditId);
        LocalDate createdDate = LocalDate.parse(credit.getCreatedDate(), dtf);
        return LocalDate.of(createdDate.getYear(),
                createdDate.getMonthValue() + 1,
                credit.getPayDay());
    }

    public List<Payment> getPaymentsByTerm(long creditId, int months) {
        List<Payment> result = new ArrayList<>();

        Credit credit = creditManager.getById(creditId);
        long monthPayment = getMonthPaymentByTerm(creditId, months);
        long mainDebt = credit.getSum();
        long percent;

        Payment firstPayment = new Payment();
        firstPayment.setCreditId(creditId);
        firstPayment.setPayDate((getFirstPayDate(creditId).format(dtf)));
        firstPayment.setPaySum(monthPayment);
        percent = getPercentPayment(credit, mainDebt, credit.getCreatedDate(), getFirstPayDate(creditId).format(dtf));
        firstPayment.setMainDebt(monthPayment - percent);

        mainDebt -= firstPayment.getMainDebt();
        result.add(firstPayment);

        for (int i = 1; i < months-1; i++) {
            LocalDate previousDate = LocalDate.parse(result.get(i-1).getPayDate(), dtf);

            Payment payment = new Payment();
            payment.setCreditId(creditId);

            if (previousDate.getMonthValue() == 12) {
                payment.setPayDate(LocalDate.of(
                        previousDate.getYear()+1,
                        1,
                        previousDate.getDayOfMonth()).
                        format(dtf));
            }

            payment.setPayDate(LocalDate.of(
                    previousDate.getYear(),
                    previousDate.getMonthValue() + 1,
                    previousDate.getDayOfMonth()).
                    format(dtf));
            percent = getPercentPayment(credit, mainDebt, result.get(i-1).getPayDate(), payment.getPayDate());
            payment.setMainDebt(mainDebt);
            payment.setPaySum(mainDebt + percent);
        }
        result.stream().forEach(paymentManager::save);
        return result;
    }

    private long getPercentPayment(Credit credit, long debt, String previousDateStr, String nextDateStr) {
        LocalDate previousDate = LocalDate.parse(previousDateStr, dtf);
        LocalDate nextDate = LocalDate.parse(nextDateStr, dtf);

        if (previousDate.getYear() != nextDate.getYear()) {
            return getPercentPayment(credit, debt, previousDateStr, previousDate.getYear() + "12" + "31") +
                    getPercentPayment(credit, debt, nextDate.getYear() + "01" + "01", nextDateStr);
        }

        return (long) (debt * credit.getPercent() *
                previousDate.until(nextDate, ChronoUnit.DAYS) /
                Year.of(previousDate.getYear()).length() / 10000);
    }

}
