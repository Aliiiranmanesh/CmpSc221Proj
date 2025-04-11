package View;

import Building.Property;
import Building.Room;

public class PropertyView {
    public void listRooms(Property property) {
        String roomList = "";
        for (Room room : property.getRooms()) {
            roomList += "Building.Room: " + room.getRoomNumber() + " with the description: " + room.getDescription() + "\n";
        }
        System.out.println(roomList);
    }
}
