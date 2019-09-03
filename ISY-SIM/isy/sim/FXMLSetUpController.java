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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 *
 * @author gfoster
 */
public class FXMLSetUpController implements Initializable {
    
    @FXML private TextField txtOceanWidth;
    @FXML private TextField txtOceanHeight;
    @FXML private Slider sldOceanWidth;
    @FXML private Slider sldOceanHeight;
    @FXML private Canvas cnvOcean;
    @FXML private Button btnPlay;
    @FXML private Button btnWaste;
    @FXML private Button btnLand;
    @FXML private Button btnCurrent;

    private GraphicsContext gc ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = cnvOcean.getGraphicsContext2D();
        drawOcean();
        initializeSliders();
    }

    private void initializeSliders() {
        sldOceanWidth.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setOceanWidth(newValue.doubleValue());
                txtOceanWidth.setText(String.format("%d", newValue.intValue()));
            }
        });
        sldOceanHeight.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setOceanHeight(newValue.doubleValue());
                txtOceanHeight.setText(String.format("%d", (1000-newValue.intValue())));
            }
        });
    }
    
    private void drawOcean(){
        gc.setFill(Color.AQUAMARINE);
        gc.fillRect(0, 0, 500, 500);
    }

    private void setOceanWidth(double width) {
        cnvOcean.setWidth(width);
    }

    private void setOceanHeight(double height) {
        cnvOcean.setHeight(height);
    }
}
