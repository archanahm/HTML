package com.kuoni.qa.automation.test.pricing.tax

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Tax712Test extends HotelSearchBaseTest {

    static def desc = "Tax"
    static def dataNames = ["tax.case1", "tax.case2"]
//    static def dataNames = ["tax.case2"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
