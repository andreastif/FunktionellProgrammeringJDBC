package Repository;


import Model.Customer;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Repository {

    static Properties properties = new Properties();


    public static void main(String[] args) throws IOException {
        loadProp();
        Scanner scan = new Scanner(System.in);
        boolean programOpen = true;
        while (programOpen) {
            systemMessage();
            int userChoice = Integer.parseInt(scan.nextLine());
            switch (userChoice) {
                case 0 -> programOpen = false;
                case 1 -> System.out.println(getCustomersByLastName(scan.nextLine()));
                case 2 -> System.out.println(getAllCustomers());
                case 3 ->
                        System.out.println(updateCustomerName(scan.nextLine(), scan.nextLine(), Integer.parseInt(scan.nextLine())));
                case 4 -> System.out.println(deleteCustomer(Integer.parseInt(scan.nextLine())));
                case 5 ->
                        System.out.println(createCustomer(scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextLine()));
                case 6 ->
                        System.out.println(callAddToCart(Integer.parseInt(scan.nextLine()), Integer.parseInt(scan.nextLine()), Integer.parseInt(scan.nextLine())));
                case 9 -> systemMessage();
            }
        }

    }

    public static void loadProp() throws IOException {
        properties.load(new FileInputStream("src/main/resources/dbconn.properties"));
    }

    public static boolean callAddToCart(int customerId, int orderId, int productId) {
        boolean addedToCartSuccessfully = false;

        try (Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"))) {

            //transaction
            conn.setAutoCommit(false);
            CallableStatement statement = conn.prepareCall("CALL addToCart(?,?,?)");
            statement.setInt(1, customerId);
            statement.setInt(2, orderId);
            statement.setInt(3, productId);


            ResultSet rs = statement.executeQuery();
            addedToCartSuccessfully = rs.next();
            System.out.println(addedToCartSuccessfully + "added to cart");

            if (!addedToCartSuccessfully) {
                System.out.println("ERROR: COULD NOT ADD TO CART");
            } else {
                System.out.println("ADDED TO CART SUCCESSFULLY");
                //transaction commit
                conn.commit();
            }

            //transaction
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addedToCartSuccessfully;
    }

    public static boolean createCustomer(String newFirstName, String newLastName, String newEmail, String newAddress, String newCity, String newZipCode) {
        boolean creationSuccessfull = false;

        try (Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"))) {

            //transaction
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Customer (firstName, lastName, email, address, city, zipCode) VALUES (?,?,?,?,?,?)");
            statement.setString(1, newFirstName);
            statement.setString(2, newLastName);
            statement.setString(3, newEmail);
            statement.setString(4, newAddress);
            statement.setString(5, newCity);
            statement.setString(6, newZipCode);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("ERROR: COULD NOT UPDATE");
            } else if (rowsUpdated == 1) {
                System.out.println("IF SATS: Customer CREATED successfully");
                creationSuccessfull = true;
                //transaction commit
                conn.commit();
            } else if (rowsUpdated > 1) {
                System.out.println("Something went wrong, updated more than 1 row. Check database!");
            }

            //transaction
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creationSuccessfull;
    }

    public static boolean deleteCustomer(int idToDelete) {
        boolean deleteSuccessfull = false;

        try (Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"))) {

            //transaction
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Customer WHERE id=?");
            statement.setInt(1, idToDelete);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("ERROR: COULD NOT DELETE");
            } else if (rowsUpdated == 1) {
                System.out.println("IF SATS: Customer DELETED successfully");
                deleteSuccessfull = true;
                //transaction commit
                conn.commit();
            } else if (rowsUpdated > 1) {
                System.out.println("Something went wrong, DELETED more than 1 row. Check database!");
            }

            //transaction
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deleteSuccessfull;
    }

    public static boolean updateCustomerName(String newFirstName, String newLastName, int idToUpdate) {

        //uses transaction AND try-with-resources
        //https://mkyong.com/jdbc/jdbc-transaction-example/
        boolean updateSuccessfull = false;

        try (Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"))) {

            //transaction
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("UPDATE Customer SET firstName=?, lastName=? WHERE id=?");
            statement.setString(1, newFirstName);
            statement.setString(2, newLastName);
            statement.setInt(3, idToUpdate);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("ERROR: COULD NOT UPDATE");
            } else if (rowsUpdated == 1) {
                System.out.println("IF SATS: Name updated successfully");
                updateSuccessfull = true;
                //transaction commit
                conn.commit();
            } else if (rowsUpdated > 1) {
                System.out.println("Something went wrong, UPDATED more than 1 row. Check database!");
            }

            //transaction
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateSuccessfull;
    }

    public static List<Customer> getCustomersByLastName(String searchByLastName) {
        List<Customer> customerList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"))) {


            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Customer WHERE firstName LIKE ?");
            statement.setString(1, "%" + searchByLastName + "%"); // får ej använda fnuttar för att få till LIKE
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String zipCode = resultSet.getString("zipCode");
                String created = resultSet.getString("created");
                String lastUpdate = resultSet.getString("lastUpdate");
                Customer customer = new Customer(id, firstName, lastName, email, address, city, zipCode, created, lastUpdate);
                customerList.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static List<Customer> getAllCustomers() {

        List<Customer> customerList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"))) {

            String sqlSelectTables = "SELECT * FROM Customer";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSelectTables);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String zipCode = resultSet.getString("zipCode");
                String created = resultSet.getString("created");
                String lastUpdate = resultSet.getString("lastUpdate");
                Customer customer = new Customer(id, firstName, lastName, email, address, city, zipCode, created, lastUpdate);
                customerList.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static void systemMessage() {
        System.out.println("""
                =============================================================         
                |         Tryck 0 för att avsluta programmet                |
                |         Tryck 1 för att hämta en kund                     |
                |         Tryck 2 för att hämta alla kunder                 |
                |         Tryck 3 för att uppdatera en kunds namn           |
                |         Tryck 4 för att ta bort en kund via id            |  
                |         Tryck 5 för att lägga till en ny kund             |  
                |         Tryck 6 för att använda stored procedure          |  
                |         Tryck 9 för att visa det här meddelandet igen     |
                =============================================================   
                 """);
    }
}
