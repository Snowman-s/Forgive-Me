package forgive.translate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import forgive.classfile.MethodInfo;
import forgive.classfile.OpecodeWriter;
import forgive.classfile.RuntimeConstantWriter;
import forgive.classfile.MethodInfo.LocalVariableInfo;

public class SrcTranslater extends OpecodeWriter {
    //苦肉の策
    private IOException error;
    private void registerError(IOException error) {
        this.error = error;
    }

    public SrcTranslater(MethodInfo methodInfo) {
        super(methodInfo);
    }

    public void translateAndWrite(Stream<String> srcStream, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream) throws IOException{
        error = null;
        srcStream.forEach(string -> {
            if (error != null) return;

            String[] words = string.split("\\p{javaWhitespace}+");
            if (words.length == 0) return;
            switch (words[0].toLowerCase()) {
                case "declare":
                    declare(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "migrate":
                    migrate(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "say":
                    say(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "forgive":
                    forgive(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "reminder":
                default:
                    //無視(コメント)
                    break;
            }
        });
        if (error != null){
            throw error;
        }
    }

    private void declare(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        try {
            if(data.length != 2){ 
                System.err.println("declare:引数の数が違います。");
                return;
            }

            MethodInfo method = getMethodInfo();
            int variableIndex = method.getIntLocalVariableIndex(data[1]);
            if(variableIndex == -1) {
                method.addLocals(LocalVariableInfo.intVariableOf(data[1]));
                variableIndex = method.locals().size() - 1;
            }

            iconst_i(codeOutputStream, (byte)0);
            istore_minimum(codeOutputStream, (byte)variableIndex);
        } catch(IOException e){
            registerError(e);
        }
    }
    
    private void migrate(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        try {
            if(data.length != 2){ 
                System.err.println("migrate:引数の数が違います。");
                return;
            }

            MethodInfo method = getMethodInfo();
            int variableIndex = method.getIntLocalVariableIndex(data[1]);
            if(variableIndex == -1) {
                System.err.println("migrate:「" + data[1] + "」という世界は存在しません。");
                return;
            }
            short scannerClassIndex = (short)constantWriter.writeRuntimeClass(runtimeOutputStream, "java/util/Scanner");
            short scannerInitMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V");
            short scannerNextIntMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/util/Scanner", "nextInt", "()I");
            short systemInFieldIndex = (short)constantWriter.writeRuntimeFieldref(runtimeOutputStream, "java/lang/System", "in", "Ljava/io/InputStream;");
            short printMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/io/PrintStream", "print", "(Ljava/lang/String;)V");
            byte baseStringIndex = (byte)constantWriter.writeRuntimeString(runtimeOutputStream, "input required...「"+ data[1] +"」>");
            short systemOutFieldIndex = (short)constantWriter.writeRuntimeFieldref(runtimeOutputStream, "java/lang/System", "out", "Ljava/io/PrintStream;");

            getstatic(codeOutputStream, systemOutFieldIndex);
            ldc(codeOutputStream, baseStringIndex);
            invokevirtual(codeOutputStream, printMethodIndex);
            new_(codeOutputStream, scannerClassIndex);
            dup(codeOutputStream);
            getstatic(codeOutputStream, systemInFieldIndex);
            invokespecial(codeOutputStream, scannerInitMethodIndex);
            invokevirtual(codeOutputStream, scannerNextIntMethodIndex);
            istore_n(codeOutputStream, (byte)variableIndex);

            getMethodInfo().setStackSizeIfBigger(3);
        } catch(IOException e){
            registerError(e);
        }
    }

    private void say(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        try {
            String concatData = Arrays.stream(data, 1, data.length).reduce("", (a, b) -> a + " " + b).trim();

            short printMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
            byte concatDataIndex = (byte)constantWriter.writeRuntimeString(runtimeOutputStream, concatData);
            short systemOutFieldIndex = (short)constantWriter.writeRuntimeFieldref(runtimeOutputStream, "java/lang/System", "out", "Ljava/io/PrintStream;");

            getstatic(codeOutputStream, systemOutFieldIndex);
            ldc(codeOutputStream, concatDataIndex);
            invokevirtual(codeOutputStream, printMethodIndex);

            getMethodInfo().setStackSizeIfBigger(2);
        } catch(IOException e){
            registerError(e);
        }
    }

    private void forgive(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        try {
            if(data.length != 2){ 
                System.err.println("forgive:引数の数が違います。");
                return;
            }

            MethodInfo method = getMethodInfo();
            int variableIndex = method.getIntLocalVariableIndex(data[1]);
            if(variableIndex == -1) {
                System.err.println("forgive:「" + data[1] + "」という世界は存在しません。");
                return;
            }
            short stringBuilderClassIndex = (short)constantWriter.writeRuntimeClass(runtimeOutputStream, "java/lang/StringBuilder");
            short stringBuilderInitMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/lang/StringBuilder", "<init>", "()V");
            short stringBuilderAppendIMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
            short stringBuilderAppendSMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
            short stringBuilderToStringMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
            short printMethodIndex = (short)constantWriter.writeRuntimeMethodref(runtimeOutputStream, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
            byte baseStringIndex = (byte)constantWriter.writeRuntimeString(runtimeOutputStream, " people were forgiven.");
            short systemOutFieldIndex = (short)constantWriter.writeRuntimeFieldref(runtimeOutputStream, "java/lang/System", "out", "Ljava/io/PrintStream;");

            getstatic(codeOutputStream, systemOutFieldIndex);
            new_(codeOutputStream, stringBuilderClassIndex);
            dup(codeOutputStream);
            invokespecial(codeOutputStream, stringBuilderInitMethodIndex);
            iload_minimum(codeOutputStream, (byte)variableIndex);
            invokevirtual(codeOutputStream, stringBuilderAppendIMethodIndex);
            ldc(codeOutputStream, baseStringIndex);
            invokevirtual(codeOutputStream, stringBuilderAppendSMethodIndex);
            invokevirtual(codeOutputStream, stringBuilderToStringMethodIndex);
            invokevirtual(codeOutputStream, printMethodIndex);

            getMethodInfo().setStackSizeIfBigger(3);
        } catch(IOException e){
            registerError(e);
        }
    }
}