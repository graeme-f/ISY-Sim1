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
    public WasteSourceObject(int locationX, int locationY, FXMLSetUpController.sourceSize size, FXMLSetUpController.sourceType type) {
        super(locationX, locationY);
        this.size = size;
        this.type = type;
    }

    @Override
    public void draw() {
        WasteSourceLayer wsl = WasteSourceLayer.getWasteSourceLayer();
        GraphicsContext gc = wsl.getActiveGC();
        if (null == type) {
            gc.setFill(Color.GREY);
        }
        else switch (type) {
            case MISC:
                gc.setFill(Color.web("0x57350f"));
                break;
            case OIL:
                gc.setFill(Color.BLACK);
                break;
            default:
                gc.setFill(Color.GREY);
                break;
        }
    	double cw = wsl.cellWidth()*wsl.getHScale();
        double ch = wsl.cellWidth()*wsl.getVScale();
        if (null == size) {
            double x1 = x * cw;
            double x2 = x1 + cw;
            double y1 = y * ch;
            double y2 = y1 + ch;
            double[] xCoordinates = {x1, x1, x2, x2};
            double[] yCoordinates = {y1, y2, y2, y1};
            gc.fillPolygon(xCoordinates, yCoordinates, 4);
        }
        else switch (size) {
            case MEDIUM:
                {
                    double x1 = x * cw;
                    double x2 = x1 + cw + 5;
                    double y1 = y * ch;
                    double y2 = y1 + ch + 5;
                    double[] xCoordinates = {x1, x1, x2, x2};
                    double[] yCoordinates = {y1, y2, y2, y1};
                    gc.fillPolygon(xCoordinates, yCoordinates, 4);
                    break;
                }
            case LARGE:
                {
                    double x1 = x * cw;
                    double x2 = x1 + cw + 15;
                    double y1 = y * ch;
                    double y2 = y1 + ch + 15;
                    double[] xCoordinates = {x1, x1, x2, x2};
                    double[] yCoordinates = {y1, y2, y2, y1};
                    gc.fillPolygon(xCoordinates, yCoordinates, 4);
                    break;
                }
            default:
                {
                    double x1 = x * cw;
                    double x2 = x1 + cw;
                    double y1 = y * ch;
                    double y2 = y1 + ch;
                    double[] xCoordinates = {x1, x1, x2, x2};
                    double[] yCoordinates = {y1, y2, y2, y1};
                    gc.fillPolygon(xCoordinates, yCoordinates, 4);
                    break;
                }
        }
    }


} // end of class WasteSourceObject
