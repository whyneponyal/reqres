package api;

public class EmployeeUser {
    private String name;
    private String job;

    public EmployeeUser(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public EmployeeUser() {
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
