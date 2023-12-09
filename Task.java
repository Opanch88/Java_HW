import java.io.*;
import java.util.*;

public class Task {
    private static final String FILE_NAME = "phonebook.txt";

    public static void main(String[] args) {
        Map<String, List<String>> phoneBook = loadPhoneBookFromFile();

        Scanner input = new Scanner(System.in);
        int menuNumber;

        do {
            System.out.println("""
                    Меню:
                     1 - Показать контакты
                     2 - Добавить контакт
                     3 - Удалить контакт
                     4 - Сохранить и выйти
                    """);
            System.out.print("Введите номер пункта: ");
            menuNumber = input.nextInt();

            switch (menuNumber) {
                case 1:
                    printSortedPhoneBook(phoneBook);
                    break;
                case 2:
                    addContactFromInput(phoneBook);
                    break;
                case 3:
                    deleteContactFromInput(phoneBook);
                    break;
                case 4:
                    savePhoneBookToFile(phoneBook);
                    System.out.println("Данные сохранены. Пока!");
                    break;
                default:
                    System.out.println("Вы ввели неверный номер пункта!");
                    break;
            }
        } while (menuNumber != 4);

        input.close();
    }

    private static void addContact(Map<String, List<String>> phoneBook, String name, String phoneNumber) {
        // Если контакт с таким именем уже существует,
        // добавляем новый телефон к существующему списку
        phoneBook.computeIfAbsent(name, k -> new ArrayList<>()).add(phoneNumber);
    }

    private static void addContactFromInput(Map<String, List<String>> phoneBook) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя контакта: ");
        String name = scanner.nextLine();

        System.out.print("Введите номер телефона: ");
        String phoneNumber = scanner.nextLine();

        addContact(phoneBook, name, phoneNumber);
        System.out.println("Контакт успешно добавлен!");
    }

    private static void deleteContactFromInput(Map<String, List<String>> phoneBook) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя контакта для удаления: ");
        String nameToDelete = scanner.nextLine();

        if (phoneBook.containsKey(nameToDelete)) {
            phoneBook.remove(nameToDelete);
            System.out.println("Контакт успешно удален!");
        } else {
            System.out.println("Контакт с именем " + nameToDelete + " не найден.");
        }
    }

    private static void printSortedPhoneBook(Map<String, List<String>> phoneBook) {
        List<Map.Entry<String, List<String>>> entries = new ArrayList<>(phoneBook.entrySet());

        entries.sort((entry1, entry2) -> {
            List<String> phoneNumbers1 = entry1.getValue();
            List<String> phoneNumbers2 = entry2.getValue();

            // Сравниваем первые номера телефона в списках
            String phone1 = phoneNumbers1.isEmpty() ? "" : phoneNumbers1.get(0);
            String phone2 = phoneNumbers2.isEmpty() ? "" : phoneNumbers2.get(0);

            return phone1.compareTo(phone2);
        });

        for (Map.Entry<String, List<String>> entry : entries) {
            String name = entry.getKey();
            List<String> phoneNumbers = entry.getValue();
            System.out.println(name + ": " + phoneNumbers);
        }
    }

    private static Map<String, List<String>> loadPhoneBookFromFile() {
        Map<String, List<String>> phoneBook = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0];
                String[] phoneNumbers = parts[1].split(",");
                phoneBook.computeIfAbsent(name, k -> new ArrayList<>()).addAll(Arrays.asList(phoneNumbers));
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        return phoneBook;
    }

    private static void savePhoneBookToFile(Map<String, List<String>> phoneBook) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, List<String>> entry : phoneBook.entrySet()) {
                String name = entry.getKey();
                List<String> phoneNumbers = entry.getValue();
                writer.write(name + ":" + String.join(",", phoneNumbers));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }
}
