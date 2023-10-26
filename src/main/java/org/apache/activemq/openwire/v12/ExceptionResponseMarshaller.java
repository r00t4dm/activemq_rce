//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.activemq.openwire.v12;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.OpenWireFormat;

public class ExceptionResponseMarshaller extends ResponseMarshaller {
    public ExceptionResponseMarshaller() {
    }

    public byte getDataStructureType() {
        return 31;
    }

    public DataStructure createObject() {
        return new ExceptionResponse();
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
        super.tightUnmarshal(wireFormat, o, dataIn, bs);
        ExceptionResponse info = (ExceptionResponse)o;
        info.setException(this.tightUnmarsalThrowable(wireFormat, dataIn, bs));
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
//        ExceptionResponse info = (ExceptionResponse)o;
//        int rc = super.tightMarshal1(wireFormat, o, bs);
        int rc = 1;
        rc += this.tightMarshalThrowable1(wireFormat,o, bs);
        return rc + 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
        super.tightMarshal2(wireFormat, o, dataOut, bs);
        this.tightMarshalThrowable2(wireFormat, o, dataOut, bs);
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
        super.looseUnmarshal(wireFormat, o, dataIn);
        ExceptionResponse info = (ExceptionResponse)o;
        info.setException(this.looseUnmarsalThrowable(wireFormat, dataIn));
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
        super.looseMarshal(wireFormat, o, dataOut);
        this.looseMarshalThrowable(wireFormat, o, dataOut);
    }
}
