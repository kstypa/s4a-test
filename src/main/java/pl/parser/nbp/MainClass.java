package pl.parser.nbp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Podaj poprawne argumenty w postaci: \"KOD YYYY-MM-DD YYYY-MM-DD\"");
            return;
        }

        String currencyCode = args[0];
        String startDate = args[1];
        String endDate = args[2];

        // validate input

        Parser parser = new Parser(currencyCode);
        List<Currency> rates = parser.getRates(startDate, endDate);

        ArrayList<Double> buyRates = new ArrayList<Double>();
        ArrayList<Double> sellRates = new ArrayList<Double>();

        for (Currency currency : rates) {
            buyRates.add(currency.getBuyRate());
            sellRates.add(currency.getSellRate());
        }

        double buyMean = Statistics.calculateMean(buyRates);
        double sellStdDev = Statistics.calculateStdDev(sellRates);

        DecimalFormat decimalFormat = new DecimalFormat("###0.0000");

        System.out.println("Waluta: " + currencyCode);
        System.out.println("Data: " + startDate + " - " + endDate);
        System.out.println("Średni kurs kupna: " + decimalFormat.format(buyMean));
        System.out.println("Odchylenie standardowe kursów sprzedaży: " + decimalFormat.format(sellStdDev));

    }
}
