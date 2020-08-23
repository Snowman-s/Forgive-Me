package forgive.classfile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import forgive.Integers;
import forgive.classfile.ClassFileInfo.RuntimeConstantField;
import forgive.classfile.ClassFileInfo.RuntimeConstantType;

public class RuntimeConstantWriter {
    private final ClassFileInfo classFileInfo;

    public RuntimeConstantWriter(ClassFileInfo classFileInfo){
        this.classFileInfo = classFileInfo;
    }

    public int writeRuntimeUTF8(OutputStream outputStream, String string) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();
        byte[] stringByte = string.getBytes(Charset.forName("UTF-8"));

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.Utf8 && 
                Arrays.equals(runtimeField.getValue(), 2, runtimeField.getValue().length,
                                stringByte, 0, stringByte.length)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[3 + stringByte.length];
        data[0] = ClassFileInfo.RuntimeConstantType.Utf8.getIdentifier();
        data[1] = (byte)(stringByte.length >>> 8);
        data[2] = (byte)(stringByte.length & 0xFF);
        System.arraycopy(stringByte, 0, data, 3, stringByte.length);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }

    public int writeRuntimeInteger(OutputStream outputStream, int integer) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();
        byte[] dataByte = Integers.asByteArray(integer, 4);

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.Integer && 
                Arrays.equals(runtimeField.getValue(), 1, runtimeField.getValue().length,
                                dataByte, 0, dataByte.length)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[5];
        data[0] = ClassFileInfo.RuntimeConstantType.Integer.getIdentifier();
        System.arraycopy(dataByte, 0, data, 1, 4);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }
    
    public int writeRuntimeClass(OutputStream outputStream, String className) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();

        int utf8Index = writeRuntimeUTF8(outputStream, className);

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.Class &&
                runtimeField.getValue()[0] == utf8Index >>> 8 &&
                runtimeField.getValue()[1] == (utf8Index & 0xFF)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[3];
        data[0] = ClassFileInfo.RuntimeConstantType.Class.getIdentifier();
        data[1] = (byte)(utf8Index >>> 8);
        data[2] = (byte)(utf8Index & 0xFF);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }

    public int writeRuntimeString(OutputStream outputStream, String string) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();

        int utf8Index = writeRuntimeUTF8(outputStream, string);

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.String &&
                runtimeField.getValue()[0] == utf8Index >>> 8 &&
                runtimeField.getValue()[1] == (utf8Index & 0xFF)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[3];
        data[0] = ClassFileInfo.RuntimeConstantType.String.getIdentifier();
        data[1] = (byte)(utf8Index >>> 8);
        data[2] = (byte)(utf8Index & 0xFF);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }

    public int writeRuntimeFieldref(OutputStream outputStream, String className, String fieldName, String descriptor) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();

        int classIndex = writeRuntimeClass(outputStream, className);
        int nameAndTypeIndex = writeRuntimeNameAndType(outputStream, fieldName, descriptor);

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.Fieldref &&
                runtimeField.getValue()[0] == classIndex >>> 8 &&
                runtimeField.getValue()[1] == (classIndex & 0xFF) &&
                runtimeField.getValue()[2] == nameAndTypeIndex >>> 8 &&
                runtimeField.getValue()[3] == (nameAndTypeIndex & 0xFF)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[5];
        data[0] = ClassFileInfo.RuntimeConstantType.Fieldref.getIdentifier();
        data[1] = (byte)(classIndex >>> 8);
        data[2] = (byte)(classIndex & 0xFF);
        data[3] = (byte)(nameAndTypeIndex >>> 8);
        data[4] = (byte)(nameAndTypeIndex & 0xFF);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }

    public int writeRuntimeMethodref(OutputStream outputStream, String className, String methodName, String descriptor) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();

        int classIndex = writeRuntimeClass(outputStream, className);
        int nameAndTypeIndex = writeRuntimeNameAndType(outputStream, methodName, descriptor);

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.Methodref &&
                runtimeField.getValue()[0] == classIndex >>> 8 &&
                runtimeField.getValue()[1] == (classIndex & 0xFF) &&
                runtimeField.getValue()[2] == nameAndTypeIndex >>> 8 &&
                runtimeField.getValue()[3] == (nameAndTypeIndex & 0xFF)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[5];
        data[0] = ClassFileInfo.RuntimeConstantType.Methodref.getIdentifier();
        data[1] = (byte)(classIndex >>> 8);
        data[2] = (byte)(classIndex & 0xFF);
        data[3] = (byte)(nameAndTypeIndex >>> 8);
        data[4] = (byte)(nameAndTypeIndex & 0xFF);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }

    public int writeRuntimeNameAndType(OutputStream outputStream, String name, String descriptor) throws IOException{
        List<ClassFileInfo.RuntimeConstantField> runtimeFields = classFileInfo.runtimeFields();

        int nameIndex = writeRuntimeUTF8(outputStream, name);
        int descriptorIndex = writeRuntimeUTF8(outputStream, descriptor);

        //既にあるか検査
        for (int i = 0; i < runtimeFields.size(); i++) {
            RuntimeConstantField runtimeField = runtimeFields.get(i);
            if(runtimeField.getIdentifier() == RuntimeConstantType.NameAndType &&
                runtimeField.getValue()[0] == nameIndex >>> 8 &&
                runtimeField.getValue()[1] == (nameIndex & 0xFF) &&
                runtimeField.getValue()[2] == descriptorIndex >>> 8 &&
                runtimeField.getValue()[3] == (descriptorIndex & 0xFF)){
                return i + 1;
            }
        }
        
        byte[] data = new byte[5];
        data[0] = ClassFileInfo.RuntimeConstantType.NameAndType.getIdentifier();
        data[1] = (byte)(nameIndex >>> 8);
        data[2] = (byte)(nameIndex & 0xFF);
        data[3] = (byte)(descriptorIndex >>> 8);
        data[4] = (byte)(descriptorIndex & 0xFF);

        outputStream.write(data);
        classFileInfo.addRuntimeField(data);

        return classFileInfo.runtimeFields().size();
    }

    public void writeRuntimeUserMethod(OutputStream outputStream, MethodInfo method) throws IOException{
        int nameIndex = writeRuntimeUTF8(outputStream, method.getMethodName());
        int descriptorIndex = writeRuntimeUTF8(outputStream, method.getMethodDescriptor());

        method.setMethodNameIndex(nameIndex);
        method.setMethodDescriptorIndex(descriptorIndex);
    }
}