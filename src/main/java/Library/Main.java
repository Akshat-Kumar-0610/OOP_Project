package Library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("lol");
        File inputFile = new File("resources\\input.txt");
        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        String st;
        // Condition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null)

            // Print the string
            System.out.println(st);

    }
}
