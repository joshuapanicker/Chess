import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class ChessPiece {
    private int color;
    private int row, col;
    private boolean alive;
    private char icon;
    private Player owner;
    private String name;
    private char iconTxt;
    private Image iconImage;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public ChessPiece(String name, int color, char icon, int row, int col){
        this.color = color;
        this.name = convertToName(color, icon);
        this.row = row;
        this.col = col;
        this.iconTxt = convertToIconTxt(color, icon);

        String iconImageFileName = "resources/icons/" + this.name + ".png";
        try {
            File iconImageFile = new File(iconImageFileName);
            this.iconImage = ImageIO.read(iconImageFile).getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        }
        catch (IOException e) {
            System.out.printf("Error: reading icon file %s\n", iconImageFileName);
            e.printStackTrace();
        }
    }


    private static char convertToIconTxt(int color, char icon) {
        if (color == ChessPiece.WHITE)
            return Character.toUpperCase(icon);
        else
            return Character.toLowerCase(icon);
    }

    public static String colorToString(int color) {
        if (color == ChessPiece.WHITE)
            return "white";
        else if (color == ChessPiece.BLACK)
            return "black";
        else
            return "unknown";
    }

    public static String iconToName(char icon) {
        String iconStr = "";

        switch (icon) {
            case 'R':
            case 'r':
                iconStr = "rook";
                break;

            case 'N':
            case 'n':
                iconStr = "knight";
                break;

            case 'B':
            case 'b':
                iconStr = "bishop";
                break;

            case 'Q':
            case 'q':
                iconStr = "queen";
                break;

            case 'K':
            case 'k':
                iconStr = "king";
                break;

            case 'P':
            case 'p':
                iconStr = "pawn";
                break;
        }
        return iconStr;
    }

    private static String convertToName(int color, char icon) {
        return colorToString(color) + "_" + iconToName(icon);
    }

    public void setOwner(Player owner){
        this.owner = owner;
        owner.addPiece(this);
    }

    public int[] getPosition() {
        return new int[] { this.row, this.col };
    }

    public String getName(){
        return name;
    }

    public Player getOwner(){
        return this.owner;
    }

    public int getColor(){
        return color;
    }

    public char getIcon(){
        return icon;
    }

    public int getCol(){
        return col;
    }

    public int getRow(){
        return row;
    }

    public boolean isAlive(){
        return alive;
    }

    public Image getIconImage(){
        return iconImage;
    }

    public char getIconTxt(){
        return iconTxt;
    }

    public void capture(ChessBoard board){
        this.alive = false;
        this.row = -1;
        this.col = -1;
    }

    public void move(int row, int col)
    {
        this.col = col;
        this.row = row;
    }

    public abstract boolean canMoveTo(ChessBoard chessBoard, int newRow, int newCol, boolean capture);

    public boolean isMatch(int color, char icon) {
        return (this.color == color) && (Character.toUpperCase(icon) == Character.toUpperCase(this.iconTxt));
    }
}

