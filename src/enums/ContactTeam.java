package enums;

public enum ContactTeam {
    GOOD("Good"),
    BAD("Bad");

    private String team;

    ContactTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return this.team;
    }
}
