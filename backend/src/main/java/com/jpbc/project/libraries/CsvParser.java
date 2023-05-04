package com.jpbc.project.libraries;

import com.jpbc.project.models.HistoricalData;
import com.jpbc.project.models.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CsvParser {
    public List<HistoricalData> read(MultipartFile file, Project project) {
        List<String[]> result = new ArrayList<>();
        List<HistoricalData> dataObjectListToReturn = new ArrayList<>();


        try {
            String line = null;
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("M/d/yy"))
                    .appendOptional(DateTimeFormatter.ofPattern("M/dd/yy"))
                    .appendOptional(DateTimeFormatter.ofPattern("MM/d/yy"))
                    .toFormatter();
            InputStream inputStream = file.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));

            int historyCount = 0;
            // Iterate through CSV and fill values into result ArrayList
            while ((line = bufferReader.readLine()) != null && historyCount < project.getSprintHistoryToEvaluate()) {
                result.add(new String[4]);
                String[] newLine = line.split(",");

                // Copy values from newLine to result
                System.arraycopy(newLine, 0, result.get(result.size() - 1), 0, newLine.length);
                historyCount++;
            }

            // Convert each row to HistoricalData obj
            result.forEach(x -> {
                HistoricalData entry = new HistoricalData();
                Long issueId = Long.parseLong(x[0]);
                LocalDate creationDateTime = null;
                LocalDate resolvedDateTime = null;
                creationDateTime = LocalDate.parse(x[1].split(" ")[0], dateTimeFormatter);
                resolvedDateTime = LocalDate.parse(x[2].split(" ")[0], dateTimeFormatter);

                // Change any null values for stories completed to 0 before parsing to Integer
                if (x[3] == null) {
                    entry.setStoriesPoints(0);
                } else {
                    Integer storiesCompleted = Integer.parseInt(x[3]);
                    entry.setStoriesPoints(storiesCompleted);
                }

                entry.setIssueId(issueId);
                entry.setCreationDate(creationDateTime);
                entry.setResolvedDate(resolvedDateTime);
                entry.setProject(project);
                dataObjectListToReturn.add(entry);
            });
        } catch (Exception e) {
            log.error("Error parsing CSV", e);
        }

        return dataObjectListToReturn;
    }
}
