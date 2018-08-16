package com.kuoni.qa.automation.helper

/**
 * Created by Joseph Sebastian on 21/10/2015.
 */
class CoherenceResponse {
    Map priceMap = [:]
    Map commissionMap = [:]
    Map cancellationMap = [:]
    Map roomPricePerDay = [:]

    CoherenceResponse(priceMap, commissionMap, cancellationMap) {
        this.priceMap = priceMap
        this.commissionMap = commissionMap
        this.cancellationMap = cancellationMap
    }
    CoherenceResponse(priceMap, commissionMap, cancellationMap, roomPricePerday) {
        this.priceMap = priceMap
        this.commissionMap = commissionMap
        this.cancellationMap = cancellationMap
        this.roomPricePerDay = roomPricePerday
    }
}
