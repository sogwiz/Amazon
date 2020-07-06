package com.scott_tigers.oncall.shared;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.google.gson.Gson;
import com.scott_tigers.oncall.bean.Engineer;
import com.scott_tigers.oncall.bean.TT;

public enum EngineerFiles {
    // @formatter:off
    
    ASSIGNED_TICKETS                   ("Assigned Tickets"),
    CIT_CANDIDATES_FROM_POOYA          ("CIT Candidates From Pooya"),
//    CURRENT_CUSTOMER_ISSUE_SCHEDULE    ("Current Customer Issue Schedule", Constants.JSON_EXTENSION) ,
    CURRENT_CUSTOMER_ISSUE_SCHEDULE    ("Current Customer Issue Schedule", Constants.JSON_EXTENSION),
    CUSTOMER_ISSUE_BACKLOG             ("Customer Issue Backlog"),
    CUSTOMER_ISSUE_EMAIL               ("Customer Issue Emails"),
    DAILY_STAND_UP_EMAILS              ("Daily Stand Up Emails"),
    ENGINE_TICKET_DAILY_REVIEW         ("Engine Ticket Daily Review"),
    ENGINEER_ADDS                      ("Engineers to be Added"),
    EXCECUTED_CUSTOMER_ISSUE_SCHEDULES ("Excecuted Customer Issue Schedules", Constants.JSON_EXTENSION),
    FOO                                ("foo"),
    FROM_ONLINE_SCHEDULE               ("From Online Schedule"),
    KEYWORD_POINTS                     ("Keyword Points"),
    LEVELS_FROM_QUIP                   ("Levels From Quip"),
    MASTER_LIST                        ("Engineer Master List"),
    NEW_LEVEL_ENGINEERS                ("New Level Engineers"),
    NEW_SCHEDULE                       ("New Schedule",Constants.JSON_EXTENSION),
    ON_CALL_SCHEDULE                   ("On Call Schedule"),
    ONLINE_SCHEDULE                    ("Online Schedule", Constants.JSON_EXTENSION),
    RESOLVED_TICKET_SUMMARY            ("Resolved Ticket Summary"),
    ROOT_CAUSE_TO_DO                   ("Root Cause To Do"),
    SCHEDULE_CSV                       ("Schedule"),
    TEST                               ("Test"),
    TOP_100_COMPANIES                  ("Top 100 Companies"),
    TT_DOWNLOAD                        ("TT Download"),
    UNAVAILABILITY                     ("Unavailability");
    
    // @formatter:on

    private String fileName;
    private String extension = ".csv";

    EngineerFiles(String fileName) {
	this.fileName = fileName;
    }

    EngineerFiles(String fileName, String extension) {
	this.fileName = fileName;
	this.extension = extension;
    }

    protected String extension() {
	return extension;
    }

    public String getFileName() {
	return "J:\\SupportEngineering\\OnCallData\\" + fileName + extension();
    }

    public List<String> getFirstNames() {
	return Transform.list(readCSV(), x -> x.map(Engineer::getFirstName));
    }

    public void launch() {

	try {
	    System.out.println("getFileName()=" + (getFileName()));
	    Runtime.getRuntime().exec(new String[] {
		    "C:\\Program Files (x86)\\Microsoft Office\\Office16\\EXCEL.EXE",
		    getFileName()
	    });
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public List<Engineer> readCSV() {
	return new CSVReader<Engineer>()
		.inputFile(getFileName())
		.type(Engineer.class)
		.read();
    }

    public <T> List<T> readCSVToPojo(Class<T> pojoClass) {

	return new CSVReader<T>()
		.inputFile(getFileName())
		.type(pojoClass)
		.read();
    }

    public <T> T readJson(Class<T> clazz) {
	try {
	    return new Gson().fromJson(Files.readString(Paths.get(getFileName()), StandardCharsets.US_ASCII), clazz);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private boolean renameFileToTimeStampFile() {
	File file = new File(getFileName());

	if (!file.exists()) {
	    return true;
	}

	var regex = "(.+\\\\)(.+)(\\.)";
	String replacement = "$1Revisions\\\\$2 " + Dates.TIME_STAMP.getFormattedDate() + "$3";

	String timeStampPath = file
		.getPath()
		.replaceAll(regex, replacement);

	boolean renameResult = file.renameTo(new File(timeStampPath));

	if (!renameResult) {
	    System.out.println("Cannot rename file [" + file.getPath() + "] to [" + timeStampPath + "]");
	}
	return renameResult;
    }

    public void replace(List<Engineer> exsitingEngineers) {
	try {
	    if (renameFileToTimeStampFile()) {
		writeToCSVFile(exsitingEngineers);
	    }

	} catch (Exception e) {
	    System.out.println("e=" + (e));
	    e.printStackTrace();
	}
    }

    public <T> void writeCSV(List<T> list, Class<T> pojoClass) {
	Function<CsvMapper, CsvSchema> t1 = mapper -> mapper.schemaFor(pojoClass);
	writeCSV(list, t1);
    }

    private <T> void writeCSV(List<T> list, Function<CsvMapper, CsvSchema> schemaMaker) {
	try {
	    CsvMapper mapper = new CsvMapper();
	    CsvSchema schema = schemaMaker.apply(mapper);
	    mapper.configure(Feature.IGNORE_UNKNOWN, true);

	    schema = schema.withColumnSeparator(',').withHeader();

	    OutputStreamWriter writerOutputStream = new OutputStreamWriter(
		    new BufferedOutputStream(
			    new FileOutputStream(
				    new File(getFileName())),
			    1024),
		    "UTF-8");

	    mapper
		    .writer(schema)
		    .writeValue(writerOutputStream, list);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void writeCSV(List<TT> list, List<String> columnNames) {
	Function<CsvMapper, CsvSchema> t1 = mapper -> {
	    Builder builder = CsvSchema.builder();
	    columnNames.stream().forEach(builder::addColumn);
	    return builder.build();
	};
	writeCSV(list, t1);
    }

    public <T> void writeJson(T object) {
	try {
	    if (renameFileToTimeStampFile()) {
		writeJsonFile(object);
	    }

	} catch (Exception e) {
	    System.out.println("e=" + (e));
	    e.printStackTrace();
	}
    }

    <T> void writeJsonFile(T object) {
	try {
	    writeText(Json.getJsonString(object));
	} catch (IOException e) {
	    System.out.println("Cannot write to file " + getFileName());
	    e.printStackTrace();
	}
    }

    public void writeLines(List<String> lines) {
	try {
	    FileUtils.writeLines(new File(getFileName()), lines);
	} catch (IOException e) {
	    System.out.println("e=" + (e));
	    e.printStackTrace();
	}
    }

    public void writeText(String text) throws IOException {
	Files.write(Paths.get(getFileName()), text.getBytes());
    }

    private void writeToCSVFile(List<Engineer> exsitingEngineers) throws UnsupportedEncodingException,
	    FileNotFoundException, IOException, JsonGenerationException, JsonMappingException {
	CsvMapper mapper = new CsvMapper();
	CsvSchema schema = mapper.schemaFor(Engineer.class);
	schema = schema.withColumnSeparator(',').withHeader();

	OutputStreamWriter writerOutputStream = new OutputStreamWriter(
		new BufferedOutputStream(
			new FileOutputStream(
				new File(getFileName())),
			1024),
		"UTF-8");

	mapper
		.writer(schema)
		.writeValue(writerOutputStream, exsitingEngineers);
    }

}
