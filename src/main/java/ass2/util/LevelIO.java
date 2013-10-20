package ass2.util;

import ass2.engine.model.components.Terrain;
import ass2.engine.model.components.Road;
import ass2.engine.model.components.Tree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * COMMENT: Comment LevelIO 
 *
 * @author malcolmr
 */
public class LevelIO {

    /**
     * Load a terrain object from a JSON file
     *
     * @param mapFile
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static Terrain load(File mapFile) throws FileNotFoundException, JSONException {

        Reader in = new FileReader(mapFile);
        JSONTokener jtk = new JSONTokener(in);
        JSONObject jsonTerrain = new JSONObject(jtk);

        int width = jsonTerrain.getInt("width");
        int depth = jsonTerrain.getInt("depth");
        Dimension terrainDimension = new Dimension(width, depth);

        JSONArray jsonSun = jsonTerrain.getJSONArray("sunlight");
        float dx = (float)jsonSun.getDouble(0);
        float dy = (float)jsonSun.getDouble(1);
        float dz = (float)jsonSun.getDouble(2);
        float[] terrainSun = new float[]{dx, dy, dz, 0};

        JSONArray jsonAltitude = jsonTerrain.getJSONArray("altitude");
        double[][] terrainAltitude = new double[width][depth];
        for (int i = 0; i < jsonAltitude.length(); i++) {
            int x = i % width;
            int z = i / width;

            double h = jsonAltitude.getDouble(i);
            terrainAltitude[x][z] = h;
        }

        List<Tuple2<Double, Double>> terrainTreePositions = new ArrayList<Tuple2<Double, Double>>();
        if (jsonTerrain.has("trees")) {
            JSONArray jsonTrees = jsonTerrain.getJSONArray("trees");
            for (int i = 0; i < jsonTrees.length(); i++) {
                JSONObject jsonTree = jsonTrees.getJSONObject(i);
                double x = jsonTree.getDouble("x");
                double z = jsonTree.getDouble("z");
                Tuple2<Double, Double> treePosition = new Tuple2<Double, Double>(x, z);
                terrainTreePositions.add(treePosition);
            }
        }

        List<Road> terrainRoads = new ArrayList<Road>();
        if (jsonTerrain.has("roads")) {
            JSONArray jsonRoads = jsonTerrain.getJSONArray("roads");
            for (int i = 0; i < jsonRoads.length(); i++) {
                JSONObject jsonRoad = jsonRoads.getJSONObject(i);
                double w = jsonRoad.getDouble("width");

                JSONArray jsonSpine = jsonRoad.getJSONArray("spine");
                double[] spine = new double[jsonSpine.length()];

                for (int j = 0; j < jsonSpine.length(); j++) {
                    spine[j] = jsonSpine.getDouble(j);
                }
                Road road = new Road(w, spine);
                terrainRoads.add(road);
            }
        }

        return new Terrain(
                terrainDimension,
                terrainAltitude,
                terrainTreePositions,
                terrainRoads,
                terrainSun
        );
    }

    /**
     * Write Terrain to a JSON file
     *
     * @param file
     * @throws java.io.IOException
     */
    public static void save(Terrain terrain, File file) throws IOException, JSONException {
        JSONObject json = new JSONObject();

        Dimension size = terrain.size();
        json.put("width", size.width);
        json.put("depth", size.height);

        JSONArray jsonSun = new JSONArray();
        float[] sunlight = terrain.getSunlightDirection();
        jsonSun.put(sunlight[0]);
        jsonSun.put(sunlight[1]);
        jsonSun.put(sunlight[2]);
        json.put("sunlight", jsonSun);

        JSONArray altitude = new JSONArray();
        for (int i = 0; i < size.width; i++) {
            for (int j = 0; j < size.height; j++) {
                altitude.put(terrain.getGridAltitude(i, j));
            }
        }
        json.put("altitude", altitude);

        JSONArray trees = new JSONArray();
        for (Tree t : terrain.trees()) {
            JSONObject j = new JSONObject();
            double[] position = t.getPosition();
            j.put("x", position[0]);
            j.put("z", position[2]);
            trees.put(j);
        }
        json.put("trees", trees);

        JSONArray roads = new JSONArray();
        for (Road r : terrain.roads()) {
            JSONObject j = new JSONObject();
            j.put("width", r.width());

            JSONArray spine = new JSONArray();
            int n = r.size();

            for (int i = 0; i <= n*3; i++) {
                double[] p = r.controlPoint(i);
                spine.put(p[0]);
                spine.put(p[1]);
            }
            j.put("spine", spine);
            roads.put(j);
        }
        json.put("roads", roads);

        FileWriter out = new FileWriter(file);
        json.write(out);
        out.close();

    }

    /**
     * For testing.
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, JSONException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        LevelIO.save(terrain, new File(args[1]));
    }

}
