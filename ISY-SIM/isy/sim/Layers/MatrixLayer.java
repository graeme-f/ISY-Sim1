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
import sim.FXMLSetUpController.Direction;

/**
 *
 * @author gfoster
 */
public abstract class MatrixLayer  extends Layer {
    protected SimMatrix m;

    public MatrixLayer(GraphicsContext gContext, double width, double height, int cellWidth) {
    	super(gContext, width, height);
        m = new SimMatrix((int)width/cellWidth+1, (int)height/cellWidth+1);
    }
    
    public void drawLayer() {
        m.drawMatrix();
    }
    
    public void addObject(SimObject object) {
    	m.matrix[object.getx()][object.gety()] = object;
    }

    public SimObject getNeighbour(SimObject object, Direction d) {
        if (d == Direction.UP) {
            return m.matrix[object.getx()][object.gety()-1];
        } else if (d == Direction.DOWN){
            return m.matrix[object.getx()][object.gety()+1];
        } else if (d == Direction.LEFT) {
            return m.matrix[object.getx()-1][object.gety()];
        } else {
            return m.matrix[object.getx()+1][object.gety()];
        }
    }

} // end of class Layer
