package ass2.engine.view.render.modeldrawer;

import javax.media.opengl.GL2;

/**
 * User: Pierzchalski
 * Date: 19/10/13
 * Package: ass2.engine.view.render.modeldrawer
 * Project: cs3421ass2
 */
public interface ComponentDrawer {
    public void init(GL2 gl);
    public void draw(GL2 gl);
}
