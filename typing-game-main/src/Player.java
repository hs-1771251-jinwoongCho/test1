public class Player {
	private String name;
	private int level;
	private int score;
	private String language;
	
	public Player() {
		
	}
	
	public Player (String name, int level, int score, String language) {
		this.name = name;
		this.level = level;
		this.score = score;
		this.language = language;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
}