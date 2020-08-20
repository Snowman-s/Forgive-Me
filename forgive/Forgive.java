package forgive;

import forgive.arguments.ArgumentReader;
import forgive.arguments.ArgumentReader.ArgumentType;
import java.util.*;

public class Forgive {
    static String fileName;

    public static void main(String[] args) {
        Map<ArgumentType, List<String>> options = ArgumentReader.read(args);

        if(options.containsKey(ArgumentType.Help)){
            ArgumentType.printHelp();
            return;
        }

        if(!options.containsKey(ArgumentType.FileName) ||
         options.get(ArgumentType.FileName).size() != 1){
            throw new RuntimeException("ファイル名を指定してください。");
        }
        fileName = options.get(ArgumentType.FileName).get(0);
    }
}