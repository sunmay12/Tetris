import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private ScorePanel scorePanel = new ScorePanel(); // 점수를 관리하기 위한 ScorePanel 인스턴스
    private GridLayout grid = new GridLayout(11, 11, 1, 1);
    private JLabel[][] blocks = new JLabel[11][11]; // 게임 그리드를 나타내는 배열
    private FallingThread fallingThread = new FallingThread(); // 떨어지는 블록을 처리하는 쓰레드
    private int row; // 떨어지는 블록의 현재 행
    private int col; // 떨어지는 블록의 현재 열
    private int randomColor;
    private Color currentColor; // 떨어지는 블록의 현재 색상

    // 초기 그리드를 검은색 블록으로 생성하는 메소드
    private void makeGrid() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                JLabel block = new JLabel();
                block.setOpaque(true);
                block.setBackground(Color.black);
                blocks[i][j] = block;
                add(block);
            }
        }
    }

    // 떨어지는 블록의 랜덤한 색상을 얻는 메소드
    private Color getColor() {
        int num = (int) (Math.random() * 4);
        switch (num) {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            case 2:
                return Color.CYAN;
            case 3:
                return Color.GREEN;
            default:
                return Color.BLACK;
        }
    }

    // 떨어지는 블록을 처리하는 쓰레드를 나타내는 내부 클래스
    public class FallingThread extends Thread {
        @Override
        public void run() {
            makeBlock(); // 첫 번째 떨어지는 블록 초기화
            while (true) {
                try {
                    sleep(500);
                    // 블록이 맨 위에 도달하고 다른 블록 위에 없을 때 쓰레드 중단
                    if (row == 0 && blocks[1][col].getBackground() != Color.black) {
                        interrupt(); // 조건 충족 시 쓰레드 중단
                    }
                } catch (InterruptedException e) {
                    break; // 인터럽트 시 루프 탈출
                }
                if (isEmptyDown()) {
                    fallingBlock(); // 공간이 있으면 블록을 아래로 이동
                } else {
                    makeBlock(); // 현재 블록이 아래로 이동할 수 없으면 새로운 블록 생성
                    checkAndRemoveBlocks(); // 완성된 행을 확인하고 제거
                }
            }
            repaint(); // 쓰레드 실행 후 패널을 다시 그리기
        }
    }

    // 떨어지는 블록 초기화 메소드
    public void makeBlock() {
        row = 0;
        col = (int) (Math.random() * 11);
        //col = 5;
        currentColor = getColor();
    }

    // 동기화된 메소드로 떨어지는 블록을 처리하는 메소드
    synchronized public void fallingBlock() {
        if (row < 10) {
            blocks[row][col].setBackground(Color.black);
            row++;
            blocks[row][col].setBackground(currentColor);
            repaint();
        } else {
            row = 0;
            makeBlock();
            repaint();
        }
    }

    // 동기화된 메소드로 완성된 행을 확인하고 제거하는 메소드
    synchronized private void checkAndRemoveBlocks() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 9; j++) {
                int count = 1; // 연속된 같은 색 패널의 개수를 세기 위한 변수
                Color blockColor = blocks[i][j].getBackground();

                if (blockColor != Color.black) {
                    // 가로로 확인
                    for (int k = 1; k < 10; k++) {
                        if (j + k < 11 && blocks[i][j + k].getBackground() == blockColor) {
                            count++;
                        } else {
                            break;
                        }
                    }

                    if (count >= 3 && count <= 4) {
                        // 연속된 패널이 3개 이상 4개 이하일 때
                        for (int k = 0; k < count; k++) {
                            blocks[i][j + k].setBackground(Color.black);
                            moveBlocksDown();
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 11; j++) {
                int count = 1; // 연속된 같은 색 패널의 개수를 세기 위한 변수
                Color blockColor = blocks[i][j].getBackground();

                if (blockColor != Color.black) {
                    // 세로로 확인
                    for (int k = 1; k < 10; k++) {
                        if (i + k < 11 && blocks[i + k][j].getBackground() == blockColor) {
                            count++;
                            scorePanel.increaseScore();
                        } else {
                            break;
                        }
                    }

                    if (count >= 3 && count <= 4) {
                        // 연속된 패널이 3개 이상 4개 이하일 때
                        for (int k = 0; k < count; k++) {
                            blocks[i + k][j].setBackground(Color.black);
                            moveBlocksDown();
                        }
                    }
                }
            }
        }
    }

    // 동기화된 메소드로 블록을 아래로 이동시키는 메소드
    synchronized private void moveBlocksDown() {
        // 아래에서부터 빈 공간을 찾아 위의 블록들을 아래로 이동
        for (int i = blocks.length - 1; i >= 0; i--) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j].getBackground() == Color.black) {
                    // 빈 공간 발견 시 위에 있는 블록을 아래로 이동
                    for (int k = i - 1; k >= 0; k--) {
                        if (blocks[k][j].getBackground() != Color.black) {
                            // 현재 위치에 위의 블록의 색을 설정하고, 위의 블록의 색을 검정으로 변경
                            blocks[i][j].setBackground(blocks[k][j].getBackground());
                            blocks[k][j].setBackground(Color.black);
                            break;
                        }
                    }
                }
            }
        }
    }

    // 아래쪽이 비어있는지 확인하는 메소드
    public boolean isEmptyDown() {
        if (row < 10 && blocks[row + 1][col].getBackground() == Color.black) {
            return true;
        }
        return false;
    }

    // 왼쪽이 비어있는지 확인하는 메소드
    public boolean isEmptyLeft() {
        if (row >= 0 && col > 0 && blocks[row][col - 1].getBackground() == Color.black) {
            return true;
        }
        return false;
    }

    // 오른쪽이 비어있는지 확인하는 메소드
    public boolean isEmptyRight() {
        if (col < 10 && blocks[row][col + 1].getBackground() == Color.black) {
            return true;
        }
        return false;
    }

    // 블록을 아래로 이동시키는 메소드
    public void moveDown() {
        if (row < 10) {
            blocks[row][col].setBackground(Color.black);
            row++;
            blocks[row][col].setBackground(currentColor);
            repaint();
        }
    }

    // 블록을 오른쪽으로 이동시키는 메소드
    public void moveRight() {
        if (row < 10 && col < 10) {
            blocks[row][col].setBackground(Color.black);
            col++;
            blocks[row][col].setBackground(currentColor);
            repaint();
        }
    }

    // 블록을 왼쪽으로 이동시키는 메소드
    public void moveLeft() {
        if (row < 10 && col > 0) {
            blocks[row][col].setBackground(Color.black);
            col--;
            blocks[row][col].setBackground(currentColor);
            repaint();
        }
    }

    // 생성자에서 그리드를 만들고 떨어지는 블록 쓰레드를 시작하는 메소드
    public GamePanel() {
        setLayout(grid);
        makeGrid();
        fallingThread.start();
    }
}
