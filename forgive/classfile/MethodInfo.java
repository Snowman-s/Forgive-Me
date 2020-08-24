package forgive.classfile;

import java.util.Set;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forgive.constants.AccessFlags;

public class MethodInfo {
    private int methodNameIndex = -1;
    private int methodDescriptorIndex = -1;
    private Set<AccessFlags> accessFlags;
    private String methodName;
    private String methodDescriptor;
    private int opecodeBytes = 0;
    private int stackSize = 1;
    private List<LocalVariableInfo> locals = new ArrayList<>();
    private Map<String, Integer> bookMarks = new HashMap<>();
    private int stackMapTableOffsetByte = -1;
    private int stackMapTableBytes = 0;
    private int stackMapTableCounts = 0;

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
     * {@link RuntimeConstantWriter#writeRuntimeUserMethod(java.io.OutputStream, MethodInfo)}を呼んでからでなければ正しく動作しません
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

    public void addLocals(LocalVariableInfo info) {
        this.locals.add(info);
    }

    public List<LocalVariableInfo> locals() {
        return List.copyOf(locals);
    }

    public int getIntLocalVariableIndex(String name){
        for (int i = 0; i < locals.size(); i++) {
            if(locals.get(i).name.equals(name)){
                return i;
            }
        }

        return -1;
    }

    public Map<String, Integer> bookMarks() {
        return Map.copyOf(bookMarks);
    }

    /**
     * @return nameの登録に完了したならtrue, nameが既に存在するならfalse
     */
    public boolean addBookMarks(String name){
        if(bookMarks.containsKey(name)){
            return false;
        }
        bookMarks.put(name, opecodeBytes);
        return true;
    }

    public boolean existBookMark(String name){
        return bookMarks.containsKey(name);
    }

    public int getBookMarkByte(String name) {
        return bookMarks.get(name);
    }
    
    public int getBookMarkByteAsRelative(String name, int origin) {
        return bookMarks.get(name) - origin;
    }

    public int getStackMapTableOffset(int target) {
        return target - stackMapTableOffsetByte - 1;
    }

    public void addStackMapTable(int bytes, int target){
        stackMapTableBytes += bytes;
        stackMapTableOffsetByte = target;
        stackMapTableCounts++;
    }

    public int getStackMapTableBytes() {
        return stackMapTableBytes;
    }

    public int getStackMapTableCounts() {
        return stackMapTableCounts;
    }

    public static class LocalVariableInfo {
        private String name;
        private byte[] verificationType;

        public String name() {
            return name;
        }

        public byte[] verificationType() {
            byte[] copy = new byte[verificationType.length];
            System.arraycopy(verificationType, 0, copy, 0, verificationType.length);
            return verificationType;
        }

        private boolean isIntVariable(){
            return verificationType[0] == 1;
        }

        public static LocalVariableInfo objectVariableOf(String name, short classIndex){
            LocalVariableInfo info = new LocalVariableInfo();
            info.name = name;
            info.verificationType = new byte[]{7, (byte)(classIndex >> 8), (byte)(classIndex & 0xFF)};

            return info;
        }

        public static LocalVariableInfo intVariableOf(String name){
            LocalVariableInfo info = new LocalVariableInfo();
            info.name = name;
            info.verificationType = new byte[]{1};

            return info;
        }
    }
}