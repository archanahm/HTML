package com.kuoni.qa.automation.test.pricing.commission

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Commission713ESTest extends HotelSearchBaseTest {

    static def desc = "Commission"
    static def dataNames =  ["commission.case9"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client713ES")
    }

}
