package com.buy360.isaac.protocprofile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Ice.Communicator;
import Ice.InputStream;
import Ice.OutputStream;
import Ice.Util;

import com.buy360.isaac.protocprofile.javaice.Person;
import com.buy360.isaac.protocprofile.javaice.PhoneNumber;
import com.buy360.isaac.protocprofile.javaice.PhoneType;

public class IceProfiler extends AbstractProfiler {

    private Communicator communicator = Util.initialize();
    
    public IceProfiler(boolean equalTarget) {
        super(equalTarget, "ice");
    }

    @Override
    protected Object buildInstance(int id) {
        Person person = null;
        if (equalTarget) {
            List<PhoneNumber> phones = new ArrayList<PhoneNumber>();
            PhoneNumber homePhoneNumber = new PhoneNumber(NUMBER_HOME, PhoneType.HOME);
            phones.add(homePhoneNumber);
            PhoneNumber mobilePhoneNumber = new PhoneNumber(NUMBER_MOBILE, PhoneType.MOBILE);
            phones.add(mobilePhoneNumber);
            person = new Person(NAME, ID, EMAIL, phones);
        } else {
            List<PhoneNumber> phones = new ArrayList<PhoneNumber>();
            PhoneNumber homePhoneNumber = new PhoneNumber(NUMBER_HOME + id, PhoneType.HOME);
            phones.add(homePhoneNumber);
            PhoneNumber mobilePhoneNumber = new PhoneNumber(NUMBER_MOBILE + id, PhoneType.MOBILE);
            phones.add(mobilePhoneNumber);
            person = new Person(NAME + id, ID + id, EMAIL + id, phones);
        }
        return person;
    }

    @Override
    protected byte[] serializeObject(Object instance) throws Exception {
        OutputStream os = Util.createOutputStream(communicator);
        os.writeSerializable((Serializable) instance);
        return os.finished();
    }

    @Override
    protected void deserializeObject(byte[] byteArray) throws Exception {
        InputStream is = Util.createInputStream(communicator, byteArray);
        is.readSerializable();
    }
    
    protected void destory() {
        communicator.destroy();
    }

}
