package Building;

public class Property {
    private Room[] rooms;
    private String address;
    private double charges;

    public Property(Room[] rooms, String address) {
        this.rooms = rooms;
        this.address = address;
        charges = 0;
    }

    public Property(String address) {
        this.address = address;
        charges = 0;
    }

    public Property() {
        this.address = "";
        rooms = null;
        charges = 0;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public String listRooms() {
        String roomList = "";
        for (Room room : rooms) {
            roomList += "Building.Room: " + room.getRoomNumber() + " with the description: " + room.getDescription() + "\n";
        }
        return roomList;
    }
}
