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
import sim.Layers.CurrentLayer;
import sim.Layers.GridLayer;
import sim.Layers.LandLayer;
import sim.Layers.WasteSourceLayer;
import sim.Objects.LandObject;
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
    @FXML private ToggleButton btnClear; // TODO why a toggle button
    @FXML private CheckMenuItem smallItem;
    @FXML private CheckMenuItem medItem;
    @FXML private CheckMenuItem largeItem;
    @FXML private CheckMenuItem oilItem;
    @FXML private CheckMenuItem plasticItem;
    @FXML private CheckMenuItem miscItem;

    private LandLayer landLayer = null;
    private GridLayer gridLayer;
    private CurrentLayer arrowLayer;
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
        gridLayer = new GridLayer(gc, (int) cnvOcean.getWidth(), (int) cnvOcean.getHeight(), 5, 20);
        arrowLayer = new CurrentLayer(gc, (int) cnvOcean.getWidth(), (int) cnvOcean.getHeight(), 2, 2);
        draw();
        initializeSliders();
        toggleCurrent();
        toggleLand();
        clearAll();
    } // initialises all listeners and draws main application

    private void draw() {
        drawOcean();
        if (landToggled) { // TODO This is not the right place for this property it should be in LandLayer
            landLayer.drawLayer();
        }
        if (wasteToggled) { // TODO This is not the right place for this property it should be in WasteLayer
            drawWasteSources();
        }
        arrowLayer.drawLayer();
        updateStatus();
    } // draws the land and arrows on the canvas


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
            	horizontalSpeed = newValue.doubleValue();
            	arrowLayer.setHorizontalWidth(newValue.doubleValue());
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
            	verticalSpeed = newValue.doubleValue();
            	arrowLayer.setVerticalWidth(newValue.doubleValue());
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
        gridLayer.drawLayer();
        updateStatus();
    } // draws on canvas the ocean and grid lines

    private void setOceanWidth(double width) {
        double scale = sldHorizontal.getWidth() / 900;
        double oWidth = (width-100)*scale+60;
        cnvOcean.setWidth(oWidth);
        gridLayer.setWidth(oWidth);
        arrowLayer.setWidth(oWidth);
        draw();
    }
    private void setOceanHeight(double height) {
        double scale = sldVertical.getHeight() / 900;
        double oHeight = (height-100)*scale+50;
        cnvOcean.setHeight(oHeight);
        cnvOcean.setTranslateY(525-oHeight);
        gridLayer.setHeight(oHeight);
        arrowLayer.setHeight(oHeight);
        draw();
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
                if (landLayer == null) {
                	landLayer = new LandLayer(gc, cnvOcean.getWidth(), cnvOcean.getHeight(),majorGL);
                }
                cnvOcean.setOnMouseClicked(event -> {
                	int i = (int)event.getX()/majorGL;
                	int j = (int) event.getY()/majorGL;
                	landLayer.addObject(new LandObject(gc, i, j));
                    landLayer.drawLayer();
                });
            }
        }));
    }

//    private void toggleWaste() {
//        btnWaste.pressedProperty().addListener(((observable, oldValue, newValue) -> {
//            btnLand.selectedProperty().set(false);
//            wasteToggle = !wasteToggle;
//            landToggle = !landToggle;
//            if (wasteToggle) {
//                disableSliders();
//                wasteToggled = true;
//                if (!wasteInitialized) {
//                    initializeWasteSourceLayer();
//                    wasteInitialized = true;
//                }
//                cnvOcean.setOnMouseClicked(event -> {
//                    initializeWasteSourceObjects((int) event.getX(), (int) event.getY());
//                    wasteSourceLayer.drawLayer();
//                });
//                btnLand.setSelected(false);
//                wastePref.setVisible(false);
//                wastePref.setMinWidth(1);
//                wastePref.setPrefWidth(1);
//                statusBar.setMinWidth(400);
//            } else {
//                wastePref.setVisible(true);
//                wastePref.setMinWidth(100);
//                wastePref.setPrefWidth(100);
//                statusBar.setMinWidth(355);
//            }
//        }));
//    }

    private void clearAll() {
        btnClear.selectedProperty().addListener(((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (btnClear.selectedProperty().getValue()){
                landToggled=false;
                btnLand.setSelected(false);
                sldVertical.setDisable(false);
                sldHorizontal.setDisable(false);
                txtVertical.setDisable(false);
                txtHorizontal.setDisable(false);
                landLayer = null;
                landToggled = false; // TODO why in twice (see 7 lines earlier) 
                draw();
            }
        }));
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
        int landAmt = 0; // TODO Get this from LandLayer
        int wasteAmt = 0;// TODO Get this from WasteLayer
        statusBar.setText("Action: " + action + "\t Size: "+size+ "\nCurrent Speed:" + (int)horizontalSpeed+" x "+(int)verticalSpeed + "Land amount:"+landAmt/121 + "\tWaste amount:" + wasteAmt/121);
    }

    private void initializeWasteSourceLayer() {
        wasteSourceLayer = new WasteSourceLayer(gc, cnvOcean.getWidth(), cnvOcean.getHeight(), minorGL);
    }

    private void disableSliders() {
        sldVertical.setDisable(true);
        sldHorizontal.setDisable(true);
        txtVertical.setDisable(true);
        txtHorizontal.setDisable(true);
    }
}