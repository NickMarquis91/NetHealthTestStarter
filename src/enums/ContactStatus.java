package enums;

public enum ContactStatus {
    ALIVE("Alive"),
    JAIL("Jail"),
    DEAD("Dead"),
    INCAPACITATED("Incapacitated"),
    SEWERS("Sewers"), // dev has some weird data for penguin that flips status and location, bandaid fix
    UNKNOWN("Unknown");

    private String status;

    ContactStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
