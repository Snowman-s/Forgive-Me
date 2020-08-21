package forgive.classfile;

import java.io.IOException;
import java.io.OutputStream;

public class OpecodeWriter {
    private final MethodInfo methodInfo;

    public OpecodeWriter(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    protected void write(OutputStream out, byte[] bytes) throws IOException {
        methodInfo.addOpecodeBytes(bytes.length);

        out.write(bytes);
    }

    public void nop(OutputStream out) throws IOException {
        write(out, new byte[]{0});
    }

    public void aconst_null(OutputStream out) throws IOException {
        write(out, new byte[]{1});
    }

    public void iconst_i(OutputStream out, byte i) throws IOException {
        write(out, new byte[]{(byte)(3 + i)});
    }
    
    public void lconst_l(OutputStream out, byte l) throws IOException {
        write(out, new byte[]{(byte)(9 + l)});
    }
    
    public void fconst_f(OutputStream out, byte f) throws IOException {
        write(out, new byte[]{(byte)(0xB + f)});
    }

    public void dconst_d(OutputStream out, byte d) throws IOException {
        write(out, new byte[]{(byte)(0xE + d)});
    }

    public void bipush(OutputStream out, byte b) throws IOException {
        write(out, new byte[]{0x10, b});
    }

    public void sipush(OutputStream out, short s) throws IOException {
        write(out, new byte[]{0x11, (byte)(s >>> 8), (byte)(s & 0xFF)});
    }

    public void sipush(OutputStream out, byte l) throws IOException {
        write(out, new byte[]{0x12, l});
    }

    public void ldc_w(OutputStream out, short l) throws IOException {
        write(out, new byte[]{0x13, (byte)(l >>> 8), (byte)(l & 0xFF)});
    }
    
    public void ldc2_w(OutputStream out, short l) throws IOException {
        write(out, new byte[]{0x14, (byte)(l >>> 8), (byte)(l & 0xFF)});
    }

    public void iload(OutputStream out, byte i) throws IOException {
        write(out, new byte[]{0x15, i});
    }

    public void lload(OutputStream out, byte l) throws IOException {
        write(out, new byte[]{0x16, l});
    }

    public void fload(OutputStream out, byte f) throws IOException {
        write(out, new byte[]{0x17, f});
    }

    public void dload(OutputStream out, byte d) throws IOException {
        write(out, new byte[]{0x18, d});
    }

    public void aload(OutputStream out, byte a) throws IOException {
        write(out, new byte[]{0x19, a});
    }

    public void iload_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x1A + n)});
    }
    
    public void lload_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x1E + n)});
    }

    public void fload_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x22 + n)});
    }

    public void dload_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x26 + n)});
    }
    
    public void aload_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x2A + n)});
    }

    public void iaload(OutputStream out) throws IOException {
        write(out, new byte[]{0x2E});
    }

    public void laload(OutputStream out) throws IOException {
        write(out, new byte[]{0x2F});
    }

    public void faload(OutputStream out) throws IOException {
        write(out, new byte[]{0x30});
    }

    public void daload(OutputStream out) throws IOException {
        write(out, new byte[]{0x31});
    }

    public void aaload(OutputStream out) throws IOException {
        write(out, new byte[]{0x32});
    }

    public void baload(OutputStream out) throws IOException {
        write(out, new byte[]{0x33});
    }

    public void caload(OutputStream out) throws IOException {
        write(out, new byte[]{0x34});
    }

    public void saload(OutputStream out) throws IOException {
        write(out, new byte[]{0x35});
    }

    public void istore(OutputStream out, byte i) throws IOException {
        write(out, new byte[]{0x36, i});
    }

    public void lstore(OutputStream out, byte l) throws IOException {
        write(out, new byte[]{0x37, l});
    }

    public void fstore(OutputStream out, byte f) throws IOException {
        write(out, new byte[]{0x38, f});
    }

    public void dstore(OutputStream out, byte d) throws IOException {
        write(out, new byte[]{0x39, d});
    }

    public void astore(OutputStream out, byte a) throws IOException {
        write(out, new byte[]{0x3A, a});
    }

    public void istore_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x3B + n)});
    }

    public void lstore_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x3F + n)});
    }

    public void fstore_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x43 + n)});
    }

    public void dstore_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x47 + n)});
    }

    public void astore_n(OutputStream out, byte n) throws IOException {
        write(out, new byte[]{(byte)(0x4B + n)});
    }

    public void iastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x4F});
    }

    public void lastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x50});
    }

    public void fastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x51});
    }

    public void dastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x52});
    }

    public void aastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x53});
    }

    public void bastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x54});
    }

    public void castore(OutputStream out) throws IOException {
        write(out, new byte[]{0x55});
    }

    public void sastore(OutputStream out) throws IOException {
        write(out, new byte[]{0x56});
    }

    public void pop(OutputStream out) throws IOException {
        write(out, new byte[]{0x57});
    }

    public void pop2(OutputStream out) throws IOException {
        write(out, new byte[]{0x58});
    }

    public void dup(OutputStream out) throws IOException {
        write(out, new byte[]{0x59});
    }

    public void dup_x1(OutputStream out) throws IOException {
        write(out, new byte[]{0x5A});
    }

    public void dup_x2(OutputStream out) throws IOException {
        write(out, new byte[]{0x5B});
    }

    public void dup2(OutputStream out) throws IOException {
        write(out, new byte[]{0x5C});
    }

    public void dup2_x1(OutputStream out) throws IOException {
        write(out, new byte[]{0x5D});
    }

    public void dup2_x2(OutputStream out) throws IOException {
        write(out, new byte[]{0x5E});
    }

    public void swap(OutputStream out) throws IOException {
        write(out, new byte[]{0x5F});
    }

    public void iadd(OutputStream out) throws IOException {
        write(out, new byte[]{0x60});
    }

    public void ladd(OutputStream out) throws IOException {
        write(out, new byte[]{0x61});
    }

    public void fadd(OutputStream out) throws IOException {
        write(out, new byte[]{0x62});
    }

    public void dadd(OutputStream out) throws IOException {
        write(out, new byte[]{0x63});
    }

    public void isub(OutputStream out) throws IOException {
        write(out, new byte[]{0x64});
    }

    public void lsub(OutputStream out) throws IOException {
        write(out, new byte[]{0x65});
    }

    public void fsub(OutputStream out) throws IOException {
        write(out, new byte[]{0x66});
    }

    public void dsub(OutputStream out) throws IOException {
        write(out, new byte[]{0x67});
    }

    public void imul(OutputStream out) throws IOException {
        write(out, new byte[]{0x68});
    }

    public void lmul(OutputStream out) throws IOException {
        write(out, new byte[]{0x69});
    }

    public void fmul(OutputStream out) throws IOException {
        write(out, new byte[]{0x6A});
    }

    public void dmul(OutputStream out) throws IOException {
        write(out, new byte[]{0x6B});
    }

    public void idiv(OutputStream out) throws IOException {
        write(out, new byte[]{0x6C});
    }

    public void ldiv(OutputStream out) throws IOException {
        write(out, new byte[]{0x6D});
    }

    public void fdiv(OutputStream out) throws IOException {
        write(out, new byte[]{0x6E});
    }

    public void ddiv(OutputStream out) throws IOException {
        write(out, new byte[]{0x6F});
    }

    public void irem(OutputStream out) throws IOException {
        write(out, new byte[]{0x70});
    }

    public void lrem(OutputStream out) throws IOException {
        write(out, new byte[]{0x71});
    }

    public void frem(OutputStream out) throws IOException {
        write(out, new byte[]{0x72});
    }

    public void drem(OutputStream out) throws IOException {
        write(out, new byte[]{0x73});
    }

    public void ineg(OutputStream out) throws IOException {
        write(out, new byte[]{0x74});
    }

    public void lneg(OutputStream out) throws IOException {
        write(out, new byte[]{0x75});
    }

    public void fneg(OutputStream out) throws IOException {
        write(out, new byte[]{0x76});
    }

    public void dneg(OutputStream out) throws IOException {
        write(out, new byte[]{0x77});
    }

    public void ishl(OutputStream out) throws IOException {
        write(out, new byte[]{0x78});
    }
    
    public void lshl(OutputStream out) throws IOException {
        write(out, new byte[]{0x79});
    }
    
    public void ishr(OutputStream out) throws IOException {
        write(out, new byte[]{0x7A});
    }
    
    public void lshr(OutputStream out) throws IOException {
        write(out, new byte[]{0x7B});
    }

    public void iushr(OutputStream out) throws IOException {
        write(out, new byte[]{0x7C});
    }

    public void lushr(OutputStream out) throws IOException {
        write(out, new byte[]{0x7D});
    }

    public void iand(OutputStream out) throws IOException {
        write(out, new byte[]{0x7E});
    }

    public void land(OutputStream out) throws IOException {
        write(out, new byte[]{0x7F});
    }
    
    public void ior(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x80)});
    }

    public void lor(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x81)});
    }

    public void ixor(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x82)});
    }

    public void lxor(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x83)});
    }

    public void iinc(OutputStream out, byte v, byte b) throws IOException {
        write(out, new byte[]{(byte)(0x84), v, b});
    }

    public void i2l(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x85)});
    }

    public void i2f(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x86)});
    }

    public void i2d(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x87)});
    }

    public void l2i(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x88)});
    }

    public void l2f(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x89)});
    }

    public void l2d(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x8A)});
    }
    
    public void f2i(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x8B)});
    }

    public void f2l(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x8C)});
    }

    public void f2d(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x8D)});
    }

    public void d2i(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x8E)});
    }

    public void d2l(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x8F)});
    }

    public void d2f(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x90)});
    }

    public void i2b(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x91)});
    }

    public void i2c(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x92)});
    }

    public void i2s(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x93)});
    }

    public void lcmp(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x94)});
    }

    public void fcmpl(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x95)});
    }

    public void fcmpg(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x96)});
    }

    public void dcmpl(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x97)});
    }

    public void dcmpg(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0x98)});
    }

    public void ifeq(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x99), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void ifne(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x9A), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void iflt(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x9B), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void ifge(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x9C), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }    

    public void ifgt(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x9D), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void ifle(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x9E), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void if_icmpeq(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0x9F), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void if_icmpne(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA0), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }
    
    public void if_icmplt(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA1), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }
    
    public void if_icmpge(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA2), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void if_icmpgt(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA3), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void if_icmple(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA4), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void if_acmpeq(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA5), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void if_acmpne(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA6), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void goto_(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA7), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void jsr(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA8), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void ret(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xA9), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void tableswitch(OutputStream out) throws IOException {
        //0xAA
        throw new UnimplementOpecodeException();
    }

    public void lookupswitch(OutputStream out) throws IOException {
        //0x75
        throw new UnimplementOpecodeException();
    }

    public void ireturn(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xAC)});
    }

    public void lreturn(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xAD)});
    }
    
    public void freturn(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xAE)});
    }

    public void dreturn(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xAF)});
    }

    public void areturn(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xB0)});
    }
    
    public void return_(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xB1)});
    }

    public void getstatic(OutputStream out, short s) throws IOException {
        write(out, new byte[]{(byte)(0xB2), (byte)(s >>> 8), (byte)(s & 0xFF)});
    }

    public void putstatic(OutputStream out, short s) throws IOException {
        write(out, new byte[]{(byte)(0xB3), (byte)(s >>> 8), (byte)(s & 0xFF)});
    }

    public void getfield(OutputStream out, short f) throws IOException {
        write(out, new byte[]{(byte)(0xB4), (byte)(f >>> 8), (byte)(f & 0xFF)});
    }

    public void putfield(OutputStream out, short f) throws IOException {
        write(out, new byte[]{(byte)(0xB5), (byte)(f >>> 8), (byte)(f & 0xFF)});
    }

    public void invokevirtual(OutputStream out, short v) throws IOException {
        write(out, new byte[]{(byte)(0xB6), (byte)(v >>> 8), (byte)(v & 0xFF)});
    }

    public void invokespecial(OutputStream out, short s) throws IOException {
        write(out, new byte[]{(byte)(0xB7), (byte)(s >>> 8), (byte)(s & 0xFF)});
    }

    public void invokestatic(OutputStream out, short s) throws IOException {
        write(out, new byte[]{(byte)(0xB8), (byte)(s >>> 8), (byte)(s & 0xFF)});
    }

    public void invokeinterface(OutputStream out, short index, byte count) throws IOException {
        write(out, new byte[]{(byte)(0xB9), (byte)(index >>> 8), (byte)(index & 0xFF), count, 0});
    }

    public void invokedynamic(OutputStream out, short index) throws IOException {
        write(out, new byte[]{(byte)(0xBA), (byte)(index >>> 8), (byte)(index & 0xFF), 0, 0});
    }

    public void new_(OutputStream out, short index) throws IOException {
        write(out, new byte[]{(byte)(0xBB), (byte)(index >>> 8), (byte)(index & 0xFF)});
    }

    public void newarray(OutputStream out, byte i) throws IOException {
        write(out, new byte[]{(byte)(0xBC), i});
    }

    public void anewarray(OutputStream out, short index) throws IOException {
        write(out, new byte[]{(byte)(0xBD), (byte)(index >>> 8), (byte)(index & 0xFF)});
    }

    public void arraylength(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xBE)});
    }

    public void athrow(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xBF)});
    }

    public void checkcast(OutputStream out, short index) throws IOException {
        write(out, new byte[]{(byte)(0xC0), (byte)(index >>> 8), (byte)(index & 0xFF)});
    }

    public void instanceof_(OutputStream out, short index) throws IOException {
        write(out, new byte[]{(byte)(0xC1), (byte)(index >>> 8), (byte)(index & 0xFF)});
    }

    public void monitorenter(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xC2)});
    }

    public void monitorexit(OutputStream out) throws IOException {
        write(out, new byte[]{(byte)(0xC3)});
    }

    public void wide(OutputStream out, byte opecode, short index) throws IOException {
        write(out, new byte[]{(byte)(0xC4), opecode, (byte)(index >>> 8), (byte)(index & 0xFF)});
    }

    public void wide_iinc(OutputStream out, byte opecode, short index, short num) throws IOException {
        write(out, new byte[]{(byte)(0xC4), opecode, (byte)(index >>> 8), (byte)(index & 0xFF), (byte)(num >>> 8), (byte)(num & 0xFF)});
    }

    public void multianewarray(OutputStream out, short index) throws IOException {
        write(out, new byte[]{(byte)(0xC5), (byte)(index >>> 8), (byte)(index & 0xFF)});
    }

    public void ifnull(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xC6), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }
    
    public void ifnonnull(OutputStream out, short go) throws IOException {
        write(out, new byte[]{(byte)(0xC7), (byte)(go >>> 8), (byte)(go & 0xFF)});
    }

    public void goto_w(OutputStream out, int go) throws IOException {
        //0xC8
        throw new UnimplementOpecodeException();
    }

    public void jsr_w(OutputStream out, int go) throws IOException {
        //0xC9
        throw new UnimplementOpecodeException();
    }

    public void aload_minimum(OutputStream out, byte n) throws IOException {
        if(n <= 3) {
            aload_n(out, n);
        } else {
            aload(out, n);
        }
    }

    public void astore_minimum(OutputStream out, byte n) throws IOException {
        if(n <= 3) {
            astore_n(out, n);
        } else {
            astore(out, n);
        }
    }

    public void iload_minimum(OutputStream out, byte n) throws IOException {
        if(n <= 3) {
            iload_n(out, n);
        } else {
            iload(out, n);
        }
    }

    public void istore_minimum(OutputStream out, byte n) throws IOException {
        if(n <= 3) {
            istore_n(out, n);
        } else {
            istore(out, n);
        }
    }

    class UnimplementOpecodeException extends RuntimeException{

    }
}