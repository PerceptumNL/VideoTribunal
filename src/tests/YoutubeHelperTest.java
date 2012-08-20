package tests;

import static org.junit.Assert.assertEquals;
import khan.videos.YoutubeHelper;

import org.junit.Test;

public class YoutubeHelperTest {

	@Test
	public void testTitle() {
		assertEquals("Sifl & Olly - Wizard", YoutubeHelper.getTitle("OoUFWdyJyDk"));
	}

	@Test
	public void testEmbed() {
		assertEquals(false, YoutubeHelper.isEmbeddable("Yl5SLKg7dPk"));
		assertEquals(true, YoutubeHelper.isEmbeddable("3rWFxwo7Phs"));
	}
}
