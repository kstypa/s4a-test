package pl.parser.nbp;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainClass {
    private static String currencyCode;
    private static LocalDate startDate;
    private static LocalDate endDate;

    public static void main(String[] args) {
        validateInput(args);

        Parser parser = new Parser(currencyCode);
        parser.downloadRates(startDate, endDate);

        List<Double> buyRates = parser.getBuyRates();
        List<Double> sellRates = parser.getSellRates();

        double buyMean = Statistics.calculateMean(buyRates);
        double sellStdDev = Statistics.calculateStdDev(sellRates);

        DecimalFormat decimalFormat = new DecimalFormat("###0.0000");

        System.out.println("Waluta: " + parser.getMultiplier() + " " + currencyCode);
        System.out.println("Data: " + startDate + " - " + endDate);
        if (buyRates.size() == 0 || sellRates.size() == 0) {
            System.out.println("Brak danych dla podanego zakresu dat");
        }
        else {
            System.out.println("Średni kurs kupna: " + decimalFormat.format(buyMean));
            System.out.println("Odchylenie standardowe kursów sprzedaży: " + decimalFormat.format(sellStdDev));
        }
    }

    private static void validateInput(String[] args) {
        if (args.length != 3) {
            System.out.println("Podaj poprawne argumenty w postaci: \"KOD YYYY-MM-DD YYYY-MM-DD\"");
            System.exit(-1);
        }

        currencyCode = args[0].toUpperCase();
        String startDateText = args[1];
        String endDateText = args[2];

        if (currencyCode.length() != 3) {
            System.out.println("Kod waluty nie jest poprawny");
            System.exit(-1);
        }

        try {
            startDate = LocalDate.parse(startDateText);
            endDate = LocalDate.parse(endDateText);

            if (endDate.isBefore(startDate) || startDate.getYear() < 2002 || endDate.isAfter(LocalDate.now())) {
                System.out.println("Podany zakres dat jest niepoprawny. Podaj datę z zakresu 2002-01-01 do " + LocalDate.now());
                System.exit(-1);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Podana data jest niepoprawna");
            System.exit(-1);
        }
    }
}
