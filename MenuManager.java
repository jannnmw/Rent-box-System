
import java.util.Scanner;

public class MenuManager {
    public static void main(String[] args) throws Exception {
        
        // creating the menu
        boolean exit = false;
        while(!exit) {
            System.out.println("Please pick one of the following options: ");
            System.out.println("1. I'm a New user:");
            System.out.println("2. I'm a returning customer");
            System.out.println();
            System.out.println("Pick an option. ");

            
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                NewUser();// methods
                break;
                case 2: 
                ReturningCus();//method for options 
                break;
                case 0:
                System.out.println("exit"); 
                exit = true;
                break; 
                default:
                System.out.println("Error");
            }
            scanner.close();
        }
    }

    privat static void NewUser(){
        Sysrtem.out.println("Welcome to Rent Box");
    }


            
            



        

        
            
            /* 
            System.out.println("Display Games available");
            System.out.println("Display Movies available");
            System.out.println("Display Transaction history");
            System.out.println("Display games available");
            */

        
    }    

