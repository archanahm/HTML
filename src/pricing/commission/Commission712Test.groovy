package com.kuoni.qa.automation.test.pricing.commission

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Commission712Test extends HotelSearchBaseTest {

    static def desc = "Commission"
//    static def dataNames = ["commission.case1", "commission.case2"
//                            ,"commission.case3", "commission.case4"
//                            ,"commission.case5", "commission.case6"
//            ]
    static def dataNames =  ["commission.case6"]
//    static def dataNames =  ["commission.case1", "commission.case2"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
