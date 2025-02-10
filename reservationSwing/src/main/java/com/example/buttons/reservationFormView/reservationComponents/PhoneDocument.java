package com.example.buttons.reservationFormView.reservationComponents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PhoneDocument extends PlainDocument {

    private static final int MAX_LENGTH = 15;
    private static final String PHONE_REGEX = "^[0-9+()\\-\\s]*$";

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null && str.matches(PHONE_REGEX)) {
            int currentLength = getLength() + str.length();
            if (currentLength <= MAX_LENGTH){
                super.insertString(offs, str, a);
            }
        }
    }
}
