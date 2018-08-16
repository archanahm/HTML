package com.kuoni.qa.automation.helper

import com.kuoni.qa.automation.util.TestConf
import com.typesafe.config.Config

/**
 * Created by Joseph Sebastian on 09/10/2015.
 */
class PricingSearchData extends BaseData {

    private static final String pathPrefix = "pricing."

    String caseName
    String desc
    String propertyId
    def cityAreaLandMarkHotelTypeText
    def cityAreaLandMarkHotel
    int checkInDaysToAdd
    int checkOutDaysToAdd
    int guestsNumberOfRooms
    int guestsRoom1Adults
    def expectedhotel
    def expectedcurrency
    def expectedbookRoomPrice
    def expectedbookRoomName
    def checkWasPrice
    def checkCommission
    def checkCancellationCharge
    def checkPriceBreakdown
    List<String> roomsToBook
    def travellerTitle = "Mr"
    def travellerfName = "a"
    def travellerlName = "b"
    String mealBasisKeyCoh
    String mealBasisKeyUI
    List<Integer> childrenAges
    String passengerEmail
    Boolean ignored
    Boolean hotelExpected
    Boolean shouldContinueBooking
    String mealBasisNotExpecting
    String roomNotExpecting
    def priceKey
    List<String> unExpectedPriceKey

    public PricingSearchData(String path) {
        super("$pathPrefix$path")
        caseName = path.substring(path.lastIndexOf(".") + 1, path.length())
    }

    @Override
    protected Config loadConfig(String path) {
        return TestConf.pricing.getConfig(path)
    }

    @Override
    protected Config getDefaultConfig() {
        return TestConf.pricing.getConfig(defaultPath)
    }

    @Override
    void init() {
        withDesc(getConfString("desc"))
        withPropertyId(getConfString("propertyId"))
        withCityAreaLandMarkHotelTypeText(getConfString("cityAreaLandMarkHotelTypeText"))
        withCityAreaLandMarkHotel(getConfString("cityAreaLandMarkHotel"))
        withCheckInDaysToAdd(getDaysToAdd("checkInDaysToAdd"))
        withCheckOutDaysToAdd(getDaysToAdd("checkOutDaysToAdd"))
        withGuestsNumberOfRooms(getValOrDefault("guestsNumberOfRooms").toInteger())
        withGuestsRoom1Adults(getValOrDefault("guestsRoom1Adults").toInteger())
        withExpectedhotel(getConfString("expectedhotel"))
        withExpectedcurrency(getConfString("expectedcurrency"))
        withExpectedbookRoomPrice(getConfString("expectedbookRoomPrice"))
        withExpectedbookRoomName(getConfString("expectedbookRoomName"))
        withRoomsToBook(getStringList("roomsToBook"))
        withCheckWasPrice(getValueOrNull("checkWasPrice"))
        withCheckCommission(getValueOrNull("checkCommission"))
        withCheckCancellationCharge(getValueOrNull("checkCancellationCharge"))
        withCheckPriceBreakdown(getValueOrNull("checkPriceBreakdown"))
        withMealBasisKeyCoh(getConfString("mealBasis.coherence"))
        withMealBasisKeyUI(getConfString("mealBasis.ui"))
        withPassengerEmail(getValOrDefault("passengerEmail"))
        withIgnored(getValOrDefault("ignored").toBoolean())
        hotelExpected = getValOrDefault("hotelExpected").toBoolean()
        mealBasisNotExpecting = getValOrDefault("mealBasisNotExpecting")
        roomNotExpecting = getValOrDefault("roomNotExpecting")
        childrenAges = getIntegerList("childrenAges")
        priceKey = getValOrDefault("priceKey")
        shouldContinueBooking = searchResultExpected()
        unExpectedPriceKey = getStringList("unExpectedPriceKey")
    }

    PricingSearchData withDesc(String desc) {
        this.desc = desc
        this
    }

    PricingSearchData withPropertyId(propertyId) {
        this.propertyId = propertyId
        this
    }

    PricingSearchData withCityAreaLandMarkHotelTypeText(cityAreaLandMarkHotelTypeText) {
        this.cityAreaLandMarkHotelTypeText = cityAreaLandMarkHotelTypeText
        this
    }

    PricingSearchData withCityAreaLandMarkHotel(cityAreaLandMarkHotel) {
        this.cityAreaLandMarkHotel = cityAreaLandMarkHotel
        this
    }

    PricingSearchData withCheckInDaysToAdd(checkInDaysToAdd) {
        this.checkInDaysToAdd = checkInDaysToAdd
        this
    }

    PricingSearchData withCheckOutDaysToAdd(checkOutDaysToAdd) {
        this.checkOutDaysToAdd = checkOutDaysToAdd
        this
    }

    PricingSearchData withGuestsNumberOfRooms(guestsNumberOfRooms) {
        this.guestsNumberOfRooms = guestsNumberOfRooms
        this
    }

    PricingSearchData withGuestsRoom1Adults(guestsRoom1Adults) {
        this.guestsRoom1Adults = guestsRoom1Adults
        this
    }

