package occupant;

import documents.Lease;

public class Tenant {
    String name;
    int creditScore;
    String phoneNum;
    Lease lease;

    public Tenant(String name, int creditScore, String phoneNum) {
        this.name = name;
        this.creditScore = creditScore;
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }

    public void payRent() {
        lease.setBalance(lease.getBalance() - lease.getRoom().getRent());
    }
}
