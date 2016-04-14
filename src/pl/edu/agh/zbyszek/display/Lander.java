package pl.edu.agh.zbyszek.display;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import pl.edu.agh.zbyszek.display.controller.DisplayController;

import static java.lang.Thread.sleep;

public class Lander {

    String fileName;
    Double vertSpeed;
    Double maxVertSpeed = Double.NEGATIVE_INFINITY;
    Double x,y,z;
    Double lastX, lastY, lastZ;

    Double startX;
    Double startY;
    Double startZ;

    public static final double G = 3.32;

    public static final double TERMINAL_V = 850.0;
    public static final double ATM_HEIGHT = 80000;
    public static final double ATM_DENSITY = 0.1;

    private int simTime = 0;

    FuzzyRuleSet fuzzyRuleSet;
    private DisplayController displayController;

    public Lander(DisplayController displayController,String[] args)  {

        try {
            this.displayController = displayController;
            fileName = args[0];
            setVertSpeed(Double.parseDouble(args[1]));
            startX = Double.parseDouble(args[2]);
            startY = Double.parseDouble(args[3]);
            startZ = Double.parseDouble(args[4]);

            setX(startX);
            setY(startY);
            setZ(startZ);

            FIS fis = FIS.load(fileName, false);

            fuzzyRuleSet = fis.getFuzzyRuleSet();

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Niepoprawna liczba parametrow.");
        } catch (NumberFormatException ex) {
            System.out.println("Niepoprawny parametr" + ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    public void setVariables() {
        fuzzyRuleSet.setVariable("predkoscPionowa", vertSpeed);
        fuzzyRuleSet.setVariable("wysokosc", z);
        fuzzyRuleSet.setVariable("pozycjaX", x);
        fuzzyRuleSet.setVariable("pozycjaY", y);
    }

    public void simulate() {
        while(z > 0) {
            System.out.println(String.format("The sim time is %d: %f, %f, %f, %f", simTime, vertSpeed, x, y, z));
            simTime++;
            //logika sterownika
            setVariables();
            fuzzyRuleSet.evaluate();
            // poprawienie wartosci
            setX(x + fuzzyRuleSet.getVariable("ruchX").defuzzify());
            setY(y + fuzzyRuleSet.getVariable("ruchY").defuzzify());
            setZ(z - vertSpeed);
            vertSpeed += fuzzyRuleSet.getVariable("silnikiGlowne").defuzzify();
            vertSpeed += G;
            // Utrzymanie gornej granicy
            double dragCoeff = Math.abs(ATM_HEIGHT - z) / ATM_HEIGHT * ATM_DENSITY;
            vertSpeed -= dragCoeff * (1 - vertSpeed / TERMINAL_V) * vertSpeed;
            // Handling the display
            updateTelemetry();
        }
    }


    public void updateTelemetry() {
        displayController.update(simTime);
    }

    public double getRelativeSpeed() {
        if (vertSpeed < maxVertSpeed)
            return Math.abs(vertSpeed /maxVertSpeed);
        else
            return 0;
    }

    public void setVertSpeed(double vertSpeed) {
        this.vertSpeed = (vertSpeed > TERMINAL_V) ? TERMINAL_V : vertSpeed;
        if (this.vertSpeed > maxVertSpeed)
            maxVertSpeed = vertSpeed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        if (this.x == null)
            lastX = startX;
        else
            lastX = this.x;
        this.x = x;
    }

    public void setY(double y) {
        if (this.y == null)
            lastY = startY;
        else
            lastY = this.y;
        this.y = y;
    }

    public void setZ(double z) {
        if (this.z == null)
            lastZ = startZ;
        else
            lastZ = this.z;
        this.z = z;
    }

    public Double getStartX() {
        return startX;
    }

    public Double getStartY() {
        return startY;
    }

    public double getStartZ() {
        return startZ;
    }

    public Double getLastX() {
        return lastX;
    }

    public Double getLastY() {
        return lastY;
    }

    public Double getLastZ() {
        return lastZ;
    }

    public int getSimTime() {
        return simTime;
    }
}
