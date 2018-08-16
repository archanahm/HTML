package com.kuoni.qa.automation.test.pricing.tradeConsMup

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class TradeConsumerMarkup2912Test extends HotelSearchBaseTest {

    static def desc = "TradeConsumerMarkup"
    static def dataNames = ["tradeConsumerMarkup.TradeConsMup06A"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client2912")
    }

}
