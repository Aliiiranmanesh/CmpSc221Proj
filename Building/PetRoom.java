package Building;

public class PetRoom extends Room {
    private int petNumber;

    public PetRoom(String description, double rent, int roomNumber, int petNumber, String address) {
        super(description, rent, roomNumber, address);
        this.petNumber = petNumber;
    }

    public int getPetNumber() {
        return petNumber;
    }

    public void setPetNumber(int petNumber) {
        this.petNumber = petNumber;
    }
}


