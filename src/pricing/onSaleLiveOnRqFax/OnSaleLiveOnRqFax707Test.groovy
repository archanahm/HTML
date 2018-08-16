package com.kuoni.qa.automation.test.pricing.onSaleLiveOnRqFax

import com.kuoni.qa.automation.helper.ClientData
import com.kuoni.qa.automation.test.pricing.BasePricingSpec
import com.kuoni.qa.automation.test.pricing.HotelSearchBaseTest

class OnSaleLiveOnRqFax707Test extends HotelSearchBaseTest {

    static def desc = "OnSaleLiveOnRqFax"
    static def dataNames = ["onSaleLiveOnRqFax.OnSaleL01", "onSaleLiveOnRqFax.OnSaleL02", "onSaleLiveOnRqFax.OnSaleL03",
                            "onSaleLiveOnRqFax.OnSaleL04", "onSaleLiveOnRqFax.OnSaleL05", "onSaleLiveOnRqFax.OnSaleL06",
                            "onSaleLiveOnRqFax.OnSaleL07", "onSaleLiveOnRqFax.OnSaleL08", "onSaleLiveOnRqFax.OnSaleL10"
    ]
//    static def dataNames =  ["onSaleLiveOnRqFax.OnSaleL04"]
//    static def dataNames =  ["onSaleLiveOnRqFax.OnSaleL01", "onSaleLiveOnRqFax.OnSaleL02", "onSaleLiveOnRqFax.OnSaleL03",
//                             "onSaleLiveOnRqFax.OnSaleL04", "onSaleLiveOnRqFax.OnSaleL05"]
    static def testDesc = Collections.nCopies(dataNames.size(), desc)

    def void initValues() {
        BasePricingSpec.activeClientData = new ClientData("client707")
    }

}
