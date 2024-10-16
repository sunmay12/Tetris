import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GameFrame extends JFrame {
    private GamePanel gamePanel = new GamePanel(); // 게임 패널 객체 생성
    private ScorePanel scorePanel = new ScorePanel(); // 점수 패널 객체 생성

    private BorderLayout border = new BorderLayout(); // 프레임의 레이아웃 매니저
    private Container contentPane; // 콘텐츠 팬

    private File file; // 음악 파일을 저장할 변수
    private AudioInputStream audio; // 오디오 입력 스트림
    private Clip clip; // 오디오 클립

    // 키 이벤트 처리를 위한 내부 클래스
    public class MyActionListener extends KeyAdapter {
        // 키가 눌렸을 때 호출되는 메서드
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode(); // 눌린 키의 코드를 얻어옴

            // 키 코드에 따라 동작 수행
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    if (gamePanel.isEmptyDown()) {
                        gamePanel.moveDown();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (gamePanel.isEmptyLeft()) {
                        gamePanel.moveLeft();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (gamePanel.isEmptyRight()) {
                        gamePanel.moveRight();
                    }
                    break;
            }
        }
    }

    // 게임 프레임 생성자
    public GameFrame() {
        setSize(600, 800); // 프레임 크기 설정
        setLocationRelativeTo(null); // 화면 중앙에 프레임 표시
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기 버튼 동작 설정

        file = new File("테트리스.wav"); // 음악 파일 경로 설정
        try {
            audio = AudioSystem.getAudioInputStream(file); // 오디오 입력 스트림 생성
            clip = AudioSystem.getClip(); // 오디오 클립 생성
            clip.open(audio); // 클립에 오디오 입력 스트림 연결
        } catch (Exception e) {
            e.printStackTrace(); // 오류가 발생하면 콘솔에 출력
        }
        Music(); // 음악 재생 메서드 호출

        contentPane = new Container(); // 콘텐츠 팬 생성
        contentPane.setLayout(border); // 레이아웃 매니저 설정
        contentPane.add(gamePanel, border.CENTER); // 게임 패널을 중앙에 추가
        contentPane.add(scorePanel, border.NORTH); // 점수 패널을 북쪽에 추가

        setContentPane(contentPane); // 프레임에 콘텐츠 팬 설정

        addKeyListener(new MyActionListener()); // 키 리스너 등록

        setFocusable(true); // 포커스를 받을 수 있도록 설정
        requestFocus(); // 포커스 요청

        setVisible(true); // 프레임 표시
    }

    // 음악 재생 메서드
    private void Music() {
        if (clip != null) { // 클립이 생성되었다면
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 계속 반복 재생
            clip.start(); // 재생 시작
        } else {
            System.out.println("Clip is null"); // 클립이 생성되지 않았을 경우 콘솔에 출력
        }
    }
}