    PricingSearchData withExpectedhotel(expectedhotel) {
        this.expectedhotel = expectedhotel
        this
    }

    PricingSearchData withExpectedcurrency(expectedcurrency) {
        this.expectedcurrency = expectedcurrency
        this
    }

    PricingSearchData withExpectedbookRoomPrice(expectedbookRoomPrice) {
        this.expectedbookRoomPrice = expectedbookRoomPrice
        this
    }

    PricingSearchData withExpectedbookRoomName(expectedbookRoomName) {
        this.expectedbookRoomName = expectedbookRoomName
        this
    }

    PricingSearchData withRoomsToBook(roomsToBook) {
        this.roomsToBook = roomsToBook
        this
    }

    PricingSearchData withTravellerTitle(travellerTitle) {
        this.travellerTitle = travellerTitle
        this
    }

    PricingSearchData withTravellerfName(travellerfName) {
        this.travellerfName = travellerfName
        this
    }

    PricingSearchData withTravellerlName(travellerlName) {
        this.travellerlName = travellerlName
        this
    }

    PricingSearchData withCheckWasPrice(Boolean checkWasPrice) {
        this.checkWasPrice = checkWasPrice
        this
    }

    PricingSearchData withCheckCommission(Boolean checkCommission) {
        this.checkCommission = checkCommission
        this
    }

    PricingSearchData withCheckCancellationCharge(Boolean checkCancellationCharge) {
        this.checkCancellationCharge = checkCancellationCharge
        this
    }

    PricingSearchData withCheckPriceBreakdown(Boolean checkPriceBreakdown) {
        this.checkPriceBreakdown = checkPriceBreakdown
        this
    }

    PricingSearchData withMealBasisKeyCoh(String mealBasisKeyCoh) {
        this.mealBasisKeyCoh = mealBasisKeyCoh
        this
    }

    PricingSearchData withMealBasisKeyUI(String mealBasisKeyUI) {
        this.mealBasisKeyUI = mealBasisKeyUI
        this
    }

    PricingSearchData withPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail
        this
    }

    PricingSearchData withIgnored(Boolean ignored) {
        this.ignored = ignored
        this
    }

    Boolean shouldCheckWasPrice() {
        return checkWasPrice != null ? checkWasPrice : false
    }

    Boolean shouldCheckCommission() {
        return checkCommission != null ? checkCommission : false
    }

    Boolean shouldCheckCancellationCharge() {
        return checkCancellationCharge != null ? checkCancellationCharge : false
    }

    Boolean shouldCheckMealBasis() {
        return !mealBasisKeyCoh.equals("") && !mealBasisKeyUI.equals("")
    }

    Boolean shouldCheckPriceBreakdown() {
        return checkPriceBreakdown != null ? checkPriceBreakdown : false
    }

    Boolean hasPriceKey() {
        return priceKey != "" ? true : false
    }

    private Boolean searchResultExpected() {
        hotelExpected && "".equals(roomNotExpecting) && "".equals(mealBasisNotExpecting)
    }

    public static void main(String[] args) {
        PricingSearchData data = new PricingSearchData("adjustments.case1")
        println(data.desc)
//        println(data.getStringOrDefault("desc"))
//        println(data.getStringOrDefault("default.guestsNumberOfRooms"))
    }

    @Override
    public String toString() {
        return "PricingSearchData{" +
                "caseName='" + caseName + '\'' +
                ", desc='" + desc + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", cityAreaLandMarkHotelTypeText=" + cityAreaLandMarkHotelTypeText +
                ", cityAreaLandMarkHotel=" + cityAreaLandMarkHotel +
                ", checkInDaysToAdd=" + checkInDaysToAdd +
                ", checkOutDaysToAdd=" + checkOutDaysToAdd +
                ", guestsNumberOfRooms=" + guestsNumberOfRooms +
                ", guestsRoom1Adults=" + guestsRoom1Adults +
                ", expectedhotel=" + expectedhotel +
                ", expectedcurrency=" + expectedcurrency +
                ", expectedbookRoomPrice=" + expectedbookRoomPrice +
                ", expectedbookRoomName=" + expectedbookRoomName +
                ", checkWasPrice=" + checkWasPrice +
                ", checkCommission=" + checkCommission +
                ", checkCancellationCharge=" + checkCancellationCharge +
                ", checkPriceBreakdown=" + checkPriceBreakdown +
                ", roomsToBook=" + roomsToBook +
                ", travellerTitle=" + travellerTitle +
                ", travellerfName=" + travellerfName +
                ", travellerlName=" + travellerlName +
                ", mealBasisKeyCoh='" + mealBasisKeyCoh + '\'' +
                ", mealBasisKeyUI='" + mealBasisKeyUI + '\'' +
                ", childrenAges=" + childrenAges +
                ", passengerEmail='" + passengerEmail + '\'' +
                ", ignored=" + ignored +
                ", hotelExpected=" + hotelExpected +
                ", mealBasisNotExpecting=" + mealBasisNotExpecting +
                ", roomNotExpecting=" + roomNotExpecting +
                ", priceKey=" + priceKey +
                '}';
    }
}
