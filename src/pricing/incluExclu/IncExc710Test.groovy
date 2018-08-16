package com.kuoni.qa.automation.test.pricing.incluExclu

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class IncExc710Test extends HotelSearchBaseTest {

    static def desc = "InclusionsExclusions"
    static
    def dataNames = ["inclusionsExclusions.incExc04", "inclusionsExclusions.incExc07" ]

    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client710")
    }

}
