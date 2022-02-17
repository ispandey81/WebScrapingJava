import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import parsing.PublicHolidayParser;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PublichHolidayParserTest {

    @Test
    public void testDisplayParsedInfo() {
        try {
            Document doc = Jsoup.connect(PublicHolidayParser.PUBLIC_HOLIDAYS_URL).get();
            List<String> result = PublicHolidayParser.parsedHolidays(doc);
            assertNotNull(result);
            assertEquals(11, result.size(), "Expected array size is equal to the actual array size");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
