package util;

import java.util.HashMap;
/** Workaround class to OWM JAPIs language change not working
 * */
public class WeatherDescriptionTranslator {
	private static HashMap<String, String> translations = new HashMap<>();
	static {
		translations.put("thunderstorm with light rain", "burza z lekkimi opadami deszczu");
		translations.put("thunderstorm with rain" , "burza z opadami deszczu");
		translations.put("thunderstorm with heavy rain", "burza z dużymi opadami deszczu");
		translations.put("light thunderstorm" , "lekka burza");
		translations.put("thunderstorm", "burza");
		translations.put("heavy thunderstorm", "gwałtowna burza");
		translations.put("ragged thunderstorm", "potężna burza");
		translations.put("thunderstorm with light drizzle", "burza z lekką mżawką");
		translations.put("thunderstorm with drizzle", "burza z mżawką");
		translations.put("thunderstorm with heavy drizzle", "burza z intensywną mżawką");
		translations.put("light intensity drizzle", "mżawka o małej intensywności");
		translations.put("drizzle", "mżawka");
		translations.put("heavy intensity drizzle", "intensywna mżawka");
		translations.put("light intensity drizzle rain", "lekka mżawka");
		translations.put("drizzle rain", "mżawka");
		translations.put("heavy intensity drizzle rain", "intensywna mżawka");
		translations.put("shower rain and drizzle", "przelotny deszcz i mżawka");
		translations.put("heavy shower rain and drizzle", "intensywna przelotna mżawka");
		translations.put("shower drizzle", "przelotna mżawka");
		translations.put("light rain", "lekki deszcz");
		translations.put("moderate rain", "umiarkowany deszcz");
		translations.put("heavy intensity rain", "intensywny deszcz");
		translations.put("very heavy rain", "bardzo intensywny deszcz");
		translations.put("extreme rain", "ekstremalny deszcz");
		translations.put("freezing rain", "mroźny deszcz");
		translations.put("light intensity shower rain", "deszcz");
		translations.put("shower rain", "przelotny deszcz");
		translations.put("heavy intensity shower rain", "intensywny deszcz");
		translations.put("ragged shower rain", "ulewa");
		translations.put("light snow", "lekki śnieg");
		translations.put("snow", "śnieg");
		translations.put("heavy snow", "duże opady śniegu");
		translations.put("sleet", "deszcz ze śniegiem");
		translations.put("shower sleet", "deszcz ze śniegiem");
		translations.put("light rain and snow", "lekki deszcz ze śniegiem");
		translations.put("rain and snow", "deszcz ze śniegiem");
		translations.put("light shower snow", "lekkie opady śniegu");
		translations.put("shower snow", "opady śniegu");
		translations.put("heavy shower snow", "ciężkie opady śniegu");
		translations.put("mist", "mgła");
		translations.put("smoke", "dym");
		translations.put("haze", "mgiełka");
		translations.put("sand, dust whirls", "piach, wiry kurzu");
		translations.put("fog", "mgła");
		translations.put("sand", "piasek");
		translations.put("dust", "kurz");
		translations.put("volcanic ash", "pył wulkaniczny");
		translations.put("squalls", "szkwał");
		translations.put("tornado", "tornado");
		translations.put("clear sky", "czyste niebo");
		translations.put("few clouds", "małe zachmurzenie");
		translations.put("scattered clouds", "rozproszone chmury");
		translations.put("broken clouds", "załamanie chmur");
		translations.put("overcast clouds", "duże zachmurzenie");
	}
	public static String translate(String text) {
		return translations.get(text);
	}
}
