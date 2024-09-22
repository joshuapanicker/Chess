public class Bishop extends ChessPiece {
    private char color;
    private int row, col;

    public Bishop(int color, int row, int col){
        super("Bishop", color, 'b', row, col);
    }

   @Override
    public boolean canMoveTo(ChessBoard chessBoard, int newRow, int newCol, boolean capture)
    {
        int row = this.getRow();
        int col = this.getCol();
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);

        if (row > 7 || col > 7 || row < 0 || col < 0)
            return false;

        if (rowDiff == colDiff) {
            if (chessBoard.isPathClear(row, col, newRow, newCol))
                return true;
        }

        return false;
    }
}
