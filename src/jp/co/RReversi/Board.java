package jp.co.RReversi;

/**
 * �{�[�h�֘A�N���X
 */
public class Board {
	
	//�@�{�[�h
	private Stone[][] board = new Stone[10][10];

	/**
	 * �{�[�h�̕�����Ԃ��B
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
	 * �{�[�h������������B
	 */
	public void init() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				board[x][y] = new Stone();
				board[x][y].setX(x);
				board[x][y].setY(y);
			}
		}
		// �����z�u
		initStone();
	}
	/**
	 *  �����z�u���s��
	 */
	public void initStone() {
		setStoneColor(4,4,Constants.STONE_WHITE);
		setStoneColor(5,5,Constants.STONE_WHITE);
		setStoneColor(4,5,Constants.STONE_BLACK);
		setStoneColor(5,4,Constants.STONE_BLACK);
	}
	/**
	 * �{�[�h�����Z�b�g����
	 * �����C�������ōēx�C�x���g�����͍s��Ȃ��̂ŏ������Ƃ͈قȂ�
	 */
	public void reset() {
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				Stone stone = getStone(x, y);
				stone.setColor(null);
			}
		}
		//�@�����z�u
		initStone();
	}
	
	/**
	 *  �w�肳�ꂽ���W�ɐF���Z�b�g����
	 * @param x
	 * @param y
	 * @param color
	 */
	public void setStoneColor(final int x, final int y, final String color) {
		board[x][y].setColor(color);
	}
	/**
	 * �w�肳�ꂽ���W�̐΂�Ԃ�
	 * @param x
	 * @param y
	 * @return Stone
	 */
	 
	public Stone getStone(final int x, final int y) {
		return board[x][y];
	}
	/**
	 *  ID�ɕR�Â��΂�Ԃ�
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
	 * �Βu���`�F�b�N����
	 * �w�肵�����W�ɐ΁i�F�j�����邩�ǂ���
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
	 * �w�肵�����W���Ֆʓ����ǂ���
	 * @param x
	 * @param y
	 * @return true/false
	 */
	private boolean isInBoard(int x, int y){
		return (1 <= x && x < 9) && (1 <= y && y < 9);
	}
	
	/**
	 * �Βu���`�F�b�N����
	 * �w�肵���F�ŕԂ���΂����邩�ǂ���
	 * @param color
	 * @return true:�L/false:��
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
	 * ���Ԃ�΂̐����J�E���g
	 * @param stone
	 * @param x
	 * @param y
	 * @return ��
	 */
	public int countReverceStones(final Stone stone, final int x, final int y) {
		if (stone == null || !isInBoard(x, y)) {
			return 0;
		}
		int num = 0;
		// 8�����ɒT��
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
	 * �u�����Ƃ��ł��邩�`�F�b�N����B
	 * @return true:�u�����Ƃ��ł���B false:�u�����Ƃ��ł��Ȃ��B
	 */
	public boolean canPut(final Stone stone, final int x, final int y) {
		return 0 < countReverceStones(stone, x, y);
	}
	/**
	 * �΂�u��
	 * 
	 * @param stone
	 * @param x
	 * @param y
	 * @return true:�u����/false:�u���Ȃ�����
	 */
	public boolean putStone(final Stone stone, final int x, final int y) {
		if (stone == null || !isInBoard(x, y)) {
			return false;
		}
		int num = 0;
		Board b = new Board();
		b = getCopyBoard();
		// 8�����ɒT��
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
