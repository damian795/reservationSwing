package com.example.buttons.reservationFormView.reservationComponents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FullNameDocument extends PlainDocument {

    private final int MAX_LENGTH = 60;
    private final String NAME_REGEX = "[^a-zA-Z\\s]";

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) return;

        //usuwamy niedozowolne znaki
        str = str.replaceAll(NAME_REGEX, "");

        //potem sprawdzamy dlugosc stringa
        if ((getLength() + str.length()) <= MAX_LENGTH) {
            super.insertString(offs, str, a);
        }
    }
}
