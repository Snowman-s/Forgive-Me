package forgive.translate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import forgive.classfile.MethodInfo;
import forgive.classfile.OpecodeWriter;
import forgive.classfile.RuntimeConstantWriter;

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
                case "say":
                    say(words, constantWriter, runtimeOutputStream, codeOutputStream);
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
}