package frame;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * 時間をデジタル表示する時計を表示するメインクラス
 * 
 * @author takuto yoshida
 * 
 */
public class DigitalClock extends Frame implements ActionListener, Runnable {

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
	private Logger logger =AppLog.createAppLogger(this.getClass().getSimpleName());
	

	
	/**
	 * コンストラクタ
	 */
	public DigitalClock() {

		logger.log(Level.INFO,AppLog.APP_START_MSG);
		
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

		setLocation(Integer.parseInt(conf.loadProperty(Key.INIT_LOCATION_X))
				, Integer.parseInt(conf.loadProperty(Key.INIT_LOCATION_Y)));

		setTitle(AppStyle.WINDOW_TITLE);
		setSize(windowWidth, windowHeight);
		setResizable(false);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				/* ロケーションをセットして終了する。 */
				conf.saveProperty(Key.INIT_LOCATION_X, Integer.toString(getLocation().x));
				conf.saveProperty(Key.INIT_LOCATION_Y, Integer.toString(getLocation().y));
				logger.log(Level.INFO,AppLog.APP_END_MSG);
				System.exit(0);
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				setTitle(AppStyle.WINDOW_TITLE);
			}

			@Override
			public void windowIconified(WindowEvent e) {
				setTitle(timeValue);
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				setTitle(timeValue);
			}

			@Override
			public void windowActivated(WindowEvent e) {
				setTitle(AppStyle.WINDOW_TITLE);
			}

		});

		/* メニューを追加 */
		MenuBar menuBar = new MenuBar();
		setMenuBar(menuBar);

		Menu menu = new Menu(AppStyle.Menu.TITLE.disp());
		menuBar.add(menu);

		MenuItem menuProp = new MenuItem(AppStyle.Menu.PROPERTY.disp());
		menu.add(menuProp);
		menuProp.addActionListener(this);

	}

	/**
	 * デジタル時計を起動する。 表示されたWindowの終了処理を行うまで表示する。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new DigitalClock()).start();
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// no action
			}
		}
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == AppStyle.Menu.PROPERTY.disp()) {
			new PropDialog(this);
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */
	@Override
	public void update(Graphics g) {
		paint(g);
	}


	/*
	 * (非 Javadoc)
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {

		/* 時間の取得 */
		timeValue = SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());

		if (!AppStyle.WINDOW_TITLE.equals(getTitle())) {
			setTitle(timeValue);
		}

		/* イメージの作成 */
		Image image = createImage(windowWidth, windowHeight);

		font = new Font(fontName, Font.PLAIN, fontSize);

		Graphics timeGraphics = image.getGraphics();
		timeGraphics.setFont(font);
		timeGraphics.setColor(fontColor);

		/* ウィンドゥサイズの設定 */
		int fontWidth = timeGraphics.getFontMetrics().stringWidth(AppStyle.DATE_PATTERN);
		int fontHeight = timeGraphics.getFontMetrics().getHeight();
		windowWidth = fontWidth * 2 + 20;
		windowHeight = fontHeight * 2 + 50 ;
		setSize(windowWidth, windowHeight);
		
		
		/* 描画位置を調整 */
		int graohicsX = (windowWidth - fontWidth) / 2;
		int graphicsY = (windowHeight + fontHeight ) / 2;
		timeGraphics.drawString(timeValue, graohicsX, graphicsY);
		
		setBackground(bgColor);

		g.drawImage(image, 0, 0, this);
	}
	
	/**
	 * メニューに表示するプロパティダイアログを表すクラス
	 */
	class PropDialog extends Dialog implements ActionListener, ItemListener {

		Choice fontChoice = new Choice();
		Choice fontSizeChoice = new Choice();
		Choice fontColorChoice = new Choice();
		Choice bgColorChoice = new Choice();

		public PropDialog(Frame owner) {
			super(owner);
			setLayout(new GridLayout(5, 2));

			/* fontの選択 */
			add(new Label(AppStyle.DialogMenu.FONT.disp()));
			fontChoice.addItemListener(this);
			for (AppFont appFont : AppFont.values()) {
				fontChoice.add(appFont.font());
			}
			fontChoice.select(fontName);
			add(fontChoice);

			/* fontSizeの選択 */
			add(new Label(AppStyle.DialogMenu.FONT_SIZE.disp()));
			fontSizeChoice.addItemListener(this);
			for (int fontSize : AppStyle.FONT_SIZES) {
				fontSizeChoice.add(new Integer(fontSize).toString());
			}
			fontSizeChoice.select(new Integer(fontSize).toString());
			add(fontSizeChoice);

			/* font colorの選択 */
			add(new Label(AppStyle.DialogMenu.FONT_COLOR.disp()));
			fontColorChoice.addItemListener(this);
			for (AppColor appColor : AppColor.values()) {
				fontColorChoice.add(appColor.name());
			}
			fontColorChoice.select(AppColor.convertAppColor(fontColor).name());
			add(fontColorChoice);

			/* groudColorの選択 */
			add(new Label(AppStyle.DialogMenu.BG_COLOR.disp()));
			bgColorChoice.addItemListener(this);
			for (AppColor appColor : AppColor.values()) {
				bgColorChoice.add(appColor.name());
			}
			bgColorChoice.select(AppColor.convertAppColor(bgColor).name());
			add(bgColorChoice);

			add(new Label(""));

			Button b1 = new Button(AppStyle.DialogMenu.OK.disp());
			b1.addActionListener(this);
			add(b1);

			setTitle(AppStyle.DialogMenu.TITLE.disp());
			setModal(true);
			setSize(AppStyle.DIALOG_WIDTH, AppStyle.DIALOG_HEIGHT);
			Point point = DigitalClock.this.getLocation();
			setLocation(point.x + 40, point.y + 40); // ウィンドゥの生成位置を呼び出しウィンドゥから少しずらす
			setResizable(false);
			setVisible(true);

		}

		/* (非 Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}

		/* (非 Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {

			/* プロパティの設定に応じて表示を変更 および 設定をファイルに保存する */
			if (e.getSource() == fontChoice) {
				fontName = fontChoice.getSelectedItem();
				conf.saveProperty(Key.FONT, fontChoice.getSelectedItem());
			} else if (e.getSource() == fontSizeChoice) {
				fontSize = Integer.parseInt(fontSizeChoice.getSelectedItem());
				conf.saveProperty(Key.FONT_SIZE,fontSizeChoice.getSelectedItem());
			} else if (e.getSource() == fontColorChoice) {
				fontColor = AppColor.valueOf(fontColorChoice.getSelectedItem()).color();
				conf.saveProperty(Key.FONT_COLOR,fontColorChoice.getSelectedItem());
			} else if (e.getSource() == bgColorChoice) {
				bgColor = AppColor.valueOf(bgColorChoice.getSelectedItem()).color();
				conf.saveProperty(Key.BG_COLOR, bgColorChoice.getSelectedItem());
			}

		}

	}



}
