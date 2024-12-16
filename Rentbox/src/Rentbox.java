import java.io.*;
import java.util.*;
import java.util.LinkedList;
import java.util.Queue;
//TODO: Fix issue where code doesnt recognize existing users
//TODO: Add better lines + spacing to make reading things less cramped
//TODO: ViewKioskTitles, give list of each kiosk numbered 1 - 17 alphabetically, select the number to get the data for that kiosk

//TODO:in user creation, reword "available kiosk locations"


public class Rentbox {

    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Movie> movieInventory = new HashMap<>();
    private static Map<String, VideoGame> videoGameInventory = new HashMap<>();
    private static Map<String, Kiosk> kiosks = new HashMap<>();

    public static void main(String[] args) {
        loadUsers();
        loadWarehouse();
        loadKiosks();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Rentbox! Are you logging in as an admin or a user?");
        String role = scanner.nextLine();

        if (role.equalsIgnoreCase("admin")) {
            adminLogin(scanner);
        } else if (role.equalsIgnoreCase("user")) {
            userLogin(scanner);
        } else {
            System.out.println("Invalid choice. Exiting Rentbox.");
        }
    }

    private static void adminLogin(Scanner scanner) {
        System.out.println("Admin Login:");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("__________________________________");
            System.out.println("Admin logged in successfully!");
            boolean running = true;
            while (running) {
                System.out.println("__________________________________");
                System.out.println("\nAdmin Menu:");
                System.out.println("1. View Warehouse Stock");
                System.out.println("2. View Kiosk Data");
                System.out.println("3. View All Users and Their Rentals");
                System.out.println("4. Exit");
                System.out.println("__________________________________");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewWarehouse();
                        break;
                    case 2:
                        viewKioskData();
                        break;
                    case 3:
                        viewAllUsers();
                        break;
                    case 4:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            }
        } else {
            System.out.println("Invalid admin credentials. Exiting Rentbox.");
        }
    }

    private static void userLogin(Scanner scanner) {
        System.out.println("User Login:");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine().trim(); // Trim username input to remove extra spaces
    
        if (users.containsKey(username)) {
            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();  // Trim password input to remove extra spaces
    
            // Debugging output to check password entry
            //System.out.println("Entered Password: " + password);
    
            if (users.get(username).getPassword().equals(password)) {
                System.out.println("__________________________________");
                System.out.println("User logged in successfully!");
                userMenu(username, scanner);
            } else {
                System.out.println("Invalid password. Exiting Rentbox.");
            }
        } else {
            System.out.print("No account found. Would you like to create a new account? (yes/no): ");
            String createAccount = scanner.nextLine();
            if (createAccount.equalsIgnoreCase("yes")) {
                createNewAccount(username, scanner);
            } else {
                System.out.println("Exiting Rentbox.");
            }
        }
    }
    

    private static void createNewAccount(String username, Scanner scanner) {
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
    
        System.out.println("Please select closest kiosk location: ");
        List<String> kioskLocations = new ArrayList<>(kiosks.keySet());
        Collections.sort(kioskLocations);  
    
        for (int i = 0; i < kioskLocations.size(); i++) {
            String location = kioskLocations.get(i);
            System.out.println((i + 1) + ". " + location);
        }
    
        System.out.print("Enter your location (choose a number): ");
        int locationChoice = Integer.parseInt(scanner.nextLine());
        String location = kioskLocations.get(locationChoice - 1);
    
        User newUser = new User(username, password, location);
        users.put(username, newUser);
        saveUsers();
        System.out.println("Account created successfully at " + location + "!");
        userMenu(username, scanner);
        System.out.println("__________________________________");
    }

    private static void userMenu(String username, Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("__________________________________");
            System.out.println("\nUser Menu:");
            System.out.println("1. Modify Wish List");
            System.out.println("2. View Titles at Kiosk");
            System.out.println("3. Search for a Movie/Game");
            System.out.println("4. View Active Rentals");
            System.out.println("5. View Rental History");
            System.out.println("6. Exit");
            System.out.println("__________________________________");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    modifyWishList(username, scanner);
                    break;
                case 2:
                    viewKioskTitles(scanner);
                    break;
                case 3:
                    searchItem(scanner);
                    break;
                case 4:
                    viewActiveRentals(username);
                    break;
                case 5:
                    viewRentalHistory(username);
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void modifyWishList(String username, Scanner scanner) {
        User user = users.get(username);
        System.out.println("Modify Wish List for " + username);
        boolean adding = true;
        while (adding) {
            System.out.println("1. Add Item to Wish List");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter Item ID (Movie/Game): ");
                    String itemId = scanner.nextLine();
                    if (movieInventory.containsKey(itemId) || videoGameInventory.containsKey(itemId)) {
                        user.addToWishList(itemId);
                        System.out.println("Item added to wish list.");
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;
                case 2:
                    adding = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void viewKioskTitles(Scanner scanner) {
        System.out.print("Enter Kiosk Location: ");
        String location = scanner.nextLine();
        if (kiosks.containsKey(location)) {
            Kiosk kiosk = kiosks.get(location);
            System.out.println("Items available at " + location + ":");
            displayKioskItems(kiosk);
        } else {
            System.out.println("Kiosk not found.");
        }
    }

    private static void searchItem(Scanner scanner) {
        System.out.print("Enter Movie or Game Title: ");
        String title = scanner.nextLine();
        boolean found = false;

        // Search Movies and Video Games in a unified manner
        found = searchInInventory(title, movieInventory, "Movie") || searchInInventory(title, videoGameInventory, "Game");

        if (!found) {
            System.out.println("No items found matching your search.");
        }
    }

    private static boolean searchInInventory(String title, Map<String, ? extends Item> inventory, String itemType) {
        boolean found = false;
        for (Item item : inventory.values()) {
            if (item.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(itemType + " found: " + item.getTitle() + " at " + item.getStock() + " stock.");
                found = true;
            }
        }
        return found;
    }

    private static void viewActiveRentals(String username) {
        User user = users.get(username);
        user.viewActiveRentals();
    }

    private static void viewRentalHistory(String username) {
        User user = users.get(username);
        user.viewRentalHistory();
    }

    private static void viewWarehouse() {
        System.out.println("Warehouse Stock:");
        System.out.println("Movies:");
        movieInventory.forEach((id, movie) -> System.out.println(" - " + movie));
        System.out.println("Videogames:");
        videoGameInventory.forEach((id, game) -> System.out.println(" - " + game));
    }

    private static void viewKioskData() {
        if (kiosks.isEmpty()) {
            System.out.println("No kiosk data available.");
            return;
        }

        kiosks.forEach((location, kiosk) -> {
            System.out.println("Kiosk at " + location + ":");
            displayKioskItems(kiosk);
            System.out.println(); // Print an empty line for better readability
        });
    }

    private static void displayKioskItems(Kiosk kiosk) {
        if (kiosk.movies.isEmpty() && kiosk.videoGames.isEmpty()) {
            System.out.println("No items available.");
        } else {
            if (!kiosk.movies.isEmpty()) {
                System.out.println("Movies available:");
                kiosk.movies.forEach((id, movie) -> {
                    if (movieInventory.containsKey(id)) {
                        Movie warehouseMovie = movieInventory.get(id);
                        System.out.println("  " + warehouseMovie.getTitle() + ": ");
                    } else {
                        System.out.println("  " + movie.getTitle() + " (Details not found in warehouse)");
                    }
                });
            }
            if (!kiosk.videoGames.isEmpty()) {
                System.out.println("Video Games available:");
                kiosk.videoGames.forEach((id, game) -> {
                    if (videoGameInventory.containsKey(id)) {
                        VideoGame warehouseGame = videoGameInventory.get(id);
                        System.out.println("  " + warehouseGame.getTitle() + ": ");
                    } else {
                        System.out.println("  " + game.getTitle() + " (Details not found in warehouse)");
                    }
                });
            }
        }
    }
    

    private static void viewAllUsers() {
        users.forEach((username, user) -> System.out.println(user));
    }

    private static void loadUsers() {
        File userFile = new File("users.txt");
        if (!userFile.exists()) {
            System.out.println("No users file found. Creating a new file...");
            try {
                userFile.createNewFile(); // Create an empty file
            } catch (IOException e) {
                System.out.println("Error creating users file.");
                e.printStackTrace();
            }
            return; // Skip loading users if the file doesn't exist
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Clean up any leading/trailing spaces
                if (line.isEmpty()) continue;  // Skip empty lines
    
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String username = data[0].trim();  // Remove any unwanted spaces
                    String password = data[1].trim();
                    String state = data[2].trim();
                    String city = data[3].trim();
    
                    // Combine state and city to form location
                    String location = city + ", " + state; 
    
                    // Debugging output to check if users are loaded correctly
                    //System.out.println("Loaded user: " + username + ", Location: " + location);
    
                    // Store user in HashMap
                    users.put(username, new User(username, password, location));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file.");
            e.printStackTrace();
        }
    
        // Debugging to show the content of users map after loading
        //System.out.println("Users loaded into the system:");
        //for (String username : users.keySet()) {
        //    System.out.println(username);
        //}
    }
    


    private static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getLocation() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
            e.printStackTrace();
        }
    }

    private static void loadWarehouse() {
        File warehouseFile = new File("warehouse.txt");
        if (!warehouseFile.exists()) {
            System.out.println("No warehouse file found. Creating a new file...");
            try {
                warehouseFile.createNewFile(); // Create an empty file
            } catch (IOException e) {
                System.out.println("Error creating warehouse file.");
                e.printStackTrace();
            }
            return; // Skip loading if file doesn't exist
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(warehouseFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading/trailing spaces
                if (line.isEmpty()) continue; // Skip empty lines
    
                String[] data = line.split(",", -1);  // Split by comma
                if (data.length == 5) {
                    String type = data[0].trim();
                    String id = data[1].trim();
                    String title = data[2].trim().replaceAll("\"", "");  // Remove quotes around title
                    String description = data[3].trim().replaceAll("\"", "");  // Remove quotes around description
                    int stock = Integer.parseInt(data[4].trim());
    
                    if (type.equalsIgnoreCase("Movie")) {
                        Movie movie = new Movie(id, title, description, stock);
                        movieInventory.put(id, movie);
                    } else if (type.equalsIgnoreCase("Game")) {
                        VideoGame game = new VideoGame(id, title, description, stock);
                        videoGameInventory.put(id, game);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading warehouse file.");
            e.printStackTrace();
        }
    
        // Debugging to show loaded movies and games
        //System.out.println("Movies and Games loaded into the system:");
        //movieInventory.forEach((id, movie) -> System.out.println("Movie: " + movie));
        //videoGameInventory.forEach((id, game) -> System.out.println("Game: " + game));
    }
    

    private static void loadKiosks() {
        File kioskFile = new File("kiosks.txt");
        if (!kioskFile.exists()) {
            System.out.println("No kiosks file found. Creating a new file...");
            try {
                kioskFile.createNewFile(); // Create an empty file
            } catch (IOException e) {
                System.out.println("Error creating kiosks file.");
                e.printStackTrace();
            }
            return; // Skip loading kiosks if the file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(kioskFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String kioskId = data[0];
                    String location = data[1] + ", " + data[2]; // Combine state and city as location
                    Kiosk kiosk = new Kiosk(location);

                    // Assuming Movies and Games are listed after location
                    for (int i = 3; i < data.length; i++) {
                        if (data[i].startsWith("Movie")) {
                            kiosk.movies.put(data[i], new Movie(data[i], data[i], "Description", 10));
                        } else if (data[i].startsWith("Game")) {
                            kiosk.videoGames.put(data[i], new VideoGame(data[i], data[i], "Description", 10));
                        }
                    }
                    kiosks.put(location, kiosk);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading kiosks file.");
            e.printStackTrace();
        }
    }

    // Sample user, movie, video game, and kiosk classes for completeness:
    static class Item {
        private String id;
        private String title;
        private String description;
        private int stock;

        public Item(String id, String title, String description, int stock) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.stock = stock;
        }

        public String getTitle() {
            return title;
        }

        public int getStock() {
            return stock;
        }

        @Override
        public String toString() {
            return title + " (" + stock + " in stock)";
        }
    }

    static class User {
        private String username;
        private String password;
        private String location;
        private List<String> wishList = new ArrayList<>();
        private List<String> activeRentals = new ArrayList<>();
        private List<String> rentalHistory = new ArrayList<>();

        public User(String username, String password, String location) {
            this.username = username;
            this.password = password;
            this.location = location;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }

        public String getLocation() {
            return location;
        }

        public void addToWishList(String itemId) {
            wishList.add(itemId);
        }

        public void viewActiveRentals() {
            System.out.println("Active Rentals: " + activeRentals);
        }

        public void viewRentalHistory() {
            System.out.println("Rental History: " + rentalHistory);
        }

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", location='" + location + '\'' +
                    '}';
        }
    }

    static class Movie extends Item {
        public Movie(String id, String title, String description, int stock) {
            super(id, title, description, stock);
        }
    }

    static class VideoGame extends Item {
        public VideoGame(String id, String title, String description, int stock) {
            super(id, title, description, stock);
        }
    }

    static class Kiosk {
        private String location;
        private Map<String, Movie> movies = new HashMap<>();
        private Map<String, VideoGame> videoGames = new HashMap<>();

        public Kiosk(String location) {
            this.location = location;
        }

        public void viewItems() {
            System.out.println("Movies at this Kiosk:");
            movies.forEach((id, movie) -> System.out.println(movie));
            System.out.println("Video Games at this Kiosk:");
            videoGames.forEach((id, game) -> System.out.println(game));
        }
    }
}
