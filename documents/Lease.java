package documents;

import Building.Room;
import Owner.LandLord;
import occupant.Tenant;

public class Lease {
    Room room;
    Tenant tenant;
    double balance;
    LandLord landLord;

    public Lease(Room room, Tenant tenant, double balance, LandLord landLord) {
        this.room = room;
        this.tenant = tenant;
        this.balance = balance;
        this.landLord = landLord;
    }
    public Lease() {
        this.room = null;
        this.tenant = null;
        this.balance = 0;
        this.landLord = null;
    }


    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LandLord getLandLord() {
        return landLord;
    }

    public void setLandLord(LandLord landLord) {
        this.landLord = landLord;
    }

    public void endLease() {
        room.setAvailable(false);
        tenant.setLease(null);
    }
}
