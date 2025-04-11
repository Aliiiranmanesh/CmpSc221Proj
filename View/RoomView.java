package View;

import Building.Property;
import Building.Room;

public class RoomView {
    public void ViewRoom(Room room) {
        System.out.println("Building.Room: " + room.getRoomNumber() + " with the description: " + room.getDescription() + "\n");
    }
}
