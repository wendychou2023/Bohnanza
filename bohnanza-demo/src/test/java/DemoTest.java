import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.bitbucket.plt.sdp.bohnanza.testdemo.HighscoreDataSource;
import io.bitbucket.plt.sdp.bohnanza.testdemo.HighscoreFormatter;

@ExtendWith(MockitoExtension.class)
public class DemoTest {
	
	@Mock
	HighscoreDataSource source;
	HighscoreFormatter formatter;
	
	@BeforeEach
	public void setupSource() {
		Mockito.lenient().when(source.getNumberOfEntries()).thenReturn(1);
		Mockito.lenient().when(source.getPlayerNameFor(0)).thenReturn("Christoph");
		Mockito.lenient().when(source.getCoinsFor(0)).thenReturn(42);
		
		formatter = new HighscoreFormatter(source);
	}
	
	@Test
	@DisplayName("The information from a Highscore Data Source is correcltly formatted to a String.")
	public void testCorrectFormatting() {
		String formattedText = formatter.formatAll();
		
		assertEquals(
				"=== Bohnanza ===\n"
				+ "- Hall of Fame -\n"
				+ "\n"
				+ "Christoph            42\n"
				+ "",
				formattedText);
	}
	
	@Test
	@DisplayName("When attempting to format an entry not containted in the Highsore Data Source, an appropriate exception is thrown.")
	public void testException() {
		assertThrows(IllegalArgumentException.class, () -> {
			formatter.formatOne(null, 2);
		});
	}

	@Test
	@DisplayName("Demonstration of failing.")
	public void demoFailure() {
		fail();
	}

}
