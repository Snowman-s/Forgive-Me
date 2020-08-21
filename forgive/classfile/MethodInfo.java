package forgive.classfile;

public class MethodInfo {
    private int methodNameIndex = -1;
    private int methodDescriptorIndex = -1;
    private String methodName;
    private String methodDescriptor;
    private int opecodeBytes = 0;

    public MethodInfo(String methodName, String methodDescriptor){
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
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
}