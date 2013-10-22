package window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import resource.AppColor;
import resource.AppFont;
import resource.AppStyle;
import config.AppConf;
import config.AppConf.Key;
import config.AppLog;

/**
 * デジタル表示する時計を表示するメインクラス
 * 
 * @author takuto yoshida
 * 
 */
public class DigitalClock extends Window implements ActionListener, Runnable , MouseListener{

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(AppStyle.DATE_PATTERN);

	private static String timeValue;

	private int windowWidth = 1; // 初期値は0以外で任意。fontSizeによって動的に変化する
	private int windowHeight = 1; // 初期値は0以外で任意。fontSizeによって動的に変化する

	private int fontSize;
	private Color fontColor;
	private Color bgColor;
	private String fontName;
	private Font font;

	private AppConf conf;
	private Logger logger;
	private PopupMenu popupMenu;
	
	static int gapXClickToLocate = 0;
	static int gapYClickToLocate = 0;
	
	/**
	 * コンストラクタ
	 */
	DigitalClock(){
		super(null);
		logger = AppLog.createAppLogger(this.getClass().getSimpleName());
		logger.log(Level.INFO,AppLog.APP_START_MSG);
		confLoad();
		setupConf();
		setupMenu();
	}
	
	/**
	 * 設定を呼び出す
	 */
	private void confLoad(){
		try {
			conf = AppConf.getInstance(AppStyle.CONF_FILE_NAME);
			fontColor = AppColor.valueOf(conf.loadProperty(Key.FONT_COLOR)).color();
			bgColor = AppColor.valueOf(conf.loadProperty(Key.BG_COLOR)).color();
			fontName = conf.loadProperty(Key.FONT);
			fontSize = Integer.parseInt(conf.loadProperty(Key.FONT_SIZE));
			font = new Font(fontName, Font.PLAIN, fontSize);
		} catch (Exception e) {
			logger.log(Level.SEVERE, AppLog.APP_CONF_ERR_MSG, e);
			System.exit(-1);
		}
	}
	
	/**
	 * 初期値を設定する
	 */
	private void setupConf(){
		setLocation(Integer.parseInt(conf.loadProperty(Key.INIT_LOCATION_X))
				, Integer.parseInt(conf.loadProperty(Key.INIT_LOCATION_Y)));

		setName(AppStyle.WINDOW_TITLE);
		setSize(windowWidth, windowHeight);

		setVisible(true);
		addMouseListener(this);
	}
	
