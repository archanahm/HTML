package com.kuoni.qa.automation.test.pricing.offer

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class Offer708Test extends HotelSearchBaseTest {

    static def desc = "Offer"
    static def dataNames = ["offer.case1", "offer.case2"
                            ,"offer.case3", "offer.case4"
                            ,"offer.case5", "offer.case6"
                            ,"offer.case7" ,"offer.case8"
                            ,"offer.case9" ,"offer.case10"
                            ,"offer.case11"
            ]
//    static def dataNames =  ["offer.case2"]
//    static def dataNames =  ["offer.case3", "offer.case4","offer.case8", "offer.case7"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client708")
    }

}
