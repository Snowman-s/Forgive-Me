package forgive.classfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import forgive.Integers;
import forgive.constants.AccessFlags;

public class MethodWriter {
    private final MethodInfo methodInfo;

    public MethodWriter(MethodInfo methodInfo){
        this.methodInfo = methodInfo;
    }

    public void writeMethod(int runtimeUtf8CodeIndex, int runtimeUtf8StackMapIndex, InputStream opecodeInputStream, OutputStream methodOutputStream, InputStream stackMapInputStream) throws IOException{    
        methodOutputStream.write(Integers.asByteArray(methodInfo.getAccessFlags().stream().mapToInt(AccessFlags::getData).sum(), 2));
        methodOutputStream.write(Integers.asByteArray(methodInfo.getMethodNameIndex(), 2));
        methodOutputStream.write(Integers.asByteArray(methodInfo.getMethodDescriptorIndex(), 2));
        methodOutputStream.write(new byte[]{0, 1});

        //Code:
        methodOutputStream.write(Integers.asByteArray(runtimeUtf8CodeIndex, 2));
        methodOutputStream.write(Integers.asByteArray(methodInfo.getOpecodeBytes() + 12 + 
                        (methodInfo.getStackMapTableBytes() == 0? 0: 8 + methodInfo.getStackMapTableBytes()), 4));
        //stack
        methodOutputStream.write(Integers.asByteArray(methodInfo.getStackSize(), 2));
        //locals
        methodOutputStream.write(Integers.asByteArray(methodInfo.locals().size(), 2));
        methodOutputStream.write(Integers.asByteArray(methodInfo.getOpecodeBytes(), 4));
        opecodeInputStream.transferTo(methodOutputStream);
        methodOutputStream.write(Integers.asByteArray(0, 2));
        if(methodInfo.getStackMapTableBytes() == 0) {
            methodOutputStream.write(Integers.asByteArray(0, 2));
        } else {
            methodOutputStream.write(Integers.asByteArray(1, 2));
            //StackMapTable
            methodOutputStream.write(Integers.asByteArray(runtimeUtf8StackMapIndex, 2));
            methodOutputStream.write(Integers.asByteArray(methodInfo.getStackMapTableBytes() + 2, 4));
            methodOutputStream.write(Integers.asByteArray(methodInfo.getStackMapTableCounts(), 2));

            stackMapInputStream.transferTo(methodOutputStream);
        }
    }
}