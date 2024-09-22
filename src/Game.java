import java.util.ArrayList;

public class Game{
    private ChessBoard chessBoard;
    private Player player1, player2;

    ArrayList<ChessPiece> whiteChessPieces;
    ArrayList<ChessPiece> blackChessPieces;
    ChessNotation chessNotation;
    int nextMove;

    public Game(){

        whiteChessPieces = createPieces(ChessPiece.WHITE);
        blackChessPieces = createPieces(ChessPiece.BLACK);

        chessBoard = new ChessBoard();
        chessBoard.clear();
        chessBoard.placePieces(whiteChessPieces);
        chessBoard.placePieces(blackChessPieces);

        chessNotation = new ChessNotation();
        nextMove = 0;
    }

    public static ArrayList<ChessPiece> createPieces(int color)
    {
        ArrayList<ChessPiece> chessPieces = new ArrayList<ChessPiece>();
        int row;

        row = (color == ChessPiece.WHITE) ? 1 : 6;
        for (int col = 0; col < 8; ++col)
            chessPieces.add(new Pawn(color, row, col));
        row = (color == ChessPiece.WHITE) ? 0 : 7;

        chessPieces.add(new Rook(color, row, 0));
        chessPieces.add(new Rook(color, row, 7));
        chessPieces.add(new Knight(color, row, 1));
        chessPieces.add(new Knight(color, row, 6));
        chessPieces.add(new Bishop(color, row, 2));
        chessPieces.add(new Bishop(color, row, 5));
        chessPieces.add(new Queen(color, row, 3));
        chessPieces.add(new King(color, row, 4));

        return chessPieces;
    }

    public static void initChessBoard(ChessBoard chessBoard) {
        chessBoard.clear();
        chessBoard.placePieces(createPieces(ChessPiece.WHITE));
        chessBoard.placePieces(createPieces(ChessPiece.BLACK));
    }


    public ChessBoard getChessBoard(){
        return chessBoard;
    }

    public void load(String fileName) {
        chessNotation.loadGame(fileName);
    }

    public void save(String fileName){
        chessNotation.saveGame(fileName);
    }

    public void restore(){
        chessBoard.restore(chessNotation);
    }

    public void playNext(){nextMove++;}

    public ChessMove replayNext(){
        ChessMove chessMove = chessNotation.getMove(nextMove);

        if (chessMove != null){
            chessBoard.move(chessMove);
            nextMove++;
        }
        return  chessMove;
    }


    public void move(ChessPiece chessPiece, int newRow, int newCol) {
        ChessMove chessMove = new ChessMove(0, "move", chessPiece);
        chessMove.setSlot(newRow, newCol, false, false, false);
        chessBoard.move(chessMove);
    }
}