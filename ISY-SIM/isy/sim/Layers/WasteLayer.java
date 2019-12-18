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
    private final ArrayList<HashMap<Posn, ArrayList<WasteObject>>> time; // when were they there
    private int currentTime;
    
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
    		throw new NullPointerException("WasteLayer not instansiated");
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
        time = new ArrayList();
    } // end private constructor

    protected void grabCurrentLayer(){
        // Has a layer been added for the currentTime
        if (currentTime >= time.size()){
            layer = new HashMap();
            time.add(layer);
        } else {
            layer = time.get(currentTime);
        }
    } // end of method grabCurrentLayer
    
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
        if (currentTime > -1){
            grabCurrentLayer();
            // Does anything already exist in the current cell
            grabCell(new Posn(x,y));
            // add the waste object to the current cell
            cell.add(wo);
        }
    }
    
    public void addTime(){currentTime++;}
    
    public void circulate(double hSpeed, double vSpeed, LandLayer ll){
        // Check that there is a  previous layer to move
        if (currentTime > 0){
            // Get each cell from the previous layer
            for(Posn key : time.get(currentTime-1).keySet()){
                cell = time.get(currentTime-1).get(key);
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
    }
    
    public int currentLayerWasteCount(){
        int total = 0;
        if (currentTime > -1){
            for(Posn key : time.get(currentTime).keySet()){
                cell = time.get(currentTime).get(key);
                total += cellCount();
            }
        }
        return total;
    }
    
    public int cellCount(){
        int total = 0;
        for (WasteObject wo: cell){
            total += wo.getSize();
        }
        return total;
    }
    
    public void merge(){
        if (currentTime > -1){
            for(Posn key : time.get(currentTime).keySet()){
                cell = time.get(currentTime).get(key);
                int total = cellCount();
                if (total > 100){
                    WasteMergeObject wmo = new WasteMergeObject();
                    for (WasteObject wo: cell){
                        wmo.merge(wo);
                    }
                }
            }
        }
    }
    
    public static double getRandomDoubleBetweenRange(double min, double max){
        double x = (Math.random()*((max-min)+1))+min;
        return x;
    }
    @Override public void drawLayer() {
        if (currentTime > -1){
            for(Posn key : time.get(currentTime).keySet()){
                cell = time.get(currentTime).get(key);
                for (WasteObject wo: cell){
                    wo.draw(key);
                }
            }
        }
    }
} // end of class WasteLayer
