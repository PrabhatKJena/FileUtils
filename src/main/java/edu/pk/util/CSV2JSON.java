package edu.pk.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by prabhat on 19/8/16.
 */
public class CSV2JSON {
    public static void main(String[] args) throws Exception {
        File input = new File("/home/prabhat/Documents/emp.csv");

        // Reading CSV data as list of Objects
        List<Employee> data = readObjectsFromCsv(input);

        // Writing JSON data to string writer
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, data);
        System.out.println(stringWriter.toString()); // JSON output


    }

    public static List<Employee> readObjectsFromCsv(File file) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema bootstrap = csvMapper.schemaFor(Employee.class).withHeader();
        MappingIterator<Employee> mappingIterator = csvMapper.readerFor(Employee.class).with(bootstrap).readValues(file);
        return mappingIterator.readAll();
    }

}
