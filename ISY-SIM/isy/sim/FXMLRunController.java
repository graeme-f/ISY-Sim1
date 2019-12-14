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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import sim.Layers.LandLayer;
import sim.Layers.WasteSourceLayer;

public class FXMLRunController implements Initializable {


    private boolean stopped = false;
    private static final int TIMESLICE = 100;
    @FXML private Slider sldSpeed;
    @FXML public Canvas cnvOcean;
    @FXML public Button btnStop;
    @FXML private Button btnPlay;
    @FXML private Button btnAnalysis;
    @FXML private void handleStop(ActionEvent event)
    {
            stopped = true;
            timeline.stop();
            speed = Duration.ZERO;
    }
    @FXML private void handlePlay(ActionEvent event)
    {
            stopped = false;
            timeline.stop();
            speed = Duration.millis((11-sldSpeed.getValue())*TIMESLICE);
            startTimeline();
    }



    public GraphicsContext gc;
    private LandLayer landLayer;
    private WasteSourceLayer wasteSourceLayer;
    private double hSpeed;
    private double vSpeed;

    private Rectangle r;
    private Duration speed;
    private Timeline timeline;
    
    @FXML private Label lblTime;
    private int time = 0;
    @FXML private Label lblWasteCount;
    private int wasteCount = 0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        gc = cnvOcean.getGraphicsContext2D();
        gc.fillRect(5, 5, 10, 10);
        r = new Rectangle(5,5,10,10);
        setSlider();
        speed = Duration.millis(10 * TIMESLICE);
        draw();
        startTimeline();
    }
    
    public void setup(LandLayer land
                     ,WasteSourceLayer wasteSource
                     ,double horizontalSpeed
                     , double verticalSpeed){
        gc.setFill(Color.web("#2cd5c4"));
        gc.fillRect(0, 0, cnvOcean.getWidth(), cnvOcean.getHeight());
        landLayer = land;
        if (landLayer != null){
            landLayer.setActiveGC(gc);
            landLayer.setScale(1, 1);
            landLayer.drawLayer();
        }
        wasteSourceLayer = wasteSource;
        if (wasteSourceLayer != null){
            wasteSourceLayer.setActiveGC(gc);
            wasteSourceLayer.setScale(1,1);
            wasteSourceLayer.drawLayer();
        }
        hSpeed = horizontalSpeed;
        vSpeed = verticalSpeed;
    }

    private void startTimeline() {
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(speed, e -> {draw();}));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private void setSlider(){
        sldSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            timeline.stop();
            if (!stopped){
                speed = Duration.millis((11-newValue.doubleValue())*TIMESLICE);
            }
            startTimeline();
        });
    } // end of method setSlider()

    private void draw() {
        // Generate waste from sources
        
        // Move waste around ocean

        // Merge ocean
        
        // Draw ocean
        lblTime.setText("Time: " + ++time);
    }
}
