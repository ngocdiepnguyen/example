package testsuite.datadriven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class DataDrivenCSVInput {
	private String[] nextLine;
	private List<String[]> valueToInput;
	private List<String[]> allValueMaster;
	private List<String[]> allValueChild;

	File oldFile, newFile;
	String childFileName;
	String childNewFileName = "myChildCSV.csv";
	String masterOldFileName;
	String masterNewFileName = "myCSV.csv";

	boolean readAll = false;

	public boolean isReadAll() {
		return readAll;
	}

	public void setReadAll(boolean readAll) {
		this.readAll = readAll;
	}

	public DataDrivenCSVInput() {
		valueToInput = new ArrayList<String[]>();
		allValueMaster = new ArrayList<String[]>();
		allValueChild = new ArrayList<String[]>();

	}

	public List<String[]> runChildFile(String fileURL, String className,
			int startRow, int endRow) {

		CSVReader reader;
		int countRow = 0;
		int numTest = 1;
		String alreadyRead = "Y";
		childFileName = fileURL;
		try {
			reader = new CSVReader(new FileReader(childFileName));

			while ((nextLine = reader.readNext()) != null) {
				if (countRow >= 1) {
					if (nextLine[2].equals(className)
							&& !nextLine[3].equals(alreadyRead)
							&& countRow >= startRow && countRow <= endRow
							&& numTest == 1) {
						valueToInput.add(nextLine);
						addValueRunToChildList(nextLine);
						numTest++;
					} else {
						allValueChild.add(nextLine);
					}
				} else {
					allValueChild.add(nextLine);
				}
				countRow++;
			}
			reader.close();
			for (String[] s : allValueChild) {
				if (s[3].contains("Y")) {
					setReadAll(true);
				} else {
					setReadAll(false);
				}
			}
			writeUpdateChildFile(allValueChild);
			writeUpdateMasterFile(allValueMaster);
			if (valueToInput.size() == 0) {
				System.out
						.println("Please check the test case's data. Cannot find the suitable test case!");
				return null;
			}

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return valueToInput;
	}

	private void addValueRunToChildList(String[] nextLine) {
		ArrayList<String> lineWithUpdatedColumn = new ArrayList<String>(
				Arrays.asList(nextLine));
		lineWithUpdatedColumn.remove(3);
		lineWithUpdatedColumn.add(3, "Y");
		String[] tmp = new String[lineWithUpdatedColumn.size()];
		tmp = lineWithUpdatedColumn.toArray(tmp);
		allValueChild.add(tmp);

	}

	public void addValueRunToList(String[] nextLine) {
		ArrayList<String> lineWithUpdatedColumn = new ArrayList<String>(
				Arrays.asList(nextLine));
		lineWithUpdatedColumn.remove(2);
		lineWithUpdatedColumn.add(2, "Y");
		String[] tmp = new String[lineWithUpdatedColumn.size()];
		tmp = lineWithUpdatedColumn.toArray(tmp);
		allValueMaster.add(tmp);
	}

	public List<String[]> runMasterFile(String masterURL, String className,
			String testCaseName) {
		CSVReader reader;
		int countRow = 0;
		String startRow = null, endRow = null;
		String alreadyRead = "Y";
		masterOldFileName = masterURL;
		List<String[]> masterValue = new ArrayList<String[]>();
		try {
			reader = new CSVReader(new FileReader(masterOldFileName));
			while ((nextLine = reader.readNext()) != null) {
				// for (int i = 1; i < nextLine.length; i++) {
				if (nextLine[1].equals(className) && countRow > 0
						&& nextLine[3].equals(testCaseName)
						&& !nextLine[2].equals(alreadyRead)) {
					startRow = nextLine[5];
					endRow = nextLine[6];
					masterValue.add(nextLine);
					addValueRunToList(nextLine);
					break;
				} else {
					allValueMaster.add(nextLine);
				}
				// }
				countRow++;
			}
			reader.close();
			if (masterValue.size() == 0) {
				System.out
						.println("Please check the test case's data master. Cannot find the suitable test case!");
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return runChildFile(nextLine[3], className, Integer.parseInt(startRow),
				Integer.parseInt(endRow));
	}

	public void writeUpdateMasterFile(List<String[]> value) {
		CSVWriter writer;
		oldFile = new File(masterOldFileName);
		newFile = new File(masterNewFileName);

		if (isReadAll()) {
			try {
				writer = new CSVWriter(new FileWriter(masterNewFileName), ',',
						CSVWriter.NO_QUOTE_CHARACTER,
						CSVWriter.NO_ESCAPE_CHARACTER);
				writer.writeAll(allValueMaster);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (oldFile.delete()) {
			System.out.println("Delete old csv file successfully!");
		} else {
			System.out
					.println("Something went wrong. Cannot delete old master csv file!");
		}
		if (newFile.renameTo(new File(masterOldFileName))) {
			System.out.println("Rename the new file successfully!");
		} else {
			System.out
					.println("Something went wrong. Cannot rename old master csv file!");
		}
	}

	public void writeUpdateChildFile(List<String[]> value) {
		CSVWriter writer;
		oldFile = new File(childFileName);
		newFile = new File(childNewFileName);
		try {
			writer = new CSVWriter(new FileWriter(childNewFileName), ',',
					CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
			writer.writeAll(allValueChild);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (oldFile.delete()) {
			System.out.println("Delete old csv file successfully!");
		} else {
			System.out
					.println("Something went wrong. Cannot delete old child csv file!");
		}
		if (newFile.renameTo(new File(childFileName))) {
			System.out.println("Rename the new file successfully!");
		} else {
			System.out
					.println("Something went wrong. Cannot rename old child csv file!");
		}
	}

}
