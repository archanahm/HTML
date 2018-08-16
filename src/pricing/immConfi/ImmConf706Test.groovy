package com.kuoni.qa.automation.test.pricing.immConfi

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class ImmConf706Test extends HotelSearchBaseTest {

    static def desc = "ImmediateConfirmation"
    static def dataNames = ["immediateConfirmation.immedConf01","immediateConfirmation.immedConf02"]
//    static def dataNames =  ["immediateConfirmation.immedConf01"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client706")
    }

}
