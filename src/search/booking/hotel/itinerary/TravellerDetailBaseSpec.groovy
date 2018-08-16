package com.kuoni.qa.automation.test.search.booking.hotel.itinerary

import com.kuoni.qa.automation.helper.HotelSearchData

/**
 * Created by kmahas on 6/27/2016.
 */
abstract class TravellerDetailBaseSpec extends ItineraryHotelItemBaseSpec{

    protected def addSaveAndVerifyAdultTravellers(int travellerNum, int maxAdultTravellers, HotelSearchData hotelSearchData){
        //Add Travellers
        for(int i=travellerNum;i<=maxAdultTravellers;i++){
            if (i >= 3) {
                scrollToTopOfThePage()
            }

            //scrollAndclickOnAddTravellerBtn(1)
            clickAddTravellersButton()
            sleep(5000)
            //Input Title
            selectTitle(hotelSearchData.expected.title_txt,0)
            sleep(3000)
            //Input First Name
            //String travellerFName=hotelSearchData.expected.travellerfirstName+i
            String travellerFName=hotelSearchData.expected.travellerfirstName
            enterEditedFirstNameAndTabOut(travellerFName)
            //Input Last Name
            //String travellerLName=hotelSearchData.expected.travellerlastName+i
            String travellerLName=hotelSearchData.expected.travellerlastName
            enterEditedLastNameAndTabOut(travellerLName)
            sleep(3000)

            //Click on Save Button
            clickonSaveButton()

            sleep(6000)
            waitTillLoadingIconDisappears()
            waitTillTravellerDetailsSaved()

            if(i>=5){
                if(i>=8){
                    scrollToBottomOfThePage()
                }
                //clickOnExpandTraveller()
            }

            //Capture the entered edit details
            //capture entered  traveller details are displayed correctly
            String expectedTravellerName=hotelSearchData.expected.title_txt+" "+travellerFName+" "+travellerLName
            //String actualTravellerName=getTravellerName(i+1)
            String actualTravellerName=getLeadTravellerName(i+1)
            //Validate  Traveller Name is Saved
            assertionEquals(actualTravellerName,expectedTravellerName, "Itinerary Page, $i th Traveller name details Actual & Expected don't match")

            sleep(2000)

        }

    }
    protected def addSavAdultTravellers(int travellerNum, int maxAdultTravellers, int totalTravellers,HotelSearchData hotelSearchData){
        //Add Travellers
        for(int i=travellerNum;i<=maxAdultTravellers;i++){
            if (i >= 3) {
                scrollToTopOfThePage()
            }

            //scrollAndclickOnAddTravellerBtn(1)
            //clickAddTravellersButton()
            sleep(5000)
            //Input Title
            selectTitle(hotelSearchData.expected.title_txt,0)
            //Input First Name
            //String travellerFName=hotelSearchData.expected.travellerfirstName+i
            String travellerFName=hotelSearchData.expected.travellerfirstName
            enterEditedFirstNameAndTabOut(travellerFName)
            //Input Last Name
            //String travellerLName=hotelSearchData.expected.travellerlastName+i
            String travellerLName=hotelSearchData.expected.travellerlastName
            enterEditedLastNameAndTabOut(travellerLName)
            sleep(3000)

            if(i==totalTravellers){
                //Click on Save Button
                clickonSaveButton()

                sleep(6000)
                waitTillLoadingIconDisappears()
                waitTillTravellerDetailsSaved()
            }

        }

    }


    protected def VerifyAdultTravellers(int travellerNum, int maxAdultTravellers, int totalTravellers,HotelSearchData hotelSearchData){
        //Add Travellers
        for(int i=travellerNum;i<=maxAdultTravellers;i++){
            if (i >= 3) {
                scrollToTopOfThePage()
            }


            if(i>=5){
                clickOnExpandTraveller()
            }


            //Capture the entered edit details
            //capture entered  traveller details are displayed correctly
            String expectedTravellerName=hotelSearchData.expected.title_txt+" "+travellerFName+" "+travellerLName
            String actualTravellerName=getTravellerName(i+1)
            //Validate  Traveller Name is Saved
            assertionEquals(actualTravellerName,expectedTravellerName, "Itinerary Page, $i th Traveller name details Actual & Expected don't match")

            sleep(2000)
        }

    }


