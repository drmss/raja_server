package ir.msisoft.Models;

import org.json.JSONObject;

public class Person {
    private String first_name;
    private String last_name;
    private String code_melli;
    private int age;

    public Person(String first_name, String last_name, String code_melli, int age) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.code_melli = code_melli;
        this.age = age;
    }

    public Person(JSONObject o) {
        this.first_name = o.getString("first_name");
        this.last_name = o.getString("last_name");
        this.code_melli = o.getString("code_melli");
        this.age = o.getInt("age");
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCode_melli(String code_melli) {
        this.code_melli = code_melli;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getCode_melli() {
        return code_melli;
    }

    public int getAge() {
        return age;
    }
}
