package jp.co.RReversi;

/**
 * ボード関連クラス
 */
public class Board {
	
	//　ボード
	private Stone[][] board = new Stone[10][10];

	/**
	 * ボードの複製を返す。
	 * @return Board
	 */
	public Board getCopyBoard() {
		final Board b = new Board();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				b.board[i][j] = this.board[i][j];
			}
		}
		return b;
	}
	/**
	 * ボードを初期化する。
	 */
	public void init() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				board[x][y] = new Stone();
				board[x][y].setX(x);
				board[x][y].setY(y);
			}
		}
		// 初期配置
		initStone();
	}
	/**
	 *  初期配置を行う
	 */
	public void initStone() {
		setStoneColor(4,4,Constants.STONE_WHITE);
		setStoneColor(5,5,Constants.STONE_WHITE);
		setStoneColor(4,5,Constants.STONE_BLACK);
		setStoneColor(5,4,Constants.STONE_BLACK);
	}
	/**
	 * ボードをリセットする
	 * ※メイン処理で再度イベント生成は行わないので初期化とは異なる
	 */
	public void reset() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				Stone stone = getStone(x, y);
				stone.setColor(null);
			}
		}
		//　初期配置
		initStone();
	}
	
	/**
	 *  指定された座標に色をセットする
	 * @param x
	 * @param y
	 * @param color
	 */
	public void setStoneColor(final int x, final int y, final String color) {
		board[x][y].setColor(color);
	}
	/**
	 * 指定された座標の石を返す
	 * @param x
	 * @param y
	 * @return Stone
	 */
	 
	public Stone getStone(final int x, final int y) {
		return board[x][y];
	}
	/**
	 *  IDに紐づく石を返す
	 * @param id
	 * @return stone/null
	 */
	public Stone getStoneById(int id){
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				if (board[x][y].getId() == id) {
					return board[x][y];
				}
			}
		}
		return null;
	}
	/**
	 * 石置きチェック処理
	 * 指定した座標に石（色）があるかどうか
	 * @param x
	 * @param y
	 * @return true/false
	 */
	public boolean isFindStone(int x, int y) {
		Stone stone = getStone(x, y);
		if (stone.getColor() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 指定した座標が盤面内かどうか
	 * @param x
	 * @param y
	 * @return true/false
	 */
	private boolean isInBoard(int x, int y){
		return (1 <= x && x < 9) && (1 <= y && y < 9);
	}
	
	/**
	 * 石置きチェック処理
	 * 指定した色で返せる石があるかどうか
	 * @param color
	 * @return true:有/false:無
	 */
	public boolean canReverse(String color){
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				Stone stone = getStone(x,y);
				if (stone.getColor() != null) {
					continue;
				}
				String tmpColor = stone.getColor();
				stone.setColor(color);
				if (canPut(stone, x, y)) {
					stone.setColor(tmpColor);
					return true;
				}
				stone.setColor(tmpColor);
			}
		}
		return false;
	}	
	/**
	 * 裏返る石の数をカウント
	 * @param stone
	 * @param x
	 * @param y
	 * @return 数
	 */
	public int countReverceStones(final Stone stone, final int x, final int y) {
		if (stone == null || !isInBoard(x, y)) {
			return 0;
		}
		int num = 0;
		// 8方向に探索
		for (int vy = -1; vy <= 1; vy++) {
			for (int vx = -1; vx <= 1; vx++) {
				if (vy == 0 && vx == 0) {
					continue;
				}
				int tmpNum = 0;
				for (int i = 1; i < 8; i++) {
					final int ax = x + vx * i;
					final int ay = y + vy * i;
					if (!isInBoard(ax, ay)) {
						break;
					}
					final Stone tmpStone = getStone(ax, ay);
					if (tmpStone.getColor() == null) {
						break;
					} else if (tmpStone.getColor() == stone.getColor()) {
						num+=tmpNum;
						break;
					} else if (tmpStone.getColor() == stone.getReverse()) {
						tmpNum++;
					}
				}
			}
		}
		return num;
	}
	/**
	 * 置くことができるかチェックする。
	 * @return true:置くことができる。 false:置くことができない。
	 */
	public boolean canPut(final Stone stone, final int x, final int y) {
		return 0 < countReverceStones(stone, x, y);
	}
	/**
	 * 石を置く
	 * 
	 * @param stone
	 * @param x
	 * @param y
	 * @return true:置けた/false:置けなかった
	 */
	public boolean putStone(final Stone stone, final int x, final int y) {
		if (stone == null || !isInBoard(x, y)) {
			return false;
		}
		int num = 0;
		Board b = new Board();
		b = getCopyBoard();
		// 8方向に探索
		for (int vy = -1; vy <= 1; vy++) {
			for (int vx = -1; vx <= 1; vx++) {
				if (vy == 0 && vx == 0) {
					continue;
				}
				int tmpNum = 0;
				for (int i = 1; i < 8; i++) {
					final int ax = x + vx * i;
					final int ay = y + vy * i;
					if (!isInBoard(ax, ay)) {
						break;
					}
					final Stone tmpStone = getStone(ax, ay);
					if (tmpStone.getColor() == null) {
						break;
					} else if (tmpStone.getColor() == stone.getColor()) {
						for (int j = 0; j < tmpNum + 1; j++) {
							final int bx = x + vx * j;
							final int by = y + vy * j;
							b.board[bx][by].setColor(stone.getColor());
						}
						num+=tmpNum;
						break;
					} else if (tmpStone.getColor() == stone.getReverse()) {
						tmpNum++;
					}
				}
			}
		}
		if (0 < num) {
			board = b.board;
			return true;
		}
		return false;
	}
}
