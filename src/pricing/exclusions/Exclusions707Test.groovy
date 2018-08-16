package com.kuoni.qa.automation.test.pricing.exclusions

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Exclusions707Test extends HotelSearchBaseTest {

    static def desc = "Exclusions"
    static def dataNames = ["exclusions.case2", "exclusions.case4", "exclusions.case8"]
//    static def dataNames = ["exclusions.case4"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client707")
    }

}
