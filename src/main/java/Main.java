
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;


public class Main {
    public static void main(String[] agrs){
        String[]listEmployee = {"1,John,Smith,USA,25","2,Inav,Petrov,RU,23"};

        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv"))) {
            for(String s:listEmployee){
                String[]employee = s.split(",");
            writer.writeNext(employee);}
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            writer.write(json);
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Object>>() {}.getType();

        Gson gson = new Gson();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName)  {
        try{
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            ColumnPositionMappingStrategy<Employee> strategy = new
                            ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> staff = csv.parse();
            return staff;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


}
