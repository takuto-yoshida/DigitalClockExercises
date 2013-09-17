package resource;

import java.awt.Font;

/**
 * アプリケーションで利用できるフォントを表すenum
 * 論理フォントの正規ファミリ名(またはそれに順ずるもの)を表す文字列定数を保持する。
 * 
 * @author takuto yoshida
 */
public enum AppFont {
	SERIF(Font.SERIF),
	DIALOG(Font.DIALOG),
	DIALOG_INPUT(Font.DIALOG_INPUT),
	MONOSPACED(Font.MONOSPACED),
	SANS_SERIF(Font.SANS_SERIF)
	;
	
	private String font;
	
	AppFont(String font){
		this.font = font;
	}
	
	/* (非 Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return font();
	}
	
	/**
	 * 論理フォントの正規ファミリ名(またはそれに順ずるもの)を表す文字列定数を返す
	 * @return
	 */
	public String font() {
		return font;
	}
	
}
