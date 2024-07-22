public class Main {
    public static void main(String[] args){
        FileOutput.writeToFile(args[2],"",false,false); // to clean the file before writing
        String[] productInput = FileInput.readFile(args[0], true, true);
        String[] purchaseInput = FileInput.readFile(args[1], true, true);
        String output = args[2];
        Machine gymMealMachine = new Machine();
        gymMealMachine.fill(productInput, output);
        gymMealMachine.print(output);
        gymMealMachine.buyFromMachine(purchaseInput, output);
        gymMealMachine.print(output);
    }
}
