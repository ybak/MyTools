package com.buy360.isaac.protocprofile;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

import com.buy360.isaac.protocprofile.thrift.Person;
import com.buy360.isaac.protocprofile.thrift.PhoneNumber;
import com.buy360.isaac.protocprofile.thrift.PhoneType;

public class ThriftProfiler extends AbstractProfiler {

    private TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());

    private TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());

    public ThriftProfiler(boolean equalTarget) {
        super(equalTarget, "thrift");
    }

    @Override
    protected Object buildInstance(int id) {
        Person person = null;
        if (equalTarget) {
            List<PhoneNumber> phones = new ArrayList<PhoneNumber>();

            PhoneNumber homePhoneNumber = new PhoneNumber(NUMBER_HOME);
            homePhoneNumber.setType(PhoneType.HOME);
            phones.add(homePhoneNumber);

            PhoneNumber mobilePhoneNumber = new PhoneNumber(NUMBER_MOBILE);
            homePhoneNumber.setType(PhoneType.MOBILE);
            phones.add(mobilePhoneNumber);
            
            person = new Person(NAME, ID, phones);
            person.setEmail(EMAIL);
        } else {
            List<PhoneNumber> phones = new ArrayList<PhoneNumber>();

            PhoneNumber homePhoneNumber = new PhoneNumber(NUMBER_HOME + id);
            homePhoneNumber.setType(PhoneType.HOME);
            phones.add(homePhoneNumber);

            PhoneNumber mobilePhoneNumber = new PhoneNumber(NUMBER_MOBILE + id);
            homePhoneNumber.setType(PhoneType.MOBILE);
            phones.add(mobilePhoneNumber);
            
            person = new Person(NAME + id, ID + id, phones);
            person.setEmail(EMAIL + id);
        }
        return person;
    }

    @Override
    protected byte[] serializeObject(Object instance) throws Exception {
        return serializer.serialize((TBase) instance);
    }

    @Override
    protected void deserializeObject(byte[] byteArray) throws Exception {
        Person person = new Person();
        deserializer.deserialize(person, byteArray);
    }

}
