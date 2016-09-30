package jp.co.RReversi;

import android.widget.ImageButton;

public class Stone {
	private String color;
	private ImageButton botton;
	private int id;
	private int x;
	private int y;
	
	/**
	 * コンストラクタ
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
	 * @param color セットする color
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
	 * @param botton セットする botton
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
	 * @param id セットする id
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
	 * @param x セットする x
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
	 * @param y セットする y
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * 反対の石色を返す
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
