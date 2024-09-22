public class ChessMove {
    private int moveNum;
    private String notationStr;
    public int color;
    public char icon;
    public int origRow;
    public int origCol;
    private int newRow;
    private int newCol;
    private boolean captured;
    private boolean castlingKingSide;
    private boolean castlingQueenSide;
    private boolean check;
    private boolean checkMate;



    public ChessMove(int moveNum, String notationStr, ChessPiece chessPiece) {
        this.moveNum = moveNum;
        this.notationStr = notationStr;
        this.color = chessPiece.getColor();
        this.icon = chessPiece.getIcon();
        this.origRow = chessPiece.getRow();
        this.origCol = chessPiece.getCol();
        this.check = false;
        this.checkMate = false;
        this.captured = false;
        this.castlingKingSide = false;
        this.castlingQueenSide = false;
    }

    public ChessMove(int moveNum, int color, String notationStr, char icon) {
        this.moveNum = moveNum;
        this.notationStr = notationStr;
        this.color = color;
        this.icon = icon;
        this.origRow = -1;
        this.origCol = -1;
        this.check = false;
        this.checkMate = false;
        this.captured = false;
        this.castlingKingSide = false;
        this.castlingQueenSide = false;
    }

    public String getMoveTypeStr() {
        String moveType = "";

        if (captured)
            moveType = moveType + "capture ";
        else if (castlingKingSide)
            moveType = moveType + "castling king side ";
        else if (castlingQueenSide)
            moveType = moveType + "castling queen side ";
        else if (check)
            moveType = moveType + "check ";
        else if (checkMate)
            moveType = moveType + "checkmate ";
        return moveType + "moves";
    }

    public String getNotationStr() {
        return notationStr;
    }


    public void setNewSlot(int newRow, int newCol, boolean captured, boolean check, boolean checkMate)
    {
        this.newRow = newRow;
        this.newCol = newCol;
        this.captured = captured;
        this.check = check;
        this.checkMate = checkMate;
    }

    public void castleKingSideSetKingSlot() {
        castlingKingSide = true;
        if (color == ChessPiece.WHITE) {
            /* e1 */
            origRow = 0;
            origCol = 4;

            /* g1 */
            newRow = 0;
            newCol = 6;
        }
        else {
            /* e8 */
            origRow = 7;
            origCol = 4;

            /* g8 */
            newRow = 7;
            newCol = 6;
        }
    }

    public void castleKingSideSetRookSlot() {
        castlingKingSide = true;
        if (this.color == ChessPiece.WHITE) {
            /* h1 */
            origRow = 0;
            origCol = 7;

            /* f1 */
            newRow = 0;
            newCol = 5;
        }
        else {
            /* h8 */
            origRow = 7;
            origCol = 7;

            /* f8 */
            newRow = 7;
            newCol = 5;
        }
    }

    public void castleQueenSideSetKingSlot() {
        castlingQueenSide = true;
        if (color == ChessPiece.WHITE) {
            /* e1 */
            origRow = 0;
            origCol = 4;

            /* c1 */
            newRow = 0;
            newCol = 2;
        }
        else {
            /* e8 */
            origRow = 7;
            origCol = 4;

            /* c8 */
            newRow = 7;
            newCol = 2;
        }
    }

    public void castleQueenSideSetRookSlot() {
        castlingQueenSide = true;
        if (color == ChessPiece.WHITE) {
            /* a1 */
            origRow = 0;
            origCol = 0;

            /* d1 */
            newRow = 0;
            newCol = 3;
        }
        else {
            /* a8 */
            origRow = 7;
            origCol = 0;

            /* d8 */
            newRow = 7;
            newCol = 3;
        }
    }

    public void setSlot(int row, int col, boolean captured, boolean check, boolean checkMate) {
        this.newRow = row;
        this.newCol = col;
        this.captured = captured;
        this.check = check;
        this.checkMate = checkMate;
    }

    public void print() {
        System.out.printf("[%3d] %-8s (%s %s %s %c%c => %c%c)\n",
                moveNum + 1,  notationStr,
                ChessPiece.colorToString(color),  getMoveTypeStr(),  ChessPiece.iconToName(icon),
                origCol + 'a', origRow + '1', newCol + 'a', newRow + '1');
    }

    public int getNewRow(){
        return newRow;
    }

    public int getNewCol(){
        return newCol;
    }

    public int getColor() {
        return color;
    }

    public int getMoveNum() {
        return moveNum;
    }
}
