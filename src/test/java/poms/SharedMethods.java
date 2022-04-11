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
        for (Map<String, String> result : results) {
            if (keywords.stream().anyMatch(result.get("websiteTitle").toLowerCase(Locale.ROOT)::contains) ||
                    keywords.stream().anyMatch(result.get("url").toLowerCase(Locale.ROOT)::contains) ||
                    keywords.stream().anyMatch(result.getOrDefault("shortDescription", ".").toLowerCase(Locale.ROOT)::contains)) {
                resultsContainingKeyword.add(result);
            } else {
                resultsNotContainingKeyword.add(result);
            }
        }

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
        List<Map<String, String>> commonResultsOfSameKeyword = new ArrayList<>();
        Set<String> uniqueUrls = new HashSet<>();
        List<Map<String, String>> duplicateResults = new ArrayList<>();

        Set<String> keywords = new HashSet<>();
        List<String> duplicateKeywords = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : TestSessionData.searchKeywordsBySearchEngine.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (!keywords.add(keyword)) {
                    duplicateKeywords.add(keyword);
                }
            }
        }

        for (int j = 0; j < TestSessionData.searchResults.size(); j++) {
            for (int i = 0; i < TestSessionData.searchResults.get(j).size(); i++) {
                if (duplicateKeywords.contains(TestSessionData.searchResults.get(j).get(i).get("keyword"))) {
                    commonResultsOfSameKeyword.add(TestSessionData.searchResults.get(j).get(i));
                }
            }
        }

        for (Map<String, String> value : commonResultsOfSameKeyword) {
            if (!uniqueUrls.add(value.get("url"))) {
                duplicateResults.add(value);
            }
        }

        if(duplicateResults.size() > 0) {
            log.info("============================== Common results ==============================");
            for (Map<String, String> value : duplicateResults) {
                log.info("Keyword: {}", value.get("keyword"));
                log.info("Website Title: {}", value.get("websiteTitle"));
                log.info("Website Url: {}", value.get("url"));
                log.info("Short Description: {}", value.getOrDefault("shortDescription", "No description available."));
            }
        }

    }
}
