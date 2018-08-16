package com.kuoni.qa.automation.test.pricing.minNtPxPeakFullStay

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class MinNtPxPeakFullStay712Test extends HotelSearchBaseTest {

    static def desc = "Min Night min Px Peak rates and FullStay"
    static def dataNames = ["minNtPxPeakFullStay.case1", "minNtPxPeakFullStay.case2"
                            , "minNtPxPeakFullStay.case3", "minNtPxPeakFullStay.case4", "minNtPxPeakFullStay.case5"
                            , "minNtPxPeakFullStay.case6", "minNtPxPeakFullStay.case7"
    ]
//    static def dataNames =  ["minNtPxPeakFullStay.case5"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client712")
    }

}
