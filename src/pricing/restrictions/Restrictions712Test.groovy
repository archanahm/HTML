package com.kuoni.qa.automation.test.pricing.restrictions

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Restrictions712Test extends HotelSearchBaseTest {

    static def desc = "Restrictions"
    static def dataNames = ["restrictions.case1", "restrictions.case2", "restrictions.case3", "restrictions.case4",
                            "restrictions.case5", "restrictions.case6", "restrictions.case7", "restrictions.case8",
                            "restrictions.case9", "restrictions.case10", "restrictions.case11"]
//    static def dataNames =  ["restrictions.case6", "restrictions.case7", "restrictions.case2"]
//    static def dataNames =  ["restrictions.case7"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
