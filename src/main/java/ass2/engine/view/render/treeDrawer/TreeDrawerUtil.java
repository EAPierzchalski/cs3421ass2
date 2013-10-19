package ass2.engine.view.render.treeDrawer;

import ass2.engine.view.render.GeometryUtil;

/**
 * User: Pierzchalski
 * Date: 17/10/13
 * Package: ass2.engine.view.render.treeDrawer
 * Project: cs3421ass2
 */

public class TreeDrawerUtil {
    private static final double TREE_POLE_RADIUS = 0.05;
    private static final int NUM_RADIAL_SEGMENTS_POLE = 20;
    private static final int NUM_VERTICAL_SEGMENTS_POLE = 4;
    private static final double TREE_HEIGHT = 1.2;

    private static final double[][][][] treeCylinder = GeometryUtil.cylinder(
            TREE_POLE_RADIUS,
            TREE_HEIGHT,
            NUM_RADIAL_SEGMENTS_POLE,
            NUM_VERTICAL_SEGMENTS_POLE
    );

    public static final double[][][] treeCylinderVertices = treeCylinder[0];
    public static final double[][][] treeCylinderNormals = treeCylinder[1];
    public static final double[][][] treeCylinderTexCoords = treeCylinder[2];

}
