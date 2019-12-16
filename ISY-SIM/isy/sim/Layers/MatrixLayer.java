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
import sim.Utilities.Posn;
import sim.Utilities.SimMatrix;

/**
 *
 * @author gfoster
 */
public abstract class MatrixLayer  extends Layer {
    
    public enum Direction {UP, LEFT, DOWN, RIGHT}
    protected SimMatrix m;

    public MatrixLayer(GraphicsContext gContext, double width, double height, double horizScale, double vertScale, int cellWidth) {
    	super(gContext, width, height, horizScale, vertScale);
        m = new SimMatrix((int)width/cellWidth, (int)height/cellWidth);
    }
    
    public void drawLayer() {
        m.drawMatrix();
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

    public Posn toScreenCoordiantes(Posn p){
        return toScreenCoordiantes(p.getX(), p.getY());
    }

    public Posn toScreenCoordiantes(double x, double y){
        return new Posn(x*layerWidth/m.getHeight(), y*layerHeight/m.getHeight());
    }
    
    public Posn toLayerCoordinates(Posn p){
        return toLayerCoordinates((int)p.getX(), (int)p.getY());
    }
    public Posn toLayerCoordinates(int x, int y){
        return new Posn(x*m.getHeight()/layerWidth, y*m.getHeight()/layerHeight);
    }

    public Posn getVacantArea(double d_x, double d_y){
        int x = (int)(d_x+.5);
        int y = (int)(d_y+0.5);
        if (m.matrix[x][y] == null){
            return new Posn(x,y);
        }
        double dx = d_x - x;
        double dy = d_y - y;
        if (dx >= 0 && dy >= 0){ // this means the sources are on the top or left edge of the land
            if (dx < dy){
                // Try left first
                if (x > 0 && m.matrix[x-1][y] == null){
                    return new Posn(x-1,y);
                }
            } else {
                // try up first
                if (y > 0 && m.matrix[x][y-1] == null){
                    return new Posn(x,y-1);
                }
            }
        } else {
            if (dx < 0){
                // try right first
                if (x < m.getWidth()-1 && m.matrix[x+1][y] == null){
                    return new Posn(x+1,y);
                }
            } else {
                // try down first
                if (y < m.getHeight()-1 && m.matrix[x][y+1] == null){
                    return new Posn(x,y+1);
                }
            }
        }
        if (y > 0 && m.matrix[x][y-1] == null){
            return new Posn(x,y-1);
        }
        if (y < m.getHeight()-1 && m.matrix[x][y+1] == null){
            return new Posn(x,y+1);
        }
        if (x > 0 && m.matrix[x-1][y] == null){
            return new Posn(x-1,y);
        }
        if (x < m.getWidth()-1 && m.matrix[x+1][y] == null){
            return new Posn(x+1,y);
        }
        return null;
    }
    
} // end of class Layer
