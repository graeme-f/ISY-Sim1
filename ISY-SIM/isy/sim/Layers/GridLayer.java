package sim.Layers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridLayer extends ResizeableLayer {

    private int minorGL;
    private int majorGL;
	
	public GridLayer(GraphicsContext gContext, double width, double height, int minorGL, int majorGL) {
		super(gContext, width, height);
		this.minorGL = minorGL;
		this.majorGL = majorGL;
	}
	
	@Override
	public void drawLayer() {
        gc.setStroke(Color.BLUE);
	    for (int width = 0; width < layerWidth; width += minorGL) {
	        if (width % majorGL == 0) {
	            gc.setLineWidth(3.0);
            } else {
	            gc.setLineWidth(0.4);
            }
	        gc.strokeLine(width, 0, width, layerHeight);
        }
	    for (int height = 0; height < layerHeight; height += minorGL) {
	        if (height % majorGL == 0) {
	            gc.setLineWidth(3.0);
            } else {
	            gc.setLineWidth(0.4);
            }
			gc.strokeLine(0, height, layerWidth, height);
        }
	} // end of method drawLayer()

}
