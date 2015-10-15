package pl.edu.agh.zbyszek;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class FuzzyExample {

    public static void main(String[] args) throws Exception {
        try {

            String fileName = args[0];
            int predkoscPionowa = Integer.parseInt(args[1]);
            int wysokosc = Integer.parseInt(args[2]);
            int pozycjaX = Integer.parseInt(args[3]);
            int pozycjaY = Integer.parseInt(args[4]);
            FIS fis = FIS.load(fileName,false);

//wyswietl wykresy funkcji fuzyfikacji i defuzyfikacji
            FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
            fuzzyRuleSet.chart();

//zadaj wartosci wejsciowe
            fuzzyRuleSet.setVariable("predkoscPionowa", predkoscPionowa);
            fuzzyRuleSet.setVariable("wysokosc", wysokosc);
            fuzzyRuleSet.setVariable("pozycjaX", pozycjaX);
            fuzzyRuleSet.setVariable("pozycjaY", pozycjaY);
//logika sterownika
            fuzzyRuleSet.evaluate();

//graficzna prezentacja wyjscia
            fuzzyRuleSet.getVariable("silnikiGlowne").chartDefuzzifier(true);
            fuzzyRuleSet.getVariable("ruchX").chartDefuzzifier(true);
            fuzzyRuleSet.getVariable("ruchY").chartDefuzzifier(true);

//System.out.println(fuzzyRuleSet);

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Niepoprawna liczba parametrow. Przyklad: java Ladownik string<plik_fcl> int<predkosc pionowa> int<wysokosc> int<pozycjaX> int<pozycjaY>");
        } catch (NumberFormatException ex) {
            System.out.println("Niepoprawny parametr. Przyklad: java Ladownik string<plik_fcl> int<predkosc pionowa> int<wysokosc>  int<pozycjaX> int<pozycjaY>");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}