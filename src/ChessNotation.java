import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ChessNotation {
    private ChessBoard chessBoard;
    private HashMap<String, String> metadata;
    private ArrayList<ChessMove> chessMoves;
    private boolean draw;
    private int winner;

    public ChessNotation() {
        this.chessBoard = new ChessBoard();
        this.metadata = new HashMap<String, String>();
        this.chessMoves = new ArrayList<ChessMove>();
        this.winner = -1;
        this.draw = false;
    }

    public void loadGame(String gameFileName) {
        Game.initChessBoard(chessBoard);

        try {
            File gameFile = new File(gameFileName);
            Scanner input = new Scanner(gameFile);

            input.useDelimiter(" +");
            while (input.hasNext()) {
                String moveStr = input.next();

                if (moveStr.charAt(0) == '[') {
                    String key = moveStr.substring(1), value = "";

                    if (input.hasNext()) {
                        String v = input.nextLine();
                        value = v.substring(0, v.length() - 1);
                        metadata.put(key, value);
                    }
                    continue;
                }

                if (moveStr.equals("1-0")) {
                    draw = false;
                    winner = ChessPiece.WHITE;
                    continue;
                }
                else if (moveStr.equals("0-1")) {
                    draw = false;
                    winner = ChessPiece.BLACK;
                    continue;
                } if (moveStr.equals("1/2-1/2")) {
                    draw = true;
                    continue;
                }

                if (input.hasNext())
                    moveStr = moveStr + " " + input.next();
                if (input.hasNext())
                    moveStr = moveStr + " " + input.next();
                chessMoves.addAll(translateMove(moveStr));
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.printf("Error: File %s not found\n", gameFileName);
            e.printStackTrace();
        }
    }

    public void saveGame(String gameFileName) {
    }

    static public ArrayList<ChessMove> translateMove(ChessBoard board, int moveNum, int color, String notationStr) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        ChessPiece chessPiece = null;
        boolean captured = false, check, checkMate;

        if (notationStr == null || notationStr.isEmpty())
            return null;

        if (notationStr.equals("O-O") || notationStr.equals("0-0")) {
            move = new ChessMove(moveNum, color, notationStr, 'K');
            move.castleKingSideSetKingSlot();
            moves.add(move);
            board.move(move);

            move = new ChessMove(moveNum, color, notationStr, 'R');
            move.castleKingSideSetRookSlot();
            moves.add(move);
            board.move(move);
            return moves;
        }

        if (notationStr.equals("O-O-O") || notationStr.equals("0-0-0")) {
            move = new ChessMove(moveNum, color, notationStr, 'K');
            move.castleQueenSideSetKingSlot();
            moves.add(move);
            board.move(move);

            move = new ChessMove(moveNum, color, notationStr, 'R');
            move.castleQueenSideSetRookSlot();
            moves.add(move);
            board.move(move);

            return moves;
        }

        char icon = notationStr.charAt(0);
        if ("RNBQK".indexOf(icon) == -1) {
            int origCol = icon - 'a';

            icon = 'P';
            chessPiece = board.findPieceAtCol(color, icon, origCol);
        }

        int index = notationStr.length() - 1;

        check = notationStr.charAt(index) == '+';
        checkMate = notationStr.charAt(index) == '#';
        if (check || checkMate)
            index--;

        int newRow = notationStr.charAt(index) - '1';
        int newCol = notationStr.charAt(index - 1) - 'a';

        index -= 2;
        if (index > 0) {
            captured = notationStr.charAt(index) == 'x';
            if (captured)
                index--;
            if (index > 0 && icon != 'P') {
                int origCol = "abcdefgh".indexOf(notationStr.charAt(index));
                int origRow = "12345678".indexOf(notationStr.charAt(index));

                if (origCol != -1) {
                    origCol = notationStr.charAt(index) - 'a';
                    chessPiece = board.findPieceAtCol(color, icon, origCol);
                }
                else if (origRow != -1) {
                    origRow = notationStr.charAt(index) - '1';
                    chessPiece = board.findPieceAtRow(color, icon, origRow);
                }
            }
        }

        if (chessPiece == null)
            chessPiece = board.findPiece(color, icon, newRow, newCol, captured);

        move = new ChessMove(moveNum, notationStr, chessPiece);
        move.setSlot(newRow, newCol, captured, check, checkMate);
        moves.add(move);
        board.move(move);

        return moves;
    }

    public ArrayList<ChessMove> translateMove(String notationStr) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        String[] moveStr = notationStr.trim().split(" ");

        if (moveStr.length < 2)
            return moves;

        int moveNum = Integer.parseInt(moveStr[0].substring(0, moveStr[0].length() - 1)) - 1;

        moves = translateMove(chessBoard, moveNum, ChessPiece.WHITE, moveStr[1]);
        if (moveStr.length == 3)
            moves.addAll(translateMove(chessBoard, moveNum, ChessPiece.BLACK, moveStr[2]));
        return moves;
    }

    public ArrayList<ChessMove> getMoves() {
        return chessMoves;
    }

    public ChessMove getMove(int index) {
        if (index >= chessMoves.size())
            return null;
        return chessMoves.get(index);
    }

    public int getMoveCount() {
        return chessMoves.size();
    }

    public void printMoves() {
        for (int i = 0; i < chessMoves.size(); ++i)
            chessMoves.get(i).print();
    }
}
