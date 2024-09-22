public class Pawn extends ChessPiece {

    ChessPiece chessPiece;

    public Pawn(int color, int row, int col){
        super("Pawn", color, 'p', row, col);

    }

    @Override
    public boolean canMoveTo(ChessBoard chessBoard, int newRow, int newCol, boolean capture)
    {
        int row = this.getRow();
        int col = this.getCol();
        int colDiff = Math.abs(newCol - col);
        int rowDiff = (getColor() == WHITE) ? (newRow - row) : (row - newRow);
        int firstRow = (getColor() == WHITE) ? 1 : 6;

        if (newRow > 7 || newCol > 7 || newRow < 0 || newCol < 0)
            return false;

        if (capture)
            return  (rowDiff == 1 && colDiff == 1);
        else if (col == newCol){
            if (row == firstRow) {
                if (rowDiff == 1 || rowDiff == 2){
                    if (chessBoard.isPathClear(row, col, newRow, newCol))
                        return true;
                }
            }
            else{
                if (rowDiff == 1){
                    if (chessBoard.isPathClear(row, col, newRow, newCol))
                        return true;
                }
            }
        }
        return false;
    }
}
