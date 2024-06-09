package io.bitbucket.plt.sdp.bohnanza.testdemo;

public class HighscoreFormatter {

	private HighscoreDataSource source;

	public HighscoreFormatter(HighscoreDataSource source) {
		this.source = source;
	}
	
	public String formatAll() {
		StringBuilder result = new StringBuilder();
		
		result.append("=== Bohnanza ===\n");
		result.append("- Hall of Fame -\n\n");
		
		for (int i = 0; i < source.getNumberOfEntries(); i++) {
			formatOne(result, i);
		}
		return result.toString();
	}

	public void formatOne(StringBuilder result, int i) {
		if (i >= source.getNumberOfEntries())
			throw new IllegalArgumentException("There is no highscore entry at index " + i);
		
		String name = source.getPlayerNameFor(i);
		String coins = Integer.toString(source.getCoinsFor(i));
		
		result.append(name).append(" ".repeat(20 - name.length() + 3 - coins.length())).append(coins).append("\n");
	}
	
}
