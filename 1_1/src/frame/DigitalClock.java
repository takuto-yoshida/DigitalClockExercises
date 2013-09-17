package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * 時間をデジタル表示する時計を表示するメインクラス
 * 
 * <課題1-1> AWT のFrame を使用して、時間を表示するデジタル時計（アナログ時計は不可）を 作成してください。 java.awt.Frame
 * を使用する。 Windows の普通のアプリケーションと同様にタイトルバーの「×」ボタンを クリックすると終了する。
 * デジタル時計の描画は、paintメソッド内でGraphicsを使用して行う。 テキストラベルによる単なる表示は、不可。
 * 
 * @author takuto yoshida
 * 
 */
public class DigitalClock extends Frame implements ActionListener, Runnable {

	private static final String DATE_PATTERN = "hh:mm:ss";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
	
	private static String timeValue;
	private static String windowTitle = "GUI1-1 [DigitalClock]";
	
	private int windowWidth = 350;
	private int windowHeight = 220;
	
	private int fontSize = 50;
	private Color fontColor = Color.CYAN;
	private Color bgColor = Color.BLACK;
	private String fontName = "Dialog";
	private Font font = new Font(fontName, Font.PLAIN, fontSize) ;
	
	/**
	 * コンストラクタ
	 */
	public DigitalClock() {

		setTitle(windowTitle);
		setSize(windowWidth, windowHeight);

		setVisible(true);
		addWindowListener(new WindowAdapter() {
			/* WindowClose時の処理を追加 */
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	/**
	 * デジタル時計を起動する。
	 * 表示されたWindowの終了処理を行うまで表示する。
	 * @param args 
	 */
	public static void main(String[] args) {
		new Thread(new DigitalClock()).start();
	}

	/* (非 Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			
			repaint();
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// no action
			}
		}
	}

	/* (非 Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//no action
	}

	/* (非 Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		
		/* 時間の取得 */
		timeValue = SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());
		
		/* ウィンドゥサイズの設定 */
		windowHeight = getHeight();
		windowWidth = getWidth();

		/* イメージの作成 */
		Image image = createImage( windowWidth , windowHeight );
		
		Graphics timeGraphics = image.getGraphics();
		timeGraphics.setFont(font);
		timeGraphics.setColor(fontColor);
		
		/* 描画位置を調整 */
		int graohicsX = (windowWidth - (timeGraphics.getFontMetrics(font)).stringWidth(DATE_PATTERN))/2;
		int graphicsY = (windowHeight + (timeGraphics.getFontMetrics(font)).getHeight()/2) /2;
		timeGraphics.drawString(timeValue, graohicsX ,graphicsY);
		
		setBackground(bgColor);
		
		g.drawImage(image, 0,  0, this);		
	}

}
