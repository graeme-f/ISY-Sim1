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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FXMLRunController implements Initializable {

	@FXML private AnchorPane anchorPane;
	@FXML private Canvas cnvOcean;
	

    private GraphicsContext gc;
	private Rectangle r;
	private Duration speed;
	private Timeline timeline;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gc = cnvOcean.getGraphicsContext2D();
		gc.fillRect(5, 5, 10, 10);
		r = new Rectangle(5,5,10,10);
		speed = Duration.millis(10);
		draw();
		setTimeline();
        timeline.play();
	}

	private void setTimeline() {
        timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(new KeyFrame(speed, e -> {draw();}));
        timeline.setCycleCount(Timeline.INDEFINITE);
	}

    
    private void draw() {
    	// Readjusts the update speed of the animation
    	if (r.getX()%10==0) {
    		timeline.stop();
    		speed = Duration.millis(r.getX()%100+1);
    		setTimeline();
            timeline.play();
    	}
    	// Clear Move and then Draw the block on the screen
    	gc.clearRect(r.getX()-40, r.getY(), r.getWidth(), r.getHeight());
    	gc.setFill(Color.CORAL);
    	gc.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    	r.setX(r.getX()+1);
    	gc.setFill(Color.RED);
    	gc.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
}
