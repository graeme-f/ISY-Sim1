package sim.Layers;

import javafx.scene.canvas.GraphicsContext;

public abstract class ResizeableLayer extends Layer {

	public ResizeableLayer(GraphicsContext gContext, double width, double height) {
		super(gContext, width, height);
		// TODO Auto-generated constructor stub
	}

	public void setWidth(double width) {layerWidth = width;}
	public void setHeight(double height) {layerHeight = height;}
}
