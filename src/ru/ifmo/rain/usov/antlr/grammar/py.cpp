#include<iostream>
#include<string>
#include<cmath>
class TestClass {
public:
    int x;
    std::string string;
    float z;

    TestClass(int x, bool z = 0, std::string str = "hello") {
        this->x = x;
        this->string = str;
        this->z = sqrt(z);
    } 
    

    int func(TestClass & y) {
        for (int x = 0; x < y.x; x++) {
            this->x += sqrt(y.string.size());
        } 
        return this->x;
    } 
    

    std::string get_string() {
        return this->string;
    } 
    

    bool bool_expr() {
        return this->z < 5;
    } 
    

    bool another_bool(bool mode = false) {
        if (mode) {
            return this->x == 0 && this->z < 5;
        } else {
            return this->string.size() >= 6;
        } 
    } 
    

    float another_func() {
        return pow(this->z, (this->x / 2)) * this->x;
    } 
} 
;

bool global_func(TestClass & x, bool z = true) {
    return (x.bool_expr() || x.another_bool(z)) && !z;
} 

int main(int argc, char** argv) {
    float x = 0.0;
    while (true) {
        std::cout << x << pow(x, 2) << std::endl;
        x += pow(x, 2);
        if (x >= 100) {
            break;
        } 
    } 
        
    TestClass test_base = TestClass(0, 5);
    for (int x = 0; x < 10; x++) {
        TestClass test_instance = TestClass(test_base.string.size(), x);
        test_instance.func(test_base);
        std::string test_string = test_base.get_string();
        for (char & c : test_string) {
            if (test_instance.bool_expr() && c == 30) {
                std::cout << test_instance.another_bool() << x << c << std::endl;
            } 
        } 
    } 
    
}