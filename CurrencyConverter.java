package gp1.MoneyConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter {
	public static void main(String[] args) throws IOException {

		HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();
		// Add currency Codes
		currencyCodes.put(1, "USD");
		currencyCodes.put(2, "CAD");
		currencyCodes.put(3, "EUR");
		currencyCodes.put(4, "HKD");
		currencyCodes.put(5, "INR");

		String fromCode, toCode;
		double amount;
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome to the Currency Converter!");

		System.out.println("Currency Converting From?");
		System.out.println(
				"1: USD(US Dollar)\t 2: CAD(Canadian Dollar)\t 3: EUR(Euro)\t 4: HKD (Hong Kong Dollar)\t 5. INR (Indian Rupees)");
		int fromCurrencyChoice = sc.nextInt();
		fromCode = currencyCodes.get(fromCurrencyChoice);

		System.out.println("Currency Converting To?");
		System.out.println(
				"1: USD(US Dollar)\t 2: CAD(Canadian Dollar)\t 3: EUR(Euro)\t 4: HKD (Hong Kong Dollar)\t 5. INR (Indian Rupees)");
		int toCurrencyChoice = sc.nextInt();
		toCode = currencyCodes.get(toCurrencyChoice);

		System.out.println("Amount you wish to convert?");
		amount = sc.nextDouble();

		sendHttpGetRequest(fromCode, toCode, amount);

		System.out.println("Thank you for using the Currency Converter!");

	}

	private static void sendHttpGetRequest(String fromCode, String toCode, double amount) throws IOException {

		String GET_URL = "http://api.exchangerate.host/convert?access_key=ff8b13597d7e9dd892c944f57cd59f66&from="
				+ fromCode + "&to=" + toCode + "&amount=" + amount;
		URL url = new URL(GET_URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		int responseCode = httpURLConnection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) { // Success
			try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				// Parse the response JSON
				JSONObject obj = new JSONObject(response.toString());
				if (obj.has("result")) {
					double convertedAmount = obj.getDouble("result");
					System.out.println(amount + fromCode + " = " + convertedAmount + toCode);
				} else {
					System.out.println("Unable to find converted amount in response.");
				}
			} catch (JSONException e) {
				System.out.println("JSON parsing error: " + e.getMessage());
			}
		} else {
			System.out.println("GET request failed!");
		}
	}

}
