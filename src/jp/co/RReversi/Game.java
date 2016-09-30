package jp.co.RReversi;

import android.os.Handler;

/**
 * ゲーム機能
 */
public class Game implements Runnable{

	private Board board = new Board();
	private Reversi reversi;
	Handler mHandler = new Handler();

	// モード
	private String mode = Constants.MODE_ANDROID;
	// 手番プレイヤー
	private String nowPlayer = Constants.STONE_BLACK;
	// 自石（プレイヤー1）
	private String player1 = Constants.STONE_BLACK;
	// 自石（プレイヤー2）
	private String player2 = Constants.STONE_WHITE;
	// 石数（プレイヤー1）
	private int count1 = 0;
	// 石数（プレイヤー2）
	private int count2 = 0;
	
	/**
	 * コンストラクタ
	 * @param reversi
	 * @param board
	 */
	public Game(Reversi reversi, Board board) {
		this.board.init();
		this.reversi = reversi;
		this.board = board;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mHandler.post(new Runnable() {
			public void run() {
		        // COM自動処理
		        reversi.autoPlayer();
			}
		});
	}
	/**
	 *  プレイヤー1の反対の色を返す
	 * @return 石色
	 */
	public String getReversePlayer(){
		String noPlayer = null;
		if (Constants.STONE_BLACK.equals(getPlayer1())){
			noPlayer = Constants.STONE_WHITE;
		} else {
			noPlayer = Constants.STONE_BLACK;
		}
		return noPlayer;
	}
	
	/**
	 *  非手番プレイヤーの色を返す
	 * @return 石色
	 */
	public String getNoPlayer(){
		String noPlayer = null;
		if (nowPlayer == null) {
			// nullのまま
		} else if ("black".equals(getNowPlayer())){
			noPlayer = "white";
		} else if ("white".equals(getNowPlayer())){
			noPlayer = "black";
		}
		return noPlayer;
	}
	/**
	 * @return board
	 */
	public Board getBoard() {
		return board;
	}
	/**
	 * @param board セットする board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}	
	/**
	 * @return count1
	 */
	public int getCount1() {
		return count1;
	}
	/**
	 * @param count1 セットする count1
	 */
	public void setCount1(int count1) {
		this.count1 = count1;
	}
	/**
	 * @return count2
	 */
	public int getCount2() {
		return count2;
	}
	/**
	 * @param count2 セットする count2
	 */
	public void setCount2(int count2) {
		this.count2 = count2;
	}

	/**
	 * @return nowPlayer
	 */
	public String getNowPlayer() {
		return nowPlayer;
	}
	/**
	 * @param nowPlayer セットする nowPlayer
	 */
	public void setNowPlayer(String nowPlayer) {
		this.nowPlayer = nowPlayer;
	}
	/**
	 * @return mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode セットする mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return player1
	 */
	public String getPlayer1() {
		return player1;
	}
	/**
	 * @param player1 セットする player1
	 */
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	/**
	 * @return player2
	 */
	public String getPlayer2() {
		return player2;
	}
	/**
	 * @param player2 セットする player2
	 */
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
}
