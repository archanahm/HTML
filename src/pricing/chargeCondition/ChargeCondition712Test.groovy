package com.kuoni.qa.automation.test.pricing.chargeCondition

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class ChargeCondition712Test extends HotelSearchBaseTest {

    static def desc = "chargeConditions"
    static def dataNames = ["chargeConditions.case1", "chargeConditions.case2"
                            ,"chargeConditions.case3", "chargeConditions.case4"
                            ,"chargeConditions.case5", "chargeConditions.case6",
                            "chargeConditions.case7", "chargeConditions.case8"]
//    static def dataNames =  ["chargeConditions.case7"]
//    static def dataNames =  ["chargeConditions.case4", "chargeConditions.case6"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
