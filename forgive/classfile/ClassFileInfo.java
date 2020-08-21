package forgive.classfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClassFileInfo {
    private List<RuntimeConstantField> runtimeFields = new ArrayList<>(); 
    private List<MethodInfo> methods = new ArrayList<>(); 

    public List<RuntimeConstantField> runtimeFields() {
        return List.copyOf(runtimeFields);
    }

    public List<MethodInfo> methods() {
        return List.copyOf(methods);
    }

    void addRuntimeField(byte[] data) {
        RuntimeConstantField rField = new RuntimeConstantField();
        rField.identifier = data[0];

        byte[] value = new byte[data.length - 1];
        System.arraycopy(data, 1, value, 0, value.length);
        rField.value = value;

        runtimeFields.add(rField);
    }

    public void addMethods(MethodInfo method) {
        methods.add(method);
    }

    public static class RuntimeConstantField {
        private byte identifier;
        private byte[] value;

        public RuntimeConstantType getIdentifier() {
            Optional<RuntimeConstantType> result = Arrays.stream(RuntimeConstantType.values()).filter(r -> r.identifier == this.identifier).findAny();
            if(result.isPresent()) {
                return result.get();
            } else {
                return RuntimeConstantType.Undefind;
            }
        }
        
        public byte[] getValue() {
            byte[] copy = new byte[value.length];
            System.arraycopy(value, 0, copy, 0, value.length);
            return copy;
        }
    }

    public static enum RuntimeConstantType{
        Undefind(0),
        Utf8(1),
        Integer(3),
        Float(4),
        Long(5),
        Double(6),
        Class(7),
        String(8),
        Fieldref(9),
        Methodref(10),
        InterfaceMethodref(11),
        NameAndType(12),
        MethodHandle(15),
        MethodType(16),
        Dynamic(17),
        InvokeDynamic(18),
        Module(19),
        Package(20);
        
        private byte identifier;
        RuntimeConstantType(int identifier) {
            this.identifier = (byte)identifier;
        }

        public byte getIdentifier() {
            return identifier;
        }
    }
}