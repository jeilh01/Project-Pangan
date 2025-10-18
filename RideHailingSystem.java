
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;


public class RideHailingSystem {

    
    private static final double INITIAL_KM_COST = 25.00; 
    private static final double ADDITIONAL_KM_RATE = 20.00; 

    
    private static List<Booking> bookings = new ArrayList<>(); // created new array list with variable bookings
 
    private static AtomicInteger nextId = new AtomicInteger(1); // set static id for numbering

   
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    private static final DecimalFormat DISTANCE_FORMAT = new DecimalFormat("0.0");

    // created class record
    record Booking(
        int id,
        String date,
        String time,
        String passengerName,
        String pickupLocation,
        String dropoffLocation,
        double distance,
        double fare
    ) {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        addSampleBookings(); // calls the sample bookings

       // validation of choices
       // set to true for infinite loop
        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim().toLowerCase();

            System.out.println();

            switch (choice) {
                case "a":
                    viewAllBookings();
                    break;
                case "b":
                    bookRide(scanner);
                    break;
                case "c":
                    deleteBooking(scanner);
                    break;
                case "d":
                    generateReport();
                    break;
                case "e":
                    System.out.println("Thank you for using the RIDE-HAILING BOOKING SYSTEM. Goodbye!");
                    scanner.close();
                    return; 
                default:
                    System.out.println("Invalid choice. Please select an option from 'a' to 'e'.");
            }
            
            System.out.println("\nPress ENTER to return to the main menu...");
            try { scanner.nextLine(); } catch (Exception e) {}
        }
    }

    // method for adding the sample booking
    private static void addSampleBookings() {
        
        bookings.add(new Booking(
            nextId.getAndIncrement(),
            "9/10/2024", "10:00 AM", "Benken",
            "Baliwag", "Boss R", 10.0, calculateFare(10.0) 
        ));
        bookings.add(new Booking(
            nextId.getAndIncrement(),
            "9/12/2024", "11:00 AM", "NeBnek",
            "Pulonggubat Baliwag", "Park", 4.8, calculateFare(4.8) 
        ));
        bookings.add(new Booking(
            nextId.getAndIncrement(),
            "9/14/2024", "9:00 AM", "Neburnek",
            "Boss R", "Sogo Baliwag", 9.1, calculateFare(9.1) 
        ));
    }

    // method to display menu
    private static void displayMenu() {
        System.out.println("\n RIDE-HAILING BOOKING SYSTEM - MENU \n");
        System.out.println("A. View All Bookings");
        System.out.println("B. Book a Ride");
        System.out.println("C. Delete a Booking");
        System.out.println("D. Generate Booking Report");
        System.out.println("E. Exit Application \n");
        System.out.print("Enter your choice (A-E): ");
    }

    // method for calculating fare
    private static double calculateFare(double distance) {

        // condition to check if distance inserted is negative
        if (distance <= 0) {
            return 0.0; // return to the computation as 0
        } else if (distance <= 1.0) { // returns normal pricing
           
            return INITIAL_KM_COST;
        } else {
           
            double additionalDistance = distance - 1.0;
            return INITIAL_KM_COST + (ADDITIONAL_KM_RATE * additionalDistance);
        }
    }

    // method for viewing all bookings
        private static void viewAllBookings() {
        System.out.println("VIEW ALL BOOKINGS");

        // condition to check if bookings array list is empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        // table format
        System.out.printf("%-5s %-12s %-10s %-18s %-20s %-20s %-15s %-12s%n",
                        "#", "Date", "Time", "Passenger Name", "Pick-up Location", "Drop-off Location", "Distance (km)", "Fare (PHP)");

        // loop inside the booking list and print out the following objects
        for (Booking b : bookings) {
            System.out.printf("%-5d %-12s %-10s %-18s %-20s %-20s %-15s %-12s%n",
            b.id(), b.date(), b.time(), b.passengerName(), b.pickupLocation(), b.dropoffLocation(),
            DISTANCE_FORMAT.format(b.distance()), CURRENCY_FORMAT.format(b.fare()));
        }
    }

        // method for booking a ride
        private static void bookRide(Scanner scanner) {
        System.out.println("BOOK A RIDE");

        // asks for user name
        System.out.print("a. Enter Passenger Name: ");
        String name = scanner.nextLine();

        // asks for pick up date
        System.out.print("b. Enter Date (e.g., MM/DD/YYYY): ");
        String date = scanner.nextLine();

        // asks for pick up enter time
        System.out.print("c. Enter Time (e.g., HH:MM AM/PM): ");
        String time = scanner.nextLine();

        // asks for user pickup location
        System.out.print("d. Enter Pickup Location: ");
        String pickup = scanner.nextLine();

        // asks for user dropoff
        System.out.print("e. Enter Dropoff Location: ");
        String dropoff = scanner.nextLine();

        double distance = -1;

        // loop condition when user inputs 0 or negative numbers in the input
        while (distance <= 0) {
            System.out.print("f. Enter Distance (km) (e.g., 5.5): ");
            try {
               
                distance = Double.parseDouble(scanner.nextLine());
                if (distance < 0) {
                    System.out.println("Distance cannot be negative. Please try again.");
                    distance = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid distance format. Please enter a valid number.");
            }
        }

        
        double fare = calculateFare(distance);

        Booking newBooking = new Booking(
            nextId.getAndIncrement(), date, time, name, pickup, dropoff, distance, fare
        );

        bookings.add(newBooking); // adds the user inputs to the bookings database

        System.out.println("\n");
        System.out.println("SUCCESS! Ride booked.");
        System.out.println("Booking ID: " + newBooking.id());
        System.out.println("Distance: " + DISTANCE_FORMAT.format(newBooking.distance()) + " km");
        System.out.println("FARE: PHP " + CURRENCY_FORMAT.format(newBooking.fare()));
        
      // conditioning for distance pricing
        if (newBooking.distance() <= 1.0) {
            System.out.println("Formula: PHP " + CURRENCY_FORMAT.format(INITIAL_KM_COST) + " (Fixed for 1 km or less)");
        } else {
            double additionalKm = newBooking.distance() - 1.0;
            System.out.println("Formula: PHP " + CURRENCY_FORMAT.format(INITIAL_KM_COST) + " (1st km) + PHP " +
                               CURRENCY_FORMAT.format(ADDITIONAL_KM_RATE) + "/km * " + DISTANCE_FORMAT.format(additionalKm) + " km (Additional)");
        }
        System.out.println("");
    }

    // method for deleting a book
    private static void deleteBooking(Scanner scanner) {

        // condition to check if booking array list is empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings available to delete.");
            return;
        }

        
        viewAllBookings(); // shows all booking
        System.out.println("\nDELETE A BOOKING");
        System.out.print("Enter the Booking ID (#) to delete: ");

        try {
            int idToDelete = Integer.parseInt(scanner.nextLine()); // converts string to int
            boolean removed = bookings.removeIf(booking -> booking.id() == idToDelete);

            if (removed) {
                System.out.println("Successfully deleted Booking ID #" + idToDelete + ".");
            } else {
                System.out.println("Error: No booking found with ID #" + idToDelete + ".");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numerical Booking ID.");
        }
    }

   // method to generate report
    private static void generateReport() {
        System.out.println("GENERATE BOOKING REPORT");

        // checks if booking array is empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings to report on.");
            return;
        }

        long totalBookings = bookings.size();
        double totalDistance = 0;
        double totalRevenue = 0;

        // loops inside the booking list
        for (Booking b : bookings) {
            totalDistance += b.distance(); // adds to the total distance
            totalRevenue += b.fare(); // adds to the total revenue
        }

       
        System.out.println("\n");
        System.out.println("RIDE-HAILING SUMMARY REPORT");
        System.out.println("");
        System.out.printf("Total Number of Bookings: %d%n", totalBookings);
        System.out.printf("Total Distance Traveled:  %s km%n", DISTANCE_FORMAT.format(totalDistance));
        System.out.printf("Total Revenue Generated:  PHP %s%n", CURRENCY_FORMAT.format(totalRevenue));
        System.out.println("");

        
        System.out.println("NOTE on Fare Calculation:");
        System.out.printf("   PHP %s for the first 1.0 km.%n", CURRENCY_FORMAT.format(INITIAL_KM_COST));
        System.out.printf("   PHP %s for each additional kilometer.%n", CURRENCY_FORMAT.format(ADDITIONAL_KM_RATE));
    }
}
