package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.Stream;

public class HelloController {
    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;

    @FXML
    private TextField IdField;

    @FXML
    private TextField courseField;

    @FXML
    private TextField moduleField;

    @FXML
    private TextField marksField;

    @FXML
    private TextField deleteNameField;

    @FXML
    private TextField searchTextField;

    @FXML
    private ListView<Student> registerView;

    @FXML
    private void updateRegisterListView() {
        registerView.getItems().setAll(students);
    }

    private List<Student> students = new ArrayList<>();

    private String categoryChoice = "Student ID";

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private void initialize() {
        List<Student> students = Arrays.asList(
                new Student("HS01", "Harvey", "Smith", "Mathematics", "MA736", 45),
                new Student("CB01", "Charlie", "Brown", "Physics", "PH837", 57),
                new Student("LJ01", "Luke", "Jones", "Biology", "BI624", 85),
                new Student("HG01", "Hannah", "Green", "Chemistry", "CH764", 64),
                new Student("MW01", "Mike", "Williams", "Computer Science", "CI505", 71)
        );

        registerView.getItems().addAll(students);

        if (categoryChoiceBox.getItems().isEmpty()) {
            categoryChoiceBox.getItems().addAll("Student ID", "First Name", "Last Name", "Course", "Module", "Marks");
        }

        ObservableList<String> choiceBoxItems = FXCollections.observableArrayList(
                "Student ID", "First Name", "Last Name", "Course", "Module", "Marks"
        );

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    categoryChoice = newValue;
                    searchStudent(searchTextField.getText(), true);
                }
        );
    }

    @FXML
    protected void onAddStudentButtonClick() {
        String studentId = IdField.getText();
        String firstName = fNameField.getText();
        String lastName = lNameField.getText();
        String course = courseField.getText();
        String module = moduleField.getText();

        int marks;
        try {
            marks = Integer.parseInt(marksField.getText());
        } catch (NumberFormatException e) {
            marks = 0; // Default value
        }

        if (!studentId.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !course.isEmpty() && !module.isEmpty()) {
            students = addStudentToRegister(students, studentId, firstName, lastName, course, module, marks);
            updateRegisterListView();
            clearFields();
        }

    }

    @FXML
    protected void onDeleteStudentButtonClick() {
        String studentIdToDelete = deleteNameField.getText();
        if (!studentIdToDelete.isEmpty()) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Student");
            alert.setContentText("Are you sure you want to delete the student with ID: " + studentIdToDelete + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User clicked OK, proceed with deletion
                students = deleteStudentFromRegister(students, studentIdToDelete);
                updateRegisterListView();
                clearFields();
            }
        }
    }

    @FXML
    protected void onSearchButtonClick() {
        String searchText = searchTextField.getText();
        if (!searchText.isEmpty()) {
            searchStudent(searchText, true);
        } else {
            updateRegisterListView();
        }
    }

    private List<Student> addStudentToRegister(List<Student> students, String studentId, String firstName, String lastName, String course, String module, int marks) {
        return Stream.concat(students.stream(), Stream.of(new Student(studentId, firstName, lastName, course, module, marks)))
                     .collect(Collectors.toList());
    }

    private List<Student> deleteStudentFromRegister(List<Student> students, String studentIdToDelete) {
        return students.stream()
                     .filter(student -> !student.getStudentId().equals(studentIdToDelete))
                     .collect(Collectors.toList());
    }

    private List<Student> searchStudent(String searchText, boolean ascending) {
        Comparator<Student> comparator = getComparator();

        List<Student> searchResults = students.stream()
                .filter(student -> matchesSearch(student, searchText))
                .sorted(ascending ? comparator : comparator.reversed())
                .collect(Collectors.toList());

        registerView.getItems().setAll(searchResults);
        return searchResults;
    }

    private boolean matchesSearch(Student student, String searchText) {
        String lowerCaseSearchText = searchText.toLowerCase();

        return switch (categoryChoice) {
            case "First Name" -> student.getFirstName().toLowerCase().contains(lowerCaseSearchText);
            case "Last Name" -> student.getLastName().toLowerCase().contains(lowerCaseSearchText);
            case "Course" -> student.getCourse().toLowerCase().contains(lowerCaseSearchText);
            case "Module" -> student.getModule().toLowerCase().contains(lowerCaseSearchText);
            case "Marks" -> Integer.toString(student.getMarks()).contains(lowerCaseSearchText);
            default -> Stream.of(student.getStudentId(), student.getFirstName(), student.getLastName(), student.getCourse(), student.getModule(), Integer.toString(student.getMarks()))
                    .anyMatch(value -> value.toLowerCase().contains(lowerCaseSearchText));
        };
    }

    private void clearFields() {
        IdField.clear();
        fNameField.clear();
        lNameField.clear();
        courseField.clear();
        moduleField.clear();
        marksField.clear();
    }

    private Comparator<Student> getComparator() {
        return switch (categoryChoice) {
            case "First Name" -> Comparator.comparing(Student::getFirstName);
            case "Last Name" -> Comparator.comparing(Student::getLastName);
            case "Course" -> Comparator.comparing(Student::getCourse);
            case "Module" -> Comparator.comparing(Student::getModule);
            case "Marks" -> Comparator.comparing(Student::getMarks);
            default -> Comparator.comparing(Student::getStudentId);
        };
    }


}