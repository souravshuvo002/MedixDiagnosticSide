package com.example.medixdiagnostic.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("testByDiagnostic")
    private List<Test> allTestByDiagnostic;

    @SerializedName("allCategories")
    private List<Category> allCategories;

    @SerializedName("diagnostic")
    private Diagnostic diagnostic;

    @SerializedName("lastInsertTestID")
    private Test lastInsertTestID;

    @SerializedName("allBook")
    private List<Book> allBookList;

    @SerializedName("bookDetails")
    private Book bookDetails;

    @SerializedName("bookTestDetails")
    private List<Book> allbookTestDetails;

    @SerializedName("cusToken")
    private Diagnostic customerToken;


    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Test> getAllTestByDiagnostic() {
        return allTestByDiagnostic;
    }

    public void setAllTestByDiagnostic(List<Test> allTestByDiagnostic) {
        this.allTestByDiagnostic = allTestByDiagnostic;
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }

    public Test getLastInsertTestID() {
        return lastInsertTestID;
    }

    public void setLastInsertTestID(Test lastInsertTestID) {
        this.lastInsertTestID = lastInsertTestID;
    }

    public List<Book> getAllBookList() {
        return allBookList;
    }

    public void setAllBookList(List<Book> allBookList) {
        this.allBookList = allBookList;
    }

    public Book getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(Book bookDetails) {
        this.bookDetails = bookDetails;
    }

    public List<Book> getAllbookTestDetails() {
        return allbookTestDetails;
    }

    public void setAllbookTestDetails(List<Book> allbookTestDetails) {
        this.allbookTestDetails = allbookTestDetails;
    }

    public Diagnostic getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(Diagnostic customerToken) {
        this.customerToken = customerToken;
    }
}
