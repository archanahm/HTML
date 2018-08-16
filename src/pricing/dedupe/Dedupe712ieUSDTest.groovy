package com.kuoni.qa.automation.test.pricing.dedupe

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Dedupe712ieUSDTest extends HotelSearchBaseTest {

    static def desc = "Dedupe"
    static def dataNames = ["dedupe.dedup15", "dedupe.dedup18", "dedupe.dedup21"]
//    static def dataNames = ["dedupe.dedup21"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712ieUSD")
    }

}
