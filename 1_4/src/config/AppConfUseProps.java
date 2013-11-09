package config;

import java.util.prefs.Preferences;

/**
 * @author takuto yoshida
 * 基本はファイル保管にしていたが、
 * Propsを使った管理に変更。処理は同じ。
 * 
 */
public class AppConfUseProps extends AppConf {
	
	private Preferences child;
	private static AppConfUseProps appConfUseProps;
	
	AppConfUseProps(String dirName) {
		super(dirName);
		Preferences userRoot = Preferences.userRoot();		
		child  = userRoot.node(dirName);
	}
	
	/**
	 * インスタンスを生成する
	 * 
	 * @param filename
	 * @return
	 */
	public static synchronized AppConf getInstance(String dirName) {
		if (appConfUseProps == null) {
			return new AppConfUseProps(dirName);
		} else {
			return appConfUseProps;
		}
	}
	

	/* (非 Javadoc)
	 * @see config.AppConf#loadProperty(config.AppConf.Key)
	 */
	@Override
	public String loadProperty(Key key) {
		return child.get(key.name(), key.defaultVal());
	}

	/* (非 Javadoc)
	 * @see config.AppConf#saveProperty(config.AppConf.Key, java.lang.String)
	 */
	@Override
	public void saveProperty(Key key, String value) {
		child.put(key.name(),value);
	}	
}
