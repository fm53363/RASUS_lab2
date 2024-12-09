package hr.fer.tel.rassus.stupidudp.repo;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import hr.fer.tel.rassus.stupidudp.model.Reading;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySensorRepo {
    private static final String CSV_FILE_NAME = "readings[6].csv";
    private static MySensorRepo INSTANCE;

    private List<Reading> sensorReadings;
    private int size;


    private MySensorRepo() throws IOException, CsvException {
        this.sensorReadings = new ArrayList<>();
        readFromCsv();
    }

    public int getSize() {
        return sensorReadings.size();
    }

    private void readFromCsv() throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(CSV_FILE_NAME));
        List<String[]> records = reader.readAll();

        Iterator<String[]> iterator = records.iterator();
        iterator.next(); // skip first line for header names
        while (iterator.hasNext()) {
            String[] record = iterator.next();
            Reading reading = new Reading();
            int no2 = (record[4].isEmpty()) ? 0 : Integer.parseInt(record[4]);
            reading.setNO2(no2);
            this.sensorReadings.add(reading);
        }
        reader.close();
    }

    public Reading getReading(int id) {
        return sensorReadings.get(id);
    }

    public static MySensorRepo getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new MySensorRepo();
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        }
        return INSTANCE;
    }


    private static Double parseDoubleOrNull(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }



    public static void main(String[] args) {
        try {
            var repo = new MySensorRepo();
            repo.sensorReadings.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
