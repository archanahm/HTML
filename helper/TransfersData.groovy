package com.kuoni.qa.automation.helper

import static com.kuoni.qa.automation.util.TestConf.*

/**
 * Created by Joseph Sebastian on 07/12/2015.
 */
class TransfersData extends BaseData {

    private static final String pathPrefix = "transfers."
	String caseName
	String desc
    String pickup
    String dropoff
	String selectPickUp
	String selectDropOff
    String pax
    String checkInDays
	String cancelDays
    int expectedCount
    int index
    int transferIndex
    String expected_car_txt
    String expected_carDesc_txt
    String expected_carTime
    String expected_maxLuggage
    String expected_maxpassengersAllwd
    String expected_cancel_txt
    String expected_Cancel_header_txt
    String expected_summary_txt
    String expected_cancel_chrg_txt
    String expected_popup_txt
    String expected_itinearyPax
    String expected_transfers_title
    String fNameFirst
    String LNameFirst
	String fNameSecond
	String LNameSecond
    String fNameThird
    String LNameThird
    String emailAddress
    String countryCode
    String phoneNum
    String flightNumber
	String arrivingText
	String arrivingFrom
    String timeOfArrival_Hrs
    String timeOfArrival_mins
    String cancel_suggestedItems
    String cancel_HeaderText_1
    String cancel_HeaderText_2
    String cancel_trnsfrConditionsTxt
    String confirm_commision_txt
    String confirm_dropOff_text
    String transfers_FinalStatus_text
    String confirm_BookingStatusConfirm_txt
	String transfers_expected_Passeng
	String transfers_percentage
	String transfers_luggagePieces
    double price
    String expected_itinearytransferName_txt
	String transfers_booking_dropOff_text
	String transfers_passenger_assitance_icon
	String transfers_pickUp_text
	String transfers_pickupTime_Hrs
	String booking_pickup_txt
	String validatePagination
	String validateSorting
	int totalSearchResults
	int resultsPerPage
	int sortByLowestPrice_Index
	int sortByHighestPrice_Index
	int sortByAlphabeticalOrder_Index
	int sortByreverseAlphabeticalOrder_Index
	String transfers_trainName
	String transfers_AgentEmailId
    String transfers_shipName
    String transfers_shipCompany
    String transfers_bookingConfirm_dropOff_Txt
    String transfers_Address
    String transfers_Pincode
    String transfers_dropFlightNo
    String timeOfDepart_Hrs
    String timeOfDepart_mins
    String transfers_departTxt
    String transfers_departingTo
    String transfers_mailTxt
    boolean isPresent
    String agentRef
    String dispStatus
    String notDispStatus
    String defaultTime
    String defaultroomPax
    String mousehoverDurationTxt
    String mousehoverLuggageTxt
    String mousehoverMaxPaxTxt
    String mousehoverlanguageTxt
    String languageTxt
    String splConditionTxt
    String trnsfrConditionTxt
    String meetingPointTxt
    String mousehoverTxt
    String nonBookedItemsHeaderTxt
    String manageItineraryValue
    String infoTxt
    String validateAllSearchResults
    String transfers_Address1
    String transfers_Address2
    String city
    Map expected
	
    TransfersData(String caseName, String subPath) {
        super("$pathPrefix$subPath$caseName", subPath)
		this.caseName = caseName 
    }

