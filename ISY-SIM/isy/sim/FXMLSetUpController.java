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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

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
    @FXML private Button btnWaste;
    @FXML private Button btnLand;
    @FXML private Toggle btnCurrent;

    private GraphicsContext gc ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = cnvOcean.getGraphicsContext2D();
        drawOcean();
        initializeSliders();
    }

    private void initializeSliders() {
        sldHorizontal.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setOceanWidth(newValue.doubleValue());
                txtHorizontal.setText(String.format("%d", newValue.intValue()));
            }
        });
        sldVertical.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setOceanHeight(1100-newValue.doubleValue());
                txtVertical.setText(String.format("%d", (1100-newValue.intValue())));
            }
        });
    }
    
    private void drawOcean(){
        gc.setFill(Color.AQUAMARINE);
        gc.fillRect(0, 0, cnvOcean.getWidth(), cnvOcean.getHeight());
    }

    private void setOceanWidth(double width) {
        double scale = sldHorizontal.getWidth() / 900;
        cnvOcean.setWidth((width-100)*scale+40);
        drawOcean();
    }

    private void setOceanHeight(double height) {
        double scale = sldVertical.getHeight() / 900;
        cnvOcean.setHeight((height-100)*scale+25);
        drawOcean();
    }
}
