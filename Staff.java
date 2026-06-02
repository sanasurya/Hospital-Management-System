public class Staff extends Person {
    private String staffId;
    private String role;

    public Staff(String id, String name, int age, String gender, String staffId, String role) {
        super(id, name, age, gender);
        this.staffId = staffId;
        this.role = role;
    }

    public String getStaffId() { return staffId; }
    public String getRole() { return role; }

    @Override
    public String displayDetails() {
        return String.format("Staff [ID=%s, Name=%s, Age=%d, Gender=%s, Role=%s]", 
            staffId, getName(), getAge(), getGender(), role);
    }
}
