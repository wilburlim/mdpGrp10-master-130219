
package mdp.common;


public enum Direction {
    Up, Left, Down, Right;
    private Direction _getWithOffset(int offset) {
        return values()[(this.ordinal() + offset + values().length) % values().length];
    }
    public Direction getLeft() {
        return _getWithOffset(1);
    }
    public Direction getRight() {
        return _getWithOffset(-1);
    }
    public Direction getBehind() {
        return _getWithOffset(2);
    }
    public Vector2 toVector2() {
        switch (this) {
            case Up: return new Vector2(-1, 0);
            case Down: return new Vector2(1, 0);
            case Left: return new Vector2(0, -1);
            case Right: return new Vector2(0, 1);
            default: return new Vector2(0, 0);
        }
    }
}