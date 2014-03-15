package game2048;

import java.awt.event.KeyEvent;

public enum Move {
    LEFT(0, -1, "<", KeyEvent.VK_LEFT),
    UP(-1, 0, "^", KeyEvent.VK_UP),
    RIGHT(0, 1, ">", KeyEvent.VK_RIGHT),
    DOWN(1, 0, "v", KeyEvent.VK_DOWN);

    final int dx, dy;
    final String arrow;
    final int keyCode;
    final static Move[] ALL = Move.values();

    private Move(int dx, int dy, String arrow, int keyCode) {
        this.dx = dx;
        this.dy = dy;
        this.arrow = arrow;
        this.keyCode = keyCode;
    }
}
