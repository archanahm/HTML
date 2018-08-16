package com.kuoni.qa.automation.test.pricing.exclusions

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Exclusions704Test extends HotelSearchBaseTest {

    static def desc = "Exclusions"
    static def dataNames = ["exclusions.case13", "exclusions.case14"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client704USD")
    }

}
