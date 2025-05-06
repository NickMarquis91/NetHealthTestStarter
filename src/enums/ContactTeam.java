package enums;

public enum ContactTeam {
    GOOD("Good"),
    BAD("Bad");

    private String team;

    ContactTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }
}
