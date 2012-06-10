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
	public static final String[] COUNTRY_ISO_CODE = { "ABW", "AFG", "AGO",
			"AIA", "ALA", "ALB", "AND", "ARE", "ARG", "ARM", "ASM", "ATA",
			"ATF", "ATG", "AUS", "AUT", "AZE", "BDI", "BEL", "BEN", "BES",
			"BFA", "BGD", "BGR", "BHR", "BHS", "BIH", "BLM", "BLR", "BLZ",
			"BMU", "BOL", "BRA", "BRB", "BRN", "BTN", "BVT", "BWA", "CAF",
			"CAN", "CCK", "CHE", "CHL", "CHN", "CIV", "CMR", "COD", "COG",
			"COK", "COL", "COM", "CPV", "CRI", "CUB", "CUW", "CXR", "CYM",
			"CYP", "CZE", "DEU", "DJI", "DMA", "DNK", "DOM", "DZA", "ECU",
			"EGY", "ERI", "ESH", "ESP", "EST", "ETH", "FIN", "FJI", "FLK",
			"FRA", "FRO", "FSM", "GAB", "GBR", "GEO", "GGY", "GHA", "GIB",
			"GIN", "GLP", "GMB", "GNB", "GNQ", "GRC", "GRD", "GRL", "GTM",
			"GUF", "GUM", "GUY", "HKG", "HMD", "HND", "HRV", "HTI", "HUN",
			"IDN", "IMN", "IND", "IOT", "IRL", "IRN", "IRQ", "ISL", "ISR",
			"ITA", "JAM", "JEY", "JOR", "JPN", "KAZ", "KEN", "KGZ", "KHM",
			"KIR", "KNA", "KOR", "KWT", "LAO", "LBN", "LBR", "LBY", "LCA",
			"LIE", "LKA", "LSO", "LTU", "LUX", "LVA", "MAC", "MAF", "MAR",
			"MCO", "MDA", "MDG", "MDV", "MEX", "MHL", "MKD", "MLI", "MLT",
			"MMR", "MNE", "MNG", "MNP", "MOZ", "MRT", "MSR", "MTQ", "MUS",
			"MWI", "MYS", "MYT", "NAM", "NCL", "NER", "NFK", "NGA", "NIC",
			"NIU", "NLD", "NOR", "NPL", "NRU", "NZL", "OMN", "PAK", "PAN",
			"PCN", "PER", "PHL", "PLW", "PNG", "POL", "PRI", "PRK", "PRT",
			"PRY", "PSE", "PYF", "QAT", "REU", "ROU", "RUS", "RWA", "SAU",
			"SDN", "SEN", "SGP", "SGS", "SHN", "SJM", "SLB", "SLE", "SLV",
			"SMR", "SOM", "SPM", "SRB", "SSD", "STP", "SUR", "SVK", "SVN",
			"SWE", "SWZ", "SXM", "SYC", "SYR", "TCA", "TCD", "TGO", "THA",
			"TJK", "TKL", "TKM", "TLS", "TON", "TTO", "TUN", "TUR", "TUV",
			"TWN", "TZA", "UGA", "UKR", "UMI", "URY", "USA", "UZB", "VAT",
			"VCT", "VEN", "VGB", "VIR", "VNM", "VUT", "WLF", "WSM", "YEM",
			"ZAF", "ZMB", "ZWE" };

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
		CURRENCY_MAP.put("AFN", "Afghani");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("ALL", "Lek");
		CURRENCY_MAP.put("DZD", "Algerian Dinar");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("AOA", "Kwanza");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("ARS", "Argentine Peso");
		CURRENCY_MAP.put("AMD", "Armenian Dram");
		CURRENCY_MAP.put("AWG", "Aruban Florin");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("AZN", "Azerbaijanian Manat");
		CURRENCY_MAP.put("BSD", "Bahamian Dollar");
		CURRENCY_MAP.put("BHD", "Bahraini Dinar");
		CURRENCY_MAP.put("BDT", "Taka");
		CURRENCY_MAP.put("BBD", "Barbados Dollar");
		CURRENCY_MAP.put("BYR", "Belarussian Ruble");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("BZD", "Belize Dollar");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("BMD", "Bermudian Dollar");
		CURRENCY_MAP.put("BTN", "Ngultrum");
		CURRENCY_MAP.put("INR", "Indian Rupee");
		CURRENCY_MAP.put("BOB", "Boliviano");
		CURRENCY_MAP.put("BOV", "Mvdol");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("BAM", "Convertible Mark");
		CURRENCY_MAP.put("BWP", "Pula");
		CURRENCY_MAP.put("NOK", "Norwegian Krone");
		CURRENCY_MAP.put("BRL", "Brazilian Real");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("BND", "Brunei Dollar");
		CURRENCY_MAP.put("BGN", "Bulgarian Lev");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("BIF", "Burundi Franc");
		CURRENCY_MAP.put("KHR", "Riel");
		CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
		CURRENCY_MAP.put("CAD", "Canadian Dollar");
		CURRENCY_MAP.put("CVE", "Cape Verde Escudo");
		CURRENCY_MAP.put("KYD", "Cayman Islands Dollar");
		CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
		CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
		CURRENCY_MAP.put("CLF", "Unidades de fomento");
		CURRENCY_MAP.put("CLP", "Chilean Peso");
		CURRENCY_MAP.put("CNY", "Yuan Renminbi");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("COP", "Colombian Peso");
		CURRENCY_MAP.put("COU", "Unidad de Valor Real");
		CURRENCY_MAP.put("KMF", "Comoro Franc");
		CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
		CURRENCY_MAP.put("CDF", "Congolese Franc");
		CURRENCY_MAP.put("NZD", "New Zealand Dollar");
		CURRENCY_MAP.put("CRC", "Costa Rican Colon");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("HRK", "Croatian Kuna");
		CURRENCY_MAP.put("CUC", "Peso Convertible");
		CURRENCY_MAP.put("CUP", "Cuban Peso");
		CURRENCY_MAP.put("ANG", "Netherlands Antillean Guilder");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("CZK", "Czech Koruna");
		CURRENCY_MAP.put("DKK", "Danish Krone");
		CURRENCY_MAP.put("DJF", "Djibouti Franc");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("DOP", "Dominican Peso");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("EGP", "Egyptian Pound");
		CURRENCY_MAP.put("SVC", "El Salvador Colon");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
		CURRENCY_MAP.put("ERN", "Nakfa");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("ETB", "Ethiopian Birr");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("FKP", "Falkland Islands Pound");
		CURRENCY_MAP.put("DKK", "Danish Krone");
		CURRENCY_MAP.put("FJD", "Fiji Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("XPF", "CFP Franc");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("XAF", "CFA Franc BEAC");
		CURRENCY_MAP.put("GMD", "Dalasi");
		CURRENCY_MAP.put("GEL", "Lari");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("GHS", "Ghana Cedi");
		CURRENCY_MAP.put("GIP", "Gibraltar Pound");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("DKK", "Danish Krone");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("GTQ", "Quetzal");
		CURRENCY_MAP.put("GBP", "Pound Sterling");
		CURRENCY_MAP.put("GNF", "Guinea Franc");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("GYD", "Guyana Dollar");
		CURRENCY_MAP.put("HTG", "Gourde");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("HNL", "Lempira");
		CURRENCY_MAP.put("HKD", "Hong Kong Dollar");
		CURRENCY_MAP.put("HUF", "Forint");
		CURRENCY_MAP.put("ISK", "Iceland Krona");
		CURRENCY_MAP.put("INR", "Indian Rupee");
		CURRENCY_MAP.put("IDR", "Rupiah");
		CURRENCY_MAP.put("XDR", "SDR (Special Drawing Right)");
		CURRENCY_MAP.put("IRR", "Iranian Rial");
		CURRENCY_MAP.put("IQD", "Iraqi Dinar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("GBP", "Pound Sterling");
		CURRENCY_MAP.put("ILS", "New Israeli Sheqel");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("JMD", "Jamaican Dollar");
		CURRENCY_MAP.put("JPY", "Yen");
		CURRENCY_MAP.put("GBP", "Pound Sterling");
		CURRENCY_MAP.put("JOD", "Jordanian Dinar");
		CURRENCY_MAP.put("KZT", "Tenge");
		CURRENCY_MAP.put("KES", "Kenyan Shilling");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("KPW", "North Korean Won");
		CURRENCY_MAP.put("KRW", "Won");
		CURRENCY_MAP.put("KWD", "Kuwaiti Dinar");
		CURRENCY_MAP.put("KGS", "Som");
		CURRENCY_MAP.put("LAK", "Kip");
		CURRENCY_MAP.put("LVL", "Latvian Lats");
		CURRENCY_MAP.put("LBP", "Lebanese Pound");
		CURRENCY_MAP.put("LSL", "Loti");
		CURRENCY_MAP.put("ZAR", "Rand");
		CURRENCY_MAP.put("LRD", "Liberian Dollar");
		CURRENCY_MAP.put("LYD", "Libyan Dinar");
		CURRENCY_MAP.put("CHF", "Swiss Franc");
		CURRENCY_MAP.put("LTL", "Lithuanian Litas");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("MOP", "Pataca");
		CURRENCY_MAP.put("MKD", "Denar");
		CURRENCY_MAP.put("MGA", "Malagasy Ariary");
		CURRENCY_MAP.put("MWK", "Kwacha");
		CURRENCY_MAP.put("MYR", "Malaysian Ringgit");
		CURRENCY_MAP.put("MVR", "Rufiyaa");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("MRO", "Ouguiya");
		CURRENCY_MAP.put("MUR", "Mauritius Rupee");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("XUA", "ADB Unit of Account");
		CURRENCY_MAP.put("MXN", "Mexican Peso");
		CURRENCY_MAP.put("MXV", "Mexican Unidad de Inversion (UDI)");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("MDL", "Moldovan Leu");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("MNT", "Tugrik");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("MAD", "Moroccan Dirham");
		CURRENCY_MAP.put("MZN", "Mozambique Metical");
		CURRENCY_MAP.put("MMK", "Kyat");
		CURRENCY_MAP.put("NAD", "Namibia Dollar");
		CURRENCY_MAP.put("ZAR", "Rand");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("NPR", "Nepalese Rupee");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("XPF", "CFP Franc");
		CURRENCY_MAP.put("NZD", "New Zealand Dollar");
		CURRENCY_MAP.put("NIO", "Cordoba Oro");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("NGN", "Naira");
		CURRENCY_MAP.put("NZD", "New Zealand Dollar");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("NOK", "Norwegian Krone");
		CURRENCY_MAP.put("OMR", "Rial Omani");
		CURRENCY_MAP.put("PKR", "Pakistan Rupee");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("PAB", "Balboa");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("PGK", "Kina");
		CURRENCY_MAP.put("PYG", "Guarani");
		CURRENCY_MAP.put("PEN", "Nuevo Sol");
		CURRENCY_MAP.put("PHP", "Philippine Peso");
		CURRENCY_MAP.put("NZD", "New Zealand Dollar");
		CURRENCY_MAP.put("PLN", "Zloty");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("QAR", "Qatari Rial");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("RON", "New Romanian Leu");
		CURRENCY_MAP.put("RUB", "Russian Ruble");
		CURRENCY_MAP.put("RWF", "Rwanda Franc");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("SHP", "Saint Helena Pound");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("XCD", "East Caribbean Dollar");
		CURRENCY_MAP.put("WST", "Tala");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("STD", "Dobra");
		CURRENCY_MAP.put("SAR", "Saudi Riyal");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("RSD", "Serbian Dinar");
		CURRENCY_MAP.put("SCR", "Seychelles Rupee");
		CURRENCY_MAP.put("SLL", "Leone");
		CURRENCY_MAP.put("SGD", "Singapore Dollar");
		CURRENCY_MAP.put("ANG", "Netherlands Antillean Guilder");
		CURRENCY_MAP.put("XSU", "Sucre");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("SBD", "Solomon Islands Dollar");
		CURRENCY_MAP.put("SOS", "Somali Shilling");
		CURRENCY_MAP.put("ZAR", "Rand");
		CURRENCY_MAP.put("SSP", "South Sudanese Pound");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("LKR", "Sri Lanka Rupee");
		CURRENCY_MAP.put("SDG", "Sudanese Pound");
		CURRENCY_MAP.put("SRD", "Surinam Dollar");
		CURRENCY_MAP.put("NOK", "Norwegian Krone");
		CURRENCY_MAP.put("SZL", "Lilangeni");
		CURRENCY_MAP.put("SEK", "Swedish Krona");
		CURRENCY_MAP.put("CHE", "WIR Euro");
		CURRENCY_MAP.put("CHF", "Swiss Franc");
		CURRENCY_MAP.put("CHW", "WIR Franc");
		CURRENCY_MAP.put("SYP", "Syrian Pound");
		CURRENCY_MAP.put("TWD", "New Taiwan Dollar");
		CURRENCY_MAP.put("TJS", "Somoni");
		CURRENCY_MAP.put("TZS", "Tanzanian Shilling");
		CURRENCY_MAP.put("THB", "Baht");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("XOF", "CFA Franc BCEAO");
		CURRENCY_MAP.put("NZD", "New Zealand Dollar");
		CURRENCY_MAP.put("TOP", "Pa’anga");
		CURRENCY_MAP.put("TTD", "Trinidad and Tobago Dollar");
		CURRENCY_MAP.put("TND", "Tunisian Dinar");
		CURRENCY_MAP.put("TRY", "Turkish Lira");
		CURRENCY_MAP.put("TMT", "Turkmenistan New Manat");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("AUD", "Australian Dollar");
		CURRENCY_MAP.put("UGX", "Uganda Shilling");
		CURRENCY_MAP.put("UAH", "Hryvnia");
		CURRENCY_MAP.put("AED", "UAE Dirham");
		CURRENCY_MAP.put("GBP", "Pound Sterling");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("USN", "US Dollar (Next day)");
		CURRENCY_MAP.put("USS", "US Dollar (Same day)");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("UYI", "Uruguay Peso en Unidades Indexadas (URUIURUI)");
		CURRENCY_MAP.put("UYU", "Peso Uruguayo");
		CURRENCY_MAP.put("UZS", "Uzbekistan Sum");
		CURRENCY_MAP.put("VUV", "Vatu");
		CURRENCY_MAP.put("EUR", "Euro");
		CURRENCY_MAP.put("VEF", "Bolivar Fuerte");
		CURRENCY_MAP.put("VND", "Dong");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("USD", "US Dollar");
		CURRENCY_MAP.put("XPF", "CFP Franc");
		CURRENCY_MAP.put("MAD", "Moroccan Dirham");
		CURRENCY_MAP.put("YER", "Yemeni Rial");
		CURRENCY_MAP.put("ZMK", "Zambian Kwacha");
		CURRENCY_MAP.put("ZWL", "Zimbabwe Dollar");
		CURRENCY_MAP.put("XBA", "Bond Markets Unit European Composite Unit (EURCO)");
		CURRENCY_MAP.put("XBB", "Bond Markets Unit European Monetary Unit (E.M.U.-6)");
		CURRENCY_MAP.put("XBC", "Bond Markets Unit European Unit of Account 9 (E.U.A.-9)");
		CURRENCY_MAP.put("XBD", "Bond Markets Unit European Unit of Account 17 (E.U.A.-17)");
		CURRENCY_MAP.put("XFU", "UIC-Franc");
		CURRENCY_MAP.put("XTS", "Codes specifically reserved for testing purposes");
		CURRENCY_MAP.put("XXX", "The codes assigned for transactions where no currency is involved");
		CURRENCY_MAP.put("XAU", "Gold");
		CURRENCY_MAP.put("XPD", "Palladium");
		CURRENCY_MAP.put("XPT", "Platinum");
		CURRENCY_MAP.put("XAG", "Silver");
	}
	/**
	 * Currency codes with an associated locale
	 */
	public static final String[] ISO_4217_WITH_LOCALE = { "JPY", "CNY", "SDG",
			"RON", "MKD", "MXN", "CAD", "ZAR", "AUD", "NOK", "ILS", "ISK",
			"SYP", "LYD", "UYU", "YER", "CSD", "EEK", "THB", "IDR", "LBP",
			"AED", "BOB", "QAR", "BHD", "HNL", "HRK", "COP", "ALL", "DKK",
			"MYR", "SEK", "RSD", "BGN", "DOP", "KRW", "LVL", "VEF", "CZK",
			"TND", "KWD", "VND", "JOD", "NZD", "PAB", "CLP", "PEN", "GBP",
			"DZD", "CHF", "RUB", "UAH", "ARS", "SAR", "EGP", "INR", "PYG",
			"TWD", "TRY", "BAM", "OMR", "SGD", "MAD", "BYR", "NIO", "HKD",
			"LTL", "SKK", "GTQ", "BRL", "EUR", "HUF", "IQD", "CRC", "PHP",
			"SVC", "PLN", "USD" };

	/**
	 * Currency codes without an associated locale
	 */
	public static final String[] ISO_4217_WITHOUT_LOCALE = { "XBB", "XBC",
			"XBD", "UGX", "MOP", "SHP", "TTD", "KGS", "DJF", "BTN", "XBA",
			"HTG", "BBD", "XAU", "FKP", "MWK", "PGK", "XCD", "RWF", "NGN",
			"BSD", "XTS", "TMT", "GEL", "VUV", "FJD", "MVR", "AZN", "MNT",
			"MGA", "WST", "KMF", "GNF", "SBD", "BDT", "MMK", "TJS", "CVE",
			"MDL", "KES", "SRD", "LRD", "MUR", "CDF", "BMD", "USN", "CUP",
			"USS", "GMD", "UZS", "CUC", "ZMK", "NPR", "NAD", "LAK", "SZL",
			"XDR", "BND", "TZS", "MXV", "LSL", "KYD", "LKR", "ANG", "PKR",
			"SLL", "SCR", "GHS", "ERN", "BOV", "GIP", "IRR", "XPT", "BWP",
			"XFU", "CLF", "ETB", "STD", "XXX", "XPD", "AMD", "XPF", "JMD",
			"MRO", "BIF", "ZWL", "AWG", "MZN", "XOF", "KZT", "BZD", "XAG",
			"KHR", "XAF", "GYD", "AFN", "SOS", "TOP", "AOA", "KPW" };
}
