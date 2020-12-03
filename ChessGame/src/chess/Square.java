package chess;

import javafx.scene.paint.*;

public class Square {
    String piece;
    int team;
    boolean selected;
    int location;

    public Square (String pieceName, int team, int location) {
        this.piece = pieceName;
        this.team = team;
        this.selected = false;
        this.location = location;
    }

    public String locationtoString() {
        char Column = (char)(this.location%8 + 65);
        char Row = (char)(8 - this.location/8 + 48);
        return Column + "" + Row;
    }

    public void setLocation(int loc) {
        this.location = loc;
    }

    public String getPiece() {
        return piece;
    }

    public int getTeam() {
        return team;
    }

    public String getTeamName() {
        if (this.team == 0) {
            return "Black";
        } else if (this.team == 1){
            return "White";
        }
        return "NULL";
    }

    public Paint getColor() {
        Paint p = Color.RED;
        if (this.team == 0) {
            p = Color.BLACK;
        } else if (this.team == 1) {
            p = Color.WHITE;
        }
        return p;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean select) {
        this.selected = select;
    }
}
