package ro.flexbiz.duncascraper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrape")
public class ScrapingController {
	
	@Autowired private Scraper scraper;
	
	@GetMapping("/{tileName}")
    public ResponseEntity<List<Map<String, String>>> scrape(@PathVariable final String tileName) {
		return new ResponseEntity<>(scraper.findTileInfo(tileName), HttpStatus.OK);
    }
}
