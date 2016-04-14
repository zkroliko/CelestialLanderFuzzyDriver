package pl.edu.agh.zbyszek.display.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pl.edu.agh.zbyszek.display.Lander;

import static java.lang.Thread.sleep;

public class DisplayController {

    double canWidth;
    double canHeight;

    private Lander lander;

    public boolean drawPath = true;

    public static final int NOMINAL_FRAMERATE = 60;
    private int simSpeed = 60;
    public static final int SIMSPEED_MAX = 10000;

    @FXML
    Canvas canvasXY;
    @FXML
    Canvas canvasXZ;
    @FXML
    Canvas canvasYZ;
    @FXML
    Canvas canvasXY2;
    @FXML
    Canvas canvasXZ2;
    @FXML
    Canvas canvasYZ2;
    @FXML
    TextField posX;
    @FXML
    TextField posY;
    @FXML
    TextField vert;
    @FXML
    TextField height;
    @FXML
    Slider simSpeedSlider;

    Timeline timelineXYZ = new Timeline();
    AnimationTimer timer;

    @FXML
    private void initialize() {
        canWidth = canvasXY.getWidth();
        canHeight = canvasXY.getHeight();
        canvasXY2.toBack();
        canvasXZ2.toBack();
        canvasYZ2.toBack();
    }

    final DoubleProperty x  = new SimpleDoubleProperty();
    final DoubleProperty y  = new SimpleDoubleProperty();
    final DoubleProperty z  = new SimpleDoubleProperty();

    public void playLanderAnimation() {
        System.out.println(String.format("Running animation at %s frames per second. Animation consists of %s frames.", simSpeed, timelineXYZ.getKeyFrames().size()));
        timer.start();
        timelineXYZ.playFromStart();
    }

    public void setUpAnimationFraming() {
        // Configuring the timeline
        timelineXYZ = new Timeline();
        timelineXYZ.setRate(simSpeed);
        timelineXYZ.setCycleCount(Timeline.INDEFINITE);

        // New animation timer
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                displayLander();
            }
        };
    }

    private void addLanderFrame() {
        timelineXYZ.getKeyFrames().add(
                new KeyFrame(Duration.seconds(lander.getSimTime()),
                        new KeyValue(x, normalizeToCanvas(lander.getX(), lander.getStartX(), canWidth / 2)),
                        new KeyValue(y, normalizeToCanvas(lander.getY(), lander.getStartY(), canHeight / 2)),
                        new KeyValue(z, normalizeToCanvas(lander.getZ(), lander.getStartZ(), canHeight))
                )
        );
    }

    private void displayLander() {
        clearForeground();
        setForegroundColor(Color.SILVER);
        drawLanderGraphics(8);
    }

    private void drawPath() {
        double temp = lander.getRelativeSpeed();
        setBackgroundColor(Color.color(Math.pow(temp, 0.4), 1 / ((8 * temp + 1)), 0, 1));
        drawLines();
    }

    private void setBackgroundColor(Color color) {
        setCanvasColor(canvasXY, color);
        setCanvasColor(canvasXZ, color);
        setCanvasColor(canvasYZ, color);
    }

    private void setForegroundColor(Color color) {
        setCanvasColor(canvasXY2, color);
        setCanvasColor(canvasXZ2, color);
        setCanvasColor(canvasYZ2, color);
    }

    private void setCanvasColor(Canvas canvas, Color color) {
        canvas.getGraphicsContext2D().setFill(color);
        canvas.getGraphicsContext2D().setStroke(color);
    }

    private void drawLanderGraphics(int size) {
        int offset = size /2;
        canvasXY2.getGraphicsContext2D().fillOval(canWidth / 2 + x.doubleValue() - offset, canHeight / 2 + y.doubleValue() -offset, size, size);
        canvasXZ2.getGraphicsContext2D().fillOval(canWidth / 2 + x.doubleValue() -offset, canHeight - z.doubleValue() -offset, size, size);
        canvasYZ2.getGraphicsContext2D().fillOval(canWidth / 2 + y.doubleValue() -offset, canHeight - z.doubleValue() -offset, size, size);
    }

    // This is ugly :(
    private void drawLines() {
        canvasXY.getGraphicsContext2D().strokeLine(canWidth / 2 + normalizeToCanvas(lander.getLastX(), lander.getStartX(), canWidth / 2), canHeight / 2 + normalizeToCanvas(lander.getLastY(), lander.getStartY(), canHeight / 2), canWidth / 2 + normalizeToCanvas(lander.getX(), lander.getStartX(), canWidth / 2), canHeight / 2 + normalizeToCanvas(lander.getY(), lander.getStartY(), canHeight / 2));
        canvasXZ.getGraphicsContext2D().strokeLine(canWidth / 2 + normalizeToCanvas(lander.getLastX(), lander.getStartX(), canWidth / 2), canHeight - normalizeToCanvas(lander.getLastZ(), lander.getStartZ(), canHeight), canWidth / 2 + normalizeToCanvas(lander.getX(), lander.getStartX(), canWidth / 2), canHeight - normalizeToCanvas(lander.getZ(), lander.getStartZ(), canHeight));
        canvasYZ.getGraphicsContext2D().strokeLine(canWidth / 2 + normalizeToCanvas(lander.getLastY(), lander.getStartY(), canHeight / 2), canHeight - normalizeToCanvas(lander.getLastZ(), lander.getStartZ(), canHeight), canWidth / 2 + normalizeToCanvas(lander.getY(), lander.getStartY(), canHeight / 2), canHeight - normalizeToCanvas(lander.getZ(), lander.getStartZ(), canHeight));
    }

    private double normalizeToCanvas(double x, double scale, double limit) {
        return (x/scale)*limit*0.9;
    }

    private void clearAll() {
        clearForeground();
        clearBackground();
    }

    private void clearBackground() {
        clearCanvas(canvasXY);
        clearCanvas(canvasXZ);
        clearCanvas(canvasYZ);
    }

    private void clearForeground() {
        clearCanvas(canvasXY2);
        clearCanvas(canvasXZ2);
        clearCanvas(canvasYZ2);
    }

    private void clearCanvas(Canvas canvas) {
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public boolean up;

    public void update(int simTime) {
        // Important thing to note: we want a certain line density
        // for the lander's path
        if (drawPath) {
                drawPath();
        }
        addLanderFrame();
    }

    public void handleStart(ActionEvent event) {
        clearAll();
        setUpAnimationFraming();
        String [] args = {"controller.fcl", vert.getText(), posX.getText(), posY.getText(),height.getText()};
        lander = new Lander(this, args);
        lander.simulate();
        playLanderAnimation();
    }

    public void handleSlide(Event event) {
        setSimSpeed((int)simSpeedSlider.getValue());
    }

    public void setSimSpeed(int simSpeed) {
        this.simSpeed = (simSpeed > SIMSPEED_MAX) ? SIMSPEED_MAX : simSpeed;
        timelineXYZ.setRate(simSpeed);
        System.out.println("Simspeed set to: " + this.simSpeed);
    }

    public double getSimSpeedSlider() {
        return simSpeedSlider.getValue();
    }
}
