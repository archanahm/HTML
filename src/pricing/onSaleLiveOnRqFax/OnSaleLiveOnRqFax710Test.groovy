package com.kuoni.qa.automation.test.pricing.onSaleLiveOnRqFax

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class OnSaleLiveOnRqFax710Test extends HotelSearchBaseTest {

    static def desc = "OnSaleLiveOnRqFax"
    static def dataNames = ["onSaleLiveOnRqFax.OnSaleL13", "onSaleLiveOnRqFax.OnSaleL14"]
//    static def dataNames = ["onSaleLiveOnRqFax.OnSaleL14"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client710")
    }

}
