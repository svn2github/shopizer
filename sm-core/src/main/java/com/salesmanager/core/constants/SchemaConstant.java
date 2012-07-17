package com.salesmanager.core.constants;

import java.util.HashMap;
import java.util.Locale;

public class SchemaConstant {

	public final static String SALESMANAGER_SCHEMA = "SALESMANAGER";

	/**
	 * Languages iso codes
	 * 
	 */
	public static final String[] LANGUAGE_ISO_CODE = {"en", "fr"};

	/**
	 * Country iso codes
	 */
	public static final String[] COUNTRY_ISO_CODE = { "AF","AX","AL","DZ",
		"AS","AD","AO","AI","AQ","AG","AR","AM","AW","AU","AT","AZ","BS","BH",
		"BD","BB","BY","BE","BZ","BJ","BM","BT","BO","BA","BW","BV","BR","IO",
		"BN","BG","BF","BI","KH","CM","CA","CV","KY","CF","TD","CL","CN","CX",
		"CC","CO","KM","CG","CD","CK","CR","CI","HR","CU","CY","CZ","DK","DJ",
		"DM","DO","EC","EG","SV","GQ","ER","EE","ET","FK","FO","FJ","FI","FR",
		"GF","PF","TF","GA","GM","GE","DE","GH","GI","GR","GL","GD","GP","GU",
		"GT","GG","GN","GW","GY","HT","HM","VA","HN","HK","HU","IS","IN","ID",
		"IR","IQ","IE","IM","IL","IT","JM","JP","JE","JO","KZ","KE","KI","KP",
		"KR","KW","KG","LA","LV","LB","LS","LR","LY","LI","LT","LU","MO","MK",
		"MG","MW","MY","MV","ML","MT","MH","MQ","MR","MU","YT","MX","FM","MD",
		"MC","MN","ME","MS","MA","MZ","MM","NA","NR","NP","NL","AN","NC","NZ",
		"NI","NE","NG","NU","NF","MP","NO","OM","PK","PW","PS","PA","PG","PY",
		"PE","PH","PN","PL","PT","PR","QA","RE","RO","RU","RW","SH","KN","LC",
		"PM","VC","WS","SM","ST","SA","SN","RS","SC","SL","SG","SK","SI","SB",
		"SO","ZA","GS","ES","LK","SD","SR","SJ","SZ","SE","CH","SY","TW","TJ",
		"TZ","TH","TL","TG","TK","TO","TT","TN","TR","TM","TC","TV","UG","UA",
		"AE","GB","US","UM","UY","UZ","VU","VE","VN","VG","VI","WF","EH",
	    "YE","ZM","ZW" };

	/**
	 * Locale per country iso codes
	 */
	public static final HashMap<String, Locale> LOCALES = new HashMap<String, Locale>();

	static {
		for (Locale locale : Locale.getAvailableLocales()) {
			LOCALES.put(locale.getISO3Country(), locale);
		}
	}
	
	/**
	 * Currency codes with name
	 */
	public static final HashMap<String, String> CURRENCY_MAP = new HashMap<String, String>();
	
