package forgive;

import forgive.arguments.ArgumentReader;
import forgive.arguments.ArgumentReader.ArgumentType;
import forgive.classfile.ClassFileByteWriter;
import forgive.constants.CharacterManager;
import forgive.constants.DefaultCharacterManager;
import forgive.tempfile.TempFileLapper;
import forgive.tempfile.TempFileLapper.TempFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Forgive {
    private static String fileName;
    private static String absFileName;
    private static TempFileLapper tempFileLapper;
    private static CharacterManager characterManager;
    private static ClassFileByteWriter classFileByteWriter;

    private static int runtimeClassObjectIndex;
    private static int runtimeMethodObjetInitIndex;
    private static int runtimeClassIndex;
    private static int runtimeUtf8CodeIndex;
    private static int runtimeUtf8MainNameIndex;
    private static int runtimeUtf8MainDescriptorIndex;

    private static final String SRC_FILE_EXT = ".forgive";

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

        if(!fileName.endsWith(SRC_FILE_EXT)) {
            throw new RuntimeException("ソース・ファイルの拡張子は「" + SRC_FILE_EXT + "」でなければなりません。");    
        }

        absFileName = Path.of(fileName).getFileName().toString();
        absFileName = absFileName.substring(0, absFileName.length() - SRC_FILE_EXT.length());

        tempFileLapper = new TempFileLapper();
        tempFileLapper.createAllTempFile(false);

        characterManager = new DefaultCharacterManager();
        classFileByteWriter = new ClassFileByteWriter();

        try{
            separateSrcFile();

            createOutputFile();

            setupRuntimeField();
        } catch(IOException e) {
            throw new RuntimeException("未知の理由で書き込みに失敗しました。");
        }
    }

    private static void separateSrcFile(){
        try(BufferedWriter writer = tempFileLapper.getWriter(TempFiles.SRC_FILE_SEPARATE);
            BufferedReader reader = Files.newBufferedReader(Path.of(fileName))){

            int readChar;
            while((readChar = reader.read()) != -1){
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

    private static void createOutputFile() throws IOException{
        try(OutputStream output = tempFileLapper.getOutputStream(TempFiles.OUTPUT_CLASSFILE)){
            //magic number
            output.write(new byte[]{(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE});
            //minor version
            output.write(new byte[]{0, 0});
            //major version
            output.write(new byte[]{0, 55});
        }
    }

    private static void setupRuntimeField() throws IOException{
        try (OutputStream outputStream = tempFileLapper.getOutputStream(TempFiles.RUNTIME_CONSTANT_MEMO)) {
            //method Name "<init>":()V
            runtimeMethodObjetInitIndex = classFileByteWriter.writeRuntimeMethodref(outputStream, "java/lang/Object", "<init>", "()V");
            //class Name "Object"
            runtimeClassObjectIndex = classFileByteWriter.writeRuntimeClass(outputStream, "java/lang/Object");
            //class Name
            runtimeClassIndex = classFileByteWriter.writeRuntimeClass(outputStream, absFileName);
            //utf8 "Code"
            runtimeUtf8CodeIndex = classFileByteWriter.writeRuntimeUTF8(outputStream, "Code");
            //utf8 "main"
            runtimeUtf8MainNameIndex = classFileByteWriter.writeRuntimeUTF8(outputStream, "main");
            //utf8 "([Ljava/lang/String;)V"
            runtimeUtf8MainDescriptorIndex = classFileByteWriter.writeRuntimeUTF8(outputStream, "([Ljava/lang/String;)V");
        }
    }

    private static void readStatement() throws IOException{
        try (BufferedReader reader = tempFileLapper.getReader(TempFiles.SRC_FILE_SEPARATE)) {
            
        }
    }
}