package com.kuoni.qa.automation.test.pricing.fixPricMup

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class FixedPriceMarkup2895Test extends HotelSearchBaseTest {

    static def desc = "Fixed Price Markup"
    static def dataNames =  ["fixedPriceMarkup.case3", "fixedPriceMarkup.case4",
                             "fixedPriceMarkup.case5", "fixedPriceMarkup.case6", "fixedPriceMarkup.case7"]
//    static def dataNames =  ["fixedPriceMarkup.case4"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client2895")
    }

}
