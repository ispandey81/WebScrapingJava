package parsing;

import entity.HolidayDetails;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.HibernateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PublicHolidayParser {
    public static final String PUBLIC_HOLIDAYS_URL = "https://www.commerce.wa.gov.au/labour-relations/public-holidays-western-australia";

    // use the connectAndReturnResult method if you just want to print to the console
    public static void main(String[] args) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Document doc = Jsoup.connect(PUBLIC_HOLIDAYS_URL).get();
            List<String> result = parsedHolidays(doc);
            // start a transaction
            transaction = session.beginTransaction();
            // save the holidaydetails objects
            for (String yearsAndHolidays: result) {
                session.save(new HolidayDetails(yearsAndHolidays));
            }
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List <HolidayDetails> holidayDetails = session.createQuery("from HolidayDetails", HolidayDetails.class).list();
            holidayDetails.forEach(h -> System.out.println(h.getHoliday()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void connectAndReturnResult() {
        try {
            Document doc = Jsoup.connect(PUBLIC_HOLIDAYS_URL).get();
            List<String> result = parsedHolidays(doc);
            for (String yearsAndHolidays: result) {
                System.out.println(yearsAndHolidays);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> parsedHolidays(Document document) {
        Elements divContainingData = document.select(".field-name-body");
        Elements yearsOfPublicHolidays = divContainingData.select("th[scope$=col]");
        Elements namesOfPublicHolidays = divContainingData.select("th[scope$=row] strong");
        Elements datesOfHolidays = divContainingData.select("td");
        String years = yearsOfPublicHolidays.stream().map(e -> e.text()).collect(Collectors.joining("->"));
        List<String> holidays = namesOfPublicHolidays.stream().map(e -> years+" | "+e.text()).toList();
        List<String> dates = datesOfHolidays.stream().map(e -> e.text()).toList();
        int partitionSize = 3;
        List<List<String>> partitions = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i=0; i<dates.size(); i += partitionSize) {
            partitions.add(dates.subList(i, Math.min(i + partitionSize, dates.size())));
        }
        for (int i = 0; i < holidays.size(); i++) {
            result.add(holidays.get(i) + " = " + partitions.get(i));
        }
        return result;
    }
}