	static {
		CURRENCY_MAP.put("AF", "Afghani");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("AL", "Lek");
        CURRENCY_MAP.put("DZ", "Algerian Dinar");
        CURRENCY_MAP.put("US", "US Dollar");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("AO", "Kwanza");
        CURRENCY_MAP.put("AI", "East Caribbean Dollar");
        CURRENCY_MAP.put("AG", "East Caribbean Dollar");
        CURRENCY_MAP.put("AR", "Argentine Peso");
        CURRENCY_MAP.put("AM", "Armenian Dram");
        CURRENCY_MAP.put("AW", "Aruban Florin");
        CURRENCY_MAP.put("AU", "Australian Dollar");
        CURRENCY_MAP.put("AT", "Euro");
        CURRENCY_MAP.put("AZ", "Azerbaijanian Manat");
        CURRENCY_MAP.put("BS", "Bahamian Dollar");
        CURRENCY_MAP.put("BH", "Bahraini Dinar");
        CURRENCY_MAP.put("BD", "Taka");
        CURRENCY_MAP.put("BB", "Barbados Dollar");
        CURRENCY_MAP.put("BY", "Belarussian Ruble");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("BZ", "Belize Dollar");
        CURRENCY_MAP.put("BJ", "CFA Franc BCEAO");
        CURRENCY_MAP.put("BM", "Bermudian Dollar");
        CURRENCY_MAP.put("BT", "Ngultrum");
        CURRENCY_MAP.put("BT", "Indian Rupee");
        CURRENCY_MAP.put("BO", "Boliviano");
        CURRENCY_MAP.put("BO", "Mvdol");
        CURRENCY_MAP.put("US", "US Dollar");
        CURRENCY_MAP.put("BA", "Convertible Mark");
        CURRENCY_MAP.put("BW", "Pula");
        CURRENCY_MAP.put("BV", "Norwegian Krone");
        CURRENCY_MAP.put("BR", "Brazilian Real");
        CURRENCY_MAP.put("US", "US Dollar");
        CURRENCY_MAP.put("BN", "Brunei Dollar");
        CURRENCY_MAP.put("BG", "Bulgarian Lev");
        CURRENCY_MAP.put("BI", "CFA Franc BCEAO");
        CURRENCY_MAP.put("BI", "Burundi Franc");
        CURRENCY_MAP.put("KH", "Riel");
        CURRENCY_MAP.put("CM", "CFA Franc BEAC");
        CURRENCY_MAP.put("CA", "Canadian Dollar");
        CURRENCY_MAP.put("CV", "Cape Verde Escudo");
        CURRENCY_MAP.put("KY", "Cayman Islands Dollar");
        CURRENCY_MAP.put("CF", "CFA Franc BEAC");
        CURRENCY_MAP.put("TD", "CFA Franc BEAC");
        CURRENCY_MAP.put("CL", "Unidades de fomento");
        CURRENCY_MAP.put("CL", "Chilean Peso");
        CURRENCY_MAP.put("CN", "Yuan Renminbi");
        CURRENCY_MAP.put("CX", "Australian Dollar");
        CURRENCY_MAP.put("CC", "Australian Dollar");
        CURRENCY_MAP.put("CO", "Colombian Peso");
        CURRENCY_MAP.put("CO", "Unidad de Valor Real");
        CURRENCY_MAP.put("KM", "Comoro Franc");
        CURRENCY_MAP.put("CG", "CFA Franc BEAC");
        CURRENCY_MAP.put("CD", "Congolese Franc");
        CURRENCY_MAP.put("CK", "New Zealand Dollar");
        CURRENCY_MAP.put("CR", "Costa Rican Colon");
        CURRENCY_MAP.put("CI", "CFA Franc BCEAO");
        CURRENCY_MAP.put("HR", "Croatian Kuna");
        CURRENCY_MAP.put("CU", "Peso Convertible");
        CURRENCY_MAP.put("CU", "Cuban Peso");
        CURRENCY_MAP.put("AN", "Netherlands Antillean Guilder");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("CZ", "Czech Koruna");
        CURRENCY_MAP.put("DK", "Danish Krone");
        CURRENCY_MAP.put("DJ", "Djibouti Franc");
        CURRENCY_MAP.put("DM", "East Caribbean Dollar");
        CURRENCY_MAP.put("DO", "Dominican Peso");
        CURRENCY_MAP.put("US", "US Dollar");
        CURRENCY_MAP.put("EG", "Egyptian Pound");
        CURRENCY_MAP.put("SV", "El Salvador Colon");
        CURRENCY_MAP.put("US", "US Dollar");
        CURRENCY_MAP.put("GQ", "CFA Franc BEAC");
        CURRENCY_MAP.put("ER", "Nakfa");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("ET", "Ethiopian Birr");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("FK", "Falkland Islands Pound");
        CURRENCY_MAP.put("FO", "Danish Krone");
        CURRENCY_MAP.put("FJ", "Fiji Dollar");
        CURRENCY_MAP.put("FI", "Euro");
        CURRENCY_MAP.put("FR", "Euro");
        CURRENCY_MAP.put("FR", "Euro");
        CURRENCY_MAP.put("PF", "CFP Franc");
        CURRENCY_MAP.put("TF", "Euro");
        CURRENCY_MAP.put("GA", "CFA Franc BEAC");
        CURRENCY_MAP.put("GM", "Dalasi");
        CURRENCY_MAP.put("GE", "Lari");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("GH", "Ghana Cedi");
        CURRENCY_MAP.put("GI", "Gibraltar Pound");
        CURRENCY_MAP.put("GR", "Euro");
        CURRENCY_MAP.put("GL", "Danish Krone");
        CURRENCY_MAP.put("GD", "East Caribbean Dollar");
        CURRENCY_MAP.put("GP", "Euro");
        CURRENCY_MAP.put("GU", "US Dollar");
        CURRENCY_MAP.put("GT", "Quetzal");
        CURRENCY_MAP.put("GB", "Pound Sterling");
        CURRENCY_MAP.put("GN", "Guinea Franc");
        CURRENCY_MAP.put("GW", "CFA Franc BCEAO");
        CURRENCY_MAP.put("GY", "Guyana Dollar");
        CURRENCY_MAP.put("HT", "Gourde");
        CURRENCY_MAP.put("HT", "US Dollar");
        CURRENCY_MAP.put("HM", "Australian Dollar");
        CURRENCY_MAP.put("VA", "Euro");
        CURRENCY_MAP.put("HN", "Lempira");
        CURRENCY_MAP.put("HK", "Hong Kong Dollar");
        CURRENCY_MAP.put("HU", "Forint");
        CURRENCY_MAP.put("IS", "Iceland Krona");
        CURRENCY_MAP.put("IN", "Indian Rupee");
        CURRENCY_MAP.put("ID", "Rupiah");
        CURRENCY_MAP.put("XDR", "SDR (Special Drawing Right)");
        CURRENCY_MAP.put("IR", "Iranian Rial");
        CURRENCY_MAP.put("IQ", "Iraqi Dinar");
        CURRENCY_MAP.put("IE", "Euro");
        CURRENCY_MAP.put("IE", "Pound Sterling");
        CURRENCY_MAP.put("IL", "New Israeli Sheqel");
        CURRENCY_MAP.put("IT", "Euro");
        CURRENCY_MAP.put("JM", "Jamaican Dollar");
        CURRENCY_MAP.put("JP", "Yen");
        CURRENCY_MAP.put("GB", "Pound Sterling");
        CURRENCY_MAP.put("JO", "Jordanian Dinar");
        CURRENCY_MAP.put("KZ", "Tenge");
        CURRENCY_MAP.put("KE", "Kenyan Shilling");
        CURRENCY_MAP.put("AU", "Australian Dollar");
        CURRENCY_MAP.put("KP", "North Korean Won");
        CURRENCY_MAP.put("KR", "Won");
        CURRENCY_MAP.put("KW", "Kuwaiti Dinar");
        CURRENCY_MAP.put("KG", "Som");
        CURRENCY_MAP.put("LA", "Kip");
        CURRENCY_MAP.put("LV", "Latvian Lats");
        CURRENCY_MAP.put("LB", "Lebanese Pound");
        CURRENCY_MAP.put("LS", "Loti");
        CURRENCY_MAP.put("LS", "Rand");
        CURRENCY_MAP.put("LR", "Liberian Dollar");
        CURRENCY_MAP.put("LY", "Libyan Dinar");
        CURRENCY_MAP.put("LI", "Swiss Franc");
        CURRENCY_MAP.put("LT", "Lithuanian Litas");
        CURRENCY_MAP.put("LU", "Euro");
        CURRENCY_MAP.put("MO", "Pataca");
        CURRENCY_MAP.put("MK", "Denar");
        CURRENCY_MAP.put("MG", "Malagasy Ariary");
        CURRENCY_MAP.put("MW", "Kwacha");
        CURRENCY_MAP.put("MY", "Malaysian Ringgit");
        CURRENCY_MAP.put("MV", "Rufiyaa");
        CURRENCY_MAP.put("ML", "CFA Franc BCEAO");
        CURRENCY_MAP.put("MT", "Euro");
        CURRENCY_MAP.put("MH", "US Dollar");
        CURRENCY_MAP.put("MQ", "Euro");
        CURRENCY_MAP.put("MR", "Ouguiya");
        CURRENCY_MAP.put("MU", "Mauritius Rupee");
        CURRENCY_MAP.put("YT", "Euro");
        CURRENCY_MAP.put("XUA", "ADB Unit of Account");
        CURRENCY_MAP.put("MX", "Mexican Peso");
        CURRENCY_MAP.put("MX", "Mexican Unidad de Inversion (UDI)");
        CURRENCY_MAP.put("FM", "US Dollar");
        CURRENCY_MAP.put("MD", "Moldovan Leu");
        CURRENCY_MAP.put("MC", "Euro");
        CURRENCY_MAP.put("MN", "Tugrik");
        CURRENCY_MAP.put("ME", "Euro");
        CURRENCY_MAP.put("MS", "East Caribbean Dollar");
        CURRENCY_MAP.put("MA", "Moroccan Dirham");
        CURRENCY_MAP.put("MZ", "Mozambique Metical");
        CURRENCY_MAP.put("MM", "Kyat");
        CURRENCY_MAP.put("NA", "Namibia Dollar");
        CURRENCY_MAP.put("NA", "Rand");
        CURRENCY_MAP.put("NR", "Australian Dollar");
        CURRENCY_MAP.put("NP", "Nepalese Rupee");
        CURRENCY_MAP.put("NL", "Euro");
        CURRENCY_MAP.put("NC", "CFP Franc");
        CURRENCY_MAP.put("NZ", "New Zealand Dollar");
        CURRENCY_MAP.put("NI", "Cordoba Oro");
        CURRENCY_MAP.put("NE", "CFA Franc BCEAO");
        CURRENCY_MAP.put("NG", "Naira");
        CURRENCY_MAP.put("NU", "New Zealand Dollar");
        CURRENCY_MAP.put("NF", "Australian Dollar");
        CURRENCY_MAP.put("MP", "US Dollar");
        CURRENCY_MAP.put("NO", "Norwegian Krone");
        CURRENCY_MAP.put("OM", "Rial Omani");
        CURRENCY_MAP.put("PK", "Pakistan Rupee");
        CURRENCY_MAP.put("PW", "US Dollar");
        CURRENCY_MAP.put("PA", "Balboa");
        CURRENCY_MAP.put("PA", "US Dollar");
        CURRENCY_MAP.put("PG", "Kina");
        CURRENCY_MAP.put("PY", "Guarani");
        CURRENCY_MAP.put("PE", "Nuevo Sol");
        CURRENCY_MAP.put("PH", "Philippine Peso");
        CURRENCY_MAP.put("PN", "New Zealand Dollar");
        CURRENCY_MAP.put("PL", "Zloty");
        CURRENCY_MAP.put("PT", "Euro");
        CURRENCY_MAP.put("PR", "US Dollar");
        CURRENCY_MAP.put("QA", "Qatari Rial");
        CURRENCY_MAP.put("RO", "Euro");
        CURRENCY_MAP.put("RO", "New Romanian Leu");
        CURRENCY_MAP.put("RU", "Russian Ruble");
        CURRENCY_MAP.put("RW", "Rwanda Franc");
        CURRENCY_MAP.put("RE", "Euro");
        CURRENCY_MAP.put("SH", "Saint Helena Pound");
        CURRENCY_MAP.put("KN", "East Caribbean Dollar");
        CURRENCY_MAP.put("LC", "East Caribbean Dollar");
        CURRENCY_MAP.put("PM", "Euro");
        CURRENCY_MAP.put("EUR", "Euro");
        CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
        CURRENCY_MAP.put("WS", "Tala");
        CURRENCY_MAP.put("EUR", "Euro");
        CURRENCY_MAP.put("ST", "Dobra");
        CURRENCY_MAP.put("SA", "Saudi Riyal");
        CURRENCY_MAP.put("SN", "CFA Franc BCEAO");
        CURRENCY_MAP.put("RS", "Serbian Dinar");
        CURRENCY_MAP.put("SC", "Seychelles Rupee");
        CURRENCY_MAP.put("SL", "Leone");
        CURRENCY_MAP.put("SG", "Singapore Dollar");
        CURRENCY_MAP.put("ANG", "Netherlands Antillean Guilder");
        CURRENCY_MAP.put("XSU", "Sucre");
        CURRENCY_MAP.put("EUR", "Euro");
        CURRENCY_MAP.put("EUR", "Euro");
        CURRENCY_MAP.put("SB", "Solomon Islands Dollar");
        CURRENCY_MAP.put("SO", "Somali Shilling");
        CURRENCY_MAP.put("ZA", "Rand");
        CURRENCY_MAP.put("SSP", "South Sudanese Pound");
        CURRENCY_MAP.put("ES", "Euro");
        CURRENCY_MAP.put("LK", "Sri Lanka Rupee");
        CURRENCY_MAP.put("SD", "Sudanese Pound");
        CURRENCY_MAP.put("SR", "Surinam Dollar");
        CURRENCY_MAP.put("SJ", "Norwegian Krone");
        CURRENCY_MAP.put("SZ", "Lilangeni");
        CURRENCY_MAP.put("SE", "Swedish Krona");
        CURRENCY_MAP.put("CH", "WIR Euro");
        CURRENCY_MAP.put("CH", "Swiss Franc");
        CURRENCY_MAP.put("CH", "WIR Franc");
        CURRENCY_MAP.put("SY", "Syrian Pound");
        CURRENCY_MAP.put("TW", "New Taiwan Dollar");
        CURRENCY_MAP.put("TJ", "Somoni");
        CURRENCY_MAP.put("TZ", "Tanzanian Shilling");
        CURRENCY_MAP.put("TH", "Baht");
        CURRENCY_MAP.put("TL", "US Dollar");
        CURRENCY_MAP.put("TG", "CFA Franc BCEAO");
        CURRENCY_MAP.put("TK", "New Zealand Dollar");
        CURRENCY_MAP.put("TO", "Pa‰Ûªanga");
        CURRENCY_MAP.put("TT", "Trinidad and Tobago Dollar");
        CURRENCY_MAP.put("TN", "Tunisian Dinar");
        CURRENCY_MAP.put("TR", "Turkish Lira");
        CURRENCY_MAP.put("TMT", "Turkmenistan New Manat");
        CURRENCY_MAP.put("TC", "US Dollar");
        CURRENCY_MAP.put("TV", "Australian Dollar");
        CURRENCY_MAP.put("UG", "Uganda Shilling");
        CURRENCY_MAP.put("UA", "Hryvnia");
        CURRENCY_MAP.put("AE", "UAE Dirham");
        CURRENCY_MAP.put("GB", "Pound Sterling");
        CURRENCY_MAP.put("US", "US Dollar");
        CURRENCY_MAP.put("US", "US Dollar (Next day)");
        CURRENCY_MAP.put("US", "US Dollar (Same day)");
        CURRENCY_MAP.put("UM", "US Dollar");
        CURRENCY_MAP.put("UY", "Uruguay Peso en Unidades Indexadas (URUIURUI)");
        CURRENCY_MAP.put("UY", "Peso Uruguayo");
        CURRENCY_MAP.put("UZ", "Uzbekistan Sum");
        CURRENCY_MAP.put("VU", "Vatu");
        CURRENCY_MAP.put("EU", "Euro");
        CURRENCY_MAP.put("VE", "Bolivar Fuerte");
        CURRENCY_MAP.put("VN", "Dong");
        CURRENCY_MAP.put("VG", "US Dollar");
        CURRENCY_MAP.put("VI", "US Dollar");
        CURRENCY_MAP.put("WF", "CFP Franc");
        CURRENCY_MAP.put("EH", "Moroccan Dirham");
        CURRENCY_MAP.put("YE", "Yemeni Rial");
        CURRENCY_MAP.put("ZM", "Zambian Kwacha");
        CURRENCY_MAP.put("ZW", "Zimbabwe Dollar");
/*        CURRENCY_MAP.put("XBA", "Bond Markets Unit European Composite Unit (EURCO)");
        CURRENCY_MAP.put("XBB", "Bond Markets Unit European Monetary Unit (E.M.U.-6)");
        CURRENCY_MAP.put("XBC", "Bond Markets Unit European Unit of Account 9 (E.U.A.-9)");
        CURRENCY_MAP.put("XBD", "Bond Markets Unit European Unit of Account 17 (E.U.A.-17)");
        CURRENCY_MAP.put("XFU", "UIC-Franc");
        CURRENCY_MAP.put("XTS", "Codes specifically reserved for testing purposes");
        CURRENCY_MAP.put("XXX", "The codes assigned for transactions where no currency is involved");
        CURRENCY_MAP.put("XAU", "Gold");
        CURRENCY_MAP.put("XPD", "Palladium");
        CURRENCY_MAP.put("XPT", "Platinum");
        CURRENCY_MAP.put("XAG", "Silver");*/
	}
}
