package com.kuoni.qa.automation.test.pricing.onSaleLiveOnRqFax

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class OnSaleLiveOnRqFax713Test extends HotelSearchBaseTest {

    static def desc = "OnSaleLiveOnRqFax"
    static def dataNames = ["onSaleLiveOnRqFax.OnSaleL11", "onSaleLiveOnRqFax.OnSaleL12"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client713")
    }

}
