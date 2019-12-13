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

package sim.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sim.Layers.LandLayer;
import sim.Layers.MatrixLayer.Direction;

/**
 *
 * @author gfoster
 */
public class LandObject extends SimObject{

    public LandObject(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public void draw() {
    	LandLayer ll = LandLayer.getLandLayer(); 
        GraphicsContext gc = ll.getActiveGC();
    	double cw = ll.cellWidth()*ll.getHScale();
        double ch = ll.cellWidth()*ll.getVScale();

        {
            double x1 = x*cw;
            double x2 = x1+ cw;
            double y1 = y * ch;
            double y2 = y1 + ch;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.GREEN);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);
        }
    	
        if (ll.getNeighbour(this, Direction.UP) == null && !ll.isEdge(this, Direction.UP)) {
            double x1 = x*cw;
            double x2 = x1 + cw;
            double y1 = y * ch;
            double y2 = y1 + 3;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }

        if (ll.getNeighbour(this, Direction.LEFT) == null && !ll.isEdge(this, Direction.LEFT)) {
            double x1 = x*cw;
            double x2 = x1 + 3;
            double y1 = y * ch;
            double y2 = y1 + ch;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }
        if (ll.getNeighbour(this, Direction.DOWN) == null && !ll.isEdge(this, Direction.DOWN)) {
            double x1 = x*cw;
            double x2 = x1 + cw;
            double y1 = y * ch + ch;
            double y2 = y1 - 3;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }

        if (ll.getNeighbour(this, Direction.RIGHT) == null && !ll.isEdge(this, Direction.RIGHT)) {
            double x1 = x*cw + cw;
            double x2 = x1 - 3;
            double y1 = y * ch;
            double y2 = y1 + ch;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }

    }
} // end of class LandObject
