package utils;

import java.util.ArrayList;
import java.util.Locale;

public class LocaleUtils {
	
	
	private static Locale defaultLocale = Locale.getDefault();
	private static Locale activeLocale;
	private static ArrayList<Locale> avaliableLocale = new ArrayList<Locale>();
	
	
	public static void setDefaultLocale() {
		if(avaliableLocale.contains(defaultLocale)){
		Locale.setDefault(defaultLocale);
		} else {
			Locale.setDefault(new Locale("en","GB"));
		}
	}
	
	public static void setLocale(Locale locale) {
		Locale.setDefault(locale);
		activeLocale = locale;
	}
	
	public static Locale getActiveLocale() {
		if(activeLocale == null) {
			activeLocale = defaultLocale;
		}
		return activeLocale;
	}
	
	public static void addAvailableLocale(Locale locale) {
		avaliableLocale.add(locale);
	}
	
	public static ArrayList<Locale> getAvailableLocaleList(){
		return avaliableLocale;
	}

}
