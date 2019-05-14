package pl.parser.nbp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Document document;
    private String currencyCode;
    private int multiplier = 1;
    private List<Double> buyRates;
    private List<Double> sellRates;

    public Parser(String code) {
        currencyCode = code;
        buyRates = new ArrayList<>();
        sellRates = new ArrayList<>();
    }

    public int getMultiplier() {
        return multiplier;
    }

    public List<Double> getBuyRates() {
        return buyRates;
    }

    public List<Double> getSellRates() {
        return sellRates;
    }

    private void downloadAndParseXML(URL url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream inputStream = url.openStream();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            this.document = document;

        } catch (IOException e) {
            System.out.println("Nie można otworzyć pliku XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRate() {
        NodeList nodeList = document.getElementsByTagName("pozycja");
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String code = element.getElementsByTagName("kod_waluty").item(0).getTextContent();

                if (code.equals(currencyCode)) {
                    multiplier = Integer.parseInt(element.getElementsByTagName("przelicznik").item(0).getTextContent());

                    String buyRateText = element.getElementsByTagName("kurs_kupna").item(0).getTextContent().replace(",", ".");
                    double buyRate = Double.parseDouble(buyRateText);
                    String sellRateText = element.getElementsByTagName("kurs_sprzedazy").item(0).getTextContent().replace(",", ".");
                    double sellRate = Double.parseDouble(sellRateText);

                    buyRates.add(buyRate);
                    sellRates.add(sellRate);
                }
            }
        }
    }

    public void downloadRates(LocalDate startDate, LocalDate endDate) {
        Period period = startDate.until(endDate);
        int days = period.getDays() + 1;

        for (int i = 0; i < days; ++i) {
            LocalDate date = startDate.plusDays(i);
            URL url = prepareURL(date);
            if (url == null) {
                System.out.println("Brak danych dla dnia " + date);
                continue;
            }
            downloadAndParseXML(prepareURL(date));
            addRate();
        }
    }

    private URL prepareURL(LocalDate date) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("00");
            String yearString = Integer.toString(date.getYear()).substring(2, 4);
            String monthString = decimalFormat.format(date.getMonthValue());
            String dayString = decimalFormat.format(date.getDayOfMonth());
            String dateString = yearString + monthString + dayString;

            int currentYear = LocalDate.now().getYear();
            String year;
            if (date.getYear() == currentYear)
                year = "";
            else
                year = Integer.toString(date.getYear());

            URL list = new URL("https://www.nbp.pl/kursy/xml/dir" + year + ".txt");
            InputStreamReader inputStream = new InputStreamReader(list.openStream());
            BufferedReader reader = new BufferedReader(inputStream);

            String filename;
            while ((filename = reader.readLine()) != null) {
                if (filename.charAt(0) == 'c' && filename.contains(dateString))
                    break;
            }

            reader.close();

            if (filename == null)
                return null;

            return new URL("https://www.nbp.pl/kursy/xml/" + filename + ".xml");
        } catch (MalformedURLException e) {
            System.out.println("Adres URL nie jest poprawny");
        } catch (IOException e) {
            System.out.println("Nie można otworzyć listy plików XML");
        }
        return null;
    }
}
