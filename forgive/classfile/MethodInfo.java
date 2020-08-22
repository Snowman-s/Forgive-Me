package forgive.classfile;

import java.util.Set;
import java.util.EnumSet;

import forgive.constants.AccessFlags;

public class MethodInfo {
    private int methodNameIndex = -1;
    private int methodDescriptorIndex = -1;
    private Set<AccessFlags> accessFlags;
    private String methodName;
    private String methodDescriptor;
    private int opecodeBytes = 0;
    private int stackSize = 1;
    private int locals = 0;

    public MethodInfo(Set<AccessFlags> accessFlagSet, String methodName, String methodDescriptor){
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.accessFlags = accessFlagSet;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodDescriptor() {
        return methodDescriptor;
    }

    public int getMethodNameIndex() {
        return methodNameIndex;
    }

    public int getMethodDescriptorIndex() {
        return methodDescriptorIndex;
    }

    /**
     * このメソッドの識別子を得ます
     * {@link RuntimeConstantWriter#writeRuntimeUserMethod(java.io.OutputStream, MethodInfo)}を呼んでからでなければ正しく動作しません動作
     * @return
     */
    public int getIdentity() {
        return methodNameIndex << 16 + methodDescriptorIndex;
    }

    void setMethodNameIndex(int methodNameIndex) {
        this.methodNameIndex = methodNameIndex;
    }

    void setMethodDescriptorIndex(int methodDescriptorIndex) {
        this.methodDescriptorIndex = methodDescriptorIndex;
    }

    void addOpecodeBytes(int bytes){
        opecodeBytes += bytes;
    }

    public int getOpecodeBytes() {
        return opecodeBytes;
    }

    public Set<AccessFlags> getAccessFlags() {
        return EnumSet.copyOf(accessFlags);
    }

    public void setStackSizeIfBigger(int stackSize) {
        if(stackSize > this.stackSize){
            this.stackSize = stackSize;
        }
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setLocals(int locals) {
        this.locals = locals;
    }

    public int getLocals() {
        return locals;
    }

    public void addLocals(int add) {
        this.locals += locals;
    }
}