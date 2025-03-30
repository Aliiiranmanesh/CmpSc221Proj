package Building;

public class PetRoom extends Room {
    private int petNumber;

    public PetRoom(String description, double rent, int roomNumber, int petNumber) {
        super(description, rent, roomNumber);
        this.petNumber = petNumber;
    }

    public int getPetNumber() {
        return petNumber;
    }

    public void setPetNumber(int petNumber) {
        this.petNumber = petNumber;
    }
}


