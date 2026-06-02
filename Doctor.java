import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String doctorId;
    private String specialization;
    private List<Patient> assignedPatients;

    public Doctor(String id, String name, int age, String gender, String doctorId, String specialization) {
        super(id, name, age, gender);
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.assignedPatients = new ArrayList<>();
    }

    public void assignPatient(Patient patient) {
        if (!assignedPatients.contains(patient)) {
            assignedPatients.add(patient);
        }
    }

    public List<Patient> viewPatients() {
        return assignedPatients;
    }

    public String getDoctorId() { return doctorId; }
    public String getSpecialization() { return specialization; }

    @Override
    public String displayDetails() {
        return String.format("Doctor [ID=%s, Name=%s, Age=%d, Gender=%s, Specialization=%s]", 
            doctorId, getName(), getAge(), getGender(), specialization);
    }
}
