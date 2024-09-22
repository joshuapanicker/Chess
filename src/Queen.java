public class Queen extends ChessPiece {
    private char color;
    private int row, col;

    public Queen(int color, int row, int col){
        super("Queen", color, 'q', row, col);
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

        if (newRow == row || newCol == col || rowDiff == colDiff) {
            if (chessBoard.isPathClear(row, col, newRow, newCol))
                return true;
        }
        return false;
    }
}
