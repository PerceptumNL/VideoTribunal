package tests;

import static org.junit.Assert.assertEquals;
import khan.videos.YoutubeHelper;

import org.junit.Test;

public class YoutubeHelperTest {

	@Test
	public void testTitle() {
		assertEquals(YoutubeHelper.getTitle("OoUFWdyJyDk"), "Sifl & Olly - Wizard");
	}

}
