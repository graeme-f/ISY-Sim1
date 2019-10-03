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

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import com.sun.deploy.panel.TextFieldProperty;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

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

    private boolean currentToggle = false;
    private boolean landToggle = false;
    private GraphicsContext gc;
    private double oceanWidth=500;
    private double oceanHeight=500;
    private double horizontalSpeed = 2;
    private double verticalSpeed = 2;

    private StringProperty txtHor;
    private DoubleProperty sldHor;
    private StringConverter<Number> convHorizontal;

    private StringProperty txtVer;
    private DoubleProperty sldVer;
    private StringConverter<Number> convVertical;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wastePref.setVisible(false);
        wastePref.setMinWidth(1);
        wastePref.setPrefWidth(1);
        statusBar.setMinWidth(450);
        updateStatus();
        toggleWaste();
        gc = cnvOcean.getGraphicsContext2D();
        drawOcean();
        initializeSliders();
        toggleCurrent();
    }

    private void initializeSliders() {
        txtHor = txtHorizontal.textProperty();
        sldHor = sldHorizontal.valueProperty();
        convHorizontal = new NumberStringConverter();
        txtVer = txtVertical.textProperty();
        sldVer = sldVertical.valueProperty();
        convVertical = new NumberStringConverter();

        Bindings.bindBidirectional(txtHor, sldHor, convHorizontal);
        Bindings.bindBidirectional(txtVer, sldVer, convVertical);

        sldHorizontal.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (currentToggle) {
                    setCurrentHorizontal(newValue.doubleValue());
                    drawOcean();
                    sldHorizontal.setValue(horizontalSpeed);

                } else {
                    setOceanWidth(newValue.doubleValue());
                    updateStatus();
                    oceanWidth = sldHorizontal.getValue();
                }
                txtHorizontal.setText(String.format("%d", newValue.intValue()));
            }
        });

        sldVertical.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (currentToggle) {
                    setCurrentVertical(newValue.doubleValue());
                    drawOcean();
                    sldVertical.setValue(verticalSpeed);
                } else {
                    setOceanHeight(newValue.doubleValue());
                    updateStatus();
                    oceanHeight = sldVertical.getValue();
                }
                txtVertical.setText(String.format("%d", (newValue.intValue())));
            }
        });
    }
    private void drawOcean(){
        gc.setFill(Color.AQUAMARINE);
        gc.fillRect(0, 0, cnvOcean.getWidth(), cnvOcean.getHeight());
        drawGridLines(gc, 5, 50);
        initializeArrows(gc);
        updateStatus();
    }
    private void setOceanWidth(double width) {
        double scale = sldHorizontal.getWidth() / 900;
        double oWidth = (width-100)*scale+60;
        cnvOcean.setWidth(oWidth);
        drawOcean();
    }
    private void setOceanHeight(double height) {
        double scale = sldVertical.getHeight() / 900;
        double oHeight = (height-100)*scale+50;
        cnvOcean.setHeight(oHeight);
        cnvOcean.setTranslateY(525-oHeight);
        drawOcean();
    }
    private void drawGridLines(GraphicsContext gc, int minorGL, int majorGL) {
        gc.setStroke(Color.BLUE);
        int cnvWidth = (int) cnvOcean.getWidth();
        int cnvHeight = (int) cnvOcean.getHeight();
        for (int width = 0; width < cnvWidth; width += minorGL) {
            for (int height = cnvHeight; height > 0; height -= minorGL) {
                // TODO: Find  a more concise way of doing this
                if (width % majorGL == 0 && (cnvHeight-height) % majorGL != 0) {
                    gc.setLineWidth(0.4);
                    gc.strokeLine(width, height, width+minorGL, height);
                    gc.setLineWidth(3.0);
                    gc.strokeLine(width, height, width, height-minorGL);
                } else if ((cnvHeight-height) % majorGL == 0 && width % majorGL != 0) {
                    gc.setLineWidth(3.0);
                    gc.strokeLine(width, height, width+minorGL, height);
                    gc.setLineWidth(0.4);
                    gc.strokeLine(width, height, width, height-minorGL);
                } else if ((cnvHeight-height) % majorGL == 0 && width % majorGL == 0) {
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
    private void initializeArrows(GraphicsContext gc) {
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
                txtHorizontal.setText(String.valueOf(oceanWidth));
                txtVertical.setText(String.valueOf(oceanHeight));
                setSldHorMinMax(100, 1000);
                setSldVerMinMax(100, 1000);
                txtHorizontal.setText(String.valueOf(oceanWidth));
                txtVertical.setText(String.valueOf(oceanHeight));
            }
        }));
    }


        private void toggleLand() {
        btnLand.pressedProperty().addListener(((observable, oldValue, newValue) -> {
        }));
    }
    private void toggleWaste() {
        btnWaste.pressedProperty().addListener(((observable, oldValue, newValue) -> {
            if (btnWaste.selectedProperty().getValue()) {
                wastePref.setVisible(false);
                wastePref.setMinWidth(1);
                wastePref.setPrefWidth(1);
                statusBar.setMinWidth(450);
            } else {
                wastePref.setVisible(true);
                wastePref.setMinWidth(100);
                wastePref.setPrefWidth(100);
                statusBar.setMinWidth(365);
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

    private void updateStatus(){
        String action;
        if (currentToggle){
            action="Changing Current";
        } else {
            action="Changing Size";
        }
        statusBar.setText("Action: " + action + "\t Size: "+(int)oceanWidth+"x"+(int)oceanHeight+'\t' + "Current Speed:" + (int)horizontalSpeed+" x "+(int)verticalSpeed + "\nLand amount:" + "\tWaste amount:");
    }


//    private void generateRandomIsland(GraphicsContext gc, int length) {
//        Random random = new Random();
//        int cnvHeight = (int) cnvOcean.getHeight();
//        int cnvWidth = (int) cnvOcean.getWidth();
//        int heightCoef = cnvHeight/length;
//        int widthCoef = cnvWidth/length;
//        double[] xPoints = {(double) length*random.nextInt(widthCoef), (double) length*random.nextInt(widthCoef), (double) length*random.nextInt(widthCoef)};
//        double[] yPoints = {(double) length*random.nextInt(heightCoef), (double) length*random.nextInt(heightCoef), (double) length*random.nextInt(heightCoef)};
//    }

}