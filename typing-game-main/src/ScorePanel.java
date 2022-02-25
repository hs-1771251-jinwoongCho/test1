import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
	private Player player = new Player();
	private GamePanel gamePanel = new GamePanel();
	private int score = 0;
	private int life = 5; // 생명
	private JLabel textLabel = new JLabel("점수");
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel [] lifeLabel = new JLabel[life];
	private JLabel warningLabel = new JLabel("<html>하트가 모두 없어지면<br>※※ Game Over! ※※</html>");
	private Color skyBlue = new Color(153, 214, 255);
	

	public ScorePanel() {
		setBackground(skyBlue);
		setLayout(null);
		
		textLabel.setFont(new Font("함초롬돋움",1,15));
		textLabel.setSize(50,20);
		textLabel.setLocation(20,200);
		add(textLabel);
		
		scoreLabel.setFont(new Font("함초롬돋움",1,15));
		scoreLabel.setSize(100,20);
		scoreLabel.setLocation(100,200);
		add(scoreLabel);
		
		ImageIcon heart = new ImageIcon("heart.png");
		
		for (int i=0; i<life; i++) {
			lifeLabel[i] = new JLabel(heart);
			lifeLabel[i].setSize(heart.getIconWidth(),heart.getIconHeight());
			lifeLabel[i].setLocation(30*i+20,50);
			add(lifeLabel[i]);
		}
		
		warningLabel.setFont(new Font("함초롬돋움",1,15));
		warningLabel.setSize(200,50);
		warningLabel.setLocation(20,70);
		add(warningLabel);

	}
	
	synchronized void increase(Player player) {
		score += 10;
		player.setScore(score);
		scoreLabel.setText(Integer.toString(score));
		scoreLabel.getParent().repaint();
	}
	
	synchronized void decrease(Player player) {
		score -= 10;
		player.setScore(score);
		scoreLabel.setText(Integer.toString(score));
		scoreLabel.getParent().repaint();
	}
	
	public void repaintScore() {
		scoreLabel.getParent().repaint();
	}
	
	public void initPlayerInfo(String name, int level, int score, String language) {
		player = new Player(name, level, score, language);

	}
	
	synchronized boolean decreaseLife(Player player) {
		life--;
		boolean isTrue = false;
		
		switch(life) {
		case 4: 
			lifeLabel[4].setVisible(false);
			break;
		case 3: 
			lifeLabel[0].setVisible(false);
			break;
		case 2: 
			lifeLabel[3].setVisible(false);
			break;
		case 1: 
			lifeLabel[1].setVisible(false);
			break;
		case 0: 
			lifeLabel[2].setVisible(false);
			warningLabel.setText("GAME OVER");
			warningLabel.setLocation(70,70);
			
			
			String [] answer = {"예", "다시시작"};
			int choice = JOptionPane.showOptionDialog(gamePanel, player.getName() + "은(는) " + player.getScore() + "점 입니다.\n게임을 종료하시겠습니까?",
					"게임 종료", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
			
			if(choice == 0) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
			else if(choice == 1) {
				GameFrame f = new GameFrame();
				isTrue = true;
			}
			
			break;
		}
		return isTrue;
	}
}