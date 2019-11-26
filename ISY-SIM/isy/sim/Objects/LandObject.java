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
    	int cw = LandLayer.cellWidth();
    	int x1 = x*cw;
    	int x2 = x1+ cw;
    	int y1 = y * cw;
    	int y2 = y1 + cw;
        double[] xCoordinates = {x1, x1, x2, x2};
        double[] yCoordinates = {y1, y2, y2, y1};
        gc.setFill(Color.GREEN);
        gc.fillPolygon(xCoordinates, yCoordinates, 4);


        int x1RB = x*cw + 15;
        int x2RB = x1+ cw ;
        int y1RB = y * cw;
        int y2RB = y1 + cw;
        double[] xCoordinatesRB = {x1RB, x1RB, x2RB, x2RB};
        double[] yCoordinatesRB = {y1RB, y2RB, y2RB, y1RB};
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(xCoordinatesRB, yCoordinatesRB, 4);

        int x1BB = x*cw + 15;
        int x2BB = x1+ cw ;
        int y1BB = y * cw;
        int y2BB = y1 + cw;
        double[] xCoordinatesBB = {x1BB, x1BB, x2BB, x2BB};
        double[] yCoordinatesBB = {y1BB, y2BB, y2RB, y1RB};
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(xCoordinatesRB, yCoordinatesRB, 4);

        int x1RB = x*cw + 15;
        int x2RB = x1+ cw ;
        int y1RB = y * cw;
        int y2RB = y1 + cw;
        double[] xCoordinatesRB = {x1RB, x1RB, x2RB, x2RB};
        double[] yCoordinatesRB = {y1RB, y2RB, y2RB, y1RB};
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(xCoordinatesRB, yCoordinatesRB, 4);

        int x1RB = x*cw + 15;
        int x2RB = x1+ cw ;
        int y1RB = y * cw;
        int y2RB = y1 + cw;
        double[] xCoordinatesRB = {x1RB, x1RB, x2RB, x2RB};
        double[] yCoordinatesRB = {y1RB, y2RB, y2RB, y1RB};
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(xCoordinatesRB, yCoordinatesRB, 4);
    }
} // end of class LandObject
