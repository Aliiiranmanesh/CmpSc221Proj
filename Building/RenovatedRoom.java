package Building;

import Building.Room;

public class RenovatedRoom extends Room {

    private int renovationYear;

    public RenovatedRoom(String description, double rent, int roomNumber, int renovationYear, String address) {
        super(description, rent, roomNumber, address);
        this.renovationYear = renovationYear;
        setAdditionalCharges(this.getAdditionalCharges() + 50);
    }

    public int getRenovationYear() {
        return renovationYear;
    }

    public void setRenovationYear(int renovationYear) {
        this.renovationYear = renovationYear;
    }
}
