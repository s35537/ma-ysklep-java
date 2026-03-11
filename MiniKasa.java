import java.util.Scanner;

public class MiniKasa {

    public static double parsePrice(String text) {
        try {
            double val = Double.parseDouble(text);
            if (val <= 0) throw new IllegalArgumentException();
            return val;
        } catch (Exception e) {
            throw new IllegalArgumentException("Nieprawidlowa cena: " + text);
        }
    }

    public static int parseQuantity(String text) {
        try {
            int val = Integer.parseInt(text);
            if (val < 1 || val > 100) throw new IllegalArgumentException();
            return val;
        } catch (Exception e) {
            throw new IllegalArgumentException("Nieprawidlowa ilosc: " + text);
        }
    }

    public static double computeLineTotal(double price, int qty) {
        return price * qty;
    }

    public static double computeDiscount(double subtotal) {
        return subtotal >= 200.0 ? subtotal * 0.05 : 0.0;
    }

    public static void printReceipt(String[] names, int[] quantities,
                                    double[] lineTotals, double subtotal,
                                    double discount, double vat, double finalTotal) {
        System.out.println("\n===== PARAGON =====");
        for (int i = 0; i < names.length; i++) {
            System.out.printf("%-15s x%-3d = %8.2f zl%n",
                names[i], quantities[i], lineTotals[i]);
        }
        System.out.println("-------------------");
        System.out.printf("Subtotal:  %8.2f zl%n", subtotal);
        System.out.printf("Rabat:     %8.2f zl%n", discount);
        System.out.printf("VAT (23%%): %8.2f zl%n", vat);
        System.out.printf("RAZEM:     %8.2f zl%n", finalTotal);
        System.out.printf("Zaokraglone: %d zl%n", (int) finalTotal);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = 0;

        // Pobierz liczbę produktów (1-10)
        while (true) {
            System.out.print("Podaj liczbe produktow (1-10): ");
            try {
                n = Integer.parseInt(sc.nextLine().trim());
                if (n >= 1 && n <= 10) break;
                System.out.println("Blad: podaj liczbe od 1 do 10.");
            } catch (Exception e) {
                System.out.println("Blad: nieprawidlowa wartosc.");
            }
        }

        String[] names = new String[n];
        int[] quantities = new int[n];
        double[] lineTotals = new double[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\n-- Produkt " + (i + 1) + " --");

            System.out.print("Nazwa: ");
            names[i] = sc.nextLine().trim();

            // unitPrice
            double price = 0;
            while (true) {
                System.out.print("Cena (unitPrice): ");
                try {
                    price = parsePrice(sc.nextLine().trim());
                    break;
                } catch (Exception e) {
                    System.out.println("Blad: " + e.getMessage());
                }
            }

            // quantity
            while (true) {
                System.out.print("Ilosc (quantity): ");
                try {
                    quantities[i] = parseQuantity(sc.nextLine().trim());
                    break;
                } catch (Exception e) {
                    System.out.println("Blad: " + e.getMessage());
                }
            }

            lineTotals[i] = computeLineTotal(price, quantities[i]);
        }

        double subtotal = 0;
        for (double lt : lineTotals) subtotal += lt;

        double discount = computeDiscount(subtotal);
        double vat = 0.23 * (subtotal - discount);
        double finalTotal = subtotal - discount + vat;

        printReceipt(names, quantities, lineTotals, subtotal, discount, vat, finalTotal);
        sc.close();
    }
}