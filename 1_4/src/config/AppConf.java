package config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import resource.AppColor;
import resource.AppFont;
import resource.AppStyle;

/**
 * アプリケーションの設定（外部設定）を保持するクラス
 * 
 * @author takuto yoshida
 */
public class AppConf {

	private SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
			AppStyle.LOG_DATE_PATTERN);

	private String filename;

	private Properties properties;

	private static AppConf configration;

	/**
	 * 設定のキーのセットを表す enum デフォルトの値を保持する。
	 */
	public enum Key {
		FONT_SIZE(new Integer(AppStyle.FONT_SIZES[5]).toString()), FONT_COLOR(
				AppColor.WHITE.name()), BG_COLOR(AppColor.BLACK.name()), FONT(
				AppFont.SERIF.font()), INIT_LOCATION_X("40"), INIT_LOCATION_Y(
				"40");

		private String defaultVal;

		private Key(String defaultVal) {
			this.defaultVal = defaultVal;
		}

		/**
		 * デフォルトの値を返す
		 * 
		 * @return
		 */
		public String defaultVal() {
			return defaultVal;
		}

	}

	/**
	 * コンストラクタ
	 * 
	 * @param filename
	 */
	AppConf(String filename) {
		this.filename = filename;
		try {
			properties = new Properties();
			properties.loadFromXML(new FileInputStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * インスタンスを生成する
	 * 
	 * @param filename
	 * @return
	 */
	public static synchronized AppConf getInstance(String filename) {
		if (configration == null) {
			return new AppConf(filename);
		} else {
			return configration;
		}
	}

	/**
	 * キーに対応する設定をファイルより取得する
	 * 
	 * @param key
	 * @return
	 */
	public String loadProperty(Key key) {
		if (properties.containsKey(key.name()))
			return properties.getProperty(key.name());
		else {
			return key.defaultVal();
		}
	}

	/**
	 * キーに対応する設定をファイルに書き込む。
	 * 
	 * @param key
	 * @param value
	 */
	public void saveProperty(Key key, String value) {
		properties.setProperty(key.name(), value);
		flush();
	}

	/**
	 * 書き込みをコミットする
	 */
	private void flush() {
		try {
			String comment = "LATEST SAVE " + sdfDateFormat.format(new Date());
			properties.storeToXML(new FileOutputStream(filename), comment);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
