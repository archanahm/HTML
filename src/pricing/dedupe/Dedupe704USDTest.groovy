package com.kuoni.qa.automation.test.pricing.dedupe

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Dedupe704USDTest extends HotelSearchBaseTest {

    static def desc = "Dedupe"
    static def dataNames = ["dedupe.dedup01", "dedupe.dedup02", "dedupe.dedup03", "dedupe.dedup04", "dedupe.dedup05"
                            , "dedupe.dedup06", "dedupe.dedup07", "dedupe.dedup08", "dedupe.dedup09", "dedupe.dedup10"
                            , "dedupe.dedup11", "dedupe.dedup12", "dedupe.dedup13", "dedupe.dedup23", "dedupe.dedup24"
                            , "dedupe.dedup25" , "dedupe.dedup26", "dedupe.dedup27", "dedupe.dedup28", "dedupe.dedup29"
                            , "dedupe.dedup30", "dedupe.dedup31"]
//    static def dataNames = ["dedupe.dedup23", "dedupe.dedup24", "dedupe.dedup25"
//                            , "dedupe.dedup26", "dedupe.dedup27", "dedupe.dedup28", "dedupe.dedup29", "dedupe.dedup30", "dedupe.dedup31"]
//    static def dataNames = ["dedupe.dedup31"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client704USD")
    }

}
