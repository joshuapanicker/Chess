public class Rook extends ChessPiece {

    public Rook(int color, int row, int col){
        super("Rook", color, 'r', row, col);
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

        if ((rowDiff == 0 && colDiff > 0) || (rowDiff > 0 && colDiff == 0)) {
            return (chessBoard.isPathClear(row, col, newRow, newCol));
        }
        return false;
    }
}
