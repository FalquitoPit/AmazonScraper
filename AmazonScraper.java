package org.example;

import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmazonScraper {
    public static void main(String[] args) {
        try {
            // Paso 1: Obtener los datos de la página web de Amazon
            String url = "https://www.amazon.com/s?k=juegos";
            Document document = Jsoup.connect(url).get();

            // Paso 2: Analizar el HTML y extraer los datos relevantes
            Elements productElements = document.select("div[data-component-type='s-search-result']");

            // Paso 3: Guardar los datos en un archivo de texto
            FileWriter fileWriter = new FileWriter("productos.txt");
            for (Element productElement : productElements) {
                String title = productElement.select("h2 a span").text();
                String priceText = productElement.select("span.a-offscreen").text();

                // Extraer el precio en euros utilizando una expresión regular
                String price = extractPriceInEuros(priceText);

                // Escribir el producto en el archivo
                fileWriter.write(title + " - " + price + "\n");
            }
            fileWriter.close();

            System.out.println("Se ha completado el proceso de scraping y se ha generado el archivo productos.txt.");
        } catch (IOException e) {
            System.err.println("Ocurrió un error durante el proceso de scraping: " + e.getMessage());
        }
    }

    private static String extractPriceInEuros(String priceText) {
        String price = "";
        if (priceText.matches("\\$\\d+(\\.\\d+)?")) {
            double usdPrice = Double.parseDouble(priceText.substring(1));
            double euroPrice = usdPrice * 0.85; // Conversión aproximada de dólares a euros
            price = String.format("%.2f €", euroPrice);
        }
        return price;
    }
}
