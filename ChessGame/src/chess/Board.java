package chess;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.*;

public class Board {

    boolean checking = false;
    boolean whosmove = false;
    int sqrs = 8;
    static Map<Integer, Square> boardsquares = new HashMap<Integer, Square>();

    public void addPieces() {
        boardsquares.put(0, new Square("Rook", 0, 0));
        boardsquares.put(7, new Square("Rook",0, 7));
        boardsquares.put(63, new Square("Rook",1, 63));
        boardsquares.put(63-7, new Square("Rook",1, 63-7));
        boardsquares.put(1, new Square("Knight",0,1));
        boardsquares.put(6, new Square("Knight",0,6));
        boardsquares.put(62, new Square("Knight",1,62));
        boardsquares.put(62-5, new Square("Knight",1,62-5));
        boardsquares.put(2, new Square("Bishop",0,2));
        boardsquares.put(5, new Square("Bishop",0,5));
        boardsquares.put(61, new Square("Bishop",1, 61));
        boardsquares.put(61-3, new Square("Bishop",1, 61-3));
        boardsquares.put(3, new Square("Queen",0,3));
        boardsquares.put(60, new Square("Queen",1,60));
        boardsquares.put(4, new Square("King",0, 4));
        boardsquares.put(59, new Square("King",1,59));
        for (int s = 0; s < sqrs; s++) {
            boardsquares.put(sqrs + s, new Square("Pawn",0, sqrs + s));
            boardsquares.put(63 - sqrs - s, new Square("Pawn",1, 63 - sqrs - s));
        }
    }

    public void generateBoard(GridPane root) {
        int currCol = -1;
        for (int i = 0; i < sqrs; i++) {
            if (i > 0) {
                currCol *= -1;
            }
            for (int j = 0; j < sqrs; j++) {
                int pID = i * sqrs + j;
                Rectangle defRect = new Rectangle(75, 75);
                defRect.setStroke(Color.BLACK);
                StackPane sp = new StackPane();
                if (currCol == -1) {
                    defRect.setFill(Color.DARKGRAY);
                } else if (currCol == 1) {
                    defRect.setFill(Color.DIMGRAY);
                }
                currCol *= -1;
                sp.getChildren().add(defRect);
                if (boardsquares.containsKey(pID)) {
                    Text piece = new Text(boardsquares.get(pID).getPiece());
                    piece.setFill(boardsquares.get(pID).getColor());
                    if (boardsquares.get(pID).isSelected()) {
                        piece.setFill(Color.YELLOW);
                    }
                    sp.getChildren().add(piece);
                }
                sp.setOnMouseClicked(event -> selectPiece(pID));
                root.add(sp, j, i);
            }
        }
    }

    public void selectPiece(int pID) {
        boolean hasPiece = false;
        int prevspot = -1;
        for (Integer s : boardsquares.keySet()) {
            if (boardsquares.get(s).isSelected() && boardsquares.get(s).location != pID) {
                prevspot = s;
            }
        }
        if (prevspot == -1) {
            for (Integer s : boardsquares.keySet()) {
                if (s == pID) {
                    hasPiece = true;
                    if (boardsquares.get(s).isSelected()) {
                        boardsquares.get(s).setSelected(false);
                    } else if ((boardsquares.get(s).getTeam() == 1) == whosmove) {
                        boardsquares.get(s).setSelected(true);
                    }
                    //System.out.println("Selected the " + boardsquares.get(s).getTeamName() + " " +
                    //        boardsquares.get(s).getPiece() + " @ " + boardsquares.get(s).locationtoString());
                } else {
                    boardsquares.get(s).setSelected(false);
                }
            }
            //if (!hasPiece) {
            //    System.out.println("Selected an open space @ " + (char) (pID % 8 + 65) + (char) (8 - pID / 8 + 48));
            //}
            //for (Integer s : boardsquares.keySet()) {
            //    if (boardsquares.get(s).isSelected()) {
            //        System.out.println(boardsquares.get(s).locationtoString() + " is selected");
            //    }
            //}
        } else {
            movePiece(prevspot, pID);
            System.out.println(whosmove);
        }
    }

