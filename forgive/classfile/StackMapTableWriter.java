package forgive.classfile;

import java.io.IOException;
import java.io.OutputStream;

import forgive.Integers;
import forgive.classfile.MethodInfo.LocalVariableInfo;

public class StackMapTableWriter {
    private final MethodInfo methodInfo;
    private OutputStream output;

    public StackMapTableWriter(MethodInfo methodInfo, OutputStream output){
        this.methodInfo = methodInfo;
        this.output = output;
    }

    public void writeStackMapTable(int targetByte) throws IOException{   
        /**
         * u1 frame_type = FULL_FRAME;  255 
         * u2 offset_delta;
         * u2 number_of_locals;
         * verification_type_info locals[number_of_locals];
         * u2 number_of_stack_items;
         * verification_type_info stack[number_of_stack_items];
         */
        int offset = methodInfo.getStackMapTableOffset(targetByte);
        if(offset < 0) return;
        int byteCount = 7;
        output.write(new byte[]{(byte)0xFF});
        output.write(Integers.asByteArray(offset, 2));
        output.write(Integers.asByteArray(methodInfo.locals().size(), 2));
        for (LocalVariableInfo local: methodInfo.locals()) {
            byteCount += local.verificationType().length;
            output.write(local.verificationType());
        }
        output.write(new byte[]{0, 0});
        methodInfo.addStackMapTable(byteCount, targetByte);
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }
}