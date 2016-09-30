package jp.co.RReversi;

import android.widget.ImageButton;

public class Stone {
	private String color;
	private ImageButton botton;
	private int id;
	private int x;
	private int y;
	
	/**
	 * �R���X�g���N�^
	 */
	Stone() {
		color = null;
		botton = null;
		id = 0;
		y = 0;
		x = 0;
	}
	/**
	 * @return color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color �Z�b�g���� color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return botton
	 */
	public ImageButton getBotton() {
		return botton;
	}

	/**
	 * @param botton �Z�b�g���� botton
	 */
	public void setBotton(ImageButton botton) {
		this.botton = botton;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id �Z�b�g���� id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x �Z�b�g���� x
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y �Z�b�g���� y
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * ���΂̐ΐF��Ԃ�
	 * @return
	 */
	public String getReverse() {
		if (this.getColor() == null) {
			return null;
		}
		return Constants.STONE_BLACK.equals(this.getColor())
				? Constants.STONE_WHITE
				: Constants.STONE_BLACK;	
	}
	
}
