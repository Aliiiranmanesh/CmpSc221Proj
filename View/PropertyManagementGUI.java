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
import java.util.*;
import java.util.List;

public class PropertyManagementGUI extends JFrame {

    private final databaseManager database = new databaseManager();
    private List<Property> properties;
    private List<Tenant> tenants;
    private List<LandLord> landlords;
    private List<Lease> leases;

    private final boolean isLandlord;

    private DefaultListModel<String> propertyListModel,
            tenantListModel,
            landlordListModel,
            leaseListModel,
            roomListModel;

    private JComboBox<String> addrCombo, typeC;
    private JTextField descF, rentF, numF, specF;

    private JComboBox<String> roomComboBox, tenantComboBox, landlordComboBox;

    public PropertyManagementGUI(String role) {
        this.isLandlord = role.equalsIgnoreCase("Landlord");
        reloadData();

        setTitle("Property Management System  –  " + role);
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        if (isLandlord) {
            tabs.addTab("Properties", createPropertyPanel());
            tabs.addTab("Rooms", createRoomPanel(true));
            tabs.addTab("Tenants", createTenantPanel());
            tabs.addTab("Leases", createLeasePanel());
            tabs.addTab("Landlords", createLandlordPanel());
        } else {
            tabs.addTab("Rooms", createRoomPanel(false));
            tabs.addTab("Tenants", createTenantPanel());
        }
        add(tabs);
        refreshUI();
    }

    public PropertyManagementGUI() {
        this("Landlord");
    }