    @Override
    protected void init() {
        defaultConf = getConfig(pathPrefix + subPath + "default")
		desc=getStringOrDefault("input.desc")
        pickup = getStringOrDefault("input.pickup")
        dropoff = getStringOrDefault("input.dropOff")
		selectPickUp=getStringOrDefault("input.selectPickUp")
		selectDropOff=getStringOrDefault("input.selectDropOff")
        pax = getStringOrDefault("input.pax")
        index = conf.getInt("input.index")
        transferIndex=conf.getInt("input.transferIndex")
		totalSearchResults=conf.getInt("expected.totalSearchResults")
		resultsPerPage=conf.getInt("expected.resultsPerPage")
        checkInDays = getStringOrDefault("input.checkInDays")
		cancelDays=getStringOrDefault("input.cancelDays")
        agentRef=getStringOrDefault("expected.transfers_agentRef")
        expectedCount = getStringOrDefault("expected.count").toInteger()
        expected_car_txt = getStringOrDefault("expected.transfers_carName_txt");
        expected_carDesc_txt = getStringOrDefault("expected.transfers_carDesc_txt");
        expected_carTime = getStringOrDefault("expected.transfers_carTime");
        expected_maxLuggage = getStringOrDefault("expected.transfers_maxluggage_allowed");
        expected_maxpassengersAllwd = getStringOrDefault("expected.transfers_maxpassengers_allowed")
        expected_cancel_txt = getStringOrDefault("expected.transfers_cancellationtext");
        expected_Cancel_header_txt = getStringOrDefault("expected.transfers_cancellation_Header_txt")
        expected_summary_txt = getStringOrDefault("expected.transfers_cancellation_summary_txt")
        expected_cancel_chrg_txt = getStringOrDefault("expected.transfers_cancellation_charg_txt")
        expected_popup_txt = getStringOrDefault("expected.transfers_popoup_footer_txt")
        expected_itinearytransferName_txt = getStringOrDefault("expected.transfers_itinearytransferName_txt")
        expected_itinearyPax = pax + " PAX"
        expected_transfers_title = getStringOrDefault("expected.transfers_title_txt")
        fNameFirst = getStringOrDefault("expected.transfers_FirstNameFirst")
        LNameFirst = getStringOrDefault("expected.transfers_LastNameFirst")
		fNameSecond = getStringOrDefault("expected.transfers_FirstNameSecond")
		LNameSecond = getStringOrDefault("expected.transfers_LastNameSecond")
        fNameThird = getStringOrDefault("expected.transfers_FirstNameThird")
        LNameThird = getStringOrDefault("expected.transfers_LastNameThird")
        emailAddress = getStringOrDefault("expected.transfers_email")
        countryCode = getStringOrDefault("expected.transfers_countryCode")
        phoneNum = getStringOrDefault("expected.transfers_telephone_Num")
        flightNumber = getStringOrDefault("expected.transfers_flightNo")
		arrivingText= getStringOrDefault("expected.transfers_arrivingText")
		arrivingFrom= getStringOrDefault("expected.transfers_arrivingFrom")
		transfers_pickupTime_Hrs=getStringOrDefault("expected.transfers_pickupTime_Hrs")
        timeOfArrival_Hrs = getStringOrDefault("expected.transfers_timeOfArrival_Hrs")
        timeOfArrival_mins = getStringOrDefault("expected.transfers_timeOfArrival_mins")
        cancel_suggestedItems = getStringOrDefault("expected.transfers_Cancel_suggestedItems")
        cancel_HeaderText_1 = getStringOrDefault("expected.transfers_Cancel_HeaderText_1")
        cancel_HeaderText_2 = getStringOrDefault("expected.transfers_Cancel_HeaderText_2")
        cancel_trnsfrConditionsTxt = getStringOrDefault("expected.transfers_Cancel_TransfrConditions_txt")
        confirm_commision_txt = getStringOrDefault("expected.transfers_commission_text")
        confirm_dropOff_text = getStringOrDefault("expected.transfers_dropOff_text")
        transfers_FinalStatus_text = getStringOrDefault("expected.transfers_FinalStatus_text")
        confirm_BookingStatusConfirm_txt = getStringOrDefault("expected.transfers_BookingConfirmStatus_txt")
		transfers_expected_Passeng=getStringOrDefault("expected.transfers_expected_Passeng")
		transfers_percentage=getStringOrDefault("expected.transfers_percentage")
		transfers_luggagePieces=getStringOrDefault("expected.transfers_luggagePieces")
		transfers_booking_dropOff_text=getStringOrDefault("expected.transfers_booking_dropOff_text")
		transfers_passenger_assitance_icon=getStringOrDefault("expected.passengerAssitanceIcon")
		transfers_pickUp_text=getStringOrDefault("expected.transfers_pickUp_text")
		booking_pickup_txt=getStringOrDefault("expected.bookingConfirm_pickUp_text")
		validatePagination=getStringOrDefault("expected.validatePagination")
		validateSorting=getStringOrDefault("expected.validateSorting")
        validateAllSearchResults=getStringOrDefault("expected.validateAllSearchResults")
		sortByLowestPrice_Index=conf.getInt("expected.sortByLowestPrice_Index")
		sortByHighestPrice_Index=conf.getInt("expected.sortByHighestPrice_Index")
		sortByAlphabeticalOrder_Index=conf.getInt("expected.sortByAlphabeticalOrder_Index")
		sortByreverseAlphabeticalOrder_Index=conf.getInt("expected.sortByreverseAlphabeticalOrder_Index")
		transfers_trainName=getStringOrDefault("expected.transfers_trainName")
		transfers_AgentEmailId=getStringOrDefault("expected.transfers_AgentEmailId")
        transfers_shipName=getStringOrDefault("expected.transfers_shipName")
        transfers_shipCompany=getStringOrDefault("expected.transfers_shipCompany")
        transfers_bookingConfirm_dropOff_Txt=getStringOrDefault("expected.bookingConfirm_dropOff_text")
        transfers_Address=getStringOrDefault("expected.transfers_Address")
        transfers_Address1=getStringOrDefault("expected.transfers_Address1")
        transfers_Address2=getStringOrDefault("expected.transfers_Address2")
        city=getStringOrDefault("expected.city")
        transfers_Pincode=getStringOrDefault("expected.transfers_Pincode")
        transfers_dropFlightNo= getStringOrDefault("expected.transfers_dropFlightNo")
        timeOfDepart_Hrs=getStringOrDefault("expected.transfers_timeOfDepart_Hrs")
        timeOfDepart_mins=getStringOrDefault("expected.transfers_timeOfDepart_mins")
        transfers_departTxt=getStringOrDefault("expected.transfers_departingText")
        transfers_departingTo=getStringOrDefault("expected.transfers_departingTo")
        isPresent=getStringOrDefault("expected.transfers_isPresent")
        transfers_mailTxt=getStringOrDefault("expected.transfers_mailTxt")
        dispStatus=getStringOrDefault("expected.transfers_dispStatus")
        notDispStatus=getStringOrDefault("expected.transfers_notDispStatus")
        defaultTime=getStringOrDefault("expected.transfers_defaultTime")
        defaultroomPax=getStringOrDefault("expected.transfers_defaultRooomPax")
        mousehoverDurationTxt=getStringOrDefault("expected.transfers_DurationText")
        mousehoverLuggageTxt=getStringOrDefault("expected.transfers_LuggageText")
        mousehoverMaxPaxTxt=getStringOrDefault("expected.transfers_paxText")
        mousehoverlanguageTxt=getStringOrDefault("expected.transfers_languageText")
        languageTxt=getStringOrDefault("expected.languageSupportedValText")
        splConditionTxt=getStringOrDefault("expected.transfers_specialconditionTxt")
        trnsfrConditionTxt=getStringOrDefault("expected.transfers_transferConditionTxt")
        meetingPointTxt=getStringOrDefault("expected.transfers_MeetingPointTxt")
        mousehoverTxt=getStringOrDefault("expected.transfers_qstnMarkHoverTxt")
        nonBookedItemsHeaderTxt=getStringOrDefault("expected.transfers_NonBookedItemHeaderTxt")
        manageItineraryValue=getStringOrDefault("expected.transfers_ManageItineraryValue")
        infoTxt=getStringOrDefault("expected.transfers_infoTxt")
        expected=conf.getAnyRef("expected")

    }

    public void setPrice(double price) {
        this.price = price
    }

}


