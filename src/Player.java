import java.util.ArrayList;

public class Player {
    private char color;
    private ArrayList<ChessPiece> pieces;

    public Player(){}

    public Player(char color){
        this.color = color;
        this.pieces = new ArrayList<>();
    }

    public void setColor(char color){
        this.color = color;
    }

    public char getColor(){
        return color;
    }

    public void addPiece(ChessPiece piece){
        this.pieces.add(piece);
    }


    public void play(){}
}
