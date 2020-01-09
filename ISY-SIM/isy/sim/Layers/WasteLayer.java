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
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.canvas.GraphicsContext;
import sim.Objects.WasteDischargeObject;
import sim.Objects.WasteMergeObject;
import sim.Objects.WasteObject;
import sim.Utilities.Posn;

/**
 *
 * @author gfoster
 */
public class WasteLayer extends Layer{

    protected static WasteLayer instance = null;
    private ArrayList<WasteObject> cell; // which Waste Objects are in the cell
    private HashMap<Posn, ArrayList<WasteObject>> layer; // where is the cell
    private HashMap<Posn, ArrayList<WasteObject>> oldLayer; // where is the cell
    private int currentTime;
    public int getTime(){return currentTime;}
    
    public static WasteLayer getWasteLayer(GraphicsContext gContext
                                           ,double width
                                           ,double height
                                           ,double horizScale
                                           ,double vertScale
                                           ,int cellWidth
                                          ) {
    	if (instance == null) {
            instance = new WasteLayer(gContext
                                      ,width
                                      ,height
                                      ,horizScale
                                      ,vertScale
                                      ,cellWidth
                                     );
    	}
    	return instance;
    } // end Singleton getWasteLayer
    
    // This method should only be used if the layer has been created
    // If the layer has not be created it will throw an exception
    public static WasteLayer getWasteLayer() {
    	if (instance == null) {
    		throw new NullPointerException("WasteLayer not instantiated.");
    	}
    	return instance;
    } // end Singleton getWasteLayer
    
    private WasteLayer(GraphicsContext gContext
                      ,double width
                      ,double height
                      ,double horizScale
                      ,double vertScale
                      ,int cellWidth
                      ) {
        super(gContext, width, height, horizScale, vertScale);
        this.currentTime = -1;
        oldLayer = new HashMap();
        layer = new HashMap();
    } // end private constructor

    protected void grabCell(Posn posn){
        if (!layer.containsKey(posn)){
            cell = new ArrayList();
            layer.put(posn, cell);
        } else {
            cell = layer.get(posn);
        }
    } // end of method grabCell
    
    public void addWaste(int x, int y, WasteObject wo){
        // Time needs to be incremented before this is called
            // Does anything already exist in the current cell
            grabCell(new Posn(x,y));
            // add the waste object to the current cell
            cell.add(wo);
    }
    
    public void addTime(){
        oldLayer = layer;
        layer = new HashMap();
        currentTime++;
    }
    
    public void circulate(double hSpeed, double vSpeed, LandLayer ll){
        // Get each cell from the previous layer
        for(Posn key : oldLayer.keySet()){
            cell = oldLayer.get(key);
            for (WasteObject wo: cell){
                wo.draw(key);
                int dirnX = 1;
                int dirnY = 1;
                if (key.getX()*2 < layerWidth) {dirnY = -1;}
                if (key.getY()*2 > layerHeight) {dirnX = -1;}
                double newX = key.getX() + (1-Math.abs(1-2*key.getX()/layerWidth))*vSpeed*dirnX;
                double newY = key.getY() + (1-Math.abs(1-2*key.getY()/layerHeight))*hSpeed*dirnY;
                newX += getRandomDoubleBetweenRange(vSpeed*-0.5, vSpeed*0.5);
                newY += getRandomDoubleBetweenRange(hSpeed*-0.5, hSpeed*0.5);
                addWaste((int)newX, (int)newY, wo);
            }
        }
    }
    
    public int currentLayerWasteCount(){
        int total = 0;
        int max = 0;
        for(Posn key : layer.keySet()){
            cell = layer.get(key);
            int count = cellCount();
            if (count > max){
                max = count;
            }
            total += count;
        }
        return total;
    }
    
    public int pollutedSpace(){
        return layer.size();
    }
    
    public int cellCount(){
        int total = 0;
        for (WasteObject wo: cell){
            total += wo.getSize();
        }
        return total;
    }
    
    public void merge(){
        for(Posn key : layer.keySet()){
            cell = layer.get(key);
            if (cell.size() > 1){
                WasteMergeObject wmo = new WasteMergeObject();
                for (WasteObject wo: cell){
                    wo.merge(wmo);
                }
                cell.clear();
                cell.add(wmo);
            }
        }
    }
    
    public static double getRandomDoubleBetweenRange(double min, double max){
        double x = (Math.random()*((max-min)+1))+min;
        return x;
    }
    
    @Override public void drawLayer() {
        for(Posn key : layer.keySet()){
            cell = layer.get(key);
            for (WasteObject wo: cell){
                wo.draw(key);
            }
        }
    }
} // end of class WasteLayer
