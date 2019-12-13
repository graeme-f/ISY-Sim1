package sim.Layers;

import javafx.scene.canvas.GraphicsContext;

public abstract class Layer {
	protected GraphicsContext gc;
	protected double layerWidth;
	protected double layerHeight;
	
    public Layer(GraphicsContext gContext, double width, double height) {
        gc = gContext;
        layerWidth = width;
        layerHeight = height;
    }
    
    public void setActiveGC(GraphicsContext gc){
        this.gc = gc;
    }
    
    public abstract void drawLayer();
}
