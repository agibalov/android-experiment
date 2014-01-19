package me.loki2302;

public interface MainActivityContext {
    void displayProgressDialog();
    void hideProgressDialog();
    void displayErrorMessage();
    void hideErrorMessage();
    void trace(String message);
}
