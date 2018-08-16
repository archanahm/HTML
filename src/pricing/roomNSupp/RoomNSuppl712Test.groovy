package com.kuoni.qa.automation.test.pricing.roomNSupp

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class RoomNSuppl712Test extends HotelSearchBaseTest {

    static def desc = "Room & Supplements"
    static def dataNames = ["roomAndSupplements.case1", "roomAndSupplements.case2"
                     ,"roomAndSupplements.case3", "roomAndSupplements.case4"
                     ,"roomAndSupplements.case5", "roomAndSupplements.case6"
                     ,"roomAndSupplements.case7", "roomAndSupplements.case8"
                     ,"roomAndSupplements.case9", "roomAndSupplements.case10"
 ]
//    static def dataNames =  ["roomAndSupplements.case3", "roomAndSupplements.case6", "roomAndSupplements.case10"]
//    static def dataNames =  ["roomAndSupplements.case2"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
