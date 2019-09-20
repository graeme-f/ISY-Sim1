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

import com.sun.deploy.panel.TextFieldProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import javafx.scene.control.ToggleGroup;


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
    @FXML private Button btnLand;
    @FXML private ToggleButton btnCurrent;
    @FXML private ToggleGroup wasteLand;

    private GraphicsContext gc ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = cnvOcean.getGraphicsContext2D();
        drawOcean();
        initializeSliders();
        toggleCurrent();
    }

    private void initializeSliders() {
        StringProperty txtHor = txtHorizontal.textProperty();
        DoubleProperty sldHor = sldHorizontal.valueProperty();
        StringConverter<Number> convHorizontal = new NumberStringConverter();
        Bindings.bindBidirectional(txtHor, sldHor, convHorizontal);

        StringProperty txtVer = txtVertical.textProperty();
        DoubleProperty sldVer = sldVertical.valueProperty();
        StringConverter<Number> convVertical = new NumberStringConverter();
        Bindings.bindBidirectional(txtVer, sldVer, convVertical);

        sldHorizontal.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (currentToggle) {
                    setCurrentHorizontal(newValue.doubleValue());
                }else {
                    setOceanWidth(newValue.doubleValue());
                }
                txtHorizontal.setText(String.format("%d", newValue.intValue()));
            }
        });
        sldVertical.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (currentToggle) {
                    setCurrentVertical(newValue.doubleValue());
                }else {
                    setOceanHeight(newValue.doubleValue());
                }
                txtVertical.setText(String.format("%d", (newValue.intValue())));
            }
        });
    }
    
    private void drawOcean(){
        gc.setFill(Color.AQUAMARINE);
        gc.fillRect(0, 0, cnvOcean.getWidth(), cnvOcean.getHeight());
        arwCurrentUpSize(gc);
        arwCurrentRightSize(gc);
        arwCurrentLeftSize(gc);
        arwCurrentDownSize(gc);
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


    private boolean currentToggle = false;
    private boolean landToggle = false;
    private boolean wasteToggle = false;
    private double oceanWidth;
    private double oceanHeight;
    private double horizontalSpeed = 0;
    private double verticalSpeed = 0;

    private void toggleCurrent() {
        btnCurrent.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            currentToggle = !currentToggle;
            if (currentToggle){
                oceanWidth = sldHorizontal.getValue();
                oceanHeight = sldVertical.getValue();
                txtHorizontal.setText(String.valueOf(horizontalSpeed));
                txtVertical.setText(String.valueOf(verticalSpeed));
            } else {
                horizontalSpeed = sldHorizontal.getValue();
                System.out.println(horizontalSpeed);
                txtHorizontal.setText(String.valueOf(oceanWidth));
                txtVertical.setText(String.valueOf(oceanHeight));
            }
        }));
    }

    private void toggleLand() {
        btnLand.pressedProperty().addListener(((observable, oldValue, newValue) -> {
            untoggle();
            landToggle = !landToggle;
        }));
    }
    private void toggleWaste() {
        btnWaste.pressedProperty().addListener(((observable, oldValue, newValue) -> {
            untoggle();
            landToggle = !landToggle;
        }));
    }

    private void untoggle() {
        landToggle = false;
        wasteToggle = false;
    }


    private void setCurrentHorizontal(double speed) {
        horizontalSpeed = speed;
    }

    private void setCurrentVertical(double speed) {
        verticalSpeed = speed;
    }


}