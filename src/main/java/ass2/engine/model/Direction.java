package ass2.engine.model;

public enum Direction {
    NORTH(0, 1, 1, 1),
    EAST(1, 1, 1, 0),
    SOUTH(1, 0, 0, 0),
    WEST(0, 0, 0, 1);

    public int[][] quadCorners;

    private Direction(int x1, int z1, int x2, int z2) {
        this.quadCorners = new int[][]{{x1, z1}, {x2, z2}};
    }
}