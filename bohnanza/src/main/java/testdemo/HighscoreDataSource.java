package testdemo;

public interface HighscoreDataSource {
	
	public int getNumberOfEntries();
	
	public int getCoinsFor(int index);
	
	public String getPlayerNameFor(int index);

}
