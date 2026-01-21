package ro.flexbiz.duncascraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrape")
public class ScrapingController {
	
	@Autowired private Scraper scraper;
	
	@GetMapping()
    public ResponseEntity<String> scrape() {
		return new ResponseEntity<>(scraper.findTileInfo(), HttpStatus.OK);
    }
}
