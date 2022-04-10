package poms;

import containers.TestSessionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SharedMethods {
    private static final Logger log = LogManager.getLogger(SharedMethods.class);

    public static void logResults(List<Map<String, String>> results) {
        List<String> keywords = Arrays.stream(TestSessionData.searchKeyword.split(" "))
                .map(el -> el.toLowerCase(Locale.ROOT)).toList();

        List<Map<String, String>> resultsContainingKeyword = new ArrayList<>();
        List<Map<String, String>> resultsNotContainingKeyword = new ArrayList<>();
        // for (List<Map<String, String>> searchEngineResults : TestSessionData.searchResults) {
        for (Map<String, String> result : results) {
            if (keywords.stream().anyMatch(result.get("websiteTitle").toLowerCase(Locale.ROOT)::contains) ||
                    keywords.stream().anyMatch(result.get("url").toLowerCase(Locale.ROOT)::contains) ||
                    keywords.stream().anyMatch(result.getOrDefault("shortDescription", ".").toLowerCase(Locale.ROOT)::contains)) {
                resultsContainingKeyword.add(result);
            } else {
                resultsNotContainingKeyword.add(result);
            }
        }
        // }

        log.info("----------------------- Results containing: {} -----------------------", TestSessionData.searchKeyword);
        resultsContainingKeyword.forEach(el -> {
            log.info("Website Title: {}", el.get("websiteTitle"));
            log.info("Website Url: {}", el.get("url"));
            log.info("Short Description: {}", el.getOrDefault("shortDescription", "No description available."));
        });

        log.info("----------------------- Results not containing: {} -----------------------", TestSessionData.searchKeyword);
        resultsNotContainingKeyword.forEach(el -> {
            log.info("Website Title: {}", el.get("websiteTitle"));
            log.info("Website Url: {}", el.get("url"));
            log.info("Short Description: {}", el.getOrDefault("shortDescription", "No description available."));
        });
    }

    public static void logCommonResults() {
        List<Map<String, String>> commonResults = new ArrayList<>();
        List<String> allUrls = new ArrayList<>();

        for (int j = 0; j < TestSessionData.searchResults.size() - 1; j++) {
            for (int i = 0; i < TestSessionData.searchResults.get(j).size() - 1; i++) {
                allUrls.add(TestSessionData.searchResults.get(j).get(i).get("url"));
            }
        }

        for (int j = 0; j < TestSessionData.searchResults.size() - 1; j++) {
            for (int i = 0; i < TestSessionData.searchResults.get(j).size() - 1; i++) {
                if (allUrls.get(i).contains(TestSessionData.searchResults.get(j).get(i).get("url"))) {
                    commonResults.add(TestSessionData.searchResults.get(j).get(i));
                }
            }
        }

        if(commonResults.size() > 0) log.info("============================== Common results ==============================");
        for (Map<String, String> result : commonResults) {
            log.info("Website URL: {}", result.get("url"));
        }

    }
}
