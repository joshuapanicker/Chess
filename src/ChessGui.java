import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChessGui {
    private static final Color whiteSlotColor = new Color(0xff, 0xe8, 0xd4);
    private static final Color blackSlotColor = new Color(0xbc, 0x9f, 0x8b);
    private Game game;
    private JPanel gui;
    private JPanel chessBoardPanel;
    private JButton[][] chessBoardSquares;
    private JPanel sidePanel;
    private JTextPane gameMoves;
    private JTextPane gameMessages;
    private JLabel message = new JLabel("Chess is ready to play!");
    private String preferredDir = "";
    private Font titleFont, txtAreaFont;
    private boolean gameOver = false;
    private boolean gameLoaded = false;
    private ChessPiece selected;

    ChessGui() {
        init();
    }

    private void init() {
        gui = new JPanel(new BorderLayout(3, 3));
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        initFonts();
        initToolBar();
        initChessBoard();
        initSidePanel();
    }

    private void initFonts() {
        txtAreaFont = new Font("Callibri", Font.BOLD, 18);
        titleFont = new Font("Callibri", Font.BOLD, 20);
    }

    private void initToolBar() {
        JToolBar toolBar = new JToolBar();

        toolBar.setFloatable(false);

        Action loadGameAction = new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        };
        toolBar.add(loadGameAction);

        Action saveGameAction = new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        };
        toolBar.add(saveGameAction);

        Action restoreGameAction = new AbstractAction("Restore") {
            @Override
            public void actionPerformed(ActionEvent e) { restoreGame(); }
        };
        toolBar.add(restoreGameAction);

        Action startGameAction = new AbstractAction("Start") {
            @Override
            public void actionPerformed(ActionEvent e) { startGame(); }
        };
        toolBar.add(startGameAction);

        Action playNextAction = new AbstractAction("Next Play") {
            @Override
            public void actionPerformed(ActionEvent e) { nextPlay(); }
        };
        toolBar.add(playNextAction);

        toolBar.addSeparator();
        toolBar.add(message);
        gui.add(toolBar, BorderLayout.PAGE_START);
    }

    private class ChessBoardSquareActionListener implements ActionListener {
        private int row, col;

        private ChessBoardSquareActionListener(int row, int col) {
            super();
            this.row = row;
            this.col = col;
        }

        public void actionPerformed(ActionEvent e) {
            message.setText("Selected row:" + row + " col: " + col);
            if (selected != null) {
                ChessPiece origPiece = game.getChessBoard().getPiece(row, col);
                boolean capture = false;

                if (origPiece != null &&  origPiece.getColor() != selected.getColor())
                    capture = true;

                if (selected.canMoveTo(game.getChessBoard(), row, col, capture)) {
                    game.move(selected, row, col);
                    updateChessBoard();
                }
                selected = null;
            }
            else {
                selected = game.getChessBoard().getPiece(row, col);
            }
        }
    }

    private void initChessBoard() {
        chessBoardPanel = new JPanel(new GridLayout(0, 10)) {
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();

                if (c == null)
                    prefSize = new Dimension((int)d.getWidth(), (int)d.getHeight());
                else if (c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight())
                    prefSize = c.getSize();
                else
                    prefSize = d;
                int s = Math.min((int)prefSize.getWidth(), (int)prefSize.getHeight());
                return new Dimension(s,s);
            }
        };
        chessBoardPanel.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
        ));
        chessBoardPanel.setBackground(Color.GRAY);

        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(Color.WHITE);
        boardConstrain.add(chessBoardPanel);
        gui.add(boardConstrain, BorderLayout.LINE_START);

        chessBoardSquares = new JButton[8][8];
        Insets buttonMargin = new Insets(0, 0, 0, 0);

        for (int row = 0; row < chessBoardSquares.length; row++) {
            Color color = (row % 2 == 0) ? blackSlotColor : whiteSlotColor;

            for (int col = 0; col < chessBoardSquares[row].length; col++) {
                JButton b = new JButton();

                b.setMargin(buttonMargin);

                ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                b.setBackground(color);
                chessBoardSquares[row][col] = b;
                b.addActionListener(new ChessBoardSquareActionListener(row, col));
                color = (color == blackSlotColor) ? whiteSlotColor : blackSlotColor;
            }
        }

        chessBoardPanel.add(new JLabel(""));
        char colId = 'A';
        for (int ii = 0; ii < 8; ii++, colId++)
            chessBoardPanel.add(new JLabel("" + colId, SwingConstants.CENTER));
        chessBoardPanel.add(new JLabel(""));

        char rowId = '8';
        for (int row = 0; row < 8; row++) {
            chessBoardPanel.add(new JLabel("" + rowId , SwingConstants.CENTER));
            for (int col = 0; col < 8; col++)
                chessBoardPanel.add(chessBoardSquares[7 - row][col]);
            chessBoardPanel.add(new JLabel("" + rowId , SwingConstants.CENTER));
            rowId--;
        }

        chessBoardPanel.add(new JLabel(""));
        colId = 'A';
        for (int ii = 0; ii < 8; ii++, colId++)
            chessBoardPanel.add(new JLabel("" + colId, SwingConstants.CENTER));
        chessBoardPanel.add(new JLabel(""));
    }

    private void initSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));

        JLabel movesLabel = new JLabel("Game Moves");
        movesLabel.setFont(titleFont);
        movesLabel.setForeground(Color.GRAY);
        sidePanel.add(movesLabel);

        gameMoves = new JTextPane();
        gameMoves.setPreferredSize(new Dimension(200, 20));
        gameMoves.setFont(txtAreaFont);
        gameMoves.setBackground(Color.GRAY);
        JScrollPane scrollPane = new JScrollPane(gameMoves);
        gameMoves.setEditable(false);


        sidePanel.add(scrollPane);
        JLabel messagesLabel = new JLabel("Game Messages");
        messagesLabel.setFont(titleFont);
        messagesLabel.setForeground(Color.GRAY);
        sidePanel.add(messagesLabel);

        gameMessages = new JTextPane();
        gameMessages.setPreferredSize(new Dimension(200, 50));
        gameMessages.setFont(txtAreaFont);
        gameMessages.setBackground(Color.LIGHT_GRAY);
        JScrollPane messageScrollPane = new JScrollPane(gameMessages);
        gameMessages.setEditable(false);

        sidePanel.add(messageScrollPane);

        gui.add(sidePanel, BorderLayout.LINE_END);
    }

    public final JComponent getGui() {
        return gui;
    }

    private void updateChessBoard() {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                ChessPiece chessPiece = game.getChessBoard().getPiece(row, col);
                if (chessPiece != null)
                    chessBoardSquares[row][col].setIcon(new ImageIcon(chessPiece.getIconImage()));
                else
                    chessBoardSquares[row][col].setIcon(null);
            }
        }
    }

    private void loadGame() {
        File dir;

        message.setText("Game loading...");
        if (preferredDir.isEmpty())
            dir = FileSystemView.getFileSystemView().getHomeDirectory();
        else
            dir = FileSystemView.getFileSystemView().createFileObject(preferredDir);

        JFileChooser jfc = new JFileChooser(dir);
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("PGN chess game files", "pgn"));
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();

            preferredDir = selectedFile.getParent();
            gameLoaded = true;

            game = new Game();
            game.load(fileName);
            updateChessBoard();
            message.setText("Game is loaded from " + fileName);
        }
    }

    private void saveGame() {
        if (game == null) {
            message.setText("Game can not be saved; no game in progress");
            return;
        }

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();

            game.save(fileName);
            message.setText("Game is saved to file " + fileName);
        }
    }

    private void restoreGame() {
        if (game == null) {
            message.setText("Game is not restored; please load a PGN game file");
            return;
        }
        gameLoaded = false;
        game.restore();
        updateChessBoard();
        message.setText("Restored the game");
    }

    public void addColoredText(JTextPane pane, String text, Color color) {
        StyledDocument doc = pane.getStyledDocument();

        Style style = pane.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        message.setText("Game is starting now");
        gameMoves.setText("");
        game = new Game();
        updateChessBoard();
    }

    private void nextPlayFromFile() {
        ChessMove move = game.replayNext();
        if (move != null) {
            String colorStr = move.getColor() == ChessPiece.WHITE ? "White" : "Black";
            String moveStr = "" + move.getMoveNum() + "." + move.getNotationStr() + "\n";

            updateChessBoard();
            message.setText(colorStr + " moves " + move.getNotationStr());
            if (move.color == ChessPiece.WHITE)
                addColoredText(gameMoves, moveStr, whiteSlotColor);
            else
                addColoredText(gameMoves, moveStr, blackSlotColor);
        }
    }

    public void nextPlay() {
        if (gameLoaded) {
            nextPlayFromFile();
            return;
        }
        message.setText("Computer as Player not yet implemented");
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ChessGui chessGui = new ChessGui();

                JFrame frame = new JFrame("Chess");
                frame.add(chessGui.getGui());

                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                frame.setLocationByPlatform(true);

                frame.pack();
                frame.setMinimumSize(frame.getSize());

                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