    public void movePiece(int currKey, int nextLoc) {
        if (validMove(currKey,nextLoc)) {
            whosmove = !whosmove;
            Square p2move = boardsquares.get(currKey);
            Square oldSqr = boardsquares.get(nextLoc);
            Square newSqr = new Square(p2move.getPiece(), p2move.getTeam(), nextLoc);
            boardsquares.remove(currKey);
            boardsquares.remove(nextLoc);
            boardsquares.put(nextLoc, newSqr);
            if (putsinCheck()) {
                boardsquares.remove(nextLoc);
                if (oldSqr != null) {
                    boardsquares.put(nextLoc, oldSqr);
                }
                boardsquares.put(currKey, p2move);
                whosmove = !whosmove;
            }
        }
    }

    public boolean putsinCheck() {
        for (Integer s1 : boardsquares.keySet()) {
            if (boardsquares.get(s1).getPiece().equals("King") && (boardsquares.get(s1).getTeam() == 1) != whosmove) {
                for (Integer s2 : boardsquares.keySet()) {
                    if (boardsquares.get(s2).getTeam() != boardsquares.get(s1).getTeam()) {
                        if (validMove(s2, s1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean validMove(int currkey, int nextLoc) {
        boolean valid = false;
        if (boardsquares.get(currkey).getPiece().equals("Pawn")) {
            int xspaces = Math.abs(nextLoc%8 - currkey%8);
            if (boardsquares.get(currkey).getTeam() == 0) {
                if (currkey < 16 && currkey > 7) {
                    if (nextLoc == currkey + 16) {
                        if (boardsquares.get(currkey + 8) == null && boardsquares.get(nextLoc) == null) {
                            valid = true;
                        }
                    }
                }
                if (nextLoc == currkey + 8) {
                    if (boardsquares.get(currkey + 8) == null) {
                        valid = true;
                    }
                } else if (nextLoc == currkey + 9 || nextLoc == currkey + 7) {
                    if (xspaces == 1) {
                        if (boardsquares.get(nextLoc) != null && boardsquares.get(nextLoc).getTeam() != boardsquares.get(currkey).getTeam()) {
                            valid = true;
                        }
                    }
                }
            } else if (boardsquares.get(currkey).getTeam() == 1) {
                if (currkey < 56 && currkey > 47) {
                    if (nextLoc == currkey - 16) {
                        if (boardsquares.get(currkey - 8) == null && boardsquares.get(nextLoc) == null) {
                            valid = true;
                        }
                    }
                }
                if (nextLoc == currkey - 8) {
                    if (boardsquares.get(currkey - 8) == null) {
                        valid = true;
                    }
                } else if (nextLoc == currkey - 9 || nextLoc == currkey - 7) {
                    if (xspaces == 1) {
                        if (boardsquares.get(nextLoc) != null && boardsquares.get(nextLoc).getTeam() != boardsquares.get(currkey).getTeam()) {
                            valid = true;
                        }
                    }
                }
            }
        } else if (boardsquares.get(currkey).getPiece().equals("Rook")) {
            if (nextLoc/8 == currkey/8) {
                int i = 1;
                if (currkey > nextLoc) {
                    i = -1;
                }
                valid = true;
                int diff = i;
                while (currkey + diff != nextLoc) {
                    if (boardsquares.get(currkey + diff) != null) {
                        valid = false;
                    }
                    diff += i;
                }
            } else if (nextLoc%8 == currkey%8) {
                int i = 8;
                if (currkey > nextLoc) {
                    i = -8;
                }
                valid = true;
                int diff = i;
                while (currkey + diff != nextLoc) {
                    if (boardsquares.get(currkey + diff) != null) {
                        valid = false;
                    }
                    diff += i;
                }
            }
            if (boardsquares.get(nextLoc) != null) {
                if (boardsquares.get(nextLoc).getTeam() == boardsquares.get(currkey).getTeam()) {
                    valid = false;
                }
            }
        } else if (boardsquares.get(currkey).getPiece().equals("Knight")) {
            int xspaces = Math.abs(nextLoc%8 - currkey%8);
            int yspaces = Math.abs(nextLoc/8 - currkey/8);
            if (xspaces == 2 && yspaces == 1 || xspaces == 1 && yspaces == 2) {
                if (boardsquares.get(nextLoc) == null) {
                    valid = true;
                } else {
                    if (boardsquares.get(nextLoc).getTeam() != boardsquares.get(currkey).getTeam()) {
                        valid = true;
                    }
                }
            }
        } else if (boardsquares.get(currkey).getPiece().equals("Bishop")) {
            int xspaces = Math.abs(nextLoc%8 - currkey%8);
            int yspaces = Math.abs(nextLoc/8 - currkey/8);
            int i = 1;
            int j = 8;
            if (currkey%8 > nextLoc%8) {
                i = -1;
            }
            if (currkey/8 > nextLoc/8) {
                j = -8;
            }
            int diff = i + j;
            if (xspaces == yspaces) {
                valid = true;
                while (currkey + diff != nextLoc) {
                    if (boardsquares.get(currkey + diff) != null) {
                        valid = false;
                    }
                    diff = diff + i + j;
                }
            }
            if (boardsquares.get(nextLoc) != null) {
                if (boardsquares.get(nextLoc).getTeam() == boardsquares.get(currkey).getTeam()) {
                    valid = false;
                }
            }
        } else if (boardsquares.get(currkey).getPiece().equals("Queen")) {
            int xspaces = Math.abs(nextLoc%8 - currkey%8);
            int yspaces = Math.abs(nextLoc/8 - currkey/8);
                if (xspaces == yspaces) {
                int i = 1;
                int j = 8;
                if (currkey % 8 > nextLoc % 8) {
                    i = -1;
                }
                if (currkey / 8 > nextLoc / 8) {
                    j = -8;
                }
                int diff = i + j;
                valid = true;
                while (currkey + diff != nextLoc) {
                    if (boardsquares.get(currkey + diff) != null) {
                        valid = false;
                    }
                    diff = diff + i + j;
                }
            }
            if (nextLoc/8 == currkey/8) {
                int i = 1;
                if (currkey > nextLoc) {
                    i = -1;
                }
                valid = true;
                int diff = i;
                while (currkey + diff != nextLoc) {
                    if (boardsquares.get(currkey + diff) != null) {
                        valid = false;
                    }
                    diff += i;
                }
            } else if (nextLoc%8 == currkey%8) {
                int i = 8;
                if (currkey > nextLoc) {
                    i = -8;
                }
                valid = true;
                int diff = i;
                while (currkey + diff != nextLoc) {
                    if (boardsquares.get(currkey + diff) != null) {
                        valid = false;
                    }
                    diff += i;
                }
            }
            if (boardsquares.get(nextLoc) != null) {
                if (boardsquares.get(nextLoc).getTeam() == boardsquares.get(currkey).getTeam()) {
                    valid = false;
                }
            }
        } else if (boardsquares.get(currkey).getPiece().equals("King")) {
            int xspaces = Math.abs(nextLoc%8 - currkey%8);
            int yspaces = Math.abs(nextLoc/8 - currkey/8);
            if (xspaces <= 1 && yspaces <= 1) {
                valid = true;
            }
            if (boardsquares.get(nextLoc) != null) {
                if (boardsquares.get(nextLoc).getTeam() == boardsquares.get(currkey).getTeam()) {
                    valid = false;
                }
            }
        }
        return valid;
    }
}
