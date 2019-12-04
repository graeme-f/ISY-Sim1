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

    public LandObject(GraphicsContext graphicsContext, int locationX, int locationY) {
        super(graphicsContext, locationX, locationY);
    }

    @Override
    public void draw() {
    	LandLayer ll = LandLayer.getLandLayer(); 
    	int cw = ll.cellWidth();
    	{
	    	int x1 = x*cw;
	    	int x2 = x1+ cw;
	    	int y1 = y * cw;
	    	int y2 = y1 + cw;
	        double[] xCoordinates = {x1, x1, x2, x2};
	        double[] yCoordinates = {y1, y2, y2, y1};
	        gc.setFill(Color.GREEN);
	        gc.fillPolygon(xCoordinates, yCoordinates, 4);
    	}
    	
        if (ll.getNeighbour(this, Direction.UP) == null && !ll.isEdge(this, Direction.UP)) {
        	int x1 = x*cw;
        	int x2 = x1 + cw;
        	int y1 = y * cw;
        	int y2 = y1 + 3;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }

        if (ll.getNeighbour(this, Direction.LEFT) == null && !ll.isEdge(this, Direction.LEFT)) {
        	int x1 = x*cw;
        	int x2 = x1 + 3;
        	int y1 = y * cw;
        	int y2 = y1 + cw;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }
        if (ll.getNeighbour(this, Direction.DOWN) == null && !ll.isEdge(this, Direction.DOWN)) {
        	int x1 = x*cw;
        	int x2 = x1 + cw;
        	int y1 = y * cw + cw;
        	int y2 = y1 - 3;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }

        if (ll.getNeighbour(this, Direction.RIGHT) == null && !ll.isEdge(this, Direction.RIGHT)) {
        	int x1 = x*cw + cw;
        	int x2 = x1 - 3;
        	int y1 = y * cw;
        	int y2 = y1 + cw;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.YELLOW);
            gc.fillPolygon(xCoordinates, yCoordinates, 4);       	
        }

    }
} // end of class LandObject
