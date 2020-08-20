package forgive;

import forgive.arguments.ArgumentReader;
import forgive.arguments.ArgumentReader.ArgumentType;
import forgive.constants.CharacterManager;
import forgive.constants.DefaultCharacterManager;
import forgive.tempfile.TempFileLapper;
import forgive.tempfile.TempFileLapper.TempFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Forgive {
    static String fileName;
    static TempFileLapper tempFileLapper;
    static CharacterManager characterManager;

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

        tempFileLapper = new TempFileLapper();
        tempFileLapper.createAllTempFile();

        characterManager = new DefaultCharacterManager();
        
        separateSrcFile();

        try(BufferedReader reader = tempFileLapper.getReader(TempFiles.SRC_FILE_SEPARATE)){
            reader.lines().forEach(System.out::println);
        }catch(IOException e){

        }
    }

    private static void separateSrcFile(){
        try(BufferedWriter writer = tempFileLapper.getWriter(TempFiles.SRC_FILE_SEPARATE);
            BufferedReader reader = Files.newBufferedReader(Path.of(fileName))){
            
            int readChar;
            while((readChar = reader.read())!=-1){
                if(characterManager.isSeparateChar(readChar)){
                    writer.write('\n');
                } else if(characterManager.isSpace(readChar)){
                    writer.write(' ');
                } else {
                    writer.write(readChar);
                }
            }
        } catch(IOException e) {
            throw new RuntimeException("ソース・ファイルが読み取れません。");
        }
    }
}