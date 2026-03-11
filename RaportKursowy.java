import java.io.*;

public class RaportKursowy {

    public static String[] parseCsvLine(String line) {
        return line.split(",");
    }

    public static int parseIntSafe(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public static double computeFinalScore(int quiz, int lab, int project, int exam) {
        double quizPercent    = quiz    / 20.0;
        double labPercent     = lab     / 40.0;
        double projectPercent = project / 50.0;
        double examPercent    = exam    / 100.0;
        return ((quizPercent + labPercent + projectPercent + examPercent) / 4.0) * 100.0;
    }

    public static String toPassFail(double finalScore) {
        return finalScore >= 50.0 ? "PASS" : "FAIL";
    }

    public static void printSummary(int validRows, int invalidRows,
                                    double classAverage, int passCount, int failCount) {
        System.out.println("\n===== PODSUMOWANIE =====");
        System.out.println("Poprawne wiersze:    " + validRows);
        System.out.println("Niepoprawne wiersze: " + invalidRows);
        System.out.printf("Srednia klasy:       %.2f%%%n", classAverage);
        System.out.println("PASS: " + passCount);
        System.out.println("FAIL: " + failCount);
    }

    public static void main(String[] args) {
        // Ścieżka do pliku - zakładamy że jest w tym samym folderze co program
        String path = "task2_course_results.csv";

        String[] ids   = new String[1000];
        String[] names = new String[1000];
        int[] quiz     = new int[1000];
        int[] lab      = new int[1000];
        int[] project  = new int[1000];
        int[] exam     = new int[1000];

        int validRows   = 0;
        int invalidRows = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // pomijamy nagłówek
                if (line.trim().isEmpty()) continue;

                String[] parts = parseCsvLine(line);
                if (parts.length < 6) {
                    System.out.println("Ostrzezenie: zly wiersz: " + line);
                    invalidRows++;
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                int q = parseIntSafe(parts[2]);
                int l = parseIntSafe(parts[3]);
                int p = parseIntSafe(parts[4]);
                int e = parseIntSafe(parts[5]);

                if (q == -1 || l == -1 || p == -1 || e == -1) {
                    System.out.println("Ostrzezenie: niepoprawne wartosci: " + line);
                    invalidRows++;
                    continue;
                }

                ids[validRows]     = id;
                names[validRows]   = name;
                quiz[validRows]    = q;
                lab[validRows]     = l;
                project[validRows] = p;
                exam[validRows]    = e;
                validRows++;
            }

        } catch (IOException e) {
            System.out.println("Blad odczytu pliku: " + e.getMessage());
            return;
        }

        System.out.println("ID   | Nazwa                  | Wynik  | Status");
        System.out.println("-----|------------------------|--------|-------");

        int passCount  = 0;
        int failCount  = 0;
        double sumScores = 0;

        for (int i = 0; i < validRows; i++) {
            double score  = computeFinalScore(quiz[i], lab[i], project[i], exam[i]);
            String status = toPassFail(score);

            System.out.printf("%-4s | %-22s | %6.2f | %s%n",
                    ids[i], names[i], score, status);

            sumScores += score;
            if (status.equals("PASS")) passCount++;
            else failCount++;
        }

        double classAverage = validRows > 0 ? sumScores / validRows : 0;
        printSummary(validRows, invalidRows, classAverage, passCount, failCount);
    }
}