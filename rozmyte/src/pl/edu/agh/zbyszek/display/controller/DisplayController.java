package pl.edu.agh.zbyszek.display.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import pl.edu.agh.zbyszek.display.Lander;

public class DisplayController {

    double canX;
    double canY;

    private Lander lander;

    public boolean drawPath = true;

    @FXML
    Canvas canvasXY;

    @FXML
    Canvas canvasXZ;

    @FXML
    Canvas canvasYZ;

    @FXML
    TextField posX;

    @FXML
    TextField posY;

    @FXML
    TextField vert;

    @FXML
    TextField height;

    @FXML
    Slider simSpeed;

    @FXML
    private void initialize() {
        canX = canvasXY.getWidth();
        canY = canvasXY.getHeight();
        clear();
    }

    private void displayLander() {
        clear();
        setColor(Color.RED);
        drawLander(5);
    }

    private void drawPath() {
        double temp = lander.getRelativeSpeed();
        setColor(Color.color(Math.pow(temp,0.4), 1/((8*temp+1)), 0, 1));
        drawLines();
    }

    private void setColor(Color color) {
        setCanvasColor(canvasXY, color);
        setCanvasColor(canvasXZ, color);
        setCanvasColor(canvasYZ, color);
    }

    private void setCanvasColor(Canvas canvas, Color color) {
        canvas.getGraphicsContext2D().setFill(color);
        canvas.getGraphicsContext2D().setStroke(color);
    }

    private void drawLander(int size) {
        canvasXY.getGraphicsContext2D().fillOval(canX / 2 + normalizeToCanvas(lander.getX(), lander.getStartX(), canX / 2), canY / 2 + normalizeToCanvas(lander.getY(), lander.getStartX(), canY / 2), size, size);
        canvasXZ.getGraphicsContext2D().fillOval(canX / 2 + normalizeToCanvas(lander.getX(), lander.getStartX(), canX / 2), canY - 10 - normalizeToCanvas(lander.getZ(), lander.getStartZ(), canY), size, size);
        canvasYZ.getGraphicsContext2D().fillOval(canX / 2 + normalizeToCanvas(lander.getY(), lander.getStartY(), canY / 2), canY - 10 - normalizeToCanvas(lander.getZ(), lander.getStartZ(), canY), size, size);
    }

    // This is ugly :(
    private void drawLines() {
        canvasXY.getGraphicsContext2D().strokeLine(canX / 2 + normalizeToCanvas(lander.getLastX(), lander.getStartX(), canX / 2), canY / 2 + normalizeToCanvas(lander.getLastY(), lander.getStartX(), canY / 2), canX / 2 + normalizeToCanvas(lander.getX(), lander.getStartX(), canX / 2), canY / 2 + normalizeToCanvas(lander.getY(), lander.getStartX(), canY / 2));
        canvasXZ.getGraphicsContext2D().strokeLine(canX / 2 + normalizeToCanvas(lander.getLastX(), lander.getStartX(), canX / 2), canY - 10 - normalizeToCanvas(lander.getLastZ(), lander.getStartZ(), canY), canX / 2 + normalizeToCanvas(lander.getX(), lander.getStartX(), canX / 2), canY - 10 - normalizeToCanvas(lander.getZ(), lander.getStartZ(), canY));
        canvasYZ.getGraphicsContext2D().strokeLine(canX / 2 + normalizeToCanvas(lander.getLastY(), lander.getStartY(), canY / 2), canY - 10 - normalizeToCanvas(lander.getLastZ(), lander.getStartZ(), canY), canX / 2 + normalizeToCanvas(lander.getY(), lander.getStartY(), canY / 2), canY - 10 - normalizeToCanvas(lander.getZ(), lander.getStartZ(), canY));
    }

    private double normalizeToCanvas(double x, double scale, double limit) {
        return (x/scale)*limit*0.9;
    }

    private void clear() {
        clearCanvas(canvasXY);
        clearCanvas(canvasXZ);
        clearCanvas(canvasYZ);
    }

    private void clearCanvas(Canvas canvas) {
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public boolean up;

    public void update(int simTime) {
        // Important thing to note: we want a certain line density
        // for the lander's path
        if (drawPath) {
                drawPath();
        } else
            displayLander();
    }

    public void handleStart(ActionEvent event) {
        clear();
        String [] args = {"sterownik.fcl", vert.getText(), posX.getText(), posY.getText(),height.getText(), String.valueOf(getSimSpeed())};
        lander = new Lander(this, args);
        lander.animate();
    }

    public void handleSlide(Event event) {
        if (lander != null) {
            lander.setSimSpeed((int) getSimSpeed());
        }
    }

    public double getSimSpeed () {
        return simSpeed.getValue();
    }
}
