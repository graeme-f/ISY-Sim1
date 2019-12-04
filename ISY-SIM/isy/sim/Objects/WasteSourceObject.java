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
import sim.FXMLSetUpController;
import sim.Layers.WasteSourceLayer;

/**
 *
 * @author gfoster
 */
public class WasteSourceObject extends SimObject {

    private FXMLSetUpController.sourceSize size;
    private FXMLSetUpController.sourceType type;
    public WasteSourceObject(GraphicsContext graphicsContext, int locationX, int locationY, FXMLSetUpController.sourceSize size, FXMLSetUpController.sourceType type) {
        super(graphicsContext, locationX, locationY);
        this.size = size;
        this.type = type;
    }

    @Override
    public void draw() {
        if (size == FXMLSetUpController.sourceSize.MEDIUM) {
            int cw = WasteSourceLayer.getWasteSourceLayer().cellWidth();
            int x1 = x * cw;
            int x2 = x1 + cw + 5;
            int y1 = y * cw;
            int y2 = y1 + cw + 5;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.web("0x57350f"));
            gc.fillPolygon(xCoordinates, yCoordinates, 4);
        }
        else if (size == FXMLSetUpController.sourceSize.LARGE) {
            int cw = WasteSourceLayer.getWasteSourceLayer().cellWidth();
            int x1 = x * cw;
            int x2 = x1 + cw + 15;
            int y1 = y * cw;
            int y2 = y1 + cw + 15;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.web("0x57350f"));
            gc.fillPolygon(xCoordinates, yCoordinates, 4);
        }
        else {
            int cw = WasteSourceLayer.getWasteSourceLayer().cellWidth();
            int x1 = x * cw;
            int x2 = x1 + cw;
            int y1 = y * cw;
            int y2 = y1 + cw;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.setFill(Color.web("0x57350f"));
            gc.fillPolygon(xCoordinates, yCoordinates, 4);
        }
    }


} // end of class WasteSourceObject
