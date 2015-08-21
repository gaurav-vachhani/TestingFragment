package com.testingfragment;

import java.io.Serializable;

/**
 * Created by indianic on 20/07/15.
 */
public class Person implements Serializable {
    private String name;
    private String email;

    public Person(String n, String e) { name = n; email = e; }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return name; }
}