    private JPanel createPropertyPanel() {
        JPanel p = new JPanel(new BorderLayout());
        propertyListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(propertyListModel);
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel in = new JPanel();
        in.setBorder(new TitledBorder("Add / Delete Property"));
        JTextField addr = new JTextField(20);
        JButton add = new JButton("Add"),
                del = new JButton("Delete");

        in.add(new JLabel("Address:"));
        in.add(addr);
        in.add(add);
        in.add(del);
        p.add(in, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            if (!addr.getText().trim().isEmpty()) {
                database.insertProperty(new Property(addr.getText().trim()));
                reloadData();
                refreshUI();
                addr.setText("");
            }
        });
        del.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                database.deleteProperty(properties.get(sel));
                reloadData();
                refreshUI();
            }
        });
        return p;
    }

    private JPanel createRoomPanel(boolean showAddForm) {
        JPanel panel = new JPanel(new BorderLayout());
        roomListModel = new DefaultListModel<>();
        JList<String> roomList = new JList<>(roomListModel);
        panel.add(new JScrollPane(roomList), BorderLayout.CENTER);

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBorder(new TitledBorder("Search Rooms"));

        JTextField desc = new JTextField(12),
                num = new JTextField(5),
                min = new JTextField(5),
                max = new JTextField(5);

        JComboBox<String> type = new JComboBox<>(new String[]{
                "All", "Standard", "PetRoom", "RenovatedRoom", "SmokingRoom"});
        JCheckBox avail = new JCheckBox("Available Only");
        JButton search = new JButton("Search"),
                reset = new JButton("Reset");

        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r1.add(new JLabel("Description:"));
        r1.add(desc);
        r1.add(new JLabel("Room #:"));
        r1.add(num);

        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r2.add(new JLabel("Rent Min:"));
        r2.add(min);
        r2.add(new JLabel("Rent Max:"));
        r2.add(max);

        JPanel r3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r3.add(new JLabel("Type:"));
        r3.add(type);
        r3.add(avail);
        r3.add(search);
        r3.add(reset);

        wrap.add(r1);
        wrap.add(r2);
        wrap.add(r3);
        panel.add(wrap, BorderLayout.NORTH);

        search.addActionListener(e -> applyRoomFilter(desc, num, min, max, type, avail));
        reset.addActionListener(e -> {
            desc.setText("");
            num.setText("");
            min.setText("");
            max.setText("");
            type.setSelectedIndex(0);
            avail.setSelected(false);
            refreshUI();
        });

        if (showAddForm) {
            JPanel form = new JPanel(new GridLayout(0, 2));
            form.setBorder(new TitledBorder("Add Room"));

            descF = new JTextField();
            rentF = new JTextField();
            numF = new JTextField();
            specF = new JTextField();
            addrCombo = new JComboBox<>();
            typeC = new JComboBox<>(new String[]{"Standard", "Pet", "Renovated", "Smoking"});

            form.add(new JLabel("Description:"));
            form.add(descF);
            form.add(new JLabel("Rent:"));
            form.add(rentF);
            form.add(new JLabel("Room #:"));
            form.add(numF);
            form.add(new JLabel("Property:"));
            form.add(addrCombo);
            form.add(new JLabel("Type:"));
            form.add(typeC);
            JLabel specL = new JLabel("Special:");
            form.add(specL);
            form.add(specF);
            specF.setVisible(false);
            specL.setVisible(false);

            typeC.addActionListener(e -> {
                String s = (String) typeC.getSelectedItem();
                boolean show = s.equals("Pet") || s.equals("Renovated");
                specF.setVisible(show);
                specL.setVisible(show);
                specL.setText(s.equals("Pet") ? "Pet #:" : "Year:");
            });

            JButton addBtn = new JButton("Add");
            addBtn.addActionListener(e -> addRoom());
            form.add(addBtn);
            panel.add(form, BorderLayout.SOUTH);
        }
        return panel;
    }

    private JPanel createTenantPanel() {
        JPanel p = new JPanel(new BorderLayout());
        tenantListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(tenantListModel);
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel crud = new JPanel();
        crud.setBorder(new TitledBorder("Add / Delete Tenant"));
        JTextField name = new JTextField(10),
                credit = new JTextField(4),
                phone = new JTextField(10);

        JButton add = new JButton("Add"),
                del = new JButton("Delete");

        crud.add(new JLabel("Name:"));
        crud.add(name);
        crud.add(new JLabel("Credit:"));
        crud.add(credit);
        crud.add(new JLabel("Phone:"));
        crud.add(phone);
        crud.add(add);
        crud.add(del);
        p.add(crud, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            try {
                database.insertTenant(
                        new Tenant(name.getText(),
                                Integer.parseInt(credit.getText()),
                                phone.getText()));
                reloadData();
                refreshUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bad data");
            }
        });
        del.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                database.deleteTenant(tenants.get(sel));
                reloadData();
                refreshUI();
            }
        });
        return p;
    }

    private JPanel createLandlordPanel() {
        JPanel p = new JPanel(new BorderLayout());
        landlordListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(landlordListModel);
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel in = new JPanel();
        in.setBorder(new TitledBorder("Add / Delete Landlord"));
        JTextField name = new JTextField(10);
        JButton add = new JButton("Add"),
                del = new JButton("Delete");
        in.add(new JLabel("Name:"));
        in.add(name);
        in.add(add);
        in.add(del);
        p.add(in, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            database.insertLandlord(new LandLord(name.getText()));
            reloadData();
            refreshUI();
        });
        del.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                database.deleteLandlord(landlords.get(sel));
                reloadData();
                refreshUI();
            }
        });
        return p;
    }

    private JPanel createLeasePanel() {
        JPanel p = new JPanel(new BorderLayout());
        leaseListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(leaseListModel);
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        roomComboBox = new JComboBox<>();
        tenantComboBox = new JComboBox<>();
        landlordComboBox = new JComboBox<>();

        JTextField bal = new JTextField(5);
        JButton create = new JButton("Create"),
                end = new JButton("End Lease");

        JPanel in = new JPanel();
        in.setBorder(new TitledBorder("Create / End Lease"));
        in.add(new JLabel("Room:"));
        in.add(roomComboBox);
        in.add(new JLabel("Tenant:"));
        in.add(tenantComboBox);
        in.add(new JLabel("Landlord:"));
        in.add(landlordComboBox);
        in.add(new JLabel("Balance:"));
        in.add(bal);
        in.add(create);
        in.add(end);
        p.add(in, BorderLayout.SOUTH);

        create.addActionListener(e -> {
            try {
                Room r = getAllRooms().get(roomComboBox.getSelectedIndex());
                Tenant t = tenants.get(tenantComboBox.getSelectedIndex());
                LandLord l = landlords.get(landlordComboBox.getSelectedIndex());
                Lease ls = new Lease(r, t, Double.parseDouble(bal.getText()), l);
                database.insertData(List.of(ls));
                r.setAvailable(false);
                database.updateRoomAvailability(r);
                reloadData();
                refreshUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bad data");
            }
        });

        end.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                Lease ls = leases.get(sel);
                Room r = ls.getRoom();
                database.deleteLease(ls);
                r.setAvailable(true);
                database.updateRoomAvailability(r);
                reloadData();
                refreshUI();
            }
        });

        updateLeaseCombos();
        return p;
    }


    private void applyRoomFilter(JTextField desc, JTextField num,
                                 JTextField min, JTextField max,
                                 JComboBox<String> type, JCheckBox avail) {
        roomListModel.clear();
        for (Room r : getAllRooms()) {
            boolean ok = true;
            if (!desc.getText().isBlank() &&
                    !r.getDescription().toLowerCase()
                            .contains(desc.getText().toLowerCase())) ok = false;

            if (!num.getText().isBlank() &&
                    r.getRoomNumber() != Integer.parseInt(num.getText())) ok = false;

            if (!min.getText().isBlank() &&
                    r.getRent() < Double.parseDouble(min.getText())) ok = false;

            if (!max.getText().isBlank() &&
                    r.getRent() > Double.parseDouble(max.getText())) ok = false;

            if (!type.getSelectedItem().equals("All") &&
                    !r.getClass().getSimpleName().equals(type.getSelectedItem())) ok = false;

            if (avail.isSelected() && !r.isAvailable()) ok = false;

            if (ok) roomListModel.addElement(roomString(r));
        }
    }

    private void addRoom() {
        try {
            String addr = (String) addrCombo.getSelectedItem();
            double rent = Double.parseDouble(rentF.getText());
            int no = Integer.parseInt(numF.getText());
            String d = descF.getText().trim();
            String t = (String) typeC.getSelectedItem();

            Room room = switch (t) {
                case "Pet" -> new PetRoom(d, rent, no, Integer.parseInt(specF.getText()), addr);
                case "Renovated" -> new RenovatedRoom(d, rent, no, Integer.parseInt(specF.getText()), addr);
                case "Smoking" -> new SmokingRoom(d, rent, no, addr);
                default -> new Room(d, rent, no, addr);
            };
            database.insertRoom(room);
            reloadData();
            refreshUI();

            descF.setText("");
            rentF.setText("");
            numF.setText("");
            specF.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Bad data");
        }
    }

    private void updateLeaseCombos() {
        if (roomComboBox == null) return;
        roomComboBox.removeAllItems();
        getAllRooms().stream().filter(Room::isAvailable)
                .forEach(r -> roomComboBox.addItem(roomString(r)));

        tenantComboBox.removeAllItems();
        tenants.forEach(t -> tenantComboBox.addItem(t.getName()));

        landlordComboBox.removeAllItems();
        landlords.forEach(l -> landlordComboBox.addItem(l.getName()));
    }

    private void reloadData() {
        properties = database.getAllProperties();
        tenants = database.getAllTenants();
        landlords = database.getAllLandlords();
        List<Room> rooms = database.getAllRooms();

        for (Property p : properties) p.setRooms(null);
        for (Room r : rooms) {
            properties.stream()
                    .filter(pr -> pr.getAddress().equals(r.getAddress()))
                    .findFirst()
                    .ifPresent(pr -> pr.setRooms(
                            pr.getRooms() == null ? new Room[]{r} : append(pr.getRooms(), r)));
        }
        leases = database.getAllLeases(rooms, tenants, landlords);
    }

    private void refreshUI() {
        if (propertyListModel != null) {
            propertyListModel.clear();
            properties.forEach(p -> propertyListModel.addElement(p.getAddress()));
        }
        if (tenantListModel != null) {
            tenantListModel.clear();
            tenants.forEach(t -> tenantListModel.addElement(t.getName()));
        }
        if (landlordListModel != null) {
            landlordListModel.clear();
            landlords.forEach(l -> landlordListModel.addElement(l.getName()));
        }
        if (leaseListModel != null) {
            leaseListModel.clear();
            leases.forEach(ls -> leaseListModel.addElement(
                    "Room " + ls.getRoom().getRoomNumber() + " – " + ls.getTenant().getName()));
        }
        if (roomListModel != null) {
            roomListModel.clear();
            getAllRooms().forEach(r -> roomListModel.addElement(roomString(r)));
        }
        if (addrCombo != null) {
            addrCombo.removeAllItems();
            properties.forEach(p -> addrCombo.addItem(p.getAddress()));
        }
        //update lease combo
        updateLeaseCombos();
    }

    private List<Room> getAllRooms() {
        List<Room> all = new ArrayList<>();
        for (Property p : properties)
            if (p.getRooms() != null) Collections.addAll(all, p.getRooms());
        return all;
    }

    private static String roomString(Room r) {
        return "Room " + r.getRoomNumber() + " - " + r.getDescription() +
                " ($" + r.getRent() + ") " + (r.isAvailable() ? "[Available]" : "[Occupied]");
    }

    private static Room[] append(Room[] arr, Room r) {
        Room[] out = Arrays.copyOf(arr, arr.length + 1);
        out[arr.length] = r;
        return out;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PropertyManagementGUI().setVisible(true));
    }
}
