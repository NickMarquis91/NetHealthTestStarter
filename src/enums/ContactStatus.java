package enums;

public enum ContactStatus {
    ALIVE("Alive"),
    JAIL("Jail"),
    DEAD("Dead"),
    INCAPACITATED("Incapacitated");

    private String status;

    ContactStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
