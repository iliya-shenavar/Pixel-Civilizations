public interface Inter {
    Object[][] data = new Object[4][4];

    void movePlayer(int dx, int dy);

    void updateData(int row, int col, Object value);
    void playerMoved(int x, int y);
}
