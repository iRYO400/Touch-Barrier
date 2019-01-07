package wei.mark.standout;

public class DropDownListItem {
    public int icon;
    public String description;
    public Runnable action;

    public DropDownListItem(int icon, String description, Runnable action) {
        super();
        this.icon = icon;
        this.description = description;
        this.action = action;
    }

    @Override
    public String toString() {
        return description;
    }
}
