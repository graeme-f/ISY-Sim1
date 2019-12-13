/*
 * The MIT License
 *
 * Copyright 2019 gfoster.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sim.Layers;

import javafx.scene.canvas.GraphicsContext;
import sim.Objects.SimObject;
import sim.Utilities.SimMatrix;

/**
 *
 * @author gfoster
 */
public abstract class MatrixLayer  extends Layer {
    
    public enum Direction {UP, LEFT, DOWN, RIGHT}
    protected SimMatrix m;

    public MatrixLayer(GraphicsContext gContext, double width, double height, int cellWidth) {
    	super(gContext, width, height);
        m = new SimMatrix((int)width/cellWidth+1, (int)height/cellWidth+1);
    }
    
    public void drawLayer() {
        m.drawMatrix(gc);
    }
    
    public void addObject(SimObject object) {
    	m.matrix[object.getx()][object.gety()] = object;
    }

    public SimObject getObject(int x, int y) {
        if (x < 0 || x >= m.getWidth()) return null;
        if (y < 0 || y >= m.getHeight()) return null;
    	return m.matrix[x][y];
    }

    public boolean hasObject(int x, int y) {
        return (getObject(x,y)!= null);
    }

    public SimObject removeObject(int x, int y) {
        SimObject object = getObject(x, y);
        if (object != null){
            m.matrix[x][y] = null;
        }
        return object;
    }
    
    public void clear() {
    	m.clear();
    }
    
    public boolean isEdge(SimObject object, Direction d) {
        switch (d) {
	        case UP:
	        	return object.gety() == 0;
	        case DOWN:
	        	return object.gety() == m.getHeight()-1;
	        case LEFT:
	        	return object.getx() == 0;
	        case RIGHT:
	        	return object.getx() == m.getWidth()-1;
        	default:
        		return false;
        }
    } // end of method isEdge
    
    
    public SimObject getNeighbour(SimObject object, Direction d) {
        if (isEdge(object, d)) {
        	return null;
        }
        switch (d) {
            case UP:
                return m.matrix[object.getx()][object.gety()-1];
            case DOWN:
                return m.matrix[object.getx()][object.gety()+1];
            case LEFT:
                return m.matrix[object.getx()-1][object.gety()];
            case RIGHT:
                return m.matrix[object.getx()+1][object.gety()];
            default:
                return null;
        }
    }

} // end of class Layer
