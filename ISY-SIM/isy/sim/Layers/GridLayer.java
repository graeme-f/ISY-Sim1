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
            for (int height = 0; height < layerHeight; height += minorGL) {
                // TODO: Find  a more concise way of doing this
                if (width % majorGL == 0 && height % majorGL != 0) {
                    gc.setLineWidth(0.4);
                    gc.strokeLine(width, height, width+minorGL, height);
                    gc.setLineWidth(3.0);
                    gc.strokeLine(width, height, width, height-minorGL);
                } else if (height % majorGL == 0 && width % majorGL != 0) {
                    gc.setLineWidth(3.0);
                    gc.strokeLine(width, height, width+minorGL, height);
                    gc.setLineWidth(0.4);
                    gc.strokeLine(width, height, width, height-minorGL);
                } else if (height % majorGL == 0 && width % majorGL == 0) {
                    gc.setLineWidth(3.0);
                    gc.strokeLine(width, height, width+minorGL, height);
                    gc.strokeLine(width, height, width, height-minorGL);
                } else {
                    gc.setLineWidth(0.4);
                    gc.strokeLine(width, height, width+minorGL, height);
                    gc.strokeLine(width, height, width, height-minorGL);
                }
            }
        }
	} // end of method drawLayer()

}
