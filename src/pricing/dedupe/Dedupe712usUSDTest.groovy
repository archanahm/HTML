package com.kuoni.qa.automation.test.pricing.dedupe

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Dedupe712usUSDTest extends HotelSearchBaseTest {

    static def desc = "Dedupe"
    static def dataNames = ["dedupe.dedup16", "dedupe.dedup19", "dedupe.dedup22"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712usUSD")
    }

}
