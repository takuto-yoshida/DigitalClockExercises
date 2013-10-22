package config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import resource.AppStyle;

public class AppLog {

	private AppLog() {
	}

	public static final String APP_START_MSG = "プログラムを開始します。";
	public static final String APP_END_MSG = "プログラムを終了します。";
	public static final String APP_CONF_ERR_MSG = "設定ファイルの異常です。";

	public static Logger createAppLogger(String className) {

		Logger logger = null;

		try {
			FileHandler fh = new FileHandler(AppStyle.LOG_FILE_NAME);
			Logger.getGlobal().setLevel(Level.INFO);
			logger = Logger.getLogger(className);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return logger;
	}

}
