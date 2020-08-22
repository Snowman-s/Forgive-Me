package forgive;

import forgive.arguments.ArgumentReader;
import forgive.arguments.ArgumentReader.ArgumentType;
import forgive.classfile.RuntimeConstantWriter;
import forgive.classfile.ClassFileInfo;
import forgive.classfile.MethodInfo;
import forgive.classfile.MethodWriter;
import forgive.classfile.OpecodeWriter;
import forgive.constants.AccessFlags;
import forgive.constants.CharacterManager;
import forgive.constants.DefaultCharacterManager;
import forgive.tempfile.TempFileLapper;
import forgive.tempfile.TempFileLapper.TempFileKey;
import forgive.tempfile.TempFileLapper.TempFiles;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Forgive {
    private static String fileName;
    private static String absFileName;
    private static String outputFileName;
    private static TempFileLapper tempFileLapper;
    private static CharacterManager characterManager;
    private static ClassFileInfo classFileInfo;
    private static RuntimeConstantWriter runtimeConstantWriter;

    private static int runtimeClassObjectIndex;
    private static int runtimeMethodObjetInitIndex;
    private static int runtimeClassIndex;
    private static int runtimeUtf8CodeIndex;

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

        outputFileName = fileName.substring(0, fileName.length() - SRC_FILE_EXT.length()) + ".class";

        tempFileLapper = new TempFileLapper();
        tempFileLapper.createTempFiles(Set.of(
            TempFileKey.of(TempFiles.SRC_FILE_SEPARATE), 
            TempFileKey.of(TempFiles.OUTPUT_CLASSFILE),
            TempFileKey.of(TempFiles.RUNTIME_CONSTANT_MEMO)));

        characterManager = new DefaultCharacterManager();
        classFileInfo = new ClassFileInfo();
        runtimeConstantWriter = new RuntimeConstantWriter(classFileInfo);

        try{
            separateSrcFile();

            createOutputFile();

            setupRuntimeField();

            setupMethodInit();

            readStatement();

            completeClassFile();

            copyClassFile();
        } catch(IOException e) {
            throw new RuntimeException("未知の理由で書き込みに失敗しました。");
        }
    }

    private static void separateSrcFile(){
        try(BufferedWriter writer = tempFileLapper.getWriter(TempFileKey.of(TempFiles.SRC_FILE_SEPARATE));
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
        try(OutputStream output = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.OUTPUT_CLASSFILE))){
            //magic number
            output.write(new byte[]{(byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE});
            //minor version
            output.write(new byte[]{0, 0});
            //major version
            output.write(new byte[]{0, 55});
        }
    }

    private static void setupRuntimeField() throws IOException{
        try (OutputStream outputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.RUNTIME_CONSTANT_MEMO))) {
            //method Name "<init>":()V
            runtimeMethodObjetInitIndex = runtimeConstantWriter.writeRuntimeMethodref(outputStream, "java/lang/Object", "<init>", "()V");
            //class Name "Object"
            runtimeClassObjectIndex = runtimeConstantWriter.writeRuntimeClass(outputStream, "java/lang/Object");
            //class Name
            runtimeClassIndex = runtimeConstantWriter.writeRuntimeClass(outputStream, absFileName);
            //utf8 "Code"
            runtimeUtf8CodeIndex = runtimeConstantWriter.writeRuntimeUTF8(outputStream, "Code");
       }
    }

    private static void setupMethodInit() throws IOException{
        MethodInfo methodInfo = new MethodInfo(EnumSet.of(AccessFlags.PUBLIC), "<init>", "()V");
        OpecodeWriter opecodeWriter = new OpecodeWriter(methodInfo);
        MethodWriter methodWriter = new MethodWriter(methodInfo);

        //"this"
        methodInfo.setLocals(1);
        try (OutputStream runtimeOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.RUNTIME_CONSTANT_MEMO))) {
            runtimeConstantWriter.writeRuntimeUserMethod(runtimeOutputStream, methodInfo);
        }

        tempFileLapper.createTempFiles(Set.of(TempFileKey.of(TempFiles.METHOD_MEMO, methodInfo.getIdentity()), TempFileKey.of(TempFiles.OPECODE_MEMO, methodInfo.getIdentity())));
        try (OutputStream codeOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.OPECODE_MEMO, methodInfo.getIdentity()))){
            opecodeWriter.aload_minimum(codeOutputStream, (byte)0);
            opecodeWriter.invokespecial(codeOutputStream, (short)runtimeMethodObjetInitIndex);
            opecodeWriter.return_(codeOutputStream);
        }

        try(OutputStream methodOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.METHOD_MEMO, methodInfo.getIdentity()));
            InputStream codeInputStream = tempFileLapper.getInputStream(TempFileKey.of(TempFiles.OPECODE_MEMO, methodInfo.getIdentity()))){
                
            methodWriter.writeMethod(runtimeUtf8CodeIndex, codeInputStream, methodOutputStream);
        }

        classFileInfo.addMethods(methodInfo);
    }

    private static void readStatement() throws IOException{
        MethodInfo methodInfo = new MethodInfo(EnumSet.of(AccessFlags.PUBLIC, AccessFlags.STATIC), "main", "([Ljava/lang/String;)V");
        OpecodeWriter opecodeWriter = new OpecodeWriter(methodInfo);
        MethodWriter methodWriter = new MethodWriter(methodInfo);

        //[Ljava/lang/String;
        methodInfo.setLocals(1);
        try (OutputStream runtimeOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.RUNTIME_CONSTANT_MEMO))) {
            runtimeConstantWriter.writeRuntimeUserMethod(runtimeOutputStream, methodInfo);
        }

        tempFileLapper.createTempFiles(Set.of(TempFileKey.of(TempFiles.METHOD_MEMO, methodInfo.getIdentity()), TempFileKey.of(TempFiles.OPECODE_MEMO, methodInfo.getIdentity())));
        try (OutputStream codeOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.OPECODE_MEMO, methodInfo.getIdentity()))){
            //opecodes...
            opecodeWriter.return_(codeOutputStream);
        }

        try(OutputStream methodOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.METHOD_MEMO, methodInfo.getIdentity()));
            InputStream codeInputStream = tempFileLapper.getInputStream(TempFileKey.of(TempFiles.OPECODE_MEMO, methodInfo.getIdentity()))){
            
            methodWriter.writeMethod(runtimeUtf8CodeIndex, codeInputStream, methodOutputStream);
        }

        classFileInfo.addMethods(methodInfo);
    }

    private static void completeClassFile() throws IOException{
        try (OutputStream classOutputStream = tempFileLapper.getOutputStream(TempFileKey.of(TempFiles.OUTPUT_CLASSFILE));
                InputStream runtimeInputStream = tempFileLapper.getInputStream(TempFileKey.of(TempFiles.RUNTIME_CONSTANT_MEMO))){
            //constant_pool_count;
            classOutputStream.write(Integers.asByteArray(classFileInfo.runtimeFields().size() + 1, 2));
            //constant_pool[constant_pool_count-1];
            runtimeInputStream.transferTo(classOutputStream);
            //access_flags;
            classOutputStream.write(Integers.asByteArray(AccessFlags.PUBLIC.getData(), 2));
            //this_class;
            classOutputStream.write(Integers.asByteArray(runtimeClassIndex, 2));
            //super_class;
            classOutputStream.write(Integers.asByteArray(runtimeClassObjectIndex, 2));
            //interfaces_count;
            classOutputStream.write(new byte[]{0, 0});
            //fields_count;
            classOutputStream.write(new byte[]{0, 0});
            //methods_count;
            classOutputStream.write(Integers.asByteArray(classFileInfo.methods().size(), 2));
            for(MethodInfo m:classFileInfo.methods()){
                try (InputStream methodInputStream = tempFileLapper.getInputStream(TempFileKey.of(TempFiles.METHOD_MEMO, m.getIdentity()))){
                    methodInputStream.transferTo(classOutputStream);
                } 
            };
            //attributes_count;
            classOutputStream.write(new byte[]{0, 0});
        }
    }

    private static void copyClassFile() throws IOException {
        try(OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(Path.of(outputFileName)));
            InputStream inputStream = tempFileLapper.getInputStream(TempFileKey.of(TempFiles.OUTPUT_CLASSFILE))){
                inputStream.transferTo(outputStream);
        }
    }
}