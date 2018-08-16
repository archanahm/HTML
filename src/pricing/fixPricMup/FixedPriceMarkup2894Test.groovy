package com.kuoni.qa.automation.test.pricing.fixPricMup

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class FixedPriceMarkup2894Test extends HotelSearchBaseTest {

    static def desc = "Fixed Price Markup"
    static def dataNames =  ["fixedPriceMarkup.case1", "fixedPriceMarkup.case2"]
//    static def dataNames =  ["fixedPriceMarkup.case2"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client2894")
    }

}
