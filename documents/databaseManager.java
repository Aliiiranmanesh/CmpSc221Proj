
package documents;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Building.*;
import occupant.Tenant;
import Owner.LandLord;

public class databaseManager {
    private static final String DB_URL = "jdbc:derby:PropertyManagementDB;create=true";
    private Connection connection;

    public databaseManager() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        try {
            stmt.executeUpdate("CREATE TABLE PROPERTIES (ADDRESS VARCHAR(255) PRIMARY KEY)");
        } catch (SQLException e) {
        }
        try {
            stmt.executeUpdate("CREATE TABLE TENANTS (NAME VARCHAR(255), CREDIT_SCORE INT, PHONE VARCHAR(20))");
        } catch (SQLException e) {
        }
        try {
            stmt.executeUpdate("CREATE TABLE LANDLORDS (NAME VARCHAR(255))");
        } catch (SQLException e) {
        }
        try {
            stmt.executeUpdate("CREATE TABLE ROOMS (DESCRIPTION VARCHAR(255), RENT DOUBLE, ROOM_NUMBER INT, TYPE VARCHAR(20), SPECIAL INT, ADDRESS VARCHAR(255), AVAILABLE BOOLEAN, OCCUPANCY INT)");
        } catch (SQLException e) {
            e.getMessage();
        }
        try {
            stmt.executeUpdate("CREATE TABLE LEASES (ROOM_NUMBER INT, ROOM_ADDRESS VARCHAR(255), TENANT_NAME VARCHAR(255), LANDLORD_NAME VARCHAR(255), BALANCE DOUBLE, ACTIVE BOOLEAN)");
        } catch (SQLException e) {
            e.getMessage();
        }
        try {
            stmt.executeUpdate("CREATE TABLE USERS (\n" +
                    "    USERNAME VARCHAR(255) PRIMARY KEY,\n" +
                    "    PASSWORD VARCHAR(255),\n" +
                    "    ROLE VARCHAR(20)\n" +
                    ")");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertProperty(Property p) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO PROPERTIES (ADDRESS) VALUES (?)")) {
            ps.setString(1, p.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void deleteProperty(Property p) {
        try {
            connection.setAutoCommit(false);

            // Delete rooms related to the property first
            try (PreparedStatement psRooms = connection.prepareStatement("DELETE FROM ROOMS WHERE ADDRESS=?")) {
                psRooms.setString(1, p.getAddress());
                psRooms.executeUpdate();
            }

            // Then delete the property itself
            try (PreparedStatement psProp = connection.prepareStatement("DELETE FROM PROPERTIES WHERE ADDRESS=?")) {
                psProp.setString(1, p.getAddress());
                psProp.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.getMessage();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.getMessage();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }


    public List<Property> getAllProperties() {
        List<Property> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT ADDRESS FROM PROPERTIES")) {
            while (rs.next()) list.add(new Property(rs.getString("ADDRESS")));
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    public void insertTenant(Tenant t) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO TENANTS (NAME, CREDIT_SCORE, PHONE) VALUES (?, ?, ?)")) {
            ps.setString(1, t.getName());
            ps.setInt(2, t.getCreditScore());
            ps.setString(3, t.getPhoneNum());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void deleteTenant(Tenant t) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM TENANTS WHERE NAME=?")) {
            ps.setString(1, t.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public List<Tenant> getAllTenants() {
        List<Tenant> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT NAME, CREDIT_SCORE, PHONE FROM TENANTS")) {
            while (rs.next())
                list.add(new Tenant(rs.getString("NAME"), rs.getInt("CREDIT_SCORE"), rs.getString("PHONE")));
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    public void insertLandlord(LandLord l) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO LANDLORDS (NAME) VALUES (?)")) {
            ps.setString(1, l.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void deleteLandlord(LandLord l) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM LANDLORDS WHERE NAME=?")) {
            ps.setString(1, l.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public List<LandLord> getAllLandlords() {
        List<LandLord> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT NAME FROM LANDLORDS")) {
            while (rs.next()) list.add(new LandLord(rs.getString("NAME")));
        } catch (SQLException e) {
            e.getMessage();
        }
        return list;
    }

    public void insertRoom(Room r) {
        String sql = "INSERT INTO ROOMS (DESCRIPTION, RENT, ROOM_NUMBER, TYPE, SPECIAL, ADDRESS, AVAILABLE, OCCUPANCY) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getDescription());
            ps.setDouble(2, r.getRent());
            ps.setInt(3, r.getRoomNumber());
            ps.setString(4, r.getClass().getSimpleName());
            int special = (r instanceof PetRoom) ? ((PetRoom) r).getPetNumber() : (r instanceof RenovatedRoom) ? ((RenovatedRoom) r).getRenovationYear() : 0;
            ps.setInt(5, special);
            ps.setString(6, r.getAddress());
            ps.setBoolean(7, r.isAvailable());
            ps.setInt(8, r.getOccupancy());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void updateRoomAvailability(Room room) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE ROOMS SET AVAILABLE=? WHERE ROOM_NUMBER=? AND ADDRESS=?")) {
            ps.setBoolean(1, room.isAvailable());
            ps.setInt(2, room.getRoomNumber());
            ps.setString(3, room.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM ROOMS")) {
            while (rs.next()) {
                String type = rs.getString("TYPE");
                Room r;
                if ("PetRoom".equals(type)) {
                    r = new PetRoom(rs.getString("DESCRIPTION"), rs.getDouble("RENT"), rs.getInt("ROOM_NUMBER"), rs.getInt("SPECIAL"), rs.getString("ADDRESS"));
                } else if ("RenovatedRoom".equals(type)) {
                    r = new RenovatedRoom(rs.getString("DESCRIPTION"), rs.getDouble("RENT"), rs.getInt("ROOM_NUMBER"), rs.getInt("SPECIAL"), rs.getString("ADDRESS"));
                } else if ("SmokingRoom".equals(type)) {
                    r = new SmokingRoom(rs.getString("DESCRIPTION"), rs.getDouble("RENT"), rs.getInt("ROOM_NUMBER"), rs.getString("ADDRESS"));
                } else {
                    r = new Room(rs.getString("DESCRIPTION"), rs.getDouble("RENT"), rs.getInt("ROOM_NUMBER"), rs.getString("ADDRESS"));
                }
                r.setAvailable(rs.getBoolean("AVAILABLE"));
                r.setOccupancy(rs.getInt("OCCUPANCY"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertData(List<Lease> leases) {
        String sql = "INSERT INTO LEASES (ROOM_NUMBER, ROOM_ADDRESS, TENANT_NAME, LANDLORD_NAME, BALANCE, ACTIVE) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Lease l : leases) {
                ps.setInt(1, l.getRoom().getRoomNumber());
                ps.setString(2, l.getRoom().getAddress());
                ps.setString(3, l.getTenant().getName());
                ps.setString(4, l.getLandLord().getName());
                ps.setDouble(5, l.getBalance());
                ps.setBoolean(6, true);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLeaseActive(Lease l) {
        if (l == null || l.getTenant() == null || l.getRoom() == null) {
            System.err.println("Lease or its components are null.");
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE LEASES SET ACTIVE=? WHERE ROOM_NUMBER=? AND TENANT_NAME=?")) {
            ps.setBoolean(1, false);
            ps.setInt(2, l.getRoom().getRoomNumber());
            ps.setString(3, l.getTenant().getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLease(Lease l) {
        if (l == null || l.getTenant() == null || l.getRoom() == null) {
            System.err.println("Lease or its components are null.");
            return;
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM LEASES WHERE ROOM_NUMBER=? AND ROOM_ADDRESS=? AND TENANT_NAME=?")) {
            ps.setInt(1, l.getRoom().getRoomNumber());
            ps.setString(2, l.getRoom().getAddress());
            ps.setString(3, l.getTenant().getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Lease> getAllLeases(List<Room> rooms, List<Tenant> tenants, List<LandLord> landlords) {
        List<Lease> leases = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM LEASES")) {
            while (rs.next()) {
                Room r = rooms.stream().filter(ro -> {
                    try {
                        return ro.getRoomNumber() == rs.getInt("ROOM_NUMBER") && ro.getAddress().equals(rs.getString("ROOM_ADDRESS"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).findFirst().orElse(null);
                Tenant t = tenants.stream().filter(te -> {
                    try {
                        return te.getName().equals(rs.getString("TENANT_NAME"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).findFirst().orElse(null);
                LandLord l = landlords.stream().filter(la -> {
                    try {
                        return la.getName().equals(rs.getString("LANDLORD_NAME"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).findFirst().orElse(null);
                if (r != null && t != null && l != null) {
                    Lease lease = new Lease(r, t, rs.getDouble("BALANCE"), l);
                    if (!rs.getBoolean("ACTIVE")) lease.endLease();
                    leases.add(lease);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leases;
    }

    public boolean validateUser(String username, String password, String role) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=? AND ROLE=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(String username, String password, String role) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES (?, ?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // e.g., duplicate username
        }
    }
}
