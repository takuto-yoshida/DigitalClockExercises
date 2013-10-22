package resource;

import java.awt.Color;

/**
 * アプリケーションで利用できる色を表すenum
 * 
 * @author takuto yoshida
 */
public enum AppColor {
	RED(Color.RED),
	BLUE(Color.BLUE),
	BLACK(Color.BLACK),
	CYAN(Color.CYAN),
	DARK_GRAY(Color.DARK_GRAY),
	GRAY(Color.GRAY),
	GREEN(Color.GREEN),
	LIGHT_GRAY(Color.LIGHT_GRAY),
	MAGENTA(Color.MAGENTA),
	ORANGE(Color.ORANGE),
	PINK(Color.PINK),
	WHITE(Color.WHITE),
	YELLOW(Color.YELLOW),
	AQUAMARINE(Color.getHSBColor(0.444f, 0.5019f, 1.0f)),
	VIOLET(Color.getHSBColor(0.8333f, 0.45f, 0.9333f)),
	;
	
	private Color color;

	private AppColor(Color color) {
		this.color = color;
	}
	
	/**
	 * 対応する Colorクラスを返す
	 * @return
	 */
	public Color color(){
		return color;
	}
	
	/**
	 * Colorクラスに一致するenumを返す。
	 * 該当するものが無ければ null を返す。
	 * @param color 
	 * @return
	 */
	public static AppColor convertAppColor(Color color){
		for(AppColor appColor : AppColor.values()){
			if(appColor.color().equals(color)){
				return appColor;
			}
		}
		return null;
	}
	
}
