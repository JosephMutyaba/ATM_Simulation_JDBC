package atm;

import java.sql.*;
import java.util.Scanner;

public class ATM {
    static Scanner scanner= new Scanner(System.in);
    public static void main(String[] args) {
        try{
            Scanner scanner= new Scanner(System.in);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_service", "root", "");
            Statement statement = connection.createStatement();
            
            // creating customer table
//            String customer_table="CREATE TABLE customer(pin INT(4) PRIMARY KEY, acc_number BIGINT(8) UNIQUE, cust_name VARCHAR(24), acc_balance BIGINT(12))";
//            statement.execute(customer_table);

//            String drop="DROP TABLE customer";
//            statement.execute(drop);

            // initialising customer table
//            String cust_insert="INSERT INTO customer(pin, acc_number, cust_name, acc_balance) VALUES (1234, 21112113, 'JOSEPHINE PRISCILLA', 20000000)," +
//                    "(2234, 22223333, 'MAXIMILLIAN LAWRENCE', 450000000),(4433,44334433, 'KEVIN DAVID', 34500000),(1212,12121212,'HORAES CHRISTINA', 220033400)," +
//                    "(4444, 45454545, 'DAVID WILLIAM', 25400000)";
//            statement.execute(cust_insert);

            System.out.println("\t\t\t\t\tWELCOME TO THE atm.ATM SERVICE\nPlease choose 1 to login to your account");
            System.out.println("1: Login to your account");
            int resp=scanner.nextInt();
            scanner.nextLine();
            
            int PIN = 0, acc_Number = 0;
            if (resp == 1) {
                // account number
                System.out.print("Enter your A/C number\n->");
                System.out.println();
                acc_Number= scanner.nextInt();
                
                // Personal Identification Number
                System.out.print("Enter your PIN\n->");
                System.out.println();
                PIN=scanner.nextInt();
            }else{
                System.out.println("Invalid response");
            }
            
            String auth="SELECT * FROM customer WHERE pin=? AND acc_number=?";
            PreparedStatement ps= connection.prepareStatement(auth);
            ps.setInt(1, PIN);
            ps.setInt(2, acc_Number);
            ResultSet resultSet= ps.executeQuery();
            
            if (!resultSet.isBeforeFirst()){
                System.out.println("!!!! INCORRECT ACCOUNT DETAILS, PLEASE CHECK FOR THEM AND TRY AGAIN");
            }else {
                
                
                String retrieved_name = null;
                int retrieved_pin = 0, retrieved_acNo = 0, retrieved_accBal = 0;
                while(resultSet.next()){
                    retrieved_pin=resultSet.getInt(1);
                    retrieved_acNo=resultSet.getInt(2);
                    retrieved_name= resultSet.getString(3);
                    retrieved_accBal=resultSet.getInt(4);

                }
                System.out.println("WELCOME "+retrieved_name);
                
                actions(retrieved_name,retrieved_pin,retrieved_accBal,retrieved_acNo);
                
            }

        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static void actions(String mName, int mPin, int mAcc_Bal, int acc_No) throws SQLException {
        System.out.println("Please choose an operation\n1: Make a Deposit\n2: Withdraw cash\n3: Check A/C Balance ");
//        Scanner scanner1=new Scanner(System.in);
        int response= scanner.nextInt();
        scanner.nextLine();

        if (response == 1) {
            deposit(mAcc_Bal, mPin);
        } else if (response==2) {
            withdraw(mAcc_Bal, mPin);
        } else if (response==3) {
            checkBalance(mAcc_Bal);
        }else {
            System.out.println("INVALID RESPONSE");
            System.exit(0);
        }

    }

    private static void checkBalance(int mAcc_Bal) {
        System.out.println("Your account balance is: $"+mAcc_Bal);
    }

    private static void withdraw(int mAcc_Bal, int pin) throws SQLException {
        System.out.print("Withdraw cash\nAmount->");
        int withdrawal=scanner.nextInt();
        scanner.nextLine();
        if (withdrawal > mAcc_Bal) {
            System.out.println("Insufficient Balance!!!!!!!!!!");
        }else {
            int new_Bal=mAcc_Bal-withdrawal;
            System.out.println("$"+withdrawal+" successfully withdrawn\nNew account balance: $"+new_Bal);
            
            update(new_Bal, pin);

        }
    }

    private static void deposit(int mAcc_Bal,int pin ) throws SQLException {
        System.out.println("Enter amount to deposit:\n->");
        int deposit_amount=scanner.nextInt();
        scanner.nextLine();
        int new_acc_Bal=mAcc_Bal+deposit_amount;
        update(new_acc_Bal,pin);
        System.out.println("Deposit transaction was successful\nNew account Balance: $"+new_acc_Bal+"\nthank you for banking with us :)");

    }

    private static void update(int new_Bal, int pin) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_service", "root", "");
        String acc_update=" UPDATE customer SET acc_balance=? WHERE pin=?";
        PreparedStatement pp1=connection.prepareStatement(acc_update);
        pp1.setInt(1, new_Bal );
        pp1.setInt(2, pin);
        pp1.execute();
    }
}
