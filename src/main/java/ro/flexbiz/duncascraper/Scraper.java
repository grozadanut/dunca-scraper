package ro.flexbiz.duncascraper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.WaitForOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

@Component
public class Scraper {
	@Value("${dunca.url}") private String url;
	@Value("${dunca.username}") private String username;
	@Value("${dunca.password}") private String password;
	
	public List<Map<String, String>> findTileInfo(String tileName) {
		try (Playwright playwright = Playwright.create()) {
            final Browser browser = playwright.chromium().launch();
            final Page page = browser.newPage();
            page.navigate(url);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username")).fill(username);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill(password);
            page.getByRole(AriaRole.BUTTON,
            		new Page.GetByRoleOptions().setName(
            				Pattern.compile("login", Pattern.CASE_INSENSITIVE)))
            .click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Lista Stocuri")).click();
            page.locator("#sotext-1084-inputEl").fill("*"+tileName+"*");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Browse")).click();
            
            page.locator("#ext-gen1247").getByText("Loading...")
            .waitFor(new WaitForOptions().setState(WaitForSelectorState.DETACHED));
            
            final boolean nothingFound = page.getByText("There is no data to display.").count() > 0;
            
            if (nothingFound)
            	return List.of(Map.of("error", MessageFormat.format("Produsul {0} nu a fost gasit!", tileName))); //$NON-NLS-1$

            final Locator rows = page.locator("#gridview-1127 table tbody tr");
            final int rowCount = rows.count();
            final List<Map<String, String>> result = new ArrayList<>();

            for (int i = 0; i < rowCount; i++) {
            	final Locator row = rows.nth(i);
            	final Locator cells = row.locator("td");
            	
            	if (cells.nth(0).count() <= 0)
            		continue;

            	final String name = cells.nth(1).textContent();
            	final String stockOradea = cells.nth(2).textContent();
            	final String stockBucuresti = cells.nth(3).textContent();
            	final String stockIasi = cells.nth(4).textContent();

            	result.add(Map.of("name", name, "oradea", stockOradea, "bucuresti", stockBucuresti, "iasi", stockIasi));
            }

            return result;
        }
	}
}
