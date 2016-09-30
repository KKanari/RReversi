package jp.co.RReversi;

import android.os.Handler;

/**
 * �Q�[���@�\
 */
public class Game implements Runnable{

	private Board board = new Board();
	private Reversi reversi;
	Handler mHandler = new Handler();

	// ���[�h
	private String mode = Constants.MODE_ANDROID;
	// ��ԃv���C���[
	private String nowPlayer = Constants.STONE_BLACK;
	// ���΁i�v���C���[1�j
	private String player1 = Constants.STONE_BLACK;
	// ���΁i�v���C���[2�j
	private String player2 = Constants.STONE_WHITE;
	// �ΐ��i�v���C���[1�j
	private int count1 = 0;
	// �ΐ��i�v���C���[2�j
	private int count2 = 0;
	
	/**
	 * �R���X�g���N�^
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
		        // COM��������
		        reversi.autoPlayer();
			}
		});
	}
	/**
	 *  �v���C���[1�̔��΂̐F��Ԃ�
	 * @return �ΐF
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
	 *  ���ԃv���C���[�̐F��Ԃ�
	 * @return �ΐF
	 */
	public String getNoPlayer(){
		String noPlayer = null;
		if (nowPlayer == null) {
			// null�̂܂�
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
	 * @param board �Z�b�g���� board
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
	 * @param count1 �Z�b�g���� count1
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
	 * @param count2 �Z�b�g���� count2
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
	 * @param nowPlayer �Z�b�g���� nowPlayer
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
	 * @param mode �Z�b�g���� mode
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
	 * @param player1 �Z�b�g���� player1
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
	 * @param player2 �Z�b�g���� player2
	 */
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
}
