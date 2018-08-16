package com.kuoni.qa.automation.test.pricing.adjustments

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Adjustments713Test extends HotelSearchBaseTest {

    static def desc = "Adjustments"
    static def dataNames = ["adjustments.case1", "adjustments.case2", "adjustments.case3", "adjustments.case4",
                            "adjustments.case5", "adjustments.case6", "adjustments.case7", "adjustments.case8"]
//    static def dataNames =  ["adjustments.case1"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client713")
    }

}
