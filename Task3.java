import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Task3 {
    private static final String FILE_NAME = "contacts.dat";
    private static Map<String, Contact> contacts = new HashMap<>();
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d{10}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+-]+@gmail\\.com$");

    public static void main(String[] args) {
        loadContacts();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Contact Manager!");
        System.out.println("Commands:");
        System.out.println("  add    - Add a new contact");
        System.out.println("  view   - View all contacts or a specific contact");
        System.out.println("  edit   - Edit an existing contact");
        System.out.println("  delete - Delete a contact");
        System.out.println("  save   - Save contacts and exit");

        while (true) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "add":
                    addContact(scanner);
                    break;
                case "view":
                    viewContacts(scanner);
                    break;
                case "edit":
                    editContact(scanner);
                    break;
                case "delete":
                    deleteContact(scanner);
                    break;
                case "save":
                    saveContacts();
                    System.out.println("Contacts saved. Exiting...");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }

    private static void addContact(Scanner scanner) {
        String name;
        String phoneNumber;
        String email;

        System.out.print("Enter contact name: ");
        name = scanner.nextLine();
        if (contacts.containsKey(name)) {
            System.out.println("Contact already exists. Use the edit option to update the contact.");
            return;
        }

        while (true) {
            System.out.print("Enter phone number (10 digits): ");
            phoneNumber = scanner.nextLine();
            if (PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
                break;
            } else {
                System.out.println("Invalid phone number. It must be exactly 10 digits.");
            }
        }

        while (true) {
            System.out.print("Enter email address (must contain @gmail.com): ");
            email = scanner.nextLine();
            if (EMAIL_PATTERN.matcher(email).matches()) {
                break;
            } else {
                System.out.println("Invalid email address. It must contain @gmail.com.");
            }
        }

        contacts.put(name, new Contact(name, phoneNumber, email));
        System.out.println("Contact added successfully.");
    }

    private static void viewContacts(Scanner scanner) {
        System.out.print("Enter 'all' to view all contacts or a specific name to view details: ");
        String input = scanner.nextLine().trim();
        
        if (input.equalsIgnoreCase("all")) {
            if (contacts.isEmpty()) {
                System.out.println("No contacts available.");
            } else {
                System.out.printf("%-20s %-15s %-30s%n", "Name", "Phone", "Email");
                System.out.println("------------------------------------------------------------");
                for (Contact contact : contacts.values()) {
                    System.out.printf("%-20s %-15s %-30s%n", contact.getName(), contact.getPhoneNumber(), contact.getEmail());
                }
            }
        } else {
            Contact contact = contacts.get(input);
            if (contact != null) {
                System.out.println("Details for " + input + ":");
                System.out.println("Name: " + contact.getName());
                System.out.println("Phone: " + contact.getPhoneNumber());
                System.out.println("Email: " + contact.getEmail());
            } else {
                System.out.println("Contact not found.");
            }
        }
    }

    private static void editContact(Scanner scanner) {
        System.out.print("Enter the name of the contact to edit: ");
        String name = scanner.nextLine();
        
        if (contacts.containsKey(name)) {
            String phoneNumber;
            String email;
            
            while (true) {
                System.out.print("Enter new phone number (10 digits): ");
                phoneNumber = scanner.nextLine();
                if (PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
                    break;
                } else {
                    System.out.println("Invalid phone number. It must be exactly 10 digits.");
                }
            }
            
            while (true) {
                System.out.print("Enter new email address (must contain @gmail.com): ");
                email = scanner.nextLine();
                if (EMAIL_PATTERN.matcher(email).matches()) {
                    break;
                } else {
                    System.out.println("Invalid email address. It must contain @gmail.com.");
                }
            }

            contacts.put(name, new Contact(name, phoneNumber, email));
            System.out.println("Contact updated successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void deleteContact(Scanner scanner) {
        System.out.print("Enter the name of the contact to delete: ");
        String name = scanner.nextLine();
        
        if (contacts.remove(name) != null) {
            System.out.println("Contact deleted successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private static void loadContacts() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                contacts = (HashMap<String, Contact>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading contacts: " + e.getMessage());
            }
        }
    }

    // Inner class to represent a Contact
    private static class Contact implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private String phoneNumber;
        private String email;

        public Contact(String name, String phoneNumber, String email) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + email;
        }
    }
}
