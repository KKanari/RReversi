package jp.co.RReversi;

/**
 * AIの思考ルーチン
 */
public class Ai {
	/**
	 *　現在の盤面上で一番多く石を返せる場所を選択する
	 *　複数ある場合はより、下>右>左>上の順で優先される
	 * @param board
	 * @param game
	 * @return Stone
	 */
	public Stone select(Board board, Game game) {
		int max = 0;
		Stone stone = new Stone();
		Stone retStone = new Stone();
		for (int x = 1; x < 9; x++) {
			for (int y = 1; y < 9; y++) {
				stone = board.getStone(x,y);
				if (stone.getColor() != null) {
					continue;
				}
				String tmpColor = stone.getColor();
				stone.setColor(game.getPlayer2());
				final int count = board.countReverceStones(stone, x, y);
				if (max <= count) {
					max = count;
					retStone = stone;
					retStone.setColor(tmpColor);
				}
				stone.setColor(tmpColor);
			}
		}
		return retStone;
	}
}
