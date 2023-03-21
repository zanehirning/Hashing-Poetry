import java.util.*;
import java.io.File;

public class WritePoetry {

    HashTable<String, WordFreqInfo> table = new HashTable<>();
    ArrayList<String> copyList = new ArrayList<>();

    public String writePoem(String file, String startWord, int length, boolean printHashTable) {
        readDictionary(file);
        createTable();
        char ch;
        WordFreqInfo start = table.find(startWord);
        StringBuilder sb = new StringBuilder();
        sb.append(startWord);
        Random rnd = new Random();
        for (int i=0; i<length; i++) {
            var number = rnd.nextInt(start.getOccurCount());
            ch = start.getFollowWord(number).charAt(0);
            if (!Character.isAlphabetic(ch) && start.getFollowWord(number).length() < 2) {
                sb.append(start.getFollowWord(number));
                sb.append("\n");
            }
            else {
                sb.append(" ");
                sb.append(start.getFollowWord(number));
            }
            start = table.find(start.getFollowWord(number));
        }
        sb.append(".");
        table.makeEmpty();
        copyList.removeAll(copyList);
        printHashTable=true;
        return sb.toString();
    }

    private ArrayList<String> readDictionary(String poetryFile) {
        File file = new File(poetryFile);
        char ch;
        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String[] list = input.nextLine().toLowerCase().split("[ ]");
                Collections.addAll(copyList, list);
                for (int i = 0; i < copyList.size(); i++) {
                    for (int j=0; j<copyList.get(i).length(); j++) {
                        ch = copyList.get(i).charAt(j);
                        if (!Character.isAlphabetic(ch) && j==copyList.get(i).length()-1) {
                            String punctuation = copyList.get(i).substring(copyList.get(i).length()-1);
                            String word = copyList.get(i).substring(0, copyList.get(i).length()-1);
                            copyList.remove(i);
                            copyList.add(i, word);
                            copyList.add(i+1, punctuation);
                            i++;
                        }
                    }
                }
            }
        }
        catch (java.io.IOException ex) {
            System.out.println("An error occured trying to read the file: " + ex);
        }
        copyList.removeAll(Arrays.asList(null, ""));
        return copyList;
    }

    private HashTable<String, WordFreqInfo> createTable() {
        for (int i=0; i<copyList.size()-1; i++) {
            if (table.find(copyList.get(i)) != null) {
                if (table.contains(copyList.get(i))) {
                    table.find(copyList.get(i)).updateFollows(copyList.get(i+1));
                }
            }
            else {
                table.insert(copyList.get(i), new WordFreqInfo(copyList.get(i), 0));
                table.find(copyList.get(i)).updateFollows(copyList.get(i+1));
            }
        }
        return table;
    }
}