	/**
	 * メニューを作成する
	 */
	private void setupMenu(){
		
		popupMenu = new PopupMenu();
		
		Menu fontMenu = new Menu(AppStyle.Menu.FONT.disp());
		fontMenu.addActionListener(this);
		for(AppFont appFont : AppFont.values()){
			 fontMenu.add(new MenuItem(appFont.font()));
		}
		popupMenu.add(fontMenu);
		
		Menu fontSizeMenu = new Menu(AppStyle.Menu.FONT_SIZE.disp());
		fontSizeMenu.addActionListener(this);
		for(Integer appFontSize : AppStyle.FONT_SIZES){
			fontSizeMenu.add(appFontSize.toString());
		}
		popupMenu.add(fontSizeMenu);
		
		Menu fontColor = new Menu(AppStyle.Menu.FONT_COLOR.disp());
		fontColor.addActionListener(this);
		for(AppColor appColor : AppColor.values()){
			fontColor.add(new MenuItem(appColor.name()));
		}
		popupMenu.add(fontColor);
		
		Menu bgColor = new Menu(AppStyle.Menu.BG_COLOR.disp());
		bgColor.addActionListener(this);
		for(AppColor appColor : AppColor.values()){
			bgColor.add(new MenuItem(appColor.name()));
		}
		popupMenu.add(bgColor);
		
		
        MenuItem closeClock = new MenuItem(AppStyle.Menu.EXIT.disp());
        popupMenu.add(closeClock);
        closeClock.addActionListener(this);

        add(popupMenu);
	}
	
	
	/**
	 * デジタル時計を起動する。 表示されたWindowの終了処理を行うまで表示する。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new DigitalClock()).start();
	}

	@Override
	public void run() {
		while (true) {
				repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// no action
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {

		/* 時間の取得 */
		timeValue = SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());

		if (!AppStyle.WINDOW_TITLE.equals(getName())) {
			setName(timeValue);
		}

		/* イメージの作成 */
		Image image = createImage(windowWidth, windowHeight);

		font = new Font(fontName, Font.PLAIN, fontSize);

		Graphics timeGraphics = image.getGraphics();
		timeGraphics.setFont(font);
		timeGraphics.setColor(fontColor);

		/* ウィンドゥサイズの設定 */
		int fontWidth = timeGraphics.getFontMetrics().stringWidth(AppStyle.DATE_PATTERN);
		int fontHeight = timeGraphics.getFontMetrics().getAscent();
		windowWidth = fontWidth * 2 ;
		windowHeight = fontHeight * 2 + 20 ;
		setSize(windowWidth, windowHeight);
		
		/* 描画位置を調整 */
		int graohicsX = (windowWidth - fontWidth) / 2;
		int graphicsY = (windowHeight + fontHeight ) / 2;
		timeGraphics.drawString(timeValue, graohicsX, graphicsY);
		
		setBackground(bgColor);

		g.drawImage(image, 0, 0, this);
	}
	
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		/* コマンドや実行された処理のラベルを取得する */
		String command = e.getActionCommand();
		String label = "";
		if(e.getSource() instanceof Menu){
			Menu source = (Menu)e.getSource();
			if(source != null){
				label = source.getLabel();
			}
		}
		
		/* 入力されたコマンドに対して処理を実行する */
		if(command.equals(AppStyle.Menu.EXIT.disp())){
			conf.saveProperty(Key.INIT_LOCATION_X, Integer.toString(getLocation().x));
			conf.saveProperty(Key.INIT_LOCATION_Y, Integer.toString(getLocation().y));
			logger.log(Level.INFO,AppLog.APP_END_MSG);
			System.exit(0);
		}else if(AppStyle.Menu.FONT.disp().equals(label)){
			for(AppFont appFont : AppFont.values()){
				if(appFont.font().equals(command)){
					fontName = appFont.font();
					conf.saveProperty(Key.FONT, appFont.font());
				}
			}
		}else if(AppStyle.Menu.FONT_SIZE.disp().equals(label)){
			for(Integer appFontSize : AppStyle.FONT_SIZES){
				if(appFontSize.toString().equals(command)){
					fontSize = appFontSize;
					conf.saveProperty(Key.FONT_SIZE,appFontSize.toString());
				}
			}
		}else if(AppStyle.Menu.FONT_COLOR.disp().equals(label)){
			for(AppColor appColor : AppColor.values()){
				if(appColor.toString().equals(command)){
					fontColor = AppColor.valueOf(command).color();
					conf.saveProperty(Key.FONT_COLOR,command);
				}
			}
		}else if(AppStyle.Menu.BG_COLOR.disp().equals(label)){
			for(AppColor appColor : AppColor.values()){
				if(appColor.toString().equals(command)){
					bgColor = AppColor.valueOf(command).color();
					conf.saveProperty(Key.BG_COLOR,command);
				}
			}
		}
		
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		//no action
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		gapXClickToLocate = e.getX() - getX();
		gapYClickToLocate = e.getY() - getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if (e.isPopupTrigger()){ //右クリック時の処理
			popupMenu.show(this , e.getX() , e.getY());
			
		}else{ //その他マウスをクリックしたときの処理			
			setLocation(e.getX() - gapXClickToLocate , e.getY() - gapYClickToLocate);   			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//no action
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//no action
	}
}
