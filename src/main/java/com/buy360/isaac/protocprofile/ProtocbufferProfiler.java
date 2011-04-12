package com.buy360.isaac.protocprofile;

import com.buy360.isaac.protocprofile.protocbuffers.AddressBookProtos.Person;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtocbufferProfiler extends AbstractProfiler {
    public ProtocbufferProfiler(boolean equalTarget) {
        super(equalTarget, "Protoc Buffers");
    }

    @Override
    protected Object buildInstance(int id) {
        Person john = null;
        if (equalTarget) {
            john = Person
                    .newBuilder()
                    .setId(ID)
                    .setName(NAME)
                    .setEmail(EMAIL)
                    .addPhone(Person.PhoneNumber.newBuilder().setNumber(NUMBER_HOME).setType(Person.PhoneType.HOME))
                    .addPhone(Person.PhoneNumber.newBuilder().setNumber(NUMBER_MOBILE).setType(Person.PhoneType.MOBILE))
                    .build();
        } else {
            john = Person
                    .newBuilder()
                    .setId(id)
                    .setName(NAME + id)
                    .setEmail(EMAIL + id)
                    .addPhone(Person.PhoneNumber.newBuilder().setNumber(NUMBER_HOME + id).setType(Person.PhoneType.HOME))
                    .addPhone(
                            Person.PhoneNumber.newBuilder().setNumber(NUMBER_MOBILE + id)
                                    .setType(Person.PhoneType.MOBILE)).build();
        }
        return john;
    }

    @Override
    protected byte[] serializeObject(Object instance) {
        return ((Person) instance).toByteArray();
    }

    @Override
    protected void deserializeObject(byte[] byteArray) throws InvalidProtocolBufferException {
        Person.parseFrom(byteArray);
    }

}
