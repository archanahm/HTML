package com.kuoni.qa.automation.test.pricing.nationalityDedupe

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class NationalityDedupe711Test extends HotelSearchBaseTest {

    static def desc = "NationalityDedupe"
    static def dataNames = ["nationalityDedupe.NatDedup03"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client711")
    }

}
