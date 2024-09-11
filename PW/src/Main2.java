import assets.SocketManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

public class Main2 extends JFrame implements Inter {

    private JLabel selectedPlayer;
    private static boolean addingPlayer = false;

    private final Random random = new Random(95); // Set a specific seed

    public static void main(String[] args) {
        SocketManagerTest2 socketManagerTest2 = new SocketManagerTest2();
        Main2 mainInstance2 = new Main2();
        socketManagerTest2.setMain2Instance(mainInstance2);
        System.out.println(Inter.data.toString());
    }
    Main2() {

        super("Player2");
        improveUI();
        getContentPane().setPreferredSize(new Dimension(1080, 720));
        pack();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Fill screen with tiles
        int x = 0;
        int y = 0;
        int width = 40;
        int height = 40;
        int numberOfTiles = 1080 * 720 / (width * height);

        // Add buttons for player movement
        addMovementButtons();
        // Fill screen with tiles
        for (int i = 0; i < numberOfTiles; i++) {

            if (random.nextDouble() < 0.1) {
                int randomType = random.nextInt(10);
                int randomResIndex = switch (randomType) {
                    case 0, 1, 2, 3, 4 -> random.nextInt(3) + 4;
                    case 5, 6 -> random.nextInt(3) + 10;
                    case 7 -> random.nextInt(3) + 18;
                    case 8 -> 13;
                    case 9 -> 27;
                    default -> 0;
                };
                String randomResIndexString = randomResIndex < 10 ? "0" + randomResIndex : "" + randomResIndex;
                ImageIcon randomObject = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/" + randomResIndexString + "_Tile-set - Toen's Medieval Strategy (16x16) - v.1.0.png")));
                randomObject.setImage(randomObject.getImage().getScaledInstance(40, 40, 0));
                JLabel object = new JLabel(randomObject);
                object.setBounds(x, y, width, height);
                add(object);
            }

            // Add ground tiles
            int grass = ((random.nextDouble() < 0.25) ? 1 : 0) * 2;
            ImageIcon randomTile = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/0" + ((i % 2) + grass) + "_Tile-set - Toen's Medieval Strategy (16x16) - v.1.0.png")));
            randomTile.setImage(randomTile.getImage().getScaledInstance(40, 40, 0));

            JLabel tile = new JLabel(randomTile);
            tile.setBounds(x, y, width, height);
            add(tile);

            x += width;
            if (x >= 1080) {
                x = 0;
                y += height;
            }
        }

        // Add mouse listener for player creation on click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selectPlayer(e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    
                    addNewPlayer(e.getX(), e.getY());

                    addingPlayer = true;

                }
            }
        });

        repaint();
    }

    private void selectPlayer(int x, int y) {
        Component component = findComponentAt(x, y);
        if (component instanceof JLabel) {
            selectedPlayer = (JLabel) component;
        }
    }

    private void addMovementButtons() {
        JButton upButton = new JButton("Up");
        JButton downButton = new JButton("Down");
        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");

        // Add action listeners to buttons
        upButton.addActionListener(e -> movePlayer(0, -1));
        downButton.addActionListener(e -> movePlayer(0, 1));
        leftButton.addActionListener(e -> movePlayer(-1, 0));
        rightButton.addActionListener(e -> movePlayer(1, 0));

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);

        // Create a new JFrame for buttons
        JFrame buttonFrame = new JFrame("Player Controls");
        buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buttonFrame.getContentPane().add(buttonPanel);
        buttonFrame.pack();
        buttonFrame.setLocationRelativeTo(null);
        buttonFrame.setVisible(true);
    }

    void addNewPlayer(int x, int y) {

        ImageIcon playerIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/128_Tile-set - Toen's Medieval Strategy (16x16) - v.1.0.png")));
        playerIcon.setImage(playerIcon.getImage().getScaledInstance(40, 40, 0));
        JLabel newPlayer = new JLabel(playerIcon);
        newPlayer.setBounds(x, y, 40, 40);
        add(newPlayer);
        setComponentZOrder(newPlayer, 0); // Set player to the top

        SwingUtilities.invokeLater(this::repaint);

        // Send the message to the server
        SocketManager.send("add," + x + "," + y);



        addingPlayer = false;
    }
    @Override
    public void movePlayer(int dx, int dy) {
        if (selectedPlayer != null) {
            // Calculate new position
            int newX = selectedPlayer.getX() + dx * selectedPlayer.getWidth();
            int newY = selectedPlayer.getY() + dy * selectedPlayer.getHeight();

            // Check if the new position is within the bounds of the frame
            if (newX >= 0 && newY >= 0 && newX + selectedPlayer.getWidth() <= getWidth() && newY + selectedPlayer.getHeight() <= getHeight()) {
                // Set new position for the selected player
                selectedPlayer.setLocation(newX, newY);
                SwingUtilities.invokeLater(this::repaint);
                SocketManager.send("move," + newX + "," + newY);


                playerMoved(newX, newY);
            }
        }
    }

    private void improveUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateData(int row, int col, Object value) {
        data[row][col] = value;
    }

    @Override
    public void playerMoved(int x, int y) {
        movePlayer(x, y);
    }
}
