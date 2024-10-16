import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel {
    private int score = 0; // 현재 점수를 저장하는 변수
    private JLabel scoreLabel = new JLabel(Integer.toString(score)); // 점수를 표시하는 라벨

    public ScorePanel() {
        setLayout(null); // 레이아웃 매니저 설정 - 절대 위치에 컴포넌트 배치
        this.setPreferredSize(new Dimension(300, 65)); // 패널의 크기 설정

        setBackground(Color.black); // 배경색을 검은색으로 설정

        Font font = new Font("Galmuri11 Bold", Font.PLAIN, 20); // 폰트 설정

        JLabel label1 = new JLabel("Score:"); // "Score:" 라벨 생성
        label1.setFont(font); // 폰트 설정
        label1.setForeground(Color.white); // 전경색(글자색) 설정
        label1.setBounds(390, 20, 200, 30); // 위치 및 크기 설정

        scoreLabel.setFont(font); // 폰트 설정
        scoreLabel.setForeground(Color.white); // 전경색(글자색) 설정
        scoreLabel.setBounds(470, 21, 200, 30); // 위치 및 크기 설정

        JLabel label2 = new JLabel("Level: 1"); // "Level: 1" 라벨 생성
        label2.setFont(font); // 폰트 설정
        label2.setForeground(Color.white); // 전경색(글자색) 설정
        label2.setBounds(500, 20, 200, 30); // 위치 및 크기 설정

        Font font2 = new Font("Galmuri11 Bold", Font.PLAIN, 40); // 더 큰 폰트 설정
        JLabel label3 = new JLabel("TETRIS"); // "TETRIS" 라벨 생성
        label3.setFont(font2); // 폰트 설정
        label3.setForeground(Color.yellow); // 전경색(글자색) 설정
        label3.setBounds(10, 15, 200, 45); // 위치 및 크기 설정

        // 생성한 컴포넌트들을 패널에 추가
        add(label1);
        add(label2);
        add(label3);
        add(scoreLabel);
    }

    // 점수를 증가시키고 라벨에 반영하는 메소드
    public void increaseScore() {
        score += 100; // 점수 100 증가
        scoreLabel.setText(Integer.toString(score)); // 라벨에 현재 점수 반영
    }
}
