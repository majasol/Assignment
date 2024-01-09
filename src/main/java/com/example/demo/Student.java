package com.example.demo;

public class Student {
    private final String studentId;
    private final String firstName;
    private final String lastName;
    private final String course;
    private final String module;
    private final int marks;

    public Student(String studentId, String firstName, String lastName, String course, String module, int marks) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
        this.module = module;
        this.marks = marks;
    }

    public String getStudentId(){
        return studentId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getCourse(){
        return course;
    }

    public String getModule(){
        return module;
    }

    public int getMarks(){
        return marks;
    }

    @Override
    public String toString() {
        return studentId + ", " + firstName + " " + lastName + ", " + course + ", " + module + ", " + marks;
    }
}
