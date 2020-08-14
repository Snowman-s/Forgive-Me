package forgive.arguments;
import java.util.*;

public class ArgumentReader {
    private ArgumentReader(){}

    /**
     * 指定された起動引数配列からオプションのmapを作成し返します。
     * @param args 起動引数
     * @return 起動引数とその付加文字列のマップ
     */
    public static Map<ArgumentType,List<String>> read(String[] args){
        Map<ArgumentType,List<String>> options = new EnumMap<>(ArgumentType.class);

        ArgumentLoop:
        for(int i=0;i<args.length;i++){
            for(ArgumentType type:ArgumentType.values()){
                if(type.optionNames.contains(args[i])){
                    options.put(type, new ArrayList<>());
                    if(args.length <= i + type.extraNumber){
                        throw new RuntimeException("オプションの引数が足りません。");
                    }
                    for (int i2 = 0; i2 < type.extraNumber; i2++) {
                        options.get(type).add(args[i + i2 + 1]);
                    }
                    i += type.extraNumber;
                    continue ArgumentLoop;
                }
            }
            //オプションが存在しない。
            if(i == args.length - 1){
                options.put(ArgumentType.FileName, List.of(args[i]));
                break;
            }
            throw new RuntimeException("存在しないオプションです:" + args[i]);
        }

        return options;
    }

    public enum ArgumentType{
        FileName(List.of(""), 1), Help(List.of("-h", "-help", "-?"), 0);
        ArgumentType(List<String> optionNames, int extraNumber){
            this.optionNames = optionNames;
            this.extraNumber = extraNumber;
        };
        final List<String> optionNames;
        final int extraNumber;

        public static void printHelp(){
            System.out.println(
                "Usage: forgive <options...> <file_name>\n\n" +
                "options:\n" + 
                "  -h, -help, -?\n" +
                "    ヘルプを表示して実行を終了します。");
        }
    }
}