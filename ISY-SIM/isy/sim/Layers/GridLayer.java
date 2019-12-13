package sim.Layers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridLayer extends ResizeableLayer {

    private int minorGL;
    private int majorGL;

    public GridLayer(GraphicsContext gContext, double width, double height, double horizScale, double vertScale, int minorGL, int majorGL) {
        super(gContext, width, height, horizScale, vertScale);
        this.minorGL = minorGL;
        this.majorGL = majorGL;
    }

    @Override
    public void drawLayer() {
        gc.setStroke(Color.BLUE);
        for (int width = 0; width*horizontalScale < layerWidth; width += minorGL) {
            if (width % majorGL == 0) {
                gc.setLineWidth(3.0*horizontalScale);
                gc.strokeLine(width*horizontalScale, 0, width*horizontalScale, layerHeight);
            } else {
                gc.setLineWidth(0.4*horizontalScale);
                gc.strokeLine(width*horizontalScale, 0, width*horizontalScale, layerHeight);
            }
        }
        for (int height = 0; height*verticalScale < layerHeight; height += minorGL) {

            if (height % majorGL == 0) {
                gc.setLineWidth(3.0*verticalScale);
                gc.strokeLine(0, height*verticalScale, layerWidth, height*verticalScale);
            } else {
                gc.setLineWidth(0.4*verticalScale);
                gc.strokeLine(0, height*verticalScale, layerWidth, height*verticalScale);
            }
        }
    }
}