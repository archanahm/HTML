package com.kuoni.qa.automation.test.pricing.priceBreakdown

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class PriceBreakdown712Test extends HotelSearchBaseTest {

    static def desc = "PriceBreakdown"
    static def dataNames = ["priceBreakdown.pricebreak01", "priceBreakdown.pricebreak02"
                            ,"priceBreakdown.pricebreak03", "priceBreakdown.pricebreak04"
            ]
//    static def dataNames =  ["priceBreakdown.pricebreak02"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712USD")
    }

}
