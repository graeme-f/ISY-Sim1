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
public abstract class Layer {
    GraphicsContext gc;
    protected SimMatrix m;

    public Layer(GraphicsContext gContext, double width, double height, int cellWidth) {
        gc = gContext;
        m = new SimMatrix((int)width/cellWidth+1, (int)height/cellWidth+1);
    }

    public void drawLayer() {
        m.drawMatrix();
    }
    
    public void addObject(SimObject object) {
    	m.matrix[object.getx()][object.gety()] = object;
    }

} // end of class Layer
