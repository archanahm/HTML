package com.kuoni.qa.automation.test.pricing.exclusions

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Exclusions712Test extends HotelSearchBaseTest {

    static def desc = "Exclusions"
    static def dataNames = ["exclusions.case1", "exclusions.case6", "exclusions.case7",
                            "exclusions.case9", "exclusions.case10", "exclusions.case12"]
//    static def dataNames = ["exclusions.case7" ,"exclusions.case10", "exclusions.case12"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
