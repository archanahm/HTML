package com.kuoni.qa.automation.helper

/**
 * Created by Joseph Sebastian on 04/11/2015.
 */
class CancellationConditionCoh {
    Date startDate
    Date endDate
    BigDecimal chargeAmount
    String type
    Integer duration

    CancellationConditionCoh(Date startDate, Date endDate, BigDecimal chargeAmount, String type, Integer duration) {
        this.startDate = startDate
        this.endDate = endDate
        this.chargeAmount = chargeAmount
        this.type = type
        this.duration = duration
    }


    @Override
    public String toString() {
        return "CancellationConditionCoh{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", chargeAmount=" + chargeAmount +
                ", duration=" + duration +
                ", type='" + type + '\'' +
                '}';
    }
}
