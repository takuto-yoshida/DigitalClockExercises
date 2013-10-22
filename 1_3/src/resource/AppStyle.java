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
	
	public static final int DIALOG_WIDTH = 200 ;
	
	public static final int DIALOG_HEIGHT = 180 ;
	
	public static final Integer[] FONT_SIZES = {20,30,40,50,60,70,80,90,100};

	public enum Menu{
		FONT("FONT"),
		FONT_SIZE("FONT SIZE"),
		FONT_COLOR("FONT COLOR"),
		BG_COLOR("BG COLOR"),
		EXIT("EXIT")
		;
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
		
		
		/**
		 * 表示文字列に一致するenumを返す。
		 * 該当するものが無ければ null を返す。
		 * @param disp 
		 * @return
		 */
		public static Menu convertMenu(String disp){
			for( Menu menu : Menu.values() ){
				if(menu.disp().equals(disp)){
					return menu;
				}
			}
			return null;
		}
		
	}
	
}
