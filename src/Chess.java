public class Chess {

    private Game game;
    private ChessBoard chessBoard;
    public Chess(){
        this.game = new Game();
    }

    public void run(){
        chessBoard = game.getChessBoard();
        chessBoard.print();
    }

    public static void main(String[] args) {
        Chess chess = new Chess();
        chess.run();
    }
}