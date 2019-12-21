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
import sim.Layers.WasteLayer;
import sim.Utilities.Posn;

/**
 *
 * @author gfoster
 */
public abstract class WasteObject {
    int size;
    
    public WasteObject (int size){
        this.size = size;
    }
    
    public void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return size;
    }
    
    public void draw(Posn p){
        WasteLayer wl = WasteLayer.getWasteLayer();
        GraphicsContext gc = wl.getActiveGC();
        double scale = 5.0;
        double weight = size/scale;
        String colour = "#000000";
        gc.setFill(Color.web(colour, Math.min(weight,1.0)));
        gc.fillOval(p.getX(), p.getY(), Math.sqrt(size)/scale, Math.sqrt(size)/scale);
    }
    
    public abstract void merge(WasteMergeObject wmo);
} // end of class WasteObject
