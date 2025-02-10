package Building;

public class PetRoom extends Room {
    private int animalNumber;
    private String petType;

    public PetRoom(String description, double rent, int roomNumber, int animalNumber, String petType) {
        super(description, rent, roomNumber);
        this.animalNumber = animalNumber;
        this.petType = petType;
    }

    public int getAnimalNumber() {
        return animalNumber;
    }

    public void setAnimalNumber(int animalNumber) {
        this.animalNumber = animalNumber;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }
}


