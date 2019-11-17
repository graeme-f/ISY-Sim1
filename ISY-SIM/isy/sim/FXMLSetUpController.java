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
package sim;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import sim.Layers.LandLayer;
import sim.Layers.WasteSourceLayer;
import sim.Objects.LandObject;
import sim.Objects.SimObject;
import sim.Objects.WasteSourceObject;

/**
 *
 * @author gfoster
 */
public class FXMLSetUpController implements Initializable {

    @FXML private TextField txtHorizontal;
    @FXML private TextField txtVertical;
    @FXML private Slider sldHorizontal;
    @FXML private Slider sldVertical;
    @FXML private Canvas cnvOcean;
    @FXML private Button btnPlay;
    @FXML private ToggleButton btnWaste;
    @FXML private ToggleButton btnLand;
    @FXML private ToggleButton btnCurrent;
    @FXML private MenuButton wastePref;
    @FXML private Label statusBar;
    @FXML private ToggleButton btnClear;
    @FXML private CheckMenuItem smallItem;
    @FXML private CheckMenuItem medItem;
    @FXML private CheckMenuItem largeItem;
    @FXML private CheckMenuItem oilItem;
    @FXML private CheckMenuItem plasticItem;
    @FXML private CheckMenuItem miscItem;

    private LandLayer landLayer;
    private WasteSourceLayer wasteSourceLayer;
    private boolean currentToggle = false;
    private boolean landToggle = false;
    private boolean wasteToggle = false;
    private boolean landToggled = false;
    private boolean wasteToggled = false;
    private boolean placingLand = false;
    private boolean placingWaste = false;
    private GraphicsContext gc;
    private double oceanWidth = 500;
    private double oceanHeight = 500;
    private double horizontalSpeed = 2;
    private double verticalSpeed = 2;
    private int minorGL = 5;
    public int majorGL = 20;
    private boolean landInitialized = false;
    private boolean wasteInitialized = false;
    private boolean[][] landArray;
    private boolean[][] wasteArray;
    private enum Direction {UP, LEFT, DOWN, RIGHT}
    private String size = "500x500";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeWastePrefs();
        updateStatus();
//        toggleWaste();
        gc = cnvOcean.getGraphicsContext2D();
        draw();
        initializeSliders();
        toggleCurrent();
        toggleLand();
        clearAll();
    } // initialises all listeners and draws main application

    private void draw() {
        drawOcean();
        if (landToggled) {
            landLayer.drawLayer();
        }
        if (wasteToggled) {
            wasteSourceLayer.drawLayer();
        }
        drawArrows(gc);
        updateStatus();
    } // draws the land and arrows on the canvas

    private void drawIslands() {
        for (int i = 0; i < landArray.length; i += majorGL) {
            for (int j = 0; j < landArray[0].length; j += majorGL) {
                if (landArray[i][j]) {
                    drawBlock(i, j, Color.YELLOW, Color.GREEN);
                }
            }
        }
        updateStatus();
    } // uses the array of where land is to draw islands on the grid

    private void drawWasteSources() {
        for (int i = 0; i < wasteArray.length; i += majorGL) {
            for (int j = 0; j < wasteArray[0].length; j += majorGL) {
                if (wasteArray[i][j]) {
                    drawBlock(i, j, Color.BLACK, Color.LIGHTGRAY);
                }
            }
        }
    }

    private void drawBlock(int xCoordinate, int yCoordinate, Color beach, Color land) {
        gc.setFill(beach);
        double[] xCoordinates = {xCoordinate, xCoordinate, xCoordinate+majorGL, xCoordinate+majorGL};
        double[] yCoordinates = {yCoordinate, yCoordinate+majorGL, yCoordinate+majorGL, yCoordinate};
        gc.fillPolygon(xCoordinates, yCoordinates, 4);
        gc.setFill(land);
        xCoordinates = new double[]{xCoordinate+majorGL*0.0625, xCoordinate+majorGL*0.0625, xCoordinate+majorGL*0.9375, xCoordinate+majorGL*0.9375};
        yCoordinates = new double[]{yCoordinate+majorGL*0.0625, yCoordinate+majorGL*0.9375, yCoordinate+majorGL*0.9375, yCoordinate+majorGL*0.0625};
        gc.fillPolygon(xCoordinates, yCoordinates, 4);
    } // draws the yellow beach

    /**
     *
     * @param xCoordinate
     * @param yCoordinate
     * @param direction
     */
    private void drawLand(int xCoordinate, int yCoordinate, Direction direction, Color color) {
        gc.setFill(color);
        int x = xCoordinate;
        int y = yCoordinate;
        if (direction == Direction.DOWN) {
            y += majorGL/2;
        } else if (direction == Direction.LEFT) {
            x -= majorGL/2;
        } else if (direction == Direction.UP) {
            y-= majorGL/2;
        } else if (direction == Direction.RIGHT) {
            x += majorGL;
        }
        double[] xCoordinates = {x+majorGL*0.0625, x+majorGL*0.0625, x+majorGL*0.9375, x+majorGL*0.9375};
        double[] yCoordinates = {y+majorGL*0.0625, y+majorGL*0.9375, y+majorGL*0.9375, y+majorGL*0.0625};
        gc.fillPolygon(xCoordinates, yCoordinates, 4);
    } // draws land


    private void cleanBeaches(boolean[][] arr, Color color) {
        for (int i = 0; i < arr.length; i += majorGL) {
            for (int j = 0; j < arr[0].length; j += majorGL) {
                if (arr[i][j]) {
                    if ((i < majorGL || i > (arr.length-majorGL)) && (j == 0 || j > (arr[0].length-majorGL))) {
                        ;
                    } else if ((i < majorGL) || (i > (arr.length - majorGL))) {
                        if (arr[i][j+majorGL]) {
                            drawLand(i, j, Direction.DOWN, color);
                        }
                        if (arr[i][j-majorGL]) {
                            drawLand(i, j, Direction.UP, color);
                        }
                    } else if (j < majorGL || j > (arr[0].length-majorGL)) {
                        if (arr[i+majorGL][j]) {
                            drawLand(i, j, Direction.RIGHT, color);
                        }
                        if (arr[i-majorGL][j]) {
                            drawLand(i, j, Direction.LEFT, color);
                        }
                    } else {
                        if (arr[i][j+majorGL]) {
                            drawLand(i, j, Direction.DOWN, color);
                        }
                        if (arr[i][j-majorGL]) {
                            drawLand(i, j, Direction.UP, color);
                        }
                        if (arr[i+majorGL][j]) {
                            drawLand(i, j, Direction.RIGHT, color);
                        }
                        if (arr[i-majorGL][j]) {
                            drawLand(i, j, Direction.LEFT, color);
                        }
                    }

                }
            }
        }
    }

    private void initializeWastePrefs() {
        wastePref.setVisible(false);
        wastePref.setMinWidth(1);
        wastePref.setPrefWidth(1);
        statusBar.setMinWidth(450);
    }

    private void initializeSliders() {
        StringProperty txtHor = txtHorizontal.textProperty();
        StringProperty txtVer = txtVertical.textProperty();
        DoubleProperty sldHor = sldHorizontal.valueProperty();
        DoubleProperty sldVer = sldVertical.valueProperty();
        NumberStringConverter conv = new NumberStringConverter();
        Bindings.bindBidirectional(txtHor, sldHor, conv);
        Bindings.bindBidirectional(txtVer, sldVer, conv);

        sldHorizontal.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (currentToggle) {
                setCurrentHorizontal(newValue.doubleValue());
                draw();
                sldHorizontal.setValue(horizontalSpeed);
                txtHorizontal.setText(""+(int)horizontalSpeed);
            } else {
                setOceanWidth(newValue.doubleValue());
                oceanWidth = sldHorizontal.getValue();
                txtHorizontal.setText(String.format("%d", (int)sldHorizontal.getValue()));
                updateStatus();
            }
        });
        sldVertical.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (currentToggle) {
                setCurrentVertical(newValue.doubleValue());
                draw();
                sldVertical.setValue(verticalSpeed);
                txtVertical.setText(""+(int)verticalSpeed);
            } else {
                setOceanHeight(newValue.doubleValue());
                oceanHeight = sldVertical.getValue();
                txtVertical.setText(String.format("%d", (int)oceanHeight));
                updateStatus();
            }
        });
    }
    private void drawOcean(){
        gc.setFill(Color.AQUAMARINE);
        gc.fillRect(0, 0, cnvOcean.getWidth(), cnvOcean.getHeight());
        drawGridLines(gc, minorGL, majorGL);
        updateStatus();
    } // draws on canvas the ocean and grid lines

    private void setOceanWidth(double width) {
        double scale = sldHorizontal.getWidth() / 900;
        double oWidth = (width-100)*scale+60;
        cnvOcean.setWidth(oWidth);
        draw();
    }
    private void setOceanHeight(double height) {
        double scale = sldVertical.getHeight() / 900;
        double oHeight = (height-100)*scale+50;
        cnvOcean.setHeight(oHeight);
        cnvOcean.setTranslateY(525-oHeight);
        draw();
    }
    private void drawGridLines(GraphicsContext gc, int minorGL, int majorGL) {
        gc.setStroke(Color.BLUE);
        int cnvWidth = (int) cnvOcean.getWidth();
        int cnvHeight = (int) cnvOcean.getHeight();
        for (int width = 0; width < cnvWidth; width += minorGL) {
            for (int height = 0; height < cnvHeight; height += minorGL) {
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
    }
    private void drawArrows(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(verticalSpeed);
        arwCurrentUpSize(gc);
        arwCurrentDownSize(gc);
        gc.setLineWidth(horizontalSpeed);
        arwCurrentRightSize(gc);
        arwCurrentLeftSize(gc);
    }
    private void arwCurrentUpSize(GraphicsContext gc) {
        double scaleHeight = cnvOcean.getHeight();
        gc.strokeLine(10, 20, 10, scaleHeight-20);

        double[] xPoints = {0,10,20};
        double[] yPoints = {20,0,20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);

    }
    private void arwCurrentRightSize(GraphicsContext gc) {
        double scaleWidth = cnvOcean.getWidth();
        gc.strokeLine(20, 10, scaleWidth-20, 10);

        double[] xPoints = {scaleWidth-20,scaleWidth,scaleWidth-20};
        double[] yPoints = {0,10,20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }
    private void arwCurrentLeftSize(GraphicsContext gc) {
        double scaleWidth = cnvOcean.getWidth();
        double scaleHeight = cnvOcean.getHeight();
        gc.strokeLine(scaleWidth-20, scaleHeight-10, 20, scaleHeight-10);
        double[] xPoints = {20, 0, 20};
        double[] yPoints = {scaleHeight, scaleHeight-10, scaleHeight-20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }
    private void arwCurrentDownSize(GraphicsContext gc) {
        double scaleWidth = cnvOcean.getWidth();
        double scaleHeight = cnvOcean.getHeight();
        gc.strokeLine(scaleWidth-10, scaleHeight-20, scaleWidth-10, 20);

        double[] xPoints = {scaleWidth, scaleWidth-10, scaleWidth-20};
        double[] yPoints = {scaleHeight-20, scaleHeight, scaleHeight-20};
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, 3);
    }

    private void toggleCurrent() {
        btnCurrent.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            currentToggle = !currentToggle;
            if (currentToggle) {
                setSldHorMinMax(1, horizontalSpeed);
                setSldHorMinMax(1, 10);
                setSldVerMinMax(1, verticalSpeed);
                setSldVerMinMax(1, 10);
            } else {
                setSldHorMinMax((int)oceanWidth, 1000);
                setSldVerMinMax((int)oceanHeight, 1000);
                setSldHorMinMax(100, 1000);
                setSldVerMinMax(100, 1000);
                txtHorizontal.setText(String.valueOf(oceanWidth));
                txtVertical.setText(String.valueOf(oceanHeight));
            }
        }));
    }
    private void toggleLand() {
        btnLand.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            btnWaste.selectedProperty().set(false);
            landToggle = !landToggle;
            if (landToggle) {
                disableSliders();
                landToggled = true;
                if (!landInitialized) {
                    initializeLandLayer();
                    landInitialized = true;
                }
                cnvOcean.setOnMouseClicked(event -> {
                    if (landLayer.m.matrix[(int)event.getX()][(int)event.getY()] == null) {
                        initializeLandObjects((int) event.getX(), (int) event.getY());
                    } else {
                        removeLandObjects((int) event.getX(), (int) event.getY());
                    }
                    draw();
                });
            }
        }));
    }

    private void toggleWaste() {
        btnWaste.pressedProperty().addListener(((observable, oldValue, newValue) -> {
            btnLand.selectedProperty().set(false);
            wasteToggle = !wasteToggle;
            landToggle = !landToggle;
            if (wasteToggle) {
                disableSliders();
                wasteToggled = true;
                if (!wasteInitialized) {
                    initializeWasteSourceLayer();
                    wasteInitialized = true;
                }
                cnvOcean.setOnMouseClicked(event -> {
                    try {
                        WasteSourceObject obj = new WasteSourceObject(gc, (int) event.getX(), (int) event.getY());
                    } catch (NullPointerException ignored) {
                        initializeWasteSourceObjects((int) event.getX(), (int) event.getY());
                    }
                    wasteSourceLayer.drawLayer();
                });
                btnLand.setSelected(false);
                wastePref.setVisible(false);
                wastePref.setMinWidth(1);
                wastePref.setPrefWidth(1);
                statusBar.setMinWidth(400);
            } else {
                wastePref.setVisible(true);
                wastePref.setMinWidth(100);
                wastePref.setPrefWidth(100);
                statusBar.setMinWidth(355);
            }
        }));
    }

    private void initializeLandObjects(int x, int y) {
        for (int i = (x/majorGL)*majorGL; i < (x/majorGL)*majorGL+majorGL; i++) {
            for (int j = (y/majorGL)*majorGL; j < (y/majorGL)*majorGL+majorGL; j++) {
                landLayer.m.matrix[i][j] = new LandObject(gc, i, j);
            }
        }
    }

    private void removeLandObjects(int x, int y) {
        for (int i = (x/majorGL)*majorGL; i < (x/majorGL)*majorGL+majorGL; i++) {
            for (int j = (y/majorGL)*majorGL; j < (y/majorGL)*majorGL+majorGL; j++) {
                landLayer.m.matrix[i][j] = null;
            }
        }
    }

    private void initializeWasteSourceObjects(int x, int y) {
        for (int i = (x/majorGL)*majorGL; i < (x/majorGL)*majorGL+majorGL; i++) {
            for (int j = (y/majorGL)*majorGL; j < (y/majorGL)*majorGL+majorGL; j++) {
                wasteSourceLayer.m.matrix[i][j] = new WasteSourceObject(gc, i, j);
            }
        }
    }

    private void removeWasteObjects(int x, int y) {
        for (int i = (x/majorGL)*majorGL; i < (x/majorGL)*majorGL+majorGL; i++) {
            for (int j = (y/majorGL)*majorGL; j < (y/majorGL)*majorGL+majorGL; j++) {
                wasteSourceLayer.m.matrix[i][j] = null;
            }
        }
    }

    private void clearAll() {
        btnClear.selectedProperty().addListener(((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (btnClear.selectedProperty().getValue()){
                landToggled=false;
                wasteToggled = false;
                btnLand.setSelected(false);
                sldVertical.setDisable(false);
                sldHorizontal.setDisable(false);
                txtVertical.setDisable(false);
                txtHorizontal.setDisable(false);
                landInitialized = false;
                wasteInitialized = false;
                draw();
            }
        }));
    }

    private void setCurrentHorizontal(double speed) {
        horizontalSpeed = speed;
    }
    private void setCurrentVertical(double speed) {
        verticalSpeed = speed;
    }

    private void setSldVerMinMax(double min, double max) {
        sldVertical.setMin(min);
        sldVertical.setMax(max);
    }

    private void setSldHorMinMax(double min, double max) {
        sldHorizontal.setMin(min);
        sldHorizontal.setMax(max);
    }

    private void updateStatus() {
        String action;
        if (landToggle) {
            action = "Placing Land";
            size = txtHorizontal.getText() + "x" + txtVertical.getText();
        } else if (wasteToggle) {
            action = "Placing waste";
        } else if (currentToggle) {
            action = "Changing Current";
        } else {
            action = "Changing Size";
            size = txtHorizontal.getText() + "x" + txtVertical.getText();
        }
        int landAmt = 0;
        if (landInitialized){
            for (int i = 0; i < landLayer.m.matrix.length; i += majorGL) {
                for (int j = 0; j < landLayer.m.matrix[0].length; j += majorGL) {
                    if (landLayer.m.matrix[i][j] != null) {
                        landAmt++;
                    }
                }
            }
        }
        int wasteAmt = 0;
        if (wasteInitialized){
            for (int i = 0; i < wasteSourceLayer.m.matrix.length; i += majorGL) {
                for (int j = 0; j < wasteSourceLayer.m.matrix[0].length; j += majorGL) {
                    if (wasteSourceLayer.m.matrix[i][j] != null) {
                        wasteAmt++;
                    }
                }
            }
        }
        statusBar.setText("Action: " + action + "\t Size: "+size+ "\nCurrent Speed:" + (int)horizontalSpeed+" x "+(int)verticalSpeed + "Land amount:"+landAmt + "\tWaste amount:" + wasteAmt);
    }

    private void initializeLandLayer() {
        landLayer = new LandLayer(gc, cnvOcean.getWidth(), cnvOcean.getHeight());
    }

    private void initializeWasteSourceLayer() {
        wasteSourceLayer = new WasteSourceLayer(gc, cnvOcean.getWidth(), cnvOcean.getHeight());
    }
