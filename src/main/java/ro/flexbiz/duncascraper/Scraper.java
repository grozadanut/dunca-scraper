package ro.flexbiz.duncascraper;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@Component
public class Scraper {
	public String findTileInfo() {
		try (Playwright playwright = Playwright.create()) {
            final Browser browser = playwright.chromium().launch();
            final Page page = browser.newPage();
            page.navigate("https://playwright.dev/java/docs/intro");
            System.out.println(page.title());
            return page.title();
        }
	}
}
