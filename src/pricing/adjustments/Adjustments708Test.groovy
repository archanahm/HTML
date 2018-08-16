package com.kuoni.qa.automation.test.pricing.adjustments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Adjustments708Test extends HotelSearchBaseTest {

    static def desc = "Adjustments"
    static def dataNames = ["adjustments.case6"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client708")
    }

}
