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

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import sim.Layers.CurrentLayer;
import sim.Layers.GridLayer;
import sim.Layers.LandLayer;
import sim.Layers.WasteSourceLayer;
import sim.Objects.LandObject;
import sim.Objects.WasteSourceObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    @FXML private ToggleButton btnWaste;
    @FXML private ToggleButton btnLand;
    @FXML private ToggleButton btnCurrent; // TODO disable when it can't be used
    @FXML private MenuButton wastePref;
    @FXML private Label statusBar;
    @FXML private Button btnClear;
    @FXML private CheckMenuItem smallItem;
    @FXML private CheckMenuItem medItem;
    @FXML private CheckMenuItem largeItem;
    @FXML private CheckMenuItem oilItem;
    @FXML private CheckMenuItem plasticItem;
    @FXML private CheckMenuItem miscItem;

    private LandLayer landLayer;
    private GridLayer gridLayer;
    private CurrentLayer arrowLayer;
    private WasteSourceLayer wasteSourceLayer;
    private boolean currentToggle = false;
    private boolean landToggle = false;
    private boolean wasteToggle = false;
    private boolean wastePrefToggle = false;
    private GraphicsContext gc;
    private double oceanWidth = 500;
    private double oceanHeight = 500;
    private double horizontalSpeed = 2;
    private double verticalSpeed = 2;
    private final int minorGL = 5;
    private final int majorGL = 20;
    public enum sourceSize {SMALL, MEDIUM, LARGE}
    public enum sourceType {OIL, PLASTIC, MISC}
    private sourceType type = sourceType.PLASTIC;
    private sourceSize size = sourceSize.MEDIUM;
    public Button btnPlay;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeWastePrefs();
        updateStatus();
