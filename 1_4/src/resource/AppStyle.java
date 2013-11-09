package resource;

/**
 * アプリケーションのView側のスタイルを保持する定数クラス。
 * 
 * @author takuto yoshida
 *
 */
public class AppStyle {
	
	private AppStyle() {}
	
	public static final String DATE_PATTERN = "hh:mm:ss" ;
	
	public static final String CONF_FILE_NAME = "conf.xml";
	
	public static final String LOG_FILE_NAME = "clock.log";
	
	public static final String LOG_DATE_PATTERN = "yyyy/MM/dd hh:mm:ss" ;
	
	public static final String WINDOW_TITLE = "Digital Clock" ;
	
	public static final int DIALOG_WIDTH = 400 ;
	
	public static final int DIALOG_HEIGHT = 180 ;
	
	public static final int[] FONT_SIZES = {20,30,40,50,60,70,80,90,100,120};

	public enum Menu{
		TITLE("MENU"),
		PROPERTY("PROPERTY");
		private String disp;
		private Menu(String disp) {
			this.disp = disp;
		}
		/**
		 * 表示文字列を返す
		 * @return
		 */
		public String disp() {
			return disp;
		}
	}
	
	public enum DialogMenu{
		TITLE("PROPERTY"),
		FONT("FONT"),
		FONT_SIZE("FONT SIZE"),
		FONT_COLOR("FONT COLOR"),
		BG_COLOR("BG COLOR"),
		OK("OK"),
		Cancel("Cancel")
		;
		private String disp;
		private DialogMenu(String disp) {
			this.disp = disp;
		}
		/**
		 * 表示文字列を返す
		 * @return
		 */
		public String disp() {
			return disp;
		}
	}
	
}
