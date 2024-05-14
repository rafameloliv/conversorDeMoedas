import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class Main {

    private static final String API_KEY = "d7968f80f9c9fda4016eddb2";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Gson gson = new Gson();

        Map<Integer, String> moedas = new HashMap<>() {{
            put(1, "USD para EUR");
            put(2, "USD para GBP");
            put(3, "USD para JPY");
            put(4, "USD para CAD");
            put(5, "USD para AUD");
            put(6, "USD para BRL");
            put(7, "EUR para USD");
            put(8, "GBP para USD");
            put(9, "JPY para USD");
            put(10, "CAD para USD");
            put(11, "AUD para USD");
            put(12, "BRL para USD");
            put(13, "USD para GTQ");
            put(14, "USD para ARS");
            put(15, "USD para EGP");


        }};

        while (true) {
            System.out.println("\nEscolha uma opção de conversão:");
            moedas.forEach((opcao, descricao) -> System.out.println(opcao + ". " + descricao));

            int opcao = Integer.parseInt(reader.readLine());

            String[] moedasEnvolvidas = moedas.get(opcao).split(" para ");
            String moedaBase = moedasEnvolvidas[0];
            String moedaAlvo = moedasEnvolvidas[1];

            System.out.print("Digite o valor em " + moedaBase + ": ");
            double valor = Double.parseDouble(reader.readLine());

            double valorConvertido = converterMoeda(moedaBase, moedaAlvo, valor, gson);

            System.out.println(valor + " " + moedaBase + " = " + valorConvertido + " " + moedaAlvo);

            System.out.print("\nDeseja fazer outra conversão? (s/n): ");
            if (reader.readLine().equalsIgnoreCase("n")) {
                break;
            }
        }
    }

    private static double converterMoeda(String moedaBase, String moedaAlvo, double valor, Gson gson) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Map<?, ?> map = gson.fromJson(response.toString(), Map.class);
        Map<?, ?> taxasConversao = (Map<?, ?>) map.get("conversion_rates");

        double taxaConversao = (double) taxasConversao.get(moedaAlvo);
        if (!moedaBase.equals("USD")) {
            taxaConversao /= (double) taxasConversao.get(moedaBase);
        }

        return valor * taxaConversao;
    }
}