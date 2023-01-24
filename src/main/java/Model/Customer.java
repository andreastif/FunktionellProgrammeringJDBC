package Model;

public class Customer {

    int id;
    String firstName;
    String lastName;
    String email;
    String address;
    String city;
    String zipCode;
    String created;
    String lastUpdate;

    public Customer(int id, String firstName, String lastName, String email, String address, String city, String zipCode, String created, String lastUpdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.created = created;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "\n\nCustomer{" +
                "id='" + id + '\n' +
                "firstName='" + firstName + '\n' +
                "lastName='" + lastName + '\n' +
                "email='" + email + '\n' +
                "address='" + address + '\n' +
                "city='" + city + '\n' +
                "zipCode='" + zipCode + '\n' +
                "created='" + created + '\n' +
                "lastUpdate='" + lastUpdate+'}';
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCreated() {
        return created;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
