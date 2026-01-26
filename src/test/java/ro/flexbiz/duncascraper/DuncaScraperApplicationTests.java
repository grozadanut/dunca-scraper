package ro.flexbiz.duncascraper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DuncaScraperApplicationTests {

	@Autowired private Scraper scraper;
	
	@Test
	void simpleCases() {
		assertThat(scraper.findTileInfo("asd123asd1")).contains(Map.of("error", "Produsul asd123asd1 nu a fost gasit!"));
		assertThat(scraper.findTileInfo("smoge aqua")).hasSizeGreaterThan(1);
	}
}
