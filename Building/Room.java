package Building;

import occupant.Tenant;

public class Room {
    private String description, address;
    private double rent;
    private int roomNumber;
    private boolean isAvailable;
    private double additionalCharges;
    private int occupancy;

    public Room(String description, double rent, int roomNumber, String address) {
        this.description = description;
        this.rent = rent;
        this.roomNumber = roomNumber;
        this.isAvailable = true;
        this.additionalCharges = 0;
        this.occupancy = 0;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Room() {
        this.description = "";
        this.rent = 0;
        this.roomNumber = 0;
        this.isAvailable = true;
        this.additionalCharges = 0;
        this.occupancy = 0;
        this.address = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }
}