package pl.parser.nbp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Document document;
    private List<Currency> rates;
    private String currencyCode;

    public Parser(String code) {
        currencyCode = code;
        rates = new ArrayList<Currency>();
    }

    private void downloadAndParseXML(String date) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            URL url = new URL("http://www.nbp.pl/kursy/xml/c022z130131.xml");
            InputStream inputStream = url.openStream();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            this.document = document;

        } catch (MalformedURLException e) {
            System.out.println("Adres URL nie jest poprawny");
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
                    String name = element.getElementsByTagName("nazwa_waluty").item(0).getTextContent();
                    int multiplier = Integer.parseInt(element.getElementsByTagName("przelicznik").item(0).getTextContent());

                    String buyRateText = element.getElementsByTagName("kurs_kupna").item(0).getTextContent().replace(",", ".");
                    double buyRate = Double.parseDouble(buyRateText);
                    String sellRateText = element.getElementsByTagName("kurs_sprzedazy").item(0).getTextContent().replace(",", ".");
                    double sellRate = Double.parseDouble(sellRateText);

                    Currency newRate = new Currency(name, multiplier, code, buyRate, sellRate);
                    rates.add(newRate);
                }
            }
        }
    }

    public List<Currency> getRates(String startDate, String endDate) {
        // prepare list of dates
        // for {
        downloadAndParseXML("");
        addRate();
        // }

        return rates;
    }
}
