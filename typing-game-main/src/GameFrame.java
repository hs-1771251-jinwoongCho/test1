import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class GameFrame extends JFrame {
	
	private Player player = new Player();
	private int score = 0;
	
	private LoginPanel loginPanel = new LoginPanel();
	private ScorePanel scorePanel = new ScorePanel();
	private GamePanel gamePanel = new GamePanel();
	
	
	public GameFrame() {
		setTitle("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setContentPane(loginPanel);
		
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public class LoginPanel extends JPanel{
		
		private Box langBox = Box.createHorizontalBox();
		private Box levelBox = Box.createHorizontalBox();
		private Box nameBox = Box.createHorizontalBox();
		
		private JLabel Title = new JLabel("KeyBoard Game");
		private JLabel langLabel = new JLabel("언어   ");
		private JLabel lvLabel = new JLabel("레벨   ");
		private String [] level = {"Lv.1", "Lv.2", "Lv.3", "Lv.4", "Lv.5"};
		private JComboBox<String> lvCombo = new JComboBox<String>(level);
		private JRadioButton [] langRadio = new JRadioButton [2];
		private ButtonGroup g = new ButtonGroup();
		private String [] langType = {"ko","en"};
		private JLabel name = new JLabel("이름   "); 
		private JTextField inputName = new JTextField(30);
		private JButton startBtn = new JButton("게임시작");
	
		
		private JLabel []scoreText = new JLabel[10];
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("back1.jpg");
			g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			setOpaque(false);
		}
		
		public LoginPanel() {
			
			this.setLayout(null);
			
			Title.setFont(new Font("Goudy Stout",1,40));
			Title.setBounds(60, 60, 800, 40);
			
			langLabel.setFont(new Font("함초롬돋움",1,15));
			langBox.add(langLabel);
			langBox.setBounds(280, 150, 200, 30);

			lvLabel.setFont(new Font("함초롬돋움",1,15));
			lvCombo.setFont(new Font("함초롬돋움",1,15));
			levelBox.add(lvLabel);
			levelBox.add(lvCombo);
			levelBox.setBounds(280, 200, 200, 30);
			
			for (int i=0; i<langRadio.length; i++) {
				langRadio[i] = new JRadioButton(langType[i]);
				g.add(langRadio[i]);
				langBox.add(langRadio[i]);
				langRadio[i].setFont(new Font("함초롬돋움",1,20));
				langRadio[i].setOpaque(false);	//배경색 없게
			}
			
						
			name.setFont(new Font("함초롬돋움",1,15));
			inputName.setFont(new Font("함초롬돋움",1,15));
			nameBox.add(name);
			nameBox.add(inputName);
			nameBox.setBounds(280, 250, 200, 30);
		
			startBtn.setFont(new Font("함초롬돋움",1,15));
			startBtn.setBounds(280, 300, 200, 30);
			startBtn.setBorderPainted(false);	
			
			add(Title);
			add(langBox);
			add(levelBox);
			add(nameBox);
			add(startBtn);
			
			startBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					int selectedIndex;
					if(langRadio[0].isSelected()) selectedIndex = 0;
					else selectedIndex = 1;
					
					player = new Player(inputName.getText(),
							lvCombo.getSelectedIndex()+1, score, langRadio[selectedIndex].getText());
					player.setName(inputName.getText());
					player.setLevel(lvCombo.getSelectedIndex()+1);
					player.setLanguage(langType[selectedIndex]);
					
					gamePanel = new GamePanel(scorePanel, player);
					
					setLoginPageHidden();
					
					getContentPane().setLayout(new BorderLayout());
					splitPane();
					makeInfoPanel(player);
					setResizable(false);
					repaint();
					
					gamePanel.gameStart(player);
				}
			});
			
		
	}
		
		public void setLoginPageHidden() { // 한글실행
			Title.setVisible(false);
			langBox.setVisible(false);
			levelBox.setVisible(false);
			nameBox.setVisible(false);
			startBtn.setVisible(false);

		}
		
		public void setLoginPageVisible() { // 영어실행
			Title.setVisible(true);
			langBox.setVisible(true);
			levelBox.setVisible(true);
			nameBox.setVisible(true);
			startBtn.setVisible(true);

		}
		

	private void splitPane() { // 게임 실행 화면
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(600);
		hPane.setEnabled(false);
		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(scorePanel);
	}
	
	public void makeInfoPanel(Player player) {
		
		getContentPane().add(new InfoPanel(player), BorderLayout.NORTH);
	}
	
	public class InfoPanel extends JPanel{
		
		public InfoPanel(Player player) {
			int level;
			String userName;
			String lang;
			level = player.getLevel();
			userName = player.getName();
			lang = player.getLanguage();
			
			this.setLayout(new FlowLayout());
			
			JLabel name = new JLabel("플레이어:");
			JLabel userNameInfo = new JLabel("");
			userNameInfo.setText(userName + "  / ");
			JLabel levelInfo = new JLabel("");
			levelInfo.setText("Lv." + Integer.toString(level));
			JLabel langInfo = new JLabel("");
			langInfo.setText(" / " + lang);
			
			name.setFont(new Font("함초롬돋움",Font.BOLD,12));
			userNameInfo.setFont(new Font("함초롬돋움",Font.BOLD,12));
			levelInfo.setFont(new Font("함초롬돋움",Font.BOLD,12));
			langInfo.setFont(new Font("함초롬돋움",Font.BOLD,12));
			
			add(name); 
			add(userNameInfo);
			add(levelInfo);
			add(langInfo);
		}
	
		
	}
	}
	
}