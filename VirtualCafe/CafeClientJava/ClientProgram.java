import java.util.Scanner;

public class ClientProgram {

    private static int getQuantity(String part, String beverage) {
        String[] words = part.split("\\s+");
        for (String word : words) {
            try {
                return Integer.parseInt(word);
            } catch (NumberFormatException e) {
            }
        }
        return 1;
    }

    public static void main(String[] args)
    {   System.out.println("Enter your name:");
        try
        {   Scanner in = new Scanner(System.in);
            String customerName = in.nextLine();
            try (Client client = new Client( customerName ))
            {   System.out.println("You entered to 'TheVirtualCafe' successfully!");
                while (true)
                {
                    System.out.println("--> COMMAND LIST:");
                    System.out.println("* Place order: order X teas/coffees and Y coffees/teas");
                    System.out.println("* Get your order status: order status");
                    System.out.println("* Collect your order: collect");
                    System.out.println("* Exit the cafe: exit");

                    String command = in.nextLine();
                    String validCommandRegex = "order\\s+(\\d+\\s*teas?|tea|\\d+\\s*coffees?|coffee)(\\s+and\\s+(\\d+\\s*teas?|tea|\\d+\\s*coffees?|coffee))*";

                    if (command.equalsIgnoreCase("exit")){
                        System.out.println(client.exitCafe());
                    }else if(command.equalsIgnoreCase("collect")){
                        System.out.println(client.collectOrder());
                    }else if(command.equalsIgnoreCase("order status")){
                        System.out.println(client.getOrderStatus());
                    }else if(command.matches(validCommandRegex)){
                        int numTeas = 0;
                        int numCoffees = 0;
                        String[] parts = command.toLowerCase().replace("order", "").trim().split("and");
                        for (String part : parts) {
                            part = part.trim();
                            if (part.contains("tea")) {
                                numTeas += getQuantity(part, "tea");
                            }
                            if (part.contains("coffee")) {
                                numCoffees += getQuantity(part, "coffee");
                            }
                        }
                        System.out.println(client.setOrder(numTeas, numCoffees));
                    }else{
                        System.out.println("Please enter a valid command");
                    }
                }
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
