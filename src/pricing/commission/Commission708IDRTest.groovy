package com.kuoni.qa.automation.test.pricing.commission

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Commission708IDRTest extends HotelSearchBaseTest {

    static def desc = "Commission"
    static def dataNames =  ["commission.case8"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client708IDR")
    }

}
