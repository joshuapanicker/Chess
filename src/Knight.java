public class Knight extends ChessPiece {
    private char color;
    private int row, col;

    public Knight(int color, int row, int col){
        super("Knight", color, 'n', row, col);

    }

    @Override
    public boolean canMoveTo(ChessBoard chessBoard, int newRow, int newCol, boolean capture)
    {
        int row = this.getRow();
        int col = this.getCol();
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);

        if (newRow > 7 || newCol > 7 || newRow < 0 || newCol < 0)
            return false;

        return ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2));
    }
}
