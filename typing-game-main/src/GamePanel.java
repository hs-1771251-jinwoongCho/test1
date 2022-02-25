import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel {
	
	private final int MAX_WORDS = 100;
	
	private Player player = new Player();
	
	private JTextField input = new JTextField(30);
	private Vector<JLabel>targetVector = new Vector<JLabel>();
	
	public Color skyBlue = new Color(219, 239, 255);
	public Color lightBlue = new Color(94, 177, 255);
	
	private ScorePanel scorePanel = null;
	private GameGroundPanel gameGroundPanel = new GameGroundPanel();
	private InputPanel inputPanel = new InputPanel();
	
	private TextSource textSource = new TextSource();

	
	private MakeWordThread makeWordThread = new MakeWordThread(targetVector, player);
	private DropWordThread dropWordThread = new DropWordThread(targetVector,player);
	private FailWordThread failWordThread = new FailWordThread(targetVector);
	
	// 난이도 별 속도 조절
	private int [] generationSpeed = {4000,3000,2000,1000,800};
	private int [] droppingSpeed = {400,300,200,80,40};
	
	public GamePanel() {
	}
	
	public GamePanel(ScorePanel scorePanel, Player player) {
		this.scorePanel = scorePanel;
		this.player = player;
		
		makeWordThread = new MakeWordThread(targetVector, player);
		dropWordThread = new DropWordThread(targetVector,player);
		textSource = new TextSource(player.getLanguage());

		setLayout(new BorderLayout());
		add(gameGroundPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
		
		input.setHorizontalAlignment(JTextField.CENTER);
		input.setFont(new Font("Aharoni", Font.PLAIN, 20));
		
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(targetVector) {
					JTextField t = (JTextField)(e.getSource());
					String inWord = t.getText(); // 입력한 단어
					for (int i=0; i < targetVector.size(); i++) {
						String text = targetVector.get(i).getText();
						if(text.equals(inWord)) { // 단어맞추기 성공
							// 점수 증가
							scorePanel.increase(player);
							scorePanel.repaintScore();
							gameGroundPanel.remove(targetVector.get(i));
							targetVector.remove(i);
							t.setText(null);
							// 단어 틀릴때마다 스피드 증가
							if (droppingSpeed[player.getLevel()-1] > 2)
								droppingSpeed[player.getLevel()-1]--;
							if (generationSpeed[player.getLevel()-1] > 20)
								generationSpeed[player.getLevel()-1] -= 10;
							break;
						}
						if((i == (targetVector.size() - 1)) && !targetVector.get(i).getText().equals(inWord)) {
							// 점수 감소
							scorePanel.decrease(player);
							scorePanel.repaintScore();
							t.setText(null);
						}
						t.requestFocus();
					}
				}
			}
		});
	}

	class GameGroundPanel extends JPanel{
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("back2.jpg");
			g.drawImage(icon.getImage(), 0, 0, gameGroundPanel.getWidth(),
					gameGroundPanel.getHeight(), gameGroundPanel);
			setOpaque(false);
		}
		public GameGroundPanel() {
			this.setBackground(skyBlue);

			setLayout(null);
		}
	}
	
	class InputPanel extends JPanel{
		public InputPanel() {
			setLayout(new FlowLayout());
			this.setBackground(lightBlue);
			add(input);
		}
	}
	
	public void gameStart(Player player) {
		this.player = player;

		makeWordThread.start();
		dropWordThread.start();
		failWordThread.start();
	}
	
	public void gameOver() { // 게임종료
		makeWordThread.interrupt();
		dropWordThread.interrupt();
		failWordThread.interrupt();
	}
	
	// 단어 생성하는 스레드
	public class MakeWordThread extends Thread{
		
		private Vector<JLabel>targetVector = null;
		private Player player = null;
		
		synchronized void generateWord(Player player) {
			JLabel targetLabel = new JLabel("");
			String newWord = textSource.get(player.getLanguage());
			targetLabel.setText(newWord);
			
			targetLabel.setHorizontalAlignment(JLabel.CENTER);
			targetLabel.setSize(200, 40);
			if(player.getLanguage()=="ko") {
				targetLabel.setFont(new Font("함초롬돋움",1,21));
			}
			else targetLabel.setFont(new Font("Dialog", 1, 21));
			targetLabel.setForeground(Color.WHITE);
			
			int startX = (int) (Math.random()*gameGroundPanel.getWidth());
			while(true) {
				if ((startX + targetLabel.getWidth()) > gameGroundPanel.getWidth()) 
					startX = (int) (Math.random()*gameGroundPanel.getWidth());
				else
					break;
			}
			
			targetLabel.setLocation(startX,0);
			
			targetLabel.setOpaque(false);
			targetVector.addElement(targetLabel);
			gameGroundPanel.add(targetLabel);
		}
		
		public MakeWordThread(Vector<JLabel>targetVector, Player player) {
			this.targetVector = targetVector;
			this.player = player;
		}
		
		@Override
		public void run() {
			while(true) {
				int generationTime = generationSpeed[player.getLevel()-1];
				generateWord(player);
				gameGroundPanel.repaint();
				try {
					sleep(generationTime);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	public class DropWordThread extends Thread{
		
		private Vector<JLabel>targetVector = null;
		private Player player = null;
		
		public DropWordThread(Vector<JLabel>targetVector, Player player) {
			this.targetVector = targetVector;
			this.player = player;
		}
		
		synchronized void dropWord(Player player) {
			for (int i=0; i<targetVector.size(); i++) {
				int x = targetVector.get(i).getX();
				int y = targetVector.get(i).getY();
				targetVector.get(i).setLocation(x, y+5);
				gameGroundPanel.repaint();
			}
		}
		
		@Override
		public void run() {
			 while (true){
				 int dropTime = droppingSpeed[player.getLevel()-1];
				 dropWord(player);
				 gameGroundPanel.repaint();
				 try {
					 sleep(dropTime);
					} catch (InterruptedException e) {
						return;
					}
			}
		}
	}
	
	public class FailWordThread extends Thread {
		
		private Vector<JLabel>targetVector = null;
		
		public FailWordThread(Vector<JLabel>targetVector) {
			this.targetVector = targetVector;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					sleep(1);
					for(int i=0; i<targetVector.size(); i++) {

						int y = ((JLabel)targetVector.get(i)).getY();
						if (y > gameGroundPanel.getHeight()-20) {
							System.out.println(targetVector.get(i).getText() + " 실패");
							
							boolean isGameOver =scorePanel.decreaseLife(player);
							if(isGameOver == true) {
								gameOver();
							}
							
							gameGroundPanel.remove(targetVector.get(i));
							targetVector.remove(i);
						}
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
		
	}