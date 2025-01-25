public class Room {
    private String description;
    private double Rent;
    private int roomNumber;
    private boolean isOccupied;
    private double additionalCharges;
    private Tenant[] tenants;

    public Room(String description, double rent, int roomNumber) {
        this.description = description;
        Rent = rent;
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRent() {
        return Rent;
    }

    public void setRent(double rent) {
        Rent = rent;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

        public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public Tenant[] getTenants() {
        return tenants;
    }

    public void setTenants(Tenant[] tenants) {
        this.tenants = tenants;
    }

    public void addTenant(Tenant tenant) {
        Tenant[] temp = new Tenant[tenants.length + 1];
        for (int i = 0; i < tenants.length; i++) {
            temp[i] = tenants[i];
        }
        temp[tenants.length] = tenant;
        tenants = temp;
    }

    public void removeTenant(Tenant tenant) {
        Tenant[] temp = new Tenant[tenants.length - 1];
        for (int i = 0, j = 0; i < tenants.length; i++) {
            if (tenants[i] != tenant) {
                temp[j] = tenants[i];
                j++;
            }
        }
        tenants = temp;
    }
}
