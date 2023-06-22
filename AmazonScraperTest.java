package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class AmazonScraperTest {
    private static Document document;

    @BeforeAll
    static void setUp() {
        try {
            // Obtener los datos de la página web de Amazon una vez para utilizarlos en las pruebas
            String url = "https://www.amazon.com/s?k=juegos";
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testExtractPriceInEuros() {
        // Caso de prueba: precio en dólares
        String priceText = "$29.99";
        String expectedPrice = "25.49 €"; // Conversión aproximada de dólares a euros
        String actualPrice = AmazonScraper.extractPriceInEuros(priceText);
        Assertions.assertEquals(expectedPrice, actualPrice, "El precio en euros es incorrecto");

        // Caso de prueba: precio en euros
        priceText = "19.99 €";
        expectedPrice = "19.99 €";
        actualPrice = AmazonScraper.extractPriceInEuros(priceText);
        Assertions.assertEquals(expectedPrice, actualPrice, "El precio en euros es incorrecto");

        // Caso de prueba: precio sin símbolo de moneda
        priceText = "39.99";
        expectedPrice = "";
        actualPrice = AmazonScraper.extractPriceInEuros(priceText);
        Assertions.assertEquals(expectedPrice, actualPrice, "El precio en euros es incorrecto");
    }

    @Test
    void testScrapeProducts() {
        Elements productElements = document.select("div[data-component-type='s-search-result']");
        List<String> expectedProducts = new ArrayList<>();
        expectedProducts.add("Product 1 - 25.49 €");
        expectedProducts.add("Product 2 - 19.99 €");
        expectedProducts.add("Product 3 -");

        List<String> actualProducts = new ArrayList<>();
        for (Element productElement : productElements) {
            String title = productElement.select("h2 a span").text();
            String priceText = productElement.select("span.a-offscreen").text();
            String price = AmazonScraper.extractPriceInEuros(priceText);

            String product = title + " - " + price;
            actualProducts.add(product);
        }

        Assertions.assertEquals(expectedProducts, actualProducts, "Los productos scrapeados son incorrectos");
    }
}
