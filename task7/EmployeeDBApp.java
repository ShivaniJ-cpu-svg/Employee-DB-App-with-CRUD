import java.sql.*;
import java.util.Scanner;

public class EmployeeDBApp {
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "shivani"; // Change as needed
    private static final String PASSWORD = "Shivani@123A"; // Change as needed

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\nEmployee Management:");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 : addEmployee(conn, sc); break;
                    case 2 : viewEmployees(conn); break;
                    case 3 : updateEmployee(conn, sc); break;
                    case 4 : deleteEmployee(conn, sc); break;
                    case 5 : {
                        System.out.println("Exiting...");
                        return;
                    }
                    default : System.out.println("Invalid choice!"); break;
                } 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
	sc.nextLine(); // consume newline
	String name = sc.nextLine();
	System.out.print("Enter email: ");
	String email = sc.nextLine();
	System.out.print("Enter department: ");
	String department = sc.nextLine();
	System.out.print("Enter salary: ");
	double salary = sc.nextDouble();


       String sql = "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            	ps.setString(1, name);
		ps.setString(2, email);
		ps.setString(3, department);
		ps.setDouble(4, salary);
		ps.executeUpdate();
		System.out.println("Employee added.");
        }
    }

    private static void viewEmployees(Connection conn) throws SQLException {
    String sql = "SELECT * FROM employees";
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.printf("%-5s %-20s %-30s %-20s %-10s%n", "ID", "Name", "Email", "Department", "Salary");
        while (rs.next()) {
            System.out.printf("%-5d %-20s %-30s %-20s %-10.2f%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    rs.getDouble("salary"));
        }
    }
}


    private static void updateEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new email: ");
	String email = sc.nextLine();
        System.out.print("Enter new department: ");
        String  department= sc.nextLine();
        System.out.print("Enter new salary: ");
        double salary = sc.nextDouble();
        

        String sql = "UPDATE employees SET name = ?, email = ?, department = ?, salary = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
	    ps.setString(2, email);
	    ps.setString(3, department);
	    ps.setDouble(4, salary);
	    ps.setInt(5, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Employee updated." : "Employee not found.");
        }
    }

    private static void deleteEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Employee deleted." : "Employee not found.");
        }
    }
}

