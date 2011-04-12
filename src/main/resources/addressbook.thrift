namespace java com.buy360.isaac.protocprofile.thrift

typedef i32 int
typedef i64 long

enum PhoneType {
    MOBILE = 0,
    HOME = 1,
    WORK = 2,
}

struct PhoneNumber{
  1: required string number,
  2: optional PhoneType type,  
}

struct Person {
  1: required string name,  
  2: required int id,  
  3: optional string email,  
  4: required list<PhoneNumber> phone,
}