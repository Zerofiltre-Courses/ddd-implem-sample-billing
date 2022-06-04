package tech.zerofiltre.freeland.application;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.payment.*;
import tech.zerofiltre.freeland.domain.payment.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;

import java.text.*;
import java.time.*;

public class FreelancerPaymentScheduler {

    private final static float TAXES_RATE = 0.47f;
    private final PaymentProvider paymentProvider;
    private final ServiceContractProvider serviceContractProvider;

    public FreelancerPaymentScheduler(PaymentProvider paymentProvider, ServiceContractProvider serviceContractProvider) {
        this.paymentProvider = paymentProvider;
        this.serviceContractProvider = serviceContractProvider;
    }

    public Payment execute(ServiceContractId serviceContractId, FreelancerId freelancerId,
                           Rate serviceContractRate, float serviceFeesRate) throws ScheduleFreelancerPaymentException {

        ServiceContract.builder()
                .serviceContractProvider(serviceContractProvider)
                .build()
                .of(serviceContractId)
                .orElseThrow(() -> new ScheduleFreelancerPaymentException(
                        "The service contract of id: " + serviceContractId.getContractNumber() + "is either missing or terminated"));

        LocalDateTime now = LocalDateTime.now();
        return Payment.builder()
                .serviceContractId(serviceContractId)
                .freelancerId(freelancerId)
                .amount(getAmount(serviceContractRate, serviceFeesRate))
                .executionDate(now)
                .executionPeriod(now.minusMonths(1))
                .provider(paymentProvider)
                .build()
                .send();
    }


    private Amount getAmount(Rate serviceContractRate, float serviceFeesRate) {

        DecimalFormat df = new DecimalFormat("0.00");
        float value;
        switch (serviceContractRate.getFrequency()) {
            case DAILY:
                value = serviceContractRate.getValue() * 20;
                break;
            case MONTHLY:
                value = serviceContractRate.getValue();
                break;
            default:
                value = serviceContractRate.getValue() / 12;
        }

        return new Amount(Float.parseFloat(df.format(value * (1 - serviceFeesRate) * (1 - TAXES_RATE))),
                serviceContractRate.getCurrency());
    }


}
