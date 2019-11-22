package sim.Layers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CurrentLayer extends ResizeableLayer {

	private double verticalWidth;
	private double horizontalWidth;
	
	public CurrentLayer(GraphicsContext gContext, double width, double height, double vw, double hw) {
		super(gContext, width, height);
		verticalWidth = vw;
		horizontalWidth = hw;
	}

	public void setVerticalWidth(double vw) {verticalWidth = vw;}
	public void setHorizontalWidth(double hw) {horizontalWidth = hw;}
	
	@Override
	public void drawLayer() {
		gc.setStroke(Color.BLACK);
        gc.setLineWidth(verticalWidth);
        arwCurrentUpSize(gc);
        arwCurrentDownSize(gc);
        gc.setLineWidth(horizontalWidth);
        arwCurrentRightSize(gc);
        arwCurrentLeftSize(gc);
	} // end of method drawLayer

    private void arwCurrentUpSize(GraphicsContext gc) {
        gc.strokeLine(10, 20, 10, layerHeight-20);
        double[] xPoints = {0,10,20};
        double[] yPoints = {20,0,20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }
    private void arwCurrentRightSize(GraphicsContext gc) {
        gc.strokeLine(20, 10, layerWidth-20, 10);
        double[] xPoints = {layerWidth-20,layerWidth,layerWidth-20};
        double[] yPoints = {0,10,20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }
    private void arwCurrentLeftSize(GraphicsContext gc) {
        gc.strokeLine(layerWidth-20, layerHeight-10, 20, layerHeight-10);
        double[] xPoints = {20, 0, 20};
        double[] yPoints = {layerHeight, layerHeight-10, layerHeight-20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }
    private void arwCurrentDownSize(GraphicsContext gc) {
        gc.strokeLine(layerWidth-10, layerHeight-20, layerWidth-10, 20);
        double[] xPoints = {layerWidth, layerWidth-10, layerWidth-20};
        double[] yPoints = {layerHeight-20, layerHeight, layerHeight-20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }

}
