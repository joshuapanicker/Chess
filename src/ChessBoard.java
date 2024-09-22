import java.util.ArrayList;

public class ChessBoard {
    ChessPiece[][] board;
    private boolean gameOver = false;
    private int currentTurn = ChessPiece.WHITE;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        clear();
    }

    public void clear(){
        for (int row = 0; row < 8; ++row)
            for (int col = 0; col < 8; ++col)
                board[row][col] = null;
        gameOver = false;
        currentTurn = ChessPiece.WHITE;
    }

    public void restore(ChessNotation chessNotation) {
        for (int i = 0; i < chessNotation.getMoveCount(); ++i)
            move(chessNotation.getMove(i));
    }

    public void move(ChessMove move) {
        if (gameOver) {
            System.out.println("GAME OVER. No more moves...");
            return;
        }

        ChessPiece chessPiece = board[move.origRow][move.origCol];
        if (chessPiece.getColor() != currentTurn){
            System.out.println("It is NOT your turn... Current turn: "+ (currentTurn == ChessPiece.WHITE ? "White" : "Black"));
            return;
        }

        ChessPiece otherPiece = board[move.getNewRow()][move.getNewCol()];

        if (otherPiece != null && otherPiece.getColor() == chessPiece.getColor()) {
            System.out.println("Invalid move! You cannot capture a piece of the same color.");
            return;
        }

        board[move.origRow][move.origCol] = null;
        board[move.getNewRow()][move.getNewCol()] = chessPiece;
        chessPiece.move(move.getNewRow(), move.getNewCol());

        int playerColor = chessPiece.getColor();
        if (isCheck(playerColor)) {
            board[move.getNewRow()][move.getNewCol()] = otherPiece;
            board[move.origRow][move.origCol] = chessPiece;
            chessPiece.move(move.origRow, move.origCol);

            System.out.println("Invalid move! You cannot expose your King to check.");
            return;
        }


        placePiece(chessPiece);

        int opponentColor = (playerColor == ChessPiece.WHITE) ? ChessPiece.BLACK : ChessPiece.WHITE;
        if (isCheck(opponentColor)) {
            System.out.println("Check! The " + (opponentColor == ChessPiece.WHITE ? "White" : "Black") + " King is in check.");
        }

        if (isCheckMate(opponentColor)) {
            System.out.println("Checkmate! " + (opponentColor == ChessPiece.WHITE ? "Black" : "White") + " wins!");
            gameOver = true;
        }

        switchTurn();
    }

    private void switchTurn() {
        currentTurn = (currentTurn == ChessPiece.WHITE) ? ChessPiece.BLACK : ChessPiece.WHITE;
        System.out.println("Next turn: " + (currentTurn == ChessPiece.WHITE ? "White" : "Black"));
    }

    public ChessPiece findPieceAtRow(int color, char icon, int row) {
        for (int col = 0; col < 8; ++col) {
            ChessPiece chessPiece = board[row][col];

            if (chessPiece != null && chessPiece.isMatch(color, icon))
                return board[row][col];
        }
        return null;
    }

    public ChessPiece findPieceAtCol(int color, char icon, int col) {
        for (int row = 0; row < 8; ++row) {
            ChessPiece chessPiece = board[row][col];

            if (chessPiece != null && chessPiece.isMatch(color, icon))
                return chessPiece;
        }
        return null;
    }

    public ChessPiece findPiece(int color, char icon, int newRow, int newCol, boolean capture) {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                ChessPiece chessPiece = board[row][col];

                if (chessPiece != null && chessPiece.isMatch(color, icon)) {
                    if (chessPiece.canMoveTo(this, newRow, newCol, capture))
                        return chessPiece;
                }
            }
        }
        return null;
    }


    public King findKing(int color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(row, col);
                if (piece instanceof King && piece.getColor() == color) {
                    return (King) piece;
                }
            }
        }
        return null;
    }

    public boolean isCheck(int kingColor) {
        King king = findKing(kingColor);

        int kingRow = king.getRow();
        int kingCol = king.getCol();
        int opponentColor = (kingColor == ChessPiece.WHITE) ? ChessPiece.BLACK : ChessPiece.WHITE;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece opponentPiece = board[row][col];

                if (opponentPiece != null && opponentPiece.getColor() == opponentColor) {
                    if (opponentPiece.canMoveTo(this, kingRow, kingCol, true)) {
                        if (opponentPiece instanceof Rook || opponentPiece instanceof Bishop || opponentPiece instanceof Queen) {
                            if (isPathClear(row, col, kingRow, kingCol)) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isCheckMate(int kingColor) {
        if (!isCheck(kingColor)) {
            return false;
        }

        King king = findKing(kingColor);
        int kingRow = king.getRow();
        int kingCol = king.getCol();

        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                if (rowDiff != 0 || colDiff != 0) {
                    int newRow = kingRow + rowDiff;
                    int newCol = kingCol + colDiff;

                    if (isInBounds(newRow, newCol)) {
                        ChessPiece pieceAtNewPosition = getPiece(newRow, newCol);
                        if (isSlotClear(newRow, newCol) || (pieceAtNewPosition != null && pieceAtNewPosition.getColor() != kingColor)) {
                            ChessPiece tempPiece = board[newRow][newCol];
                            board[kingRow][kingCol] = null;
                            board[newRow][newCol] = king;
                            king.move(newRow, newCol);

                            if (!isCheck(kingColor)) {
                                board[newRow][newCol] = tempPiece;
                                board[kingRow][kingCol] = king;
                                king.move(kingRow, kingCol);
                                return false;
                            }

                            board[newRow][newCol] = tempPiece;
                            board[kingRow][kingCol] = king;
                            king.move(kingRow, kingCol);
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placePiece(ChessPiece chessPiece){
        board[chessPiece.getRow()][chessPiece.getCol()] = chessPiece;
    }

    public void placePieces(ArrayList<ChessPiece> chessPieces){
        for (ChessPiece chessPiece : chessPieces)
            placePiece(chessPiece);
    }

    public boolean isPathClear(int startRow, int startCol, int endRow, int endCol) {
        int rowStep = 0;
        int colStep = 0;

        if (endRow > startRow) {
            rowStep = 1;
        } else if (endRow < startRow) {
            rowStep = -1;
        }

        if (endCol > startCol) {
            colStep = 1;
        } else if (endCol < startCol) {
            colStep = -1;
        }

        int currentRow = startRow + rowStep;
        int currentCol = startCol + colStep;

        while (currentRow != endRow || currentCol != endCol) {
            if (!isSlotClear(currentRow, currentCol)) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }

    public ChessPiece getPiece(int row, int col){
        return board[row][col];
    }
    public boolean isSlotClear(int row, int col) {
        return board[row][col] == null;
    }

    public void print() {
        print(null);
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void print(Player player) {
        System.out.println("+--------------------+");
        System.out.print  ("|   a b c d e f g h  |\n");
        for (int row = 7; row >= 0; --row) {
            System.out.printf("| %d ", row + 1);
            for (int col = 0; col < 8; ++col) {
                char icon = (board[row][col] == null) ? '-' : board[row][col].getIcon();
                if (player == null || player.getColor() == board[row][col].getColor())
                    System.out.printf("%c ", icon);
                else
                    System.out.print("- ");
            }
            System.out.print(" |\n");
        }
        System.out.println("+--------------------+");
    }
}