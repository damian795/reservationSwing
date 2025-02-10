package com.example.buttons.reservationFormView.reservationComponents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class EmailDocument extends PlainDocument {

    private static final int MAX_LENGTH = 100;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9@._-]+$"; //Pozwala tylko na znaki literowe, cyfry, @, ., -, _

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null && str.matches(EMAIL_REGEX)) {
            int currentLenght = str.length();
            if (currentLenght <= MAX_LENGTH) {
                super.insertString(offs, str, a);
            }
        }
    }
}
