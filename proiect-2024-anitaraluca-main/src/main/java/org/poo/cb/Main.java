package org.poo.cb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class Main {
    public static void main(String[] args) {
        if(args == null) {
            System.out.println("Running Main");
        }
        else{
            String exchangeRatesFilePath = "src/main/resources/" + args[0];
            String stockValuesFilePath = "src/main/resources/" + args[1];
            String commandsFilePath = "src/main/resources/" + args[2];
            try {
                List<String> exchangeRatesLines = Files.readAllLines(Paths.get(exchangeRatesFilePath));
                List<String> stockValuesLines = Files.readAllLines(Paths.get(stockValuesFilePath));
                List<String> commandsLines = Files.readAllLines(Paths.get(commandsFilePath));
                UserDatabase.deleteAllUsers();

                for (String line : commandsLines) {
                    // Separăm linia în cuvinte
                    String[] words = line.split("\\s+");

                    // Verificăm dacă primul cuvânt este "CREATE"
                    if (words.length > 0 && words[0].equals("CREATE")) {
                        if(words[1].equals("USER")) {
                            String address = "";
                            for (int i = 5; i < words.length; i++) {  // Concatenăm toate cuvintele începând cu words[5] până la finalul liniei
                                address += words[i];
                                if (i < words.length - 1) {  // Adăugăm un spațiu între cuvinte (dacă nu suntem la ultimul cuvânt)
                                    address += " ";
                                }
                            }
                            GenericCommand createUserCommand = new GenericCommand("CREATE USER", words[4], words[3], words[2], address);

                            createUserCommand.execute();
                        }
                    }
                    if (words.length > 0 && words[0].equals("LIST")) {
                        if (words[1].equals("USER")) {
                            GenericCommand listUserCommand = new GenericCommand("LIST USER", words[2]);
                            listUserCommand.execute();
                        }
                        if (words[1].equals("PORTFOLIO")) {
                            User user = UserDatabase.getUserByEmail(words[2]);
                            if (user != null) {
                                Portofolio portofolio = user.getPortfolio();
                                portofolio.listPortofolio();
                            } else {
                                System.out.println("Utilizatorul cu email-ul " + words[2] + " nu a fost găsit.");
                            }
                        }
                    }
                    if (words.length > 0 && words[0].equals("ADD")) {
                        if (words[1].equals("FRIEND")) {
                            String userEmail = words[2];
                            String friendEmail = words[3];

                            User user = UserDatabase.getUserByEmail(userEmail);

                            if (user != null) {
                                User friend = UserDatabase.getUserByEmail(friendEmail);

                                // Verificăm dacă prietenul există
                                if (friend != null) {
                                    // Verificăm dacă prietenul nu este deja în lista de prieteni a utilizatorului
                                    if (!user.getFriends().contains(friend)) {
                                        // Adăugăm prietenul în lista de prieteni a utilizatorului
                                        user.addFriend(friend);
                                        friend.addFriend(user);
                                    } else {
                                        System.out.println("Prietenul cu adresa de email " + friendEmail + " este deja în lista de prieteni a utilizatorului.");
                                    }
                                } else {
                                    System.out.println("Prietenul cu adresa de email " + friendEmail + " nu există.");
                                }
                            } else {
                                System.out.println("Utilizatorul cu adresa de email " + userEmail + " nu există.");
                            }

                        }
                        if (words[1].equals("ACCOUNT")) {
                            String userEmail = words[2];
                            String currency = words[3];

                            User user = UserDatabase.getUserByEmail(userEmail);
                            if (user != null) {
                                Account account = (Account) AccountFactory.createAccount(currency);
                                Portofolio portofolio = user.getPortfolio();
                                portofolio.addAccount(account);

                            } else {
                                System.out.println("Eroare: Utilizatorul nu a fost găsit.");
                            }
                        }
                        if(words[1].equals("MONEY")) {
                            String userEmail = words[2];
                            String currency = words[3];
                            User user = UserDatabase.getUserByEmail(userEmail);
                            if (user != null) {
                                Account account = user.getPortfolio().getAccountByCurrency(currency);
                                if (account != null) {
                                    Double money = account.getBalance();
                                    money += (Double.parseDouble(words[4]));
                                    account.setBalance(money);
                                } else {
                                    System.out.print("Contul pentru moneda " + currency + " nu a fost găsit în portofoliul utilizatorului.");
                                }
                            } else {
                                System.out.print("Utilizatorul cu email " + userEmail + " nu a fost găsit.");
                            }
                        }
                    }
                    if (words[0].equals("EXCHANGE")) {
                        if (words[1].equals("MONEY")) {
                            User user = UserDatabase.getUserByEmail(words[2]);
                            String currency1 = words[3];
                            String currency2 = words[4];

                            Double ue = 0.00;
                            Double eu = 0.00;

                            String lastLine = exchangeRatesLines.get(exchangeRatesLines.size() - 1);
                            String[] lastLineWords = lastLine.split(",");
                            String eurusd = lastLineWords[1];

                            String firstLine = exchangeRatesLines.get(1);
                            String[] firstLineWords = firstLine.split(",");
                            String usdeur = firstLineWords[firstLineWords.length - 1];

                            Double amountToTransfer = Double.parseDouble(words[5]);
                            Exchange exchange = new Exchange();
                            double convertedAmount = 0.00;
                            if(currency1.equals("EUR") && currency2.equals("USD")) {
                                if(user.getPortfolio().getAccountByCurrency(currency1).getBalance() >= Double.parseDouble(eurusd)*Double.parseDouble(words[5])){
                                    CurrencyConversionStrategy strategy = new EuroToUsdConversionStrategy();
                                    exchange.setConversionStrategy(strategy);
                                    if (user.getPortfolio().getAccountByCurrency(currency1).getBalance() / 2 < Double.parseDouble(eurusd)*Double.parseDouble(words[5]) & !user.isPremium()) {
                                        convertedAmount = exchange.convertCurrency(amountToTransfer, Double.parseDouble(eurusd), words[2]);
                                        convertedAmount = convertedAmount - 0.01*convertedAmount*Double.parseDouble(eurusd);
                                    }
                                    else {
                                        convertedAmount = exchange.convertCurrency(amountToTransfer, Double.parseDouble(eurusd), words[2]);
                                    }
                                }
                                else
                                    System.out.println("Insufficient amount in account EUR for exchange");
                            } else if(currency1.equals("USD") && currency2.equals("EUR")) {
                                if(user.getPortfolio().getAccountByCurrency(currency1).getBalance() >= Double.parseDouble(usdeur)*Double.parseDouble(words[5])){
                                    CurrencyConversionStrategy strategy = new UsdToEuroConversionStrategy();
                                    exchange.setConversionStrategy(strategy);

                                    if (!user.isPremium() & (user.getPortfolio().getAccountByCurrency(currency1).getBalance() / 2 < Double.parseDouble(usdeur)*Double.parseDouble(words[5]))) {
                                        convertedAmount = exchange.convertCurrency(amountToTransfer, Double.parseDouble(usdeur), words[2]);
                                        convertedAmount -= 0.01*Double.parseDouble(usdeur)*Double.parseDouble(words[5]);
                                    } else {
                                        convertedAmount = exchange.convertCurrency(amountToTransfer, Double.parseDouble(usdeur), words[2]);
                                    }
                                } else System.out.println("Insufficient amount in account USD for exchange");
                            }

                            user.getPortfolio().getAccountByCurrency(currency1).setBalance(convertedAmount);
                            Double ballance2 = Double.parseDouble(words[5]);
                            Double ballance21 = user.getPortfolio().getAccountByCurrency(currency2).getBalance();
                            user.getPortfolio().getAccountByCurrency(currency2).setBalance(ballance2 + ballance21);
                        }
                    }
                    if (words[0].equals("TRANSFER")) {
                        if (words[1].equals("MONEY")) {
                            User user = UserDatabase.getUserByEmail(words[2]);
                            User friend = UserDatabase.getUserByEmail(words[3]);
                            if (user != null && friend != null) {
                                String currency = words[4];
                                Double moneyToTransfer = Double.parseDouble(words[5]);
                                Account userAccount = user.getPortfolio().getAccountByCurrency(currency);
                                Account friendAccount = friend.getPortfolio().getAccountByCurrency(currency);

                                if (userAccount != null && friendAccount != null && user.getFriends().contains(friend)) {
                                    Double balanceUser = userAccount.getBalance();
                                    Double balanceFriend = friendAccount.getBalance();

                                    if (balanceUser >= moneyToTransfer) {
                                        balanceUser -= moneyToTransfer;
                                        balanceFriend += moneyToTransfer;

                                        userAccount.setBalance(balanceUser);  // Actualizăm soldurile conturilor
                                        friendAccount.setBalance(balanceFriend);

                                    } else {
                                        System.out.println("Insufficient amount in account EUR for transfer");
                                    }
                                } else {
                                    System.out.println("Eroare: Unul dintre utilizatori sau conturi nu există sau nu sunt prieteni.");
                                }
                            } else {
                                System.out.println("Eroare: Utilizatorii specificați nu există.");
                            }
                        }
                    }
                    if (words[0].equals("BUY")) {
                        if (words[1].equals("STOCKS")) {
                            User user = UserDatabase.getUserByEmail(words[2]);

                            String firstLine = stockValuesLines.get(0);

                            for (int i = 1; i < stockValuesLines.size(); i++) {
                                String linii = stockValuesLines.get(i);
                                String[] parts = linii.split(",");
                                if (parts.length >= 2) { // Verificare dacă există cel puțin două elemente în linie
                                    String symbol = parts[0]; // Primul element este simbolul
                                    Stock stock = new Stock(symbol);
                                    for (int k = 1; k < parts.length; k++) { // Adăugare restul elementelor ca prețuri
                                        double price = Double.parseDouble(parts[k]);
                                        stock.addValue(price);
                                    }
                                    if(stock.getCompanyName().equals(words[3])){
                                        stock.setNumberOfStocks(Integer.valueOf(words[4]));
                                        user.getPortfolio().addStock(stock);
                                        if((user.getPortfolio().getAccountByCurrency("USD").getBalance() > stock.getNumberOfStocks()*Double.parseDouble(parts[parts.length-1])) & !user.isPremium()) {
                                            Double remainingValue = user.getPortfolio().getAccountByCurrency("USD").getBalance() - stock.getNumberOfStocks()*Double.parseDouble(parts[parts.length-1]);
                                            user.getPortfolio().getAccountByCurrency("USD").setBalance(remainingValue);
                                        } else if(user.isPremium()) {
                                            Double price = stock.getNumberOfStocks()*Double.parseDouble(parts[parts.length-1]) - 0.05*stock.getNumberOfStocks()*Double.parseDouble(parts[parts.length-1]);
                                            Double remainingValue = user.getPortfolio().getAccountByCurrency("USD").getBalance() - price;
                                            user.getPortfolio().getAccountByCurrency("USD").setBalance(remainingValue);
                                        }
                                        else {
                                            System.out.println("Insufficient amount in account for buying stock");
                                        }

                                    }
                                }
                            }
                        }
                    }
                    if (words[0].equals("RECOMMEND")) {
                        if (words[1].equals("STOCKS")) {
                            ArrayList<String> recommendedStocks = new ArrayList<>();
                            String firstLine = stockValuesLines.get(0);
                            for (int i = 1; i < stockValuesLines.size(); i++) {
                                String linii = stockValuesLines.get(i);
                                String[] parts = linii.split(",");
                                // System.out.print(parts[0]);
                                double sum1 = 0.00;
                                for (int k = 1; k < 11; k++) {
                                    sum1 += Double.parseDouble(parts[k]);
                                }
                                sum1 /= 10.00;
                                double sum2 = 0.00;
                                for (int k = 6; k < 11; k++) {
                                    sum2 += Double.parseDouble(parts[k]);
                                }
                                sum2 /= 5.00;
                                if (sum2 > sum1) {
                                    recommendedStocks.add(parts[0]); // Adăugați acțiunea recomandată în ArrayList
                                }
                            }
                            StringBuilder jsonBuilder = new StringBuilder();
                            jsonBuilder.append("{\"stockstobuy\":[");
                            for (int i = 0; i < recommendedStocks.size(); i++) {
                                if (i > 0) {
                                    jsonBuilder.append(",");
                                }
                                jsonBuilder.append("\"").append(recommendedStocks.get(i)).append("\"");
                            }
                            jsonBuilder.append("]}");
                            System.out.println(jsonBuilder.toString());
                        }
                    }
                    if(words[0].equals("BUY")) {
                        if(words[1].equals("PREMIUM")) {
                            User user = UserDatabase.getUserByEmail(words[2]);
                            if(user !=null) {
                                String currency = "usd";
                                if(user.getPortfolio().getAccountByCurrency(currency).getBalance() > 100) {
                                    Double transfer = user.getPortfolio().getAccountByCurrency(currency).getBalance() -100;
                                    user.getPortfolio().getAccountByCurrency(currency).setBalance(transfer);
                                } else {
                                    System.out.println("Insufficient amount in account for premium option");
                                }
                                user.setPremium(true);
                            } else System.out.println("User with " + words[2] +" doesn't exist");
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("cannot read the file");
            }
        }
    }
}