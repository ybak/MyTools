[["java:package:com.buy360.isaac.protocprofile"]]

module javaice {
    enum PhoneType { MOBILE, HOME, WORK };
    
    struct PhoneNumber {
        string number;
        PhoneType type;
    };
    
    sequence<PhoneNumber> Phones;  
    
    struct Person {
      string name; 
      int id;
      string email;
      ["java:type:java.util.LinkedList"] Phones phone; 
    };
};