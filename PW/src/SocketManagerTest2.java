import assets.SocketManager;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SocketManagerTest2 extends JFrame {

    private static SocketManager socketManager;  // Store as a static variable
    private Main mainInstance;
    private Main2 mainInstance2;
    private boolean addingPlayer = false;
    public static void main(String[] args) {
        new SocketManagerTest2();
        System.out.println(Inter.data.toString());
    }

    SocketManagerTest2() {
        try {
            String hostOrJoin = JOptionPane.showInputDialog("Host or join? (h/j)");

            if (hostOrJoin.equals("h")) {
                socketManager = new SocketManager();
                socketManager.host();
            } else if (hostOrJoin.equals("j")) {
                socketManager = new SocketManager();
                socketManager.join();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input");
                return;
            }

            // Add a listener for general messages
            socketManager.addReceiveListener(this::handleMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainInstance(Main mainInstance) {
        this.mainInstance = mainInstance;
        this.mainInstance2 = null;
    }

    public void setMain2Instance(Main2 mainInstance2) {
        this.mainInstance2 = mainInstance2;
        this.mainInstance = null;
    }

    private void handleMessage(String msg) {
        System.out.println("Received message: " + msg);
        String[] parts = msg.split(",");
        String command = parts[0];


        switch (command) {
            case "add": {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                addPlayer(x, y);
               break;
            }
            case "move": {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                movePlayer(x, y);
                break;
            }
        }
    }

    private void addPlayer(int x, int y) {

        if (!addingPlayer) {
            addingPlayer = true;

            if (mainInstance != null) {
                mainInstance.addNewPlayer(x, y);
            } else if (mainInstance2 != null) {
                mainInstance2.addNewPlayer(x, y);
            }

            SwingUtilities.invokeLater(() -> {
                addingPlayer = false;
            });
        }
    }

    private void movePlayer(int x, int y) {
        SwingUtilities.invokeLater(() -> {
            addingPlayer = false;
            if (mainInstance != null) {
                mainInstance.movePlayer(x, y);
                mainInstance.playerMoved(x, y);
            } else if (mainInstance2 != null) {
                mainInstance2.movePlayer(x, y);
                mainInstance2.playerMoved(x, y);
            }
            addingPlayer = false;
        });
    }

    public static SocketManager getSocketManager() {
        return socketManager;
    }
}
