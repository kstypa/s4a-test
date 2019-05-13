package pl.parser.nbp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Podaj poprawne argumenty w postaci: \"<kod waluty> <data początkowa> <data końcowa>\"");
            return;
        }

        String currencyCode = args[0];
        String startDate = args[1];
        String endDate = args[2];

        List<Currency> rates = new ArrayList<Currency>();
        rates.add(new Currency("euro", 1, "EUR", 4.1301, 4.2135));
        rates.add(new Currency("euro", 1, "EUR", 4.1621, 4.2461));
        rates.add(new Currency("euro", 1, "EUR", 4.1530, 4.2370));
        rates.add(new Currency("euro", 1, "EUR", 4.1569, 4.2409));

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
