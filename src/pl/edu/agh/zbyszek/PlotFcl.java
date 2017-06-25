package pl.edu.agh.zbyszek;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class PlotFcl {

    public static void main(String[] args) throws Exception {
        try {

            String fileName = args[0];
            int vertSpeed = Integer.parseInt(args[1]);
            int height = Integer.parseInt(args[2]);
            int posX = Integer.parseInt(args[3]);
            int posY = Integer.parseInt(args[4]);
            FIS fis = FIS.load(fileName,false);

            // LanderApp plots for "fuzzyfication" and "deffuzyfication"
            FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
            fuzzyRuleSet.chart();

            // Input parameters
            fuzzyRuleSet.setVariable("vert_speed", vertSpeed);
            fuzzyRuleSet.setVariable("height", height);
            fuzzyRuleSet.setVariable("positionX", posX);
            fuzzyRuleSet.setVariable("positionY", posY);
            // Evaluation
            fuzzyRuleSet.evaluate();

            // Output
            fuzzyRuleSet.getVariable("mainEngines").chartDefuzzifier(true);
            fuzzyRuleSet.getVariable("strafeX").chartDefuzzifier(true);
            fuzzyRuleSet.getVariable("strafeY").chartDefuzzifier(true);

        //System.out.println(fuzzyRuleSet);

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Incorrect number of parameters. Usage: java <fcl file> <vert speed> <height> <positionX> <positionY>");
        } catch (NumberFormatException ex) {
            System.out.println("Incorrect parameter. Usage: java <fcl file> <vert speed> <height> <positionX> <positionY>");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}