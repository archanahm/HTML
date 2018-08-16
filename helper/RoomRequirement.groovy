package com.kuoni.qa.automation.helper

/**
 * Created by Joseph Sebastian on 07/10/2015.
 */
class RoomRequirement {

    int requestIndex = 0
    int occupancy
    int noOfInfants
    List<Integer> childAges
    int minBed
    int maxBed

    RoomRequirement(int occupancy, int requestIndex = 0) {
        this.requestIndex = requestIndex
        this.occupancy = occupancy
    }

    RoomRequirement withChildAges(List<Integer> cAges) {
        this.childAges = cAges
        this.noOfInfants = cAges.size()
        this
    }

    private Integer getNoOfInfants() {
        childAges.findAll { it < 2 }.size()
    }

    public static void main(String[] args) {
        RoomRequirement builder = new RoomRequirement(5)
        builder.withChildAges([10, 17])
        println(builder.getNoOfInfants())
    }


}
