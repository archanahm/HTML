package com.kuoni.qa.automation.helper

import java.util.List;

import com.sun.corba.se.spi.orbutil.fsm.Input;

class HotelData extends BaseData {

	private static final String pathPrefix = "booking."
	
		String caseName
		String desc
		int checkInDaysToAdd
		int checkOutDaysToAdd
		String cityTypeText
		String cityText
		String guestsRoom1Adults
		List<Integer> childrenAges
		
		HotelData(String path) {
			super("$pathPrefix$path")
		}
	
		@Override
		protected void init() {
			cityTypeText = getConfString("input.cityTypeText")
			cityText = getConfString("input.cityText")
			checkInDaysToAdd = getDaysToAdd("input.checkInDaysToAdd")
			checkOutDaysToAdd = getDaysToAdd("input.checkOutDaysToAdd")
			childrenAges = getIntegerList("input.childrenAges")
			guestsRoom1Adults = getValOrDefault("input.guestsRoom1Adults")
		}
}
