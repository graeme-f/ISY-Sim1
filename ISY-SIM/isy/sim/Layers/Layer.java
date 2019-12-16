package sim.Layers;

import javafx.scene.canvas.GraphicsContext;
import sim.Utilities.Posn;

public abstract class Layer {
	protected GraphicsContext gc;
	protected double layerWidth;
	protected double layerHeight;
        protected double horizontalScale;
        protected double verticalScale;
	
    public Layer(GraphicsContext gContext, double width, double height, double hScale, double vScale) {
        gc = gContext;
        layerWidth = width;
        layerHeight = height;
        horizontalScale = hScale;
        verticalScale = vScale;
    }
    
    public double getHScale(){return horizontalScale;}
    public double getVScale(){return verticalScale;}

    public void setScale(double hScale, double vScale){
        horizontalScale = hScale;
        verticalScale = vScale;        
    }
    
    public GraphicsContext getActiveGC(){
        return gc;
    }
    
    public void setActiveGC(GraphicsContext gc){
        this.gc = gc;
    }
    
    public abstract void drawLayer();
}