//        toggleWaste();
        initializeSliders();
        gc = cnvOcean.getGraphicsContext2D();
        gridLayer = new GridLayer(gc
                                 ,(int) cnvOcean.getWidth()
                                 ,(int) cnvOcean.getHeight()
                                 ,cnvOcean.getWidth() / sldHorizontal.getValue()
                                 ,cnvOcean.getHeight() / sldVertical.getValue()
                                 ,minorGL
                                 ,majorGL);
        arrowLayer = new CurrentLayer(gc
                                     ,(int) cnvOcean.getWidth()
                                     ,(int) cnvOcean.getHeight()
                                     ,cnvOcean.getWidth() / sldHorizontal.getValue()
                                     ,cnvOcean.getHeight() / sldVertical.getValue()
                                     ,2
                                     ,2);
        draw();
        toggleCurrent();
        toggleLand();
        toggleWaste();
        toggleWastePrefs();
        clearAll();
    } // initialises all listeners and draws main application

    @FXML
    public void playRun() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLRun.fxml"));
        Stage runStage = new Stage();
        Scene runScene = null;
        try {
            runScene = new Scene(loader.load());
        } catch (IOException e) {
            //01110011011010000110100101110100
        }
        runStage.setScene(runScene);
        FXMLRunController runController = loader.getController();
        runController.cnvOcean.setWidth(oceanWidth);
        runController.cnvOcean.setHeight(oceanHeight);
        runController.setup(landLayer
                           ,wasteSourceLayer
                           ,horizontalSpeed
                           ,verticalSpeed);
        runStage.showAndWait();
        if (landLayer != null){
            landLayer.setActiveGC(gc);
            landLayer.setScale(cnvOcean.getWidth() / sldHorizontal.getValue()
                              ,cnvOcean.getHeight() / sldVertical.getValue());
        }
        if (wasteSourceLayer != null){
            wasteSourceLayer.setActiveGC(gc);
            wasteSourceLayer.setScale(cnvOcean.getWidth() / sldHorizontal.getValue()
                                     ,cnvOcean.getHeight() / sldVertical.getValue());
        }

    }


    private void draw() {
        drawOcean();
        if (landLayer != null) {
            landLayer.drawLayer();
        }
        if (wasteSourceLayer != null) {
            wasteSourceLayer.drawLayer();
        }
        if (arrowLayer != null) {
            arrowLayer.drawLayer();
        }
        updateStatus();
    } // draws the land and arrows on the canvas

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
                if (landLayer == null) {
                	landLayer = LandLayer.getLandLayer(gc
                                                          ,oceanWidth
                                                          ,oceanHeight
                                                          ,cnvOcean.getWidth() / oceanWidth
                                                          ,cnvOcean.getHeight() / oceanHeight
                                                          ,majorGL);
                }
                cnvOcean.setOnMouseClicked(event -> {
                    int x = (int)(event.getX()/landLayer.getHScale()/majorGL);
                    int y = (int)(event.getY()/landLayer.getVScale()/majorGL);
                    if (landLayer.hasObject(x,y)){
                        landLayer.removeObject(x, y);
                    } else {
                        landLayer.addObject(new LandObject(x, y));
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
                if (wasteSourceLayer == null) {
                    wasteSourceLayer = WasteSourceLayer.getWasteSourceLayer(gc
                                                                           ,oceanWidth
                                                                           ,oceanHeight
                                                                           ,cnvOcean.getWidth() / oceanWidth
                                                                           ,cnvOcean.getHeight() / oceanHeight
                                                                           ,minorGL);
                }
                cnvOcean.setOnMouseClicked(event -> {
                    int x = (int)(event.getX()/landLayer.getHScale()/minorGL);
                    int y = (int)(event.getY()/landLayer.getVScale()/minorGL);
                    if (wasteSourceLayer.hasObject(x,y)){
                    	wasteSourceLayer.removeObject(x, y);
                    } else {
                    	wasteSourceLayer.addObject(new WasteSourceObject(x, y, size, type));
                    }
                    draw();
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

    private void toggleWastePrefs() {
        wastePref.pressedProperty().addListener((observable, oldValue, newValue) -> {
            wastePrefToggle = !wastePrefToggle;
            if (wastePrefToggle) {
                smallItem.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        medItem.selectedProperty().set(false);
                        largeItem.selectedProperty().set(false);
                        size = sourceSize.SMALL;
                    }
                });
                medItem.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        smallItem.selectedProperty().set(false);
                        largeItem.selectedProperty().set(false);
                        size = sourceSize.MEDIUM;
                    }
                });
                largeItem.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        medItem.selectedProperty().set(false);
                        smallItem.selectedProperty().set(false);
                        size = sourceSize.LARGE;
                    }
                });
                oilItem.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        plasticItem.selectedProperty().set(false);
                        miscItem.selectedProperty().set(false);
                        type = sourceType.OIL;
                    }
                });
                plasticItem.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        oilItem.selectedProperty().set(false);
                        miscItem.selectedProperty().set(false);
                        type = sourceType.PLASTIC;
                    }
                });
                miscItem.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        oilItem.selectedProperty().set(false);
                        plasticItem.selectedProperty().set(false);
                        type = sourceType.MISC;
                    }
                });
            }
        });
    }

    private void clearAll() {
        //btnClear.selectedProperty().addListener(((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //if (btnClear.selectedProperty().getValue()){
        btnClear.pressedProperty().addListener((observable, oldValue, newValue) -> {
            if (btnClear.pressedProperty().getValue()){
                btnLand.setSelected(false);
                sldVertical.setDisable(false);
                sldHorizontal.setDisable(false);
                txtVertical.setDisable(false);
                txtHorizontal.setDisable(false);
                if (landLayer != null) {
                	landLayer.clear();
                }
                if (wasteSourceLayer != null) {
                	wasteSourceLayer.clear();
                }
                draw();
            }
        });
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
        String s = "500x500";
        String action;
        if (landToggle) {
            action = "Placing Land";
            s = txtHorizontal.getText() + "x" + txtVertical.getText();
        } else if (wasteToggle) {
            action = "Placing waste";
        } else if (currentToggle) {
            action = "Changing Current";
        } else {
            action = "Changing Size";
            s = txtHorizontal.getText() + "x" + txtVertical.getText();
        }
        int landAmt = 0; // TODO Get this from LandLayer
        int wasteAmt = 0;// TODO Get this from WasteLayer
        statusBar.setText("Action: " + action + "\t Size: "+s+ "\nCurrent Speed:" + (int)horizontalSpeed+" x "+(int)verticalSpeed + "Land amount:"+landAmt/121 + "\tWaste amount:" + wasteAmt/121);
    }

    private void disableSliders() {
        sldVertical.setDisable(true);
        sldHorizontal.setDisable(true);
        txtVertical.setDisable(true);
        txtHorizontal.setDisable(true);
    }
}