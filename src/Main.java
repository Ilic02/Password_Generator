import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

public class Main {
    protected static String password(int n){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890,./;'{}[]-+*`~";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while(sb.length() < n){
            int index = (int) (random.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }
        String pass = sb.toString();
        return pass;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert a website: ");
        String website = sc.next();
        System.out.println("Insert password length: ");
        int n = sc.nextInt();
        String randomPassword = password(n);
        System.out.println("Your generated password is: " + randomPassword);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();


        String savedPassword = new String("Website: " + website + " \nPassword: " + randomPassword + " \nDate: " + dtf.format(time) + "\n\n");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/Passwords.txt", true));
            writer.write(savedPassword);
            System.out.println("Your password has been saved in Passwords.txt!");
            writer.close();
        }
        catch (IOException ex){
            System.out.println("Invalid path!");
        }
    }
}