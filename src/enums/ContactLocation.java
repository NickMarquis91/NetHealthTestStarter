package enums;

public enum ContactLocation {
    HOME("Home"),
    WORK("Work"),
    BAT_CAVE("Bat Cave"),
    BANK("Bank"),
    DOWNTOWN_GOTHAM("Downtown Gotham"),
    SEWERS("Sewers"),
    HEADQUARTERS("Headquarters"),
    DEAD("Dead"), // dev has some weird data for penguin that flips status and location, bandaid fix
    UNKNOWN("Unknown");

    private String location;

    ContactLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return this.location;
    }
}
