package enums;

public enum ContactLocation {
    HOME("Home"),
    WORK("Work"),
    BAT_CAVE("Bat Cave"),
    BANK("Bank"),
    DOWNTOWN_GOTHAM("Downtown Gotham"),
    SEWERS("Sewers"),
    HEADQUARTERS("Headquarters");

    private String location;

    ContactLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
