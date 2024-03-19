    public class BrokenKeyboard {
        public static int calculateFullyTypedWords(String message, String brokenKeys) {
            if (message.isEmpty()) {
                return 0;
            }
            message = message.trim();
            String[] words = message.split(" ");
            String[] symbols = brokenKeys.split("");
            int counter = 0;
            for (String word : words) {
                if (word.compareTo("") == 0) {
                    continue;
                }
                if (brokenKeys.isEmpty()) {
                    counter++;
                    continue;
                }
                boolean brokenKeyExists = false;
                for (String symbol : symbols) {
                    if (word.contains(symbol)) {
                        brokenKeyExists = true;
                        break;
                    }
                }
                if (!brokenKeyExists) {
                    counter++;
                }
            }
            return counter;
        }
    }
