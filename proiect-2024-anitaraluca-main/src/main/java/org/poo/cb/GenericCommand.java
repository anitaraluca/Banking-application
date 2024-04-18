package org.poo.cb;
import java.util.ArrayList;
import java.util.List;
public class GenericCommand implements Command{ ;
    private String commandType;
    private String nume;
    private String prenume;
    private String email;
    private String email2;
    private String friends;
    private String[] adresa;
    private String currency;
    private Double money;

    public GenericCommand(String commandType, String email, String currency, Double money) {
        this.commandType = commandType;
        this.currency = currency;
        this.email = email;
        this.money = money;
    }

    public GenericCommand(String commandType, String nume, String prenume, String email, String adresa) {
        this.commandType = commandType;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.adresa = new String[]{adresa};
    }
    public GenericCommand(String commandType, String email, String friends) {
        this.commandType = commandType;
        this.email = email;
        this.friends = friends;
    }
    public GenericCommand(String commandType, String email) {
        this.commandType = commandType;
        this.email = email;
    }

    @Override
    public void execute() {
        switch (commandType) {
            case "CREATE USER":
                createUser(email, prenume, nume,adresa);
                break;
            case "LIST USER":
                listUser(email);
                break;
            case "ADD FRIEND":
                //addFriend(email, friends);
                break;
            case "ADD ACCOUNT":
                //addAccountToPortofolio(email, currency);
                break;
            case "ADD MONEY":
                addMoney(email, currency,money);
            default:
                System.out.println("Comandă necunoscută.");
        }
    }
    private void addAccountToPortofolio(String userEmail, String currency) {
        // Obținerea utilizatorului din baza de date folosind adresa de email
        User user = UserDatabase.getUserByEmail(userEmail);

        // Verificarea dacă utilizatorul există
        if (user != null) {
            CurrencyAccount newAccount = AccountFactory.createAccount(currency);

            user.getPortfolio().addAccount((Account) newAccount);
            user.setPortofolio(user.getPortfolio());

            System.out.println("Account added successfully to user " + userEmail);
        } else {
            System.out.println("User with email " + userEmail + " not found.");
        }
    }

    private void createUser(String email, String prenume, String nume,String[] adresa) {
        User user = new User(email, prenume, nume, adresa[0]);
        if (UserDatabase.getUserByEmail(email) == null) {
            UserDatabase.addUser(user); // Adăugăm utilizatorul creat în baza de date a utilizatorilor

        }
        else {
            System.out.println("User with " + email + " already exists");
        }
        //System.out.println("Utilizator creat cu succes: " + user.getLastName());
    }

    private void listUser(String email) {
        User user = UserDatabase.getUserByEmail(email);

        // Verificăm dacă utilizatorul există
        //if(friends == null) {
        if (user != null) {
            // Afișăm numele și prenumele utilizatorului
            List<User> friends = user.getFriends();
            if(friends == null || friends.isEmpty()) {
                System.out.println(String.format("{\"email\":\"%s\",\"firstname\":\"%s\",\"lastname\":\"%s\",\"address\":\"%s\",\"friends\":" + "[]}",
                        user.getEmail(), user.getFirstName(), user.getLastName(), user.getAddress()));
            } else  for (User friend : friends) {
                // Afișăm numele și prenumele utilizatorului
                System.out.println(String.format("{\"email\":\"%s\",\"firstname\":\"%s\",\"lastname\":\"%s\",\"address\":\"%s\",\"friends\":" + "[\"%s\"]" +"}",
                        user.getEmail(), user.getFirstName(), user.getLastName(), user.getAddress(),friend.getEmail()));
            }
//        }
            //return;
        } else {
            // Afișăm un mesaj de eroare dacă utilizatorul nu a fost găsit
            System.out.println("User with " + email + " doesn't exist");
        }
        //}
//        else for (User friend : friends) {
//            if (user != null) {
//                // Afișăm numele și prenumele utilizatorului
//                System.out.println(String.format("{\"email\":\"%s\",\"firstname\":\"%s\",\"lastname\":\"%s\",\"address\":\"%s\",\"friends\":" + friend.getEmail() +"}",
//                        user.getEmail(), user.getFirstName(), user.getLastName(), user.getAddress()));
//            } else {
//                // Afișăm un mesaj de eroare dacă utilizatorul nu a fost găsit
//                System.out.println("Utilizatorul cu email-ul " + email + " nu a fost găsit.");
//            }
//        }
    }

    private void addMoney(String userEmail, String currency, Double money) {
        // Implementează logica pentru listarea portofoliului utilizatorului
        User user = UserDatabase.getUserByEmail(userEmail);
        if (user != null) {
            Account account = user.getPortfolio().getAccountByCurrency(currency);
            if (account != null) {
                account.deposit(money);
            } else {
                System.out.println("Contul pentru moneda " + currency + " nu a fost găsit în portofoliul utilizatorului.");
            }
        } else {
            System.out.println("Utilizatorul cu email " + userEmail + " nu a fost găsit.");
        }

    }

}
