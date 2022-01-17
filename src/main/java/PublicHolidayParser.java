import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PublicHolidayParser {

    public static void main(String[] args) {
        displayParsedHolidays();
    }

    public static void displayParsedHolidays() {
        String divider = "=";
        String publicHolidaysUrl = "https://www.commerce.wa.gov.au/labour-relations/public-holidays-western-australia";
        try {
            Document doc = Jsoup.connect(publicHolidaysUrl).get();
            Elements divContainingData = doc.select(".field-name-body");
            Elements yearsOfPublicHolidays = divContainingData.select("th[scope$=col]");
            Elements namesOfPublicHolidays = divContainingData.select("th[scope$=row] strong");
            Elements datesOfHolidays = divContainingData.select("td");
            String years = yearsOfPublicHolidays.stream().map(e -> e.text()).collect(Collectors.joining("->"));
            List<String> holidays = namesOfPublicHolidays.stream().map(e -> years+" | "+e.text()).collect(Collectors.toList());
            List<String> dates = datesOfHolidays.stream().map(e -> e.text()).collect(Collectors.toList());
            int partitionSize = 3;
            List<List<String>> partitions = new ArrayList<>();
            for (int i=0; i<dates.size(); i += partitionSize) {
                partitions.add(dates.subList(i, Math.min(i + partitionSize, dates.size())));
            }
            for (int i = 0; i < holidays.size(); i++) {
                System.out.println(holidays.get(i) + " = " + partitions.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
