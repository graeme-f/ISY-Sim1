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
import sim.Objects.WasteObject;

/**
 *
 * @author gfoster
 */
public class WasteLayer extends Layer{

    public class Posn{
        int x;
        int y;
        
        Posn (int x, int y){
            this.x = x;
            this.y = y;
        }
        
        public int getX(){return x;}
        public int getY(){return y;}
        boolean isAt (int x, int y){ return (this.x == x && this.y == y);}
    }
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
    
    @Override public void drawLayer() {
        if (currentTime > -1){
            // TODO add drawing code here
        }
    }
} // end of class WasteLayer
