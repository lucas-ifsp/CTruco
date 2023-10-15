package com.bueno.application.standalone;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class UserCSV {

    void writeCSV(String botName, List<String> otherBots, List<Double> results){
        String fileName = botName + ".csv";
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter("exemplo.csv"), CSVFormat.DEFAULT)) {
            if(!CSVHasHeader(fileName))
                csvPrinter.printRecord(otherBots);
            csvPrinter.printRecord(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readCSV(String botName){
        String fileName = botName + ".csv";
        try (CSVParser csvParser = CSVParser.parse(new File(fileName), Charset.defaultCharset(), CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvParser) {
                String nome = csvRecord.get(0);
                int idade = Integer.parseInt(csvRecord.get(1));
                String cidade = csvRecord.get(2);

                System.out.println("Nome: " + nome + ", Idade: " + idade + ", Cidade: " + cidade);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean CSVHasHeader(String fileName) throws IOException {
        try (CSVParser csvParser = CSVParser.parse(new FileReader(fileName), CSVFormat.DEFAULT)) {
            int numberOfRecords = csvParser.getRecords().size();
            return numberOfRecords <= 1;
        }
    }


}