    protected def addSaveAndVerifyChildTravellers(int travellerNum, int maxAdultTravellers, HotelSearchData hotelSearchData, def children) {
        int size=children.size()
        size = size - 1
        int minIndex = 0
        for (int i = travellerNum; i <= maxAdultTravellers; i++) {

            clickAddTravellersButton()
            sleep(5000)

            //Select radio button child
            ClickRadioButtonAdultOrChild(2)
            sleep(2000)

            //Input Title
            //selectTitle(hotelSearchData.expected.thirdTraveller_title_txt, 0)

            //Input First Name
            //String travellerChildFName = hotelSearchData.expected.childFirstName + i
            String travellerChildFName = hotelSearchData.expected.childFirstName
            enterEditedFirstNameAndTabOut(travellerChildFName)

            //Input Last Name
            //String travellerChildLName = hotelSearchData.expected.childLastName + i
            String travellerChildLName = hotelSearchData.expected.childLastName
            enterEditedLastNameAndTabOut(travellerChildLName)

            if(minIndex<=size){
            //Enter Age
            enterEditedChildAgeAndTabOut(children.getAt(minIndex).toString())

            sleep(3000)
            }

            //Click on Save Button
            clickonSaveButton()
            sleep(6000)
            waitTillLoadingIconDisappears()
            waitTillTravellerDetailsSaved()
            if (i >= travellerNum) {
                scrollToBottomOfThePage()
            }

            if (i >= 5) {
                if(i>=8){
                    scrollToBottomOfThePage()
                }
                //clickOnExpandTraveller()
            }
            if(minIndex<=size) {
                //Capture the entered edit details
                //capture entered third traveller details are displayed correctly
                //String expectedChildTravellerName = hotelSearchData.expected.thirdTraveller_title_txt + " " + travellerChildFName + " " + travellerChildLName + " " + "(" + children.getAt(minIndex).toString() + "yrs)"
                String expectedChildTravellerName = travellerChildFName + " " + travellerChildLName + " " + "(" + children.getAt(minIndex).toString() + "yrs)"
                //String actualChildTravellerName = getTravellerName(i + 1)
                String actualChildTravellerName = getLeadTravellerName(i + 1)
                //Validate Third Traveller Name is Saved
                assertionEquals(actualChildTravellerName, expectedChildTravellerName, "Itinerary Page, $i th  Traveller (child) name details Actual & Expected don't match")

                sleep(2000)
            }
            minIndex=minIndex+1

        }

    }

    protected def travellersListCheckBoxExistenceCheck(int travellerNum, int maxAdultTravellers, HotelSearchData hotelSearchData){
        //Add Travellers
        for(int i=travellerNum;i<=maxAdultTravellers;i++){

            boolean actualTravellerCheckBoxDisplayStatus=getTravellersCheckBoxDisplayStatus(i)
            //Validate  Traveller check box is displayed
            assertionEquals(actualTravellerCheckBoxDisplayStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen,  Occupancy tab, $i th Traveller check box display status Actual & Expected don't match")

            sleep(2000)
        }

    }

    protected def travellersListCheckBoxChkdOrUnchkdStatus(int travellerNum, int maxAdultTravellers, HotelSearchData hotelSearchData){
        //Add Travellers
        for(int i=travellerNum;i<=maxAdultTravellers;i++){

            boolean actualTravellerCheckBoxStatus=getTravellerCheckedStatus(i)
            //Validate  Traveller check box is displayed
            assertionEquals(actualTravellerCheckBoxStatus,hotelSearchData.expected.dispStatus, "Amend popup Screen,  Occupancy tab, $i th Traveller check box status Actual & Expected don't match")

            sleep(2000)
        }

    }

    protected def verifyAdultTravellers(int travellerNum, int maxAdultTravellers, HotelSearchData hotelSearchData){
        //Add Travellers
        for(int i=travellerNum;i<=maxAdultTravellers;i++){

            //Input First Name
            String travellerFName=hotelSearchData.expected.travellerfirstName

            //Input Last Name
            String travellerLName=hotelSearchData.expected.travellerlastName

            //capture entered  traveller details are displayed correctly
            String expectedTravellerName=travellerFName+" "+travellerLName
            String actualTravellerName=getTravellerNameTxt(i)
            //Validate  Traveller Name
            assertionEquals(actualTravellerName,expectedTravellerName, "Amend popup Screen,  Occupancy tab, $i th Traveller name details Actual & Expected don't match")

            sleep(2000)
        }

    }

    protected def verifyChildTravellers(int travellerNum, int maxAdultTravellers, HotelSearchData hotelSearchData, def children) {
        int size=children.size()
        size = size - 1
        int minIndex = 0
        for (int i = travellerNum; i <= maxAdultTravellers; i++) {


            //Input First Name
            String travellerChildFName = hotelSearchData.expected.childFirstName


            //Input Last Name
            String travellerChildLName = hotelSearchData.expected.childLastName



            //Click on Save Button

            if(minIndex<=size) {
                //Capture the entered edit details
                //capture entered third traveller details are displayed correctly
                String expectedChildTravellerName = travellerChildFName + " " + travellerChildLName + " " + "(" + children.getAt(minIndex).toString() + "yrs)"
                String actualChildTravellerName = getTravellerNameTxt(i)
                //Validate Third Traveller Name is Saved
                assertionEquals(actualChildTravellerName, expectedChildTravellerName, "Itinerary Page, $i th  Traveller (child) name details Actual & Expected don't match")

                sleep(2000)
            }
            minIndex=minIndex+1

        }
    }

}
