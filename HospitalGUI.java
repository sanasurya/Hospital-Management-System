import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalGUI extends JFrame {
    private List<Patient> patients = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();
    private List<Staff> staffs = new ArrayList<>();

    private JTextField idField, nameField, ageField, specificField1, specificField2;
    private JComboBox<String> roleCombo, genderCombo;
    private JTextArea displayArea;
    private DefaultTableModel patientTableModel, doctorTableModel;
    private JTable patientTable, doctorTable;

    public HospitalGUI() {
        super("Hospital Management System");

        // Login System (Bonus)
        if (!showLoginDialog()) {
            System.exit(0);
        }

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel: Form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Center Panel: Tables/Records
        JTabbedPane tabbedPane = createRecordsPanel();
        add(tabbedPane, BorderLayout.CENTER);

        // Bottom Panel: Actions
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private boolean showLoginDialog() {
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        Object[] message = {
            "Username (admin):", username,
            "Password (admin123):", password
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return username.getText().equals("admin") && new String(password.getPassword()).equals("admin123");
        }
        return false;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add New Record"));

        panel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"Patient", "Doctor", "Staff"});
        panel.add(roleCombo);

        panel.add(new JLabel("Person ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panel.add(genderCombo);

        JLabel lblSpecific1 = new JLabel("Patient ID:");
        panel.add(lblSpecific1);
        specificField1 = new JTextField();
        panel.add(specificField1);

        JLabel lblSpecific2 = new JLabel("Disease:");
        panel.add(lblSpecific2);
        specificField2 = new JTextField();
        panel.add(specificField2);

        roleCombo.addActionListener(e -> {
            String role = (String) roleCombo.getSelectedItem();
            if (role.equals("Patient")) {
                lblSpecific1.setText("Patient ID:");
                lblSpecific2.setText("Disease:");
            } else if (role.equals("Doctor")) {
                lblSpecific1.setText("Doctor ID:");
                lblSpecific2.setText("Specialization:");
            } else {
                lblSpecific1.setText("Staff ID:");
                lblSpecific2.setText("Staff Role:");
            }
        });

        JButton submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> addRecord());
        panel.add(submitBtn);

        return panel;
    }

    private JTabbedPane createRecordsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Patients Table
        String[] patCols = {"Patient ID", "Name", "Age", "Gender", "Disease", "Admitted", "Doctor", "Bill"};
        patientTableModel = new DefaultTableModel(patCols, 0);
        patientTable = new JTable(patientTableModel);
        tabbedPane.addTab("Patients", new JScrollPane(patientTable));

        // Doctors Table
        String[] docCols = {"Doctor ID", "Name", "Age", "Gender", "Specialization"};
        doctorTableModel = new DefaultTableModel(docCols, 0);
        doctorTable = new JTable(doctorTableModel);
        tabbedPane.addTab("Doctors", new JScrollPane(doctorTable));

        // Raw Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        tabbedPane.addTab("Raw Details", new JScrollPane(displayArea));

        return tabbedPane;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton admitBtn = new JButton("Admit Patient");
        admitBtn.addActionListener(e -> admitPatient());

        JButton dischargeBtn = new JButton("Discharge Patient");
        dischargeBtn.addActionListener(e -> dischargePatient());

        JButton assignDocBtn = new JButton("Assign Doctor");
        assignDocBtn.addActionListener(e -> assignDoctor());

        JButton billBtn = new JButton("Add Bill");
        billBtn.addActionListener(e -> addBill());

        JButton viewBtn = new JButton("Refresh Raw Details");
        viewBtn.addActionListener(e -> viewDetails());

        JButton searchBtn = new JButton("Search by Name");
        searchBtn.addActionListener(e -> searchRecord());

        panel.add(admitBtn);
        panel.add(dischargeBtn);
        panel.add(assignDocBtn);
        panel.add(billBtn);
        panel.add(viewBtn);
        panel.add(searchBtn);

        return panel;
    }

    private void addRecord() {
        try {
            String role = (String) roleCombo.getSelectedItem();
            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = (String) genderCombo.getSelectedItem();
            String spec1 = specificField1.getText();
            String spec2 = specificField2.getText();

            if (id.isEmpty() || name.isEmpty() || spec1.isEmpty() || spec2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            if (role.equals("Patient")) {
                Patient p = new Patient(id, name, age, gender, spec1, spec2);
                patients.add(p);
                refreshPatientTable();
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
            } else if (role.equals("Doctor")) {
                Doctor d = new Doctor(id, name, age, gender, spec1, spec2);
                doctors.add(d);
                refreshDoctorTable();
                JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            } else {
                Staff s = new Staff(id, name, age, gender, spec1, spec2);
                staffs.add(s);
                JOptionPane.showMessageDialog(this, "Staff added successfully!");
            }
            clearFields();
            viewDetails();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number!");
        }
    }

    private void refreshPatientTable() {
        patientTableModel.setRowCount(0);
        for (Patient p : patients) {
            String docName = (p.getAssignedDoctor() != null) ? p.getAssignedDoctor().getName() : "None";
            patientTableModel.addRow(new Object[]{
                p.getPatientId(), p.getName(), p.getAge(), p.getGender(), 
                p.getDisease(), p.isAdmitted(), docName, "$" + p.getBillAmount()
            });
        }
    }

    private void refreshDoctorTable() {
        doctorTableModel.setRowCount(0);
        for (Doctor d : doctors) {
            doctorTableModel.addRow(new Object[]{
                d.getDoctorId(), d.getName(), d.getAge(), d.getGender(), d.getSpecialization()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        specificField1.setText("");
        specificField2.setText("");
    }

    private void admitPatient() {
        String pId = JOptionPane.showInputDialog("Enter Patient ID to admit:");
        if (pId != null) {
            for (Patient p : patients) {
                if (p.getPatientId().equals(pId)) {
                    p.admitPatient();
                    refreshPatientTable();
                    viewDetails();
                    JOptionPane.showMessageDialog(this, "Patient Admitted!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Patient not found!");
        }
    }

    private void dischargePatient() {
        String pId = JOptionPane.showInputDialog("Enter Patient ID to discharge:");
        if (pId != null) {
            for (Patient p : patients) {
                if (p.getPatientId().equals(pId)) {
                    p.dischargePatient();
                    refreshPatientTable();
                    viewDetails();
                    JOptionPane.showMessageDialog(this, "Patient Discharged! Final Bill: $" + p.getBillAmount());
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Patient not found!");
        }
    }

    private void assignDoctor() {
        String pId = JOptionPane.showInputDialog("Enter Patient ID:");
        if (pId == null) return;
        String dId = JOptionPane.showInputDialog("Enter Doctor ID:");
        if (dId == null) return;

        Patient patient = null;
        Doctor doctor = null;

        for (Patient p : patients) if (p.getPatientId().equals(pId)) patient = p;
        for (Doctor d : doctors) if (d.getDoctorId().equals(dId)) doctor = d;

        if (patient != null && doctor != null) {
            patient.setAssignedDoctor(doctor);
            doctor.assignPatient(patient);
            refreshPatientTable();
            viewDetails();
            JOptionPane.showMessageDialog(this, "Doctor assigned successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Patient or Doctor ID!");
        }
    }

    private void addBill() {
        String pId = JOptionPane.showInputDialog("Enter Patient ID:");
        if (pId != null) {
            for (Patient p : patients) {
                if (p.getPatientId().equals(pId)) {
                    String amountStr = JOptionPane.showInputDialog("Enter Bill Amount:");
                    if (amountStr == null) return;
                    try {
                        double amount = Double.parseDouble(amountStr);
                        p.addBill(amount);
                        refreshPatientTable();
                        viewDetails();
                        JOptionPane.showMessageDialog(this, "Bill added successfully!");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid amount!");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Patient not found!");
        }
    }

    private void viewDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Patients ---\n");
        for (Person p : patients) { // Demonstrating Polymorphism
            sb.append(p.displayDetails()).append("\n");
        }
        sb.append("\n--- Doctors ---\n");
        for (Person p : doctors) {
            sb.append(p.displayDetails()).append("\n");
        }
        sb.append("\n--- Staff ---\n");
        for (Person p : staffs) {
            sb.append(p.displayDetails()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void searchRecord() {
        String name = JOptionPane.showInputDialog("Enter Name to Search:");
        if (name != null && !name.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder("Search Results:\n");
            boolean found = false;

            List<Person> allPersons = new ArrayList<>();
            allPersons.addAll(patients);
            allPersons.addAll(doctors);
            allPersons.addAll(staffs);

            for (Person p : allPersons) {
                if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                    sb.append(p.displayDetails()).append("\n");
                    found = true;
                }
            }

            if (found) {
                JOptionPane.showMessageDialog(this, sb.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No records found!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalGUI().setVisible(true);
        });
    }
}
