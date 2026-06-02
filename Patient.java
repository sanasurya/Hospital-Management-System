public class Patient extends Person {
    private String patientId;
    private String disease;
    private Doctor assignedDoctor;
    private boolean admitted;
    private double billAmount;

    public Patient(String id, String name, int age, String gender, String patientId, String disease) {
        super(id, name, age, gender);
        this.patientId = patientId;
        this.disease = disease;
        this.admitted = false;
        this.billAmount = 0.0;
    }

    public void admitPatient() {
        this.admitted = true;
    }

    public void dischargePatient() {
        this.admitted = false;
    }

    public String getPatientId() { return patientId; }
    public String getDisease() { return disease; }
    public Doctor getAssignedDoctor() { return assignedDoctor; }
    public void setAssignedDoctor(Doctor assignedDoctor) { this.assignedDoctor = assignedDoctor; }
    public boolean isAdmitted() { return admitted; }
    
    public void addBill(double amount) {
        this.billAmount += amount;
    }
    public double getBillAmount() { return billAmount; }

    @Override
    public String displayDetails() {
        String doctorName = (assignedDoctor != null) ? assignedDoctor.getName() : "None";
        return String.format("Patient [ID=%s, Name=%s, Age=%d, Gender=%s, Disease=%s, Admitted=%b, Doctor=%s, Bill=$%.2f]", 
            patientId, getName(), getAge(), getGender(), disease, admitted, doctorName, billAmount);
    }
}
