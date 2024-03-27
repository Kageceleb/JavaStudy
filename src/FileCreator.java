import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileCreator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("whats the name of the file? ");
            var fileName = sc.nextLine();
            File myObj = new File(fileName + ".txt");
            if(myObj.createNewFile()){
                System.out.println("File created "+ myObj.getName());
            }else {
                System.out.println("File already exists.");
            }
        } catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    } 
    
}
