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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Forgive {
    private static String fileName;
    private static TempFileLapper tempFileLapper;
    private static CharacterManager characterManager;

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
        tempFileLapper.createAllTempFile(false);

        characterManager = new DefaultCharacterManager();
        
        separateSrcFile();

        createOutputFile();
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

    private static void createOutputFile(){
        try(OutputStream output = tempFileLapper.getOutputStream(TempFiles.OUTPUT_CLASSFILE)){
            //magic number
            output.write(new byte[]{(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE});
            //minor version
            output.write(new byte[]{0, 0});
            //major version
            output.write(new byte[]{0, 55});
        } catch(IOException ignored){
            //あり得ない
        }
    }
}