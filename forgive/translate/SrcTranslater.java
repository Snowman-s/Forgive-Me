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
                case "live":
                    live(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "add":
                    add(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "subtract":
                    subtract(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "multiply":
                    multiply(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "divide":
                    divide(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "mod":
                    mod(words, constantWriter, runtimeOutputStream, codeOutputStream);
                    break;
                case "reverse":
                    reverse(words, constantWriter, runtimeOutputStream, codeOutputStream);
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

    private void live(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        try {
            if(data.length < 3){ 
                System.err.println("live:引数の数が違います。");
                return;
            }

            MethodInfo method = getMethodInfo();
            byte variableIndex = (byte)method.getIntLocalVariableIndex(data[1]);
            if(variableIndex == -1) {
                System.err.println("live:「" + data[1] + "」という世界は存在しません。");
                return;
            }

            if (data[2].toLowerCase().equals("as")) {
                if(data.length != 4){ 
                    System.err.println("live:引数の数が違います。");
                    return;
                }
                byte fromVariableIndex = (byte)method.getIntLocalVariableIndex(data[3]);
                if(fromVariableIndex == -1) {
                    System.err.println("live:「" + data[3] + "」という世界は存在しません。");
                    return;
                }

                iload_minimum(codeOutputStream, fromVariableIndex);
            } else {
                //live [world] [number]
                if(data.length != 3){ 
                    System.err.println("live:引数の数が違います。");
                    return;
                }
                int number;
                try{
                    number = Integer.parseInt(data[2]);
                } catch (NumberFormatException e){
                    System.err.println("live:整数が指定されていません。");
                    return;
                }
                if(isFitTo_iconst_i(number)){
                    iconst_i(codeOutputStream, (byte)number);
                } else {
                    int integerIndex = constantWriter.writeRuntimeInteger(runtimeOutputStream, number);
                    ldc(codeOutputStream, (byte)integerIndex);
                }
            }
            istore_minimum(codeOutputStream, variableIndex);

            getMethodInfo().setStackSizeIfBigger(1);
        } catch(IOException e){
            registerError(e);
        }
    }

    private void calcOperation(CalcOperation ope, String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        String name = ope.getName();
        try {
            if(data.length < 3){ 
                System.err.println(name + ":引数の数が違います。");
                return;
            }

            MethodInfo method = getMethodInfo();
            byte variableIndex = (byte)method.getIntLocalVariableIndex(data[1]);
            if(variableIndex == -1) {
                System.err.println(name + ":「" + data[1] + "」という世界は存在しません。");
                return;
            }

            if (data[2].toLowerCase().equals("as")) {
                if(data.length != 4){ 
                    System.err.println(name + ":引数の数が違います。");
                    return;
                }
                byte fromVariableIndex = (byte)method.getIntLocalVariableIndex(data[3]);
                if(fromVariableIndex == -1) {
                    System.err.println(name + ":「" + data[3] + "」という世界は存在しません。");
                    return;
                }

                iload_minimum(codeOutputStream, variableIndex);
                iload_minimum(codeOutputStream, fromVariableIndex);
            } else {
                //[world] [number]
                if(data.length != 3){ 
                    System.err.println(name + ":引数の数が違います。");
                    return;
                }
                int number;
                try{
                    number = Integer.parseInt(data[2]);
                } catch (NumberFormatException e){
                    System.err.println(name + ":整数が指定されていません。");
                    return;
                }
                iload_minimum(codeOutputStream, variableIndex);
                if(isFitTo_iconst_i(number)){
                    iconst_i(codeOutputStream, (byte)number);
                } else {
                    int integerIndex = constantWriter.writeRuntimeInteger(runtimeOutputStream, number);
                    ldc(codeOutputStream, (byte)integerIndex);
                }
            }
            ope.writeOpecode(this, codeOutputStream);
            istore_minimum(codeOutputStream, variableIndex);

            getMethodInfo().setStackSizeIfBigger(2);
        } catch(IOException e){
            registerError(e);
        }
    }

    private void add(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        this.calcOperation(CalcOperation.ADD, data, constantWriter, runtimeOutputStream, codeOutputStream);
    }
    private void subtract(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        this.calcOperation(CalcOperation.SUBTRACT, data, constantWriter, runtimeOutputStream, codeOutputStream);
    }
    private void multiply(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        this.calcOperation(CalcOperation.MULTIPLY, data, constantWriter, runtimeOutputStream, codeOutputStream);
    }
    private void divide(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        this.calcOperation(CalcOperation.DIVIDE, data, constantWriter, runtimeOutputStream, codeOutputStream);
    }
    private void mod(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        this.calcOperation(CalcOperation.MOD, data, constantWriter, runtimeOutputStream, codeOutputStream);
    }

    private void reverse(String[] data, RuntimeConstantWriter constantWriter, OutputStream runtimeOutputStream, OutputStream codeOutputStream){
        try {
            if(data.length != 2){ 
                System.err.println("reverse:引数の数が違います。");
                return;
            }

            MethodInfo method = getMethodInfo();
            byte variableIndex = (byte)method.getIntLocalVariableIndex(data[1]);
            if(variableIndex == -1) {
                System.err.println("reverse:「" + data[1] + "」という世界は存在しません。");
                return;
            }
            iload_minimum(codeOutputStream, variableIndex);
            ineg(codeOutputStream);
            istore_minimum(codeOutputStream, variableIndex);

            getMethodInfo().setStackSizeIfBigger(1);
        } catch(IOException e){
            registerError(e);
        }
    }

    enum CalcOperation {
        ADD("add"){
            @Override
            public void writeOpecode(OpecodeWriter opecodeWriter, OutputStream outputStream) throws IOException {
                opecodeWriter.iadd(outputStream);
            }
        }, 
        SUBTRACT("subtract"){
            @Override
            public void writeOpecode(OpecodeWriter opecodeWriter, OutputStream outputStream) throws IOException {
                opecodeWriter.isub(outputStream);
            }
        }, 
        MULTIPLY("multiply"){
            @Override
            public void writeOpecode(OpecodeWriter opecodeWriter, OutputStream outputStream) throws IOException {
                opecodeWriter.imul(outputStream);
            }
        }, 
        DIVIDE("divide"){
            @Override
            public void writeOpecode(OpecodeWriter opecodeWriter, OutputStream outputStream) throws IOException {
                opecodeWriter.idiv(outputStream);
            }
        }, 
        MOD("mod"){
            @Override
            public void writeOpecode(OpecodeWriter opecodeWriter, OutputStream outputStream) throws IOException {
                opecodeWriter.irem(outputStream);
            }
        };

        CalcOperation(String name){
            this.name = name;
        }

        private final String name;
        public String getName(){
            return name;
        }

        public abstract void writeOpecode(OpecodeWriter opecodeWriter, OutputStream outputStream) throws IOException;
    }
}