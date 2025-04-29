
// View/PropertyManagementGUI.java
// FULL IMPLEMENTATION with reorganized layout, full CRUD, and dynamic updates
// Please make sure related model classes (Property, Room, Tenant, etc.) are also updated accordingly.

package View;

import Building.*;
import documents.Lease;
import documents.databaseManager;
import Owner.LandLord;
import occupant.Tenant;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class PropertyManagementGUI extends JFrame {
    private List<Property> properties;
    private List<Tenant> tenants;
    private List<LandLord> landlords;
    private List<Lease> leases;

    private databaseManager database;

    private DefaultListModel<String> propertyListModel;
    private DefaultListModel<String> tenantListModel;
    private DefaultListModel<String> landlordListModel;
    private DefaultListModel<String> leaseListModel;
    private DefaultListModel<String> roomListModel;

    private JList<String> propertyList;
    private JComboBox<String> addrCombo, typeC, roomCombo, tenantCombo, landlordCombo;
    private JTextField specF, descF, rentF, numF;

    public PropertyManagementGUI() {
        database = new databaseManager();
        reloadData();

        setTitle("Property Management System");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Properties", createPropertyPanel());
        tabs.addTab("Rooms", createRoomPanel());
        tabs.addTab("Tenants", createTenantPanel());
        tabs.addTab("Leases", createLeasePanel());
        tabs.addTab("Landlords", createLandlordPanel());

        add(tabs);
        refreshUI();
    }

    private void reloadData() {
        properties = database.getAllProperties();
        tenants = database.getAllTenants();
        landlords = database.getAllLandlords();
        List<Room> rooms = database.getAllRooms();
        for (Room r : rooms) {
            properties.stream()
                    .filter(p -> p.getAddress().equals(r.getAddress()))
                    .findFirst()
                    .ifPresent(p -> p.setRooms(
                            p.getRooms() == null ? new Room[]{r} : appendRoom(p.getRooms(), r)
                    ));
        }
        leases = database.getAllLeases(rooms, tenants, landlords);
    }

    private JPanel createPropertyPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        propertyListModel = new DefaultListModel<>();
        propertyList = new JList<>(propertyListModel);
        panel.add(new JScrollPane(propertyList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new TitledBorder("Add Property"));
        inputPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(20);
        inputPanel.add(addressField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            String addr = addressField.getText().trim();
            if (!addr.isEmpty()) {
                Property p = new Property(addr);
                database.insertProperty(p);
                reloadData();
                refreshUI();
                addressField.setText("");
            }
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> {
            int sel = propertyList.getSelectedIndex();
            if (sel >= 0) {
                Property p = properties.get(sel);
                database.deleteProperty(p);
                reloadData();
                refreshUI();
            }
        });

        inputPanel.add(addBtn);
        inputPanel.add(deleteBtn);

        panel.add(inputPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        panel.add(new JScrollPane(roomList), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2));
        form.setBorder(new TitledBorder("Add Room"));

        descF = new JTextField(); rentF = new JTextField(); numF = new JTextField(); specF = new JTextField();
        addrCombo = new JComboBox<>(); typeC = new JComboBox<>(new String[]{"Standard", "Pet", "Renovated", "Smoking"});

        form.add(new JLabel("Description:")); form.add(descF);
        form.add(new JLabel("Rent:")); form.add(rentF);
        form.add(new JLabel("Room #:")); form.add(numF);
        form.add(new JLabel("Property:")); form.add(addrCombo);
        form.add(new JLabel("Type:")); form.add(typeC);
        JLabel specL = new JLabel("Special:");
        form.add(specL); form.add(specF);
        specF.setVisible(false); specL.setVisible(false);

        typeC.addActionListener(e -> {
            String sel = (String) typeC.getSelectedItem();
            boolean show = sel.equals("Pet") || sel.equals("Renovated");
            specF.setVisible(show);
            specL.setVisible(show);
            specL.setText(sel.equals("Pet") ? "Pet #:" : "Year:");
        });

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            try {
                Room room;
                String addr = (String) addrCombo.getSelectedItem();
                double rent = Double.parseDouble(rentF.getText());
                int roomNo = Integer.parseInt(numF.getText());
                String desc = descF.getText().trim();
                String type = (String) typeC.getSelectedItem();
                switch (type) {
                    case "Pet":
                        room = new PetRoom(desc, rent, roomNo, Integer.parseInt(specF.getText()), addr); break;
                    case "Renovated":
                        room = new RenovatedRoom(desc, rent, roomNo, Integer.parseInt(specF.getText()), addr); break;
                    case "Smoking":
                        room = new SmokingRoom(desc, rent, roomNo, addr); break;
                    default:
                        room = new Room(desc, rent, roomNo, addr);
                }
                database.insertRoom(room);
                reloadData();
                refreshUI();
                descF.setText(""); rentF.setText(""); numF.setText(""); specF.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        form.add(addBtn);
        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTenantPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tenantListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(tenantListModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel input = new JPanel();
        input.setBorder(new TitledBorder("Add Tenant"));

        JTextField name = new JTextField(10), credit = new JTextField(5), phone = new JTextField(10);
        input.add(new JLabel("Name:")); input.add(name);
        input.add(new JLabel("Credit:")); input.add(credit);
        input.add(new JLabel("Phone:")); input.add(phone);

        JButton add = new JButton("Add");
        JButton del = new JButton("Delete");

        add.addActionListener(e -> {
            Tenant t = new Tenant(name.getText(), Integer.parseInt(credit.getText()), phone.getText());
            database.insertTenant(t); reloadData(); refreshUI();
        });

        del.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                database.deleteTenant(tenants.get(sel));
                reloadData(); refreshUI();
            }
        });

        input.add(add); input.add(del);
        panel.add(input, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLandlordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        landlordListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(landlordListModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel input = new JPanel();
        input.setBorder(new TitledBorder("Add Landlord"));
        JTextField name = new JTextField(10); input.add(new JLabel("Name:")); input.add(name);

        JButton add = new JButton("Add"), del = new JButton("Delete");

        add.addActionListener(e -> {
            database.insertLandlord(new LandLord(name.getText()));
            reloadData(); refreshUI();
        });

        del.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                database.deleteLandlord(landlords.get(sel));
                reloadData(); refreshUI();
            }
        });

        input.add(add); input.add(del);
        panel.add(input, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createLeasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        leaseListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(leaseListModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel input = new JPanel();
        input.setBorder(new TitledBorder("Create Lease"));

        roomCombo = new JComboBox<>();
        tenantCombo = new JComboBox<>();
        landlordCombo = new JComboBox<>();
        JTextField balance = new JTextField(5);

        input.add(new JLabel("Room:")); input.add(roomCombo);
        input.add(new JLabel("Tenant:")); input.add(tenantCombo);
        input.add(new JLabel("Landlord:")); input.add(landlordCombo);
        input.add(new JLabel("Balance:")); input.add(balance);

        JButton add = new JButton("Create"), end = new JButton("End Lease");

        add.addActionListener(e -> {
            try {
                Room room = getAllRooms().get(roomCombo.getSelectedIndex());
                Tenant t = tenants.get(tenantCombo.getSelectedIndex());
                LandLord l = landlords.get(landlordCombo.getSelectedIndex());
                Lease lease = new Lease(room, t, Double.parseDouble(balance.getText()), l);
                database.insertData(List.of(lease));
                reloadData(); refreshUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating lease.");
            }
        });

        end.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                Lease lease = leases.get(sel);
                lease.endLease();
                database.updateLeaseActive(lease);
                leases.remove(sel);
                refreshUI();
            }
        });

        input.add(add); input.add(end);
        panel.add(input, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshUI() {
        propertyListModel.clear(); properties.forEach(p -> propertyListModel.addElement(p.getAddress()));
        tenantListModel.clear(); tenants.forEach(t -> tenantListModel.addElement(t.getName()));
        landlordListModel.clear(); landlords.forEach(l -> landlordListModel.addElement(l.getName()));
        leaseListModel.clear(); leases.forEach(l -> leaseListModel.addElement("Lease: Room " + l.getRoom().getRoomNumber() + " - " + l.getTenant().getName()));
        roomListModel.clear(); getAllRooms().forEach(r -> roomListModel.addElement("Room " + r.getRoomNumber() + " - " + r.getDescription()));
        updateComboBoxes();
    }

    private void updateComboBoxes() {
        addrCombo.removeAllItems(); properties.forEach(p -> addrCombo.addItem(p.getAddress()));
        roomCombo.removeAllItems(); getAllRooms().stream().filter(Room::isAvailable).forEach(r -> roomCombo.addItem(r.getDescription()));
        tenantCombo.removeAllItems(); tenants.forEach(t -> tenantCombo.addItem(t.getName()));
        landlordCombo.removeAllItems(); landlords.forEach(l -> landlordCombo.addItem(l.getName()));
    }

    private List<Room> getAllRooms() {
        List<Room> all = new ArrayList<>();
        properties.forEach(p -> { if (p.getRooms() != null) Collections.addAll(all, p.getRooms()); });
        return all;
    }

    private Room[] appendRoom(Room[] arr, Room r) {
        Room[] result = Arrays.copyOf(arr, arr.length + 1);
        result[arr.length] = r;
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PropertyManagementGUI().setVisible(true));
    }
}
