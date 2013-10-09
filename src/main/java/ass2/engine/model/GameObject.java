package ass2.engine.model;

import ass2.util.MathUtil;

import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.List;


/**
 * A GameObject is an object that can move around in the game world.
 * 
 * GameObjects form a scene tree. The root of the tree is the special ROOT object.
 * 
 * Each GameObject is offset from its parent by a rotation, a translation and a scale factor. 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class GameObject {

    // the list of all GameObjects in the scene tree
    public final static List<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
    
    // the root of the scene tree
    public final static GameObject ROOT = new GameObject();
    
    // the links in the scene tree
    private GameObject myParent;
    private List<GameObject> myChildren;

    // the local transformation
    private double myRotation;
    private double myScale;
    private double[] myTranslation;
    
    // is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special private constructor for creating the root node. Do not use otherwise.
     */
    private GameObject() {
        myParent = null;
        myChildren = new ArrayList<GameObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new double[2];
        myTranslation[0] = 0;
        myTranslation[1] = 0;

        amShowing = true;
        
        ALL_OBJECTS.add(this);
    }

    /**
     * Public constructor for creating GameObjects, connected to a parent (possibly the ROOT).
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public GameObject(GameObject parent) {
        myParent = parent;
        myChildren = new ArrayList<GameObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new double[2];
        myTranslation[0] = 0;
        myTranslation[1] = 0;

        // initially showing
        amShowing = true;

        ALL_OBJECTS.add(this);
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
        for (GameObject child : myChildren) {
            child.destroy();
        }
        
        myParent.myChildren.remove(this);
        ALL_OBJECTS.remove(this);
    }

    /**
     * Get the parent of this game object
     * 
     * @return
     */
    public GameObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<GameObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public double getRotation() {
        return MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(double rotation) {
        myRotation = rotation;
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(double angle) {
        myRotation += angle;
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public double getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(double scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(double factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public double[] getPosition() {
        double[] t = new double[2];
        t[0] = myTranslation[0];
        t[1] = myTranslation[1];

        return t;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        myTranslation[0] = x;
        myTranslation[1] = y;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(double dx, double dy) {
        myTranslation[0] += dx;
        myTranslation[1] += dy;
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object. This method is called once per frame. 
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(double dt) {
        // do nothing
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL2 gl) {
        // do nothing
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * TODO: Complete this method
     * 
     * @param gl
     */
    public void draw(GL2 gl) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }

        // TODO: draw the object and all its children recursively
        // setting the model transform appropriately 
    
        // Call drawSelf() to draw the object itself
        gl.glMatrixMode(GL2.GL_MODELVIEW); {
            gl.glPushMatrix(); {
                gl.glTranslated(myTranslation[0], myTranslation[1], 0);
                gl.glScaled(myScale, myScale, 1);
                gl.glRotated(myRotation, 0, 0, 1);
                drawSelf(gl);
                for (GameObject child : myChildren) {
                    child.draw(gl);
                }
            } gl.glPopMatrix();
        }
        
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * TODO: Write this method
     * 
     * @return a point in world coordinats in [x,y] form
     */
    public double[] getGlobalPosition() {
        double[] globalPosition = new double[2];
        if (myParent == null) {
            globalPosition[0] = myTranslation[0];
            globalPosition[1] = myTranslation[1];
        } else {
            double[][] parentsGlobalMatrix = myParent.getGlobalMatrix();
            double[] positionPoint = new double[3];
            positionPoint[0] = myTranslation[0];
            positionPoint[1] = myTranslation[1];
            positionPoint[2] = 1;
            double[] globalPositionPoint = MathUtil.multiply(parentsGlobalMatrix, positionPoint);
            globalPosition[0] = globalPositionPoint[0];
            globalPosition[1] = globalPositionPoint[1];
        }
        return globalPosition;
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * TODO: Write this method
     * 
     * @return the global rotation of the object (in degrees) 
     */
    public double getGlobalRotation() {
        double rotation;
        if (myParent == null) {
            rotation = myRotation;
        } else {
            rotation = MathUtil.normaliseAngle(myParent.getGlobalRotation() + myRotation);
        }
        return rotation;
    }

    /**
     * Compte the object's scale in global terms
     * 
     * TODO: Write this method
     * 
     * @return the global scale of the object 
     */
    public double getGlobalScale() {
        double scale;
        if (myParent == null) {
            scale = myScale;
        } else {
            scale = myParent.getGlobalScale() * myScale;
        }
        return scale;
    }

    /**
     * Change the parent of a game object.
     * 
     * TODO: add code so that the object does not change its global position, rotation or scale
     * when it is reparented. 
     * 
     * @param newParent
     */
    public void setParent(GameObject newParent) {

        GameObject oldParent = this.myParent;

        double oldLocalRotation = this.getRotation();
        double newLocalRotation = MathUtil.normaliseAngle(oldParent.getGlobalRotation() - newParent.getGlobalRotation() + oldLocalRotation);

        double oldLocalScale = this.getScale();
        double newLocalScale = oldParent.getGlobalScale() * oldLocalScale / newParent.getGlobalScale();

        double[] globalPosition = this.getGlobalPosition();

        double[] globalPositionPoint = new double[3];
        globalPositionPoint[0] = globalPosition[0];
        globalPositionPoint[1] = globalPosition[1];
        globalPositionPoint[2] = 1;

        double[] newLocalPositionPoint = MathUtil.multiply(newParent.getInverseGlobalMatrix(), globalPositionPoint);

        double[] newLocalPosition = new double[2];
        newLocalPosition[0] = newLocalPositionPoint[0];
        newLocalPosition[1] = newLocalPositionPoint[1];

        myParent.myChildren.remove(this);
        myParent = newParent;
        myParent.myChildren.add(this);

        this.myTranslation = newLocalPosition;
        this.myRotation = newLocalRotation;
        this.myScale = newLocalScale;
    }

    public double[][] getGlobalMatrix() {
        double[] myGlobalPosition = this.getGlobalPosition();
        double[][] myGlobalTranslationMatrix = MathUtil.translationMatrix(myGlobalPosition);

        double myGlobalRotation = this.getGlobalRotation();
        double[][] myGlobalRotationMatrix = MathUtil.rotationMatrix(myGlobalRotation);

        double myGlobalScale = this.getGlobalScale();
        double[][] myGlobalScaleMatrix = MathUtil.scaleMatrix(myGlobalScale);

        double[][] m = MathUtil.getIdentity();
        m = MathUtil.multiply(m, myGlobalTranslationMatrix);
        m = MathUtil.multiply(m, myGlobalRotationMatrix);
        m = MathUtil.multiply(m, myGlobalScaleMatrix);
        return m;
    }

    public double[][] getInverseGlobalMatrix() {
        double[] globalPosition = this.getGlobalPosition();
        double[] inverseTranslation = new double[2];
        inverseTranslation[0] = -globalPosition[0];
        inverseTranslation[1] = -globalPosition[1];
        double[][] myInverseGlobalTranslationMatrix = MathUtil.translationMatrix(inverseTranslation);

        double myInverseRotation = -this.getGlobalRotation();
        double[][] myInverseGlobalRotationMatrix = MathUtil.rotationMatrix(myInverseRotation);

        double myInverseScale = 1d / this.getGlobalScale();
        double[][] myInverseGlobalScaleMatrix = MathUtil.scaleMatrix(myInverseScale);

        double[][] m = MathUtil.getIdentity();
        m = MathUtil.multiply(m, myInverseGlobalScaleMatrix);
        m = MathUtil.multiply(m, myInverseGlobalRotationMatrix);
        m = MathUtil.multiply(m, myInverseGlobalTranslationMatrix);
        return m;
    }

}
