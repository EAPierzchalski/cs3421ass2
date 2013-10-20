package ass2.engine.view.render.modeldrawer.roadDrawer;

import ass2.engine.model.components.Road;
import ass2.engine.model.components.Terrain;
import ass2.engine.view.render.DrawUtil;
import ass2.engine.view.render.GeometryUtil;
import ass2.engine.view.render.modeldrawer.ComponentDrawer;
import ass2.engine.view.textures.Texture;

import javax.media.opengl.GL2;
import java.util.List;

/**
 * User: Pierzchalski
 * Date: 19/10/13
 * Package: ass2.engine.view.render.modeldrawer.roadDrawer
 * Project: cs3421ass2
 */
public class RoadDrawer implements ComponentDrawer {
    private static final int NUM_POINTS_PER_SPLINE = 10;

    private static final float ROAD_SHININESS = 200;

    private static final String ROAD_TEXTURE_FILE_SRC = "src/main/resources/textures/BrightPurpleMarble.png";
    private static final String ROAD_TEXTURE_FILE_TYPE = "png";
    private Texture roadTexture;

    private double[][][][] roadFaceVertices;
    private double[][][][] roadFaceNormals;
    private double[][][][] roadFaceTexCoords;

    private Terrain terrain;

    public RoadDrawer(Terrain terrain) {
        List<Road> roads = terrain.roads();
        roadFaceVertices = new double[roads.size()][][][];
        roadFaceNormals = new double[roads.size()][][][];
        roadFaceTexCoords = new double[roads.size()][][][];
        for (int roadIndex = 0; roadIndex < roads.size(); roadIndex++) {
            Road road = roads.get(roadIndex);
            double[][] spinePoints = spinePoints(road, terrain);
            double[][] tangents = spineTangents(road);
            double[][][][] faceData = GeometryUtil.extrude(spinePoints, tangents, road.width());
            roadFaceVertices[roadIndex] = faceData[0];
            roadFaceNormals[roadIndex] = faceData[1];
            roadFaceTexCoords[roadIndex] = faceData[2];
        }
        this.terrain = terrain;
    }

    public void init(GL2 gl) {
        roadTexture = new Texture(gl, ROAD_TEXTURE_FILE_SRC, ROAD_TEXTURE_FILE_TYPE);
    }

    public void draw(GL2 gl) {
        gl.glPolygonOffset(-1, -1);
        gl.glPushMatrix(); {
            for (int roadIndex = 0; roadIndex < roadFaceVertices.length; roadIndex++) {
                DrawUtil.drawMesh3d(
                        gl,
                        roadTexture,
                        ROAD_SHININESS,
                        roadFaceVertices[roadIndex],
                        roadFaceNormals[roadIndex],
                        roadFaceTexCoords[roadIndex]
                );
            }
        } gl.glPopMatrix();
        gl.glPolygonOffset(0, 0);
    }

    private double[][] spinePoints(Road road, Terrain terrain) {
        double[] start = road.point(0);
        double height = terrain.altitude(start[0], start[1]);
        int numPoints = NUM_POINTS_PER_SPLINE * road.size() + 1;
        double[][] spinePoints = new double[numPoints][];
        for (int splineIndex = 0; splineIndex < road.size(); splineIndex++) {
            for (int i = 0; i < NUM_POINTS_PER_SPLINE; i++) {
                double t = splineIndex + ((double) i)/NUM_POINTS_PER_SPLINE;
                double[] p = road.point(t);
                spinePoints[splineIndex * road.size() + i] = new double[] {
                        p[0],
                        height,
                        p[1]
                };
            }
        }
        //the last point is special
        double[] lastPoint = road.point(road.size());
        spinePoints[spinePoints.length - 1] = new double[] {
                lastPoint[0],
                height,
                lastPoint[1]
        };
        return spinePoints;
    }

    private double[][] spineTangents(Road road) {
        int numPoints = NUM_POINTS_PER_SPLINE * road.size() + 1;
        double[][] spineTangents = new double[numPoints][];
        for (int splineIndex = 0; splineIndex < road.size(); splineIndex++) {
            for (int i = 0; i < NUM_POINTS_PER_SPLINE; i++) {
                double t = splineIndex + ((double) i)/NUM_POINTS_PER_SPLINE;
                double[] p = road.tangent(t);
                spineTangents[splineIndex * road.size() + i] = new double[] {
                        p[0],
                        0,
                        p[1]
                };
            }
        }
        //the last point is special
        double[] lastPoint = road.tangent(road.size());
        spineTangents[spineTangents.length - 1] = new double[] {
                lastPoint[0],
                0,
                lastPoint[1]
        };
        return spineTangents;
    }
}
