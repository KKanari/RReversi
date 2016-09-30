package jp.co.RReversi;

/**
 * AI�̎v�l���[�`��
 */
public class Ai {
	/**
	 *�@���݂̔Ֆʏ�ň�ԑ����΂�Ԃ���ꏊ��I������
	 *�@��������ꍇ�͂��A��>�E>��>��̏��ŗD�悳���
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
