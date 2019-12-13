package sim.Layers;

import javafx.scene.canvas.GraphicsContext;

public abstract class ResizeableLayer extends Layer {

	public ResizeableLayer(GraphicsContext gContext, double width, double height, double horizScale, double vertScale) {
		super(gContext, width, height, horizScale, vertScale);
		// TODO Auto-generated constructor stub
	}

	public void setWidth(double width) {layerWidth = width;}
	public void setHeight(double height) {layerHeight = height;}
}
