java import java.io.*;
import java.util.*;

class Record {
    private List<String> fields;

    public Record(List<String> fields) {
        this.fields = new ArrayList<>(fields);
    }

    public String getField(int index) {
        if (index >= 0 && index < fields.size())
            return fields.get(index);
        return "";
    }

    public void display() {
        for (String field : fields) {
            System.out.print(field + "\t");
        }
        System.out.println();
    }
}

public class dataset_analyser {

    static List<Record> dataset = new ArrayList<>();
    static List<String> headers = new ArrayList<>();

    public static void loadCSV(String fileName) {
        dataset.clear();
        headers.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");

                if (firstLine) {
                    headers = new ArrayList<>(Arrays.asList(values));
                    firstLine = false;
                } else {
                    dataset.add(new Record(Arrays.asList(values)));
                }
            }

            System.out.println("Dataset loaded successfully.");

        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public static void displayData() {

        if (dataset.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        for (String header : headers) {
            System.out.print(header + "\t");
        }
        System.out.println();

        for (Record record : dataset) {
            record.display();
        }
    }

    public static void searchByColumn(int columnIndex, String value) {

        if (dataset.isEmpty()) {
            System.out.println("No data loaded.");
            return;
        }

        if (columnIndex < 0 || columnIndex >= headers.size()) {
            System.out.println("Invalid column index.");
            return;
        }

        boolean found = false;

        for (Record record : dataset) {
            if (record.getField(columnIndex).equalsIgnoreCase(value)) {
                record.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching record found.");
        }
    }

    public static void calculateAverage(int columnIndex) {

        if (dataset.isEmpty()) {
            System.out.println("No data loaded.");
            return;
        }

        if (columnIndex < 0 || columnIndex >= headers.size()) {
            System.out.println("Invalid column index.");
            return;
        }

        double sum = 0;
        int count = 0;

        for (Record record : dataset) {
            try {
                sum += Double.parseDouble(record.getField(columnIndex));
                count++;
            } catch (NumberFormatException ignored) {}
        }

        if (count > 0)
            System.out.println("Average: " + (sum / count));
        else
            System.out.println("Column is not numeric.");
    }

    public static void sortByColumn(int columnIndex) {

        if (dataset.isEmpty()) {
            System.out.println("No data loaded.");
            return;
        }

        if (columnIndex < 0 || columnIndex >= headers.size()) {
            System.out.println("Invalid column index.");
            return;
        }

        dataset.sort(Comparator.comparing(r -> r.getField(columnIndex)));

        System.out.println("Data sorted successfully.");
        displayData();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter CSV file name: ");
        String fileName = sc.nextLine();

        loadCSV(fileName);

        int choice;

        do {
            System.out.println("\n===== Dataset Analyzer =====");
            System.out.println("0. Load New CSV");
            System.out.println("1. Display Data");
            System.out.println("2. Search by Column");
            System.out.println("3. Calculate Average");
            System.out.println("4. Sort by Column");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 0:
                    sc.nextLine();
                    System.out.print("Enter CSV file name: ");
                    fileName = sc.nextLine();
                    loadCSV(fileName);
                    break;

                case 1:
                    displayData();
                    break;

                case 2:
                    System.out.print("Enter column index: ");
                    int col = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter value: ");
                    String val = sc.nextLine();
                    searchByColumn(col, val);
                    break;

                case 3:
                    System.out.print("Enter numeric column index: ");
                    int numCol = sc.nextInt();
                    calculateAverage(numCol);
                    break;

                case 4:
                    System.out.print("Enter column index to sort: ");
                    int sortCol = sc.nextInt();
                    sortByColumn(sortCol);
                    break;

                case 5:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}