//
//    private void updateArray(MouseEvent mouseEvent, boolean bool, boolean[][] arr) {
//        double xCoordinate = (double)((int)mouseEvent.getX()/majorGL)*majorGL;
//        double yCoordinate = (double)((int)mouseEvent.getY()/majorGL)*majorGL;
//        if (!(((xCoordinate + majorGL > cnvOcean.getWidth() || yCoordinate + majorGL> cnvOcean.getHeight())) || ((xCoordinate - majorGL > cnvOcean.getWidth() || yCoordinate - majorGL> cnvOcean.getHeight())))) {
//            for (int i = (int)xCoordinate; i <= (int)xCoordinate+majorGL/2; i++) {
//                for (int j = (int)yCoordinate; j <= (int)yCoordinate+majorGL/2; j++) {
//                    arr[i][j] = bool;
//                }
//            }
//        }
//    }
//
    private void disableSliders() {
        sldVertical.setDisable(true);
        sldHorizontal.setDisable(true);
        txtVertical.setDisable(true);
        txtHorizontal.setDisable(true);
    }
    private double[] convertMouseToGrid(double mouseX, double mouseY) {
        double gridX = mouseX*1.323529411764706+20.588235294117647;
        double gridY = mouseY*1.894736842105263+5.263157894736842;
        return new double[]{gridX, gridY};
    }
    private double[] convertGridToMouse(double gridX, double gridY) {
        double mouseX = (gridX - 20.588235294117647) / 1.323529411764706;
        double mouseY = (gridY - 5.263157894736842) / 1.894736842105263;
        return new double[]{mouseX, mouseY};
    }
}
//}