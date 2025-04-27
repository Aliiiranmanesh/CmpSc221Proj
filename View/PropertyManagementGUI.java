package View;

import Building.Property;
import Building.Room;
import Building.PetRoom;
import Building.RenovatedRoom;
import Building.SmokingRoom;
import documents.Lease;
import Owner.LandLord;
import occupant.Tenant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PropertyManagementGUI extends JFrame {
    private ArrayList<Property> properties;
    private ArrayList<Tenant> tenants;
    private ArrayList<Lease> leases;
    private ArrayList<LandLord> landlords;
    private DefaultListModel<String> roomListModel;
    private JComboBox<String> addressComboBox;

    private String userRole;

    public PropertyManagementGUI(String userRole) {
        this.userRole = userRole;
        properties = new ArrayList<>();
        tenants = new ArrayList<>();
        leases = new ArrayList<>();
        landlords = new ArrayList<>();

        setTitle("Property Management System - " + userRole);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        if (userRole.equalsIgnoreCase("Landlord")) {
            tabbedPane.addTab("Properties", createPropertyPanel());
            tabbedPane.addTab("Rooms", createRoomPanel());
            tabbedPane.addTab("Tenants", createTenantPanel());
            tabbedPane.addTab("Leases", createLeasePanel());
            tabbedPane.addTab("Landlords", createLandlordPanel());
        } else if (userRole.equalsIgnoreCase("Tenant")) {
            tabbedPane.addTab("Rooms", createRoomPanel());
            tabbedPane.addTab("Tenants", createTenantPanel());
            tabbedPane.addTab("Leases", createLeasePanel());
        }

        add(tabbedPane);
    }

    private JPanel createPropertyPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> propertyListModel = new DefaultListModel<>();
        JList<String> propertyList = new JList<>(propertyListModel);
        JScrollPane propertyScrollPane = new JScrollPane(propertyList);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JTextField addressField = new JTextField(20);
        JButton addButton = new JButton("Add Property");
        JButton viewRoomsButton = new JButton("View Rooms");

        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(addButton);
        inputPanel.add(viewRoomsButton);

        addButton.addActionListener(e -> {
            String address = addressField.getText().trim();
            if (!address.isEmpty()) {
                Property property = new Property(address);
                properties.add(property);
                propertyListModel.addElement(address);
                addressField.setText("");
                updateAddressComboBox();
            }
        });

        viewRoomsButton.addActionListener(e -> {
            int selectedIndex = propertyList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Property selectedProperty = properties.get(selectedIndex);
                PropertyView propertyView = new PropertyView();
                propertyView.listRooms(selectedProperty);
            }
        });

        panel.add(propertyScrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new GridLayout(2, 3));
        JTextField searchField = new JTextField(20);
        JTextField minRentField = new JTextField(10);
        JTextField maxRentField = new JTextField(10);
        JComboBox<String> roomTypeComboSearch = new JComboBox<>(new String[]{"All", "Standard", "Pet", "Renovated", "Smoking"});
        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search Description:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Rent Range:"));
        searchPanel.add(minRentField);
        searchPanel.add(maxRentField);
        searchPanel.add(new JLabel("Room Type:"));
        searchPanel.add(roomTypeComboSearch);

        roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        JScrollPane roomScrollPane = new JScrollPane(roomList);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(roomScrollPane, BorderLayout.CENTER);

        if (!userRole.equalsIgnoreCase("Tenant")) {
            JPanel inputPanel = new JPanel(new GridLayout(10, 2));
            JTextField descriptionField = new JTextField(20);
            JTextField rentField = new JTextField(20);
            JTextField roomNumberField = new JTextField(20);
            addressComboBox = new JComboBox<>();
            updateAddressComboBox();
            JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{"Standard", "Pet", "Renovated", "Smoking"});
            JTextField additionalChargesField = new JTextField(20);
            JLabel petNumberLabel = new JLabel("Number of Pets:");
            JTextField petNumberField = new JTextField(20);
            JLabel renovationYearLabel = new JLabel("Renovation Year:");
            JTextField renovationYearField = new JTextField(20);
            JButton addButton = new JButton("Add Room");

            inputPanel.add(new JLabel("Description:"));
            inputPanel.add(descriptionField);
            inputPanel.add(new JLabel("Rent:"));
            inputPanel.add(rentField);
            inputPanel.add(new JLabel("Room Number:"));
            inputPanel.add(roomNumberField);
            inputPanel.add(new JLabel("Address:"));
            inputPanel.add(addressComboBox);
            inputPanel.add(new JLabel("Room Type:"));
            inputPanel.add(roomTypeCombo);
            inputPanel.add(new JLabel("Additional Charges:"));
            inputPanel.add(additionalChargesField);
            inputPanel.add(petNumberLabel);
            inputPanel.add(petNumberField);
            inputPanel.add(renovationYearLabel);
            inputPanel.add(renovationYearField);
            inputPanel.add(new JLabel(""));
            inputPanel.add(addButton);

            petNumberLabel.setVisible(false);
            petNumberField.setVisible(false);
            renovationYearLabel.setVisible(false);
            renovationYearField.setVisible(false);

            roomTypeCombo.addActionListener(e -> {
                String selectedType = (String) roomTypeCombo.getSelectedItem();
                petNumberLabel.setVisible("Pet".equals(selectedType));
                petNumberField.setVisible("Pet".equals(selectedType));
                renovationYearLabel.setVisible("Renovated".equals(selectedType));
                renovationYearField.setVisible("Renovated".equals(selectedType));
            });

            addButton.addActionListener(e -> {
                try {
                    String description = descriptionField.getText().trim();
                    double rent = Double.parseDouble(rentField.getText().trim());
                    int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
                    String address = (String) addressComboBox.getSelectedItem();
                    String roomType = (String) roomTypeCombo.getSelectedItem();

                    double additionalCharges = 0;
                    if (!additionalChargesField.getText().trim().isEmpty()) {
                        additionalCharges = Double.parseDouble(additionalChargesField.getText().trim());
                    }
                    rent += additionalCharges;

                    Room room;
                    switch (roomType) {
                        case "Pet":
                            int petNumber = Integer.parseInt(petNumberField.getText().trim());
                            room = new PetRoom(description, rent, roomNumber, petNumber, address);
                            break;
                        case "Renovated":
                            int renovationYear = Integer.parseInt(renovationYearField.getText().trim());
                            room = new RenovatedRoom(description, rent, roomNumber, renovationYear, address);
                            break;
                        case "Smoking":
                            room = new SmokingRoom(description, rent, roomNumber, address);
                            break;
                        default:
                            room = new Room(description, rent, roomNumber, address);
                            break;
                    }

                    int selectedIndex = properties.size() - 1;
                    if (selectedIndex >= 0) {
                        Property property = properties.get(selectedIndex);
                        Room[] currentRooms = property.getRooms();
                        Room[] newRooms;
                        if (currentRooms == null) {
                            newRooms = new Room[1];
                            newRooms[0] = room;
                        } else {
                            newRooms = new Room[currentRooms.length + 1];
                            System.arraycopy(currentRooms, 0, newRooms, 0, currentRooms.length);
                            newRooms[currentRooms.length] = room;
                        }
                        property.setRooms(newRooms);
                        updateRoomList(searchField.getText().trim().toLowerCase(),
                                minRentField.getText().trim().isEmpty() ? 0 : Double.parseDouble(minRentField.getText().trim()),
                                maxRentField.getText().trim().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxRentField.getText().trim()),
                                (String) roomTypeComboSearch.getSelectedItem());
                    }

                    descriptionField.setText("");
                    rentField.setText("");
                    roomNumberField.setText("");
                    additionalChargesField.setText("");
                    petNumberField.setText("");
                    renovationYearField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
                }
            });

            panel.add(inputPanel, BorderLayout.SOUTH);
        }

        updateRoomList(null, 0, Double.MAX_VALUE, "All");

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toLowerCase();
            double minRent = 0;
            double maxRent = Double.MAX_VALUE;
            try {
                if (!minRentField.getText().trim().isEmpty()) {
                    minRent = Double.parseDouble(minRentField.getText().trim());
                }
                if (!maxRentField.getText().trim().isEmpty()) {
                    maxRent = Double.parseDouble(maxRentField.getText().trim());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for rent range");
                return;
            }
            String selectedType = (String) roomTypeComboSearch.getSelectedItem();
            updateRoomList(searchText, minRent, maxRent, selectedType);
        });

        return panel;
    }

    private void updateAddressComboBox() {
        if (addressComboBox != null) {
            addressComboBox.removeAllItems();
            for (Property property : properties) {
                addressComboBox.addItem(property.getAddress());
            }
        }
    }

    private void updateRoomList(String searchText, double minRent, double maxRent, String roomType) {
        roomListModel.clear();
        for (Room room : getAllRooms()) {
            boolean matchesSearch = searchText == null ||
                    room.getDescription().toLowerCase().contains(searchText) ||
                    room.getAddress().toLowerCase().contains(searchText);
            boolean matchesRent = room.getRent() >= minRent && room.getRent() <= maxRent;
            boolean matchesType = roomType.equals("All") ||
                    (roomType.equals("Standard") && room.getClass() == Room.class) ||
                    (roomType.equals("Pet") && room instanceof PetRoom) ||
                    (roomType.equals("Renovated") && room instanceof RenovatedRoom) ||
                    (roomType.equals("Smoking") && room instanceof SmokingRoom);

            if (matchesSearch && matchesRent && matchesType) {
                String roomInfo = String.format("Room %d - %s (Rent: $%.2f, Type: %s, Address: %s, Available: %s)",
                        room.getRoomNumber(),
                        room.getDescription(),
                        room.getRent(),
                        room.getClass().getSimpleName(),
                        room.getAddress(),
                        room.isAvailable() ? "Yes" : "No");
                roomListModel.addElement(roomInfo);
            }
        }
    }

    private JPanel createTenantPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tenant list
        DefaultListModel<String> tenantListModel = new DefaultListModel<>();
        JList<String> tenantList = new JList<>(tenantListModel);
        JScrollPane tenantScrollPane = new JScrollPane(tenantList);

        // Tenant input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JTextField nameField = new JTextField(20);
        JTextField creditScoreField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JButton addButton = new JButton("Add Tenant");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Credit Score:"));
        inputPanel.add(creditScoreField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (phoneField.getText().length() < 10)
                    throw new NumberFormatException();
                String phone = phoneField.getText().trim();
                int creditScore;
                try {
                    if (Integer.parseInt(creditScoreField.getText().trim()) >= 300 && Integer.parseInt(creditScoreField.getText().trim()) <= 850)
                        creditScore = Integer.parseInt(creditScoreField.getText().trim());
                    else
                        throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid credit score");
                    return;
                }

                Tenant tenant = new Tenant(name, creditScore, phone);
                tenants.add(tenant);
                tenantListModel.addElement(name + " - " + phone);

                nameField.setText("");
                creditScoreField.setText("");
                phoneField.setText("");
            } catch (Exception exx) {
                JOptionPane.showMessageDialog(this, "Unexpected error occurred: " + exx.getMessage());
            }
        });

        panel.add(tenantScrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLeasePanel() {


        JPanel panel = new JPanel(new BorderLayout());

        // Lease list
        DefaultListModel<String> leaseListModel = new DefaultListModel<>();
        JList<String> leaseList = new JList<>(leaseListModel);
        JScrollPane leaseScrollPane = new JScrollPane(leaseList);

        // Lease input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JComboBox<String> roomCombo = new JComboBox<>();
        JComboBox<String> tenantCombo = new JComboBox<>();
        JTextField balanceField = new JTextField(20);
        JComboBox<String> landlordCombo = new JComboBox<>();
        JButton addButton = new JButton("Create Lease");
        JButton endButton = new JButton("End Lease");

        inputPanel.add(new JLabel("Room:"));
        inputPanel.add(roomCombo);
        inputPanel.add(new JLabel("Tenant:"));
        inputPanel.add(tenantCombo);
        inputPanel.add(new JLabel("Initial Balance:"));
        inputPanel.add(balanceField);
        inputPanel.add(new JLabel("Landlord:"));
        inputPanel.add(landlordCombo);
        inputPanel.add(addButton);
        inputPanel.add(endButton);

        // Update combos when data changes
        updateComboBoxes(roomCombo, tenantCombo, landlordCombo);

        addButton.addActionListener(e -> {
            try {
                int roomIndex = roomCombo.getSelectedIndex();
                int tenantIndex = tenantCombo.getSelectedIndex();
                int landlordIndex = landlordCombo.getSelectedIndex();
                double balance = Double.parseDouble(balanceField.getText().trim());

                if (roomIndex >= 0 && tenantIndex >= 0 && landlordIndex >= 0) {
                    Room selectedRoom = getAllRooms().get(roomIndex);
                    Tenant selectedTenant = tenants.get(tenantIndex);
                    LandLord selectedLandlord = landlords.get(landlordIndex);

                    if (selectedRoom.isAvailable()) {
                        Lease lease = new Lease(selectedRoom, selectedTenant, balance, selectedLandlord);
                        leases.add(lease);
                        selectedRoom.setAvailable(false);
                        selectedRoom.setOccupancy(selectedRoom.getOccupancy() + 1);
                        selectedTenant.setLease(lease);

                        leaseListModel.addElement("Room " + selectedRoom.getRoomNumber() + " - " + selectedTenant.getName());
                        balanceField.setText("");
                        // Update room list after creating lease
                        updateRoomList("", 0, Double.MAX_VALUE, "All");
                    } else {
                        JOptionPane.showMessageDialog(this, "Selected room is not available");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid balance");
            }
        });

        endButton.addActionListener(e -> {
            int selectedIndex = leaseList.getSelectedIndex();
            if (selectedIndex >= 0) {
                Lease lease = leases.get(selectedIndex);
                lease.endLease();
                leases.remove(selectedIndex);
                leaseListModel.remove(selectedIndex);
                updateComboBoxes(roomCombo, tenantCombo, landlordCombo);
                // Update room list after ending lease
                updateRoomList("", 0, Double.MAX_VALUE, "All");
            }
        });

        panel.add(leaseScrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLandlordPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Landlord list
        DefaultListModel<String> landlordListModel = new DefaultListModel<>();
        JList<String> landlordList = new JList<>(landlordListModel);
        JScrollPane landlordScrollPane = new JScrollPane(landlordList);

        // Landlord input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JTextField nameField = new JTextField(20);
        JButton addButton = new JButton("Add Landlord");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                LandLord landlord = new LandLord(name);
                landlords.add(landlord);
                landlordListModel.addElement(name);
                nameField.setText("");
            }
        });

        panel.add(landlordScrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void updateComboBoxes(JComboBox<String> roomCombo, JComboBox<String> tenantCombo, JComboBox<String> landlordCombo) {
        roomCombo.removeAllItems();
        for (Room room : getAllRooms()) {
            roomCombo.addItem("Room " + room.getRoomNumber() + " - " + room.getDescription() + " (" + room.getAddress() + ")");
        }

        tenantCombo.removeAllItems();
        for (Tenant tenant : tenants) {
            tenantCombo.addItem(tenant.getName());
        }

        landlordCombo.removeAllItems();
        for (LandLord landlord : landlords) {
            landlordCombo.addItem(landlord.getName());
        }
    }

    private ArrayList<Room> getAllRooms() {
        ArrayList<Room> allRooms = new ArrayList<>();
        for (Property property : properties) {
            if (property.getRooms() != null) {
                for (Room room : property.getRooms()) {
                    allRooms.add(room);
                }
            }
        }
        return allRooms;
    }
}