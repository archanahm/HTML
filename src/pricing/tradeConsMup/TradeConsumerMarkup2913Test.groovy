package com.kuoni.qa.automation.test.pricing.tradeConsMup

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest
import spock.lang.Ignore

class TradeConsumerMarkup2913Test extends HotelSearchBaseTest {

    static def desc = "TradeConsumerMarkup"
    static def dataNames = ["tradeConsumerMarkup.TradeConsMup01", "tradeConsumerMarkup.TradeConsMup02", "tradeConsumerMarkup.TradeConsMup04A"]
//    static def dataNames =  ["tradeConsumerMarkup.TradeConsMup01"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client2913")
    }

}
