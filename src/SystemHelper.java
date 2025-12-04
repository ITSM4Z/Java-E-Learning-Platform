import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemHelper {
    public static class Choice{
        //CRITICAL: INDEX OUT OF BOUNDS EXCEPTION WHEN CHOOSING A WRONG OPTION
        String prompt;
        String emptyError;
        String negativeError;
        String boundsError;

        public Choice(String prompt, String emptyError, String negativeError, String boundsError){
            this.prompt = prompt;
            this.emptyError = emptyError;
            this.negativeError = negativeError;
            this.boundsError = boundsError;
        }
        public Choice(String prompt, String emptyError, String negativeError){
            this.prompt = prompt;
            this.emptyError = emptyError;
            this.negativeError = negativeError;
            this.boundsError = "Error: You must enter a valid choice number.";
        }
        public Choice(String prompt, String emptyError){
            this.prompt = prompt;
            this.emptyError = emptyError;
            this.negativeError = "Error: You must enter a positive number.";
            this.boundsError = "Error: You must enter a valid choice number.";
        }
        public Choice(String prompt){
            this.prompt = prompt;
            this.emptyError = "Error: You must enter a choice number.";
            this.negativeError = "Error: You must enter a positive number.";
            this.boundsError = "Error: You must enter a valid choice number.";
        }

        public int ChoiceByInt(int bounds, boolean skipException){
            Scanner sc = new Scanner(System.in);
            int choice;

            while (true){
                System.out.print(prompt);
                String userInput;
                try {
                    userInput = sc.nextLine();
                    if(userInput.isEmpty()){
                        System.out.println(emptyError);
                        continue;
                    }
                    choice = Integer.parseInt(userInput);
                    if(choice == 0){
                        break;
                    }
                    else if(choice < 0){
                        System.out.println(negativeError);
                        continue;
                    }
                    else if(choice > bounds){
                        System.out.println(boundsError);
                        continue;
                    }
                    break;
                } catch (NumberFormatException e){
                    if(skipException) return -1;
                    System.out.println("Error: You must enter a number.");
                    sc.nextLine();
                }
            }
            return choice;
        }
    }
    public static class Search{
        String prompt;
        String emptyError;
        String negativeError;

        public Search(String prompt, String emptyError, String negativeError){
            this.prompt = prompt;
            this.emptyError = emptyError;
            this.negativeError = negativeError;
        }
        public Search(String prompt, String emptyError){
            this.prompt = prompt;
            this.emptyError = emptyError;
            this.negativeError = "Error: You must enter a positive number.";
        }
        public Search(String prompt){
            this.prompt = prompt;
            this.emptyError = "Error: You must enter a user's Name or Id.";
            this.negativeError = "Error: You must enter a positive number.";
        }
        public Search(){
            this.prompt = "Enter a User's Name or Id (Enter 0 to go back): ";
            this.emptyError = "Error: You must enter a user's Name or Id.";
            this.negativeError = "Error: You must enter a positive number.";
        }

        public User searchForUser(List<User> passedUserList, Choice choice, UserRole userRole) throws UserNotFoundException{
            ArrayList<User> users = new ArrayList<>(passedUserList);

            if(users.isEmpty()) return null;

            String role = "";
            switch (userRole){
                case STUDENT:
                    role = "student";
                    break;
                case INSTRUCTOR:
                    role = "instructor";
                    break;
                case ADMIN:
                    role = "admin";
                    break;
                case null:
                    role = "user";
                    break;
            }

            Scanner sc = new Scanner(System.in);
            String userInput = "";

            while (true){
                System.out.println(users.size() + " " + role + "s.");
                System.out.print(prompt);

                try {
                    userInput = sc.nextLine().trim();

                    if(userInput.isEmpty()){
                        System.out.println(emptyError);
                        continue;
                    }
                    int id = Integer.parseInt(userInput);
                    if(id == 0){
                        break;
                    }
                    else if(id < 0){
                        System.out.println(negativeError);
                        continue;
                    }

                    for(User user : users){
                        if(user.getUserId() == id){
                            return user;
                        }
                    }
                    throw new UserNotFoundException("The " + role + " with the name/id of: " + userInput + " was not found.");
                } catch (NumberFormatException numberE){
                    while (true){
                        ArrayList<User> nameResults = new ArrayList<>();
                        userInput = userInput.toLowerCase();
                        for(User user : users){
                            if(user.getName().toLowerCase().trim().contains(userInput)){
                                nameResults.add(user);
                            }
                        }

                        System.out.println("Search results: ");
                        for (int i = 0; i < 10; i++) {

                            try{
                                System.out.println((i+1) + ". " + nameResults.get(i));
                            } catch (IndexOutOfBoundsException indexE){
                                break;
                            }
                        }

                        int option = choice.ChoiceByInt(10, true);
                        if(option == 0){
                            break;
                        }
                        else if(option == -1){
                            continue;
                        }
                        return nameResults.get(option - 1);
                    }
                }
            }
            return null;
        }
        public User searchForUser(List<User> passedUserList, Choice choice) throws UserNotFoundException{
            ArrayList<User> users = new ArrayList<>(passedUserList);

            if(users.isEmpty()) return null;

            String role = "user";

            Scanner sc = new Scanner(System.in);
            String userInput = "";

            while (true){
                System.out.println(users.size() + " " + role + "s.");
                System.out.print(prompt);

                try {
                    userInput = sc.nextLine().trim();

                    if(userInput.isEmpty()){
                        System.out.println(emptyError);
                        continue;
                    }
                    int id = Integer.parseInt(userInput);
                    if(id == 0){
                        break;
                    }
                    else if(id < 0){
                        System.out.println(negativeError);
                    }

                    for(User user : users){
                        if(user.getUserId() == id){
                            return user;
                        }
                    }
                    throw new UserNotFoundException("The " + role + " with the name/id of: " + userInput + " was not found.");
                } catch (NumberFormatException numberE){
                    while (true){
                        ArrayList<User> nameResults = new ArrayList<>();
                        userInput = userInput.toLowerCase();
                        for(User user : users){
                            if(user.getName().toLowerCase().trim().contains(userInput)){
                                nameResults.add(user);
                            }
                        }

                        System.out.println("Search results: ");
                        for (int i = 0; i < 10; i++) {

                            try{
                                System.out.println((i+1) + ". " + nameResults.get(i));
                            } catch (IndexOutOfBoundsException indexE){
                                break;
                            }
                        }

                        int option = choice.ChoiceByInt(10, true);
                        if(option == 0){
                            break;
                        }
                        else if(option == -1){
                            continue;
                        }
                        return nameResults.get(option - 1);
                    }
                }
            }
            return null;
        }
    }
}