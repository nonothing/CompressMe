/*******************************************************************************
 * Copyright (c) [2013], [Serdyuk Evgen]
 * 
 *  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * Neither the name of the {organization} nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.def.compressme.model;

public class Chiper {
    private static final char MY_CRYPT = '&';
    private int countUp;
    private boolean isUp;
    private boolean isEnglishChar;
    private boolean isOriginalChar;
    private StringBuilder newText;
    private char symbol;

    private Alphabet alphabet;

    public Chiper() {
        alphabet = new Alphabet();
    }

    private void init() {
        newText = new StringBuilder();
        isEnglishChar = false;
        countUp = 0;
        isUp = false;
        isOriginalChar = false;
    }

    public String translateToTranslit(String text) {
        init();
        text = text.replace('¸', 'å');
        text = text.replace('¨', 'Å');
        newText.append(MY_CRYPT);

        for (int count = 0; count < text.length(); count++) {
            symbol = text.charAt(count);
            checkEnglishCharInRussian(symbol);
            if (!isOriginalChar()) {
                if (!isLowRussianSymbol(text, count) && !isUpperRussianSymbol()) {
                    if (!isLastEnglishSymbol(text, count)) {
                        if (symbol != ' ' || isEnglishChar) {
                            newText.append(symbol);
                        }
                        countUp = 1;
                    }
                }
            } else {
                if (alphabet.isRussian(symbol)) {

                    if (Character.isUpperCase(symbol)) {
                        addTranslitHigh(alphabet
                                .getRussianUpperCaseIndex(symbol));
                    } else {
                        addTranslitLow(alphabet
                                .getRussianLowerCaseIndex(symbol));
                    }
                } else {
                    if (!isLastEnglishSymbol(text, count)) {
                        newText.append(symbol);
                        countUp = 1;
                    }
                }
            }
        }
        return newText.toString();
    }

    private boolean isOriginalChar() {
        if (symbol == '"') {
            if (!isOriginalChar) {
                isOriginalChar = true;
            } else {
                isOriginalChar = false;
            }
        }
        return isOriginalChar;
    }

    public String translateToRus(String text) {
        init();

        if (text.charAt(0) == MY_CRYPT) {
            for (int index = 1; index < text.length(); index++) {
                symbol = text.charAt(index);
                checkEnglishCharInTranslit();
                if (!isOriginalChar()) {
                    if (alphabet.isSymbolAll(symbol) && !isEnglishChar) {
                        if (!translateToRusIsHighSymbol(text, index)) {
                            if (!checkIsUpperNextSymbol(text, index)) {
                                addRussianLowerCaseSymbol();
                            }
                        }
                    } else {
                        translateToRusOtherSymbols(text, index);
                    }
                } else {
                    if (symbol == '"') {
                        newText.append(" ");
                        newText.append(symbol);
                    } else if (checkUpperRusianSymbol()) {
                        addRussianUpperCaseSymbol();
                    } else if (Character.isLowerCase(symbol) && !isEnglishChar) {
                        addRussianLowerCaseSymbol();
                    } else if (isEnglishChar) {
                        if (symbol != '\'') {
                            newText.append(symbol);
                        }
                    } else {

                        if (symbol != '\'') {
                            if (alphabet.isSymbolAll(symbol)) {
                                if (alphabet.isUpperCase(symbol)) {
                                    addRussianUpperCaseSymbol();
                                } else if (alphabet.isLowerCase(symbol)) {
                                    addRussianLowerCaseSymbol();
                                } else {
                                    newText.append(symbol);
                                }
                            } else {
                                newText.append(symbol);
                            }
                        }
                    }
                }
            }
        }
        return newText.toString().trim().replace("  ", "");
    }

    public int lenghtTranslate(String text) {
        return translateToTranslit(text).length();
    }

    private void checkEnglishCharInRussian(char symbol) {

        if (!alphabet.isEnglish(symbol)) {
            if (symbol != ' ') {
                isEnglishChar = false;
            }
        } else {
            if (!isEnglishChar) {
                newText.append("'");
            }
            isEnglishChar = true;
        }

    }

    private boolean translateToRusIsHighSymbol(String text, int index) {
        if (alphabet.isUpperCase(symbol)) {
            if (countUp == 0) {
                newText.append(" ");
            }
            countUp++;

            if (isUp) {
                addRussianUpperCaseSymbol();
            } else {
                isNextTwoUpperSymbol(text, index);

                if (index + 1 < text.length()
                        && alphabet.isUpperCase(text.charAt(index + 1))) {
                    addRussianUpperCaseSymbol();
                } else {
                    addRussianLowerCaseSymbol();
                }
            }
            return true;
        }
        return false;
    }

    private void addRussianLowerCaseSymbol() {
        newText.append(alphabet.getRussianLowerCaseSymbol(symbol));
    }

    private void isNextTwoUpperSymbol(String text, int index) {
        if (index + 2 < text.length()
                && alphabet.isUpperCase(text.charAt(index + 1))
                && alphabet.isUpperCase(text.charAt(index + 2))) {
            isUp = true;
        }
    }

    private boolean checkIsUpperNextSymbol(String text, int index) {
        countUp = 0;
        if (isUp) {
            if (index + 1 < text.length()
                    && alphabet.isUpperCase(text.charAt(index + 1))) {
                isUp = false;
            }
            addRussianUpperCaseSymbol();
            return true;
        }
        return false;
    }

    private void addRussianUpperCaseSymbol() {
        newText.append(alphabet.getRussianUpperCaseSymbol(symbol));
    }

    private void translateToRusOtherSymbols(String text, int index) {
        isUp = false;
        if (symbol == '\'') {
            newText.append(" ");
        } else {
            if (Character.isDigit(symbol)
                    && !Character.isDigit(text.charAt(index - 1))) {
                newText.append(" ");
            }
            newText.append(symbol);
        }
    }

    private void checkEnglishCharInTranslit() {
        if (symbol == '\'') {
            if (isEnglishChar) {
                isEnglishChar = false;
            } else {
                isEnglishChar = true;
            }
        }
    }

    private void addTranslitHigh(int index) {
        newText.append(alphabet.getTranslitHigh(index));
    }

    private void addTranslitLow(int index) {
        newText.append(alphabet.getTranslitLow(index));
    }

    private boolean isLowRussianSymbol(String text, int index) {
        if (Character.isLowerCase(symbol) && !isEnglishChar) {
            int indexLowSymbol = alphabet.getRussianLowerCaseIndex(symbol);
            if (countUp <= 0) {
                addTranslitLow(indexLowSymbol);
            } else {
                if (symbolBetweenSpace(text, index)) {
                    newText.append(" ");
                    addTranslitLow(indexLowSymbol);
                } else {
                    addTranslitHigh(indexLowSymbol);
                    countUp -= 2;
                }
            }
            return true;
        }
        return false;
    }

    private boolean symbolBetweenSpace(String text, int index) {
        return (index > 1 && index + 1 < text.length()
                && text.charAt(index - 1) == ' ' && text.charAt(index + 1) == ' ');
    }

    private boolean isLastEnglishSymbol(String text, int index) {
        if (index + 1 < text.length() && isEnglishChar
                && !alphabet.isEnglish(text.charAt(index + 1))
                && text.charAt(index + 1) != ' ') {
            newText.append(symbol);
            newText.append("'");
            return true;
        }
        return false;
    }

    private boolean isUpperRussianSymbol() {
        if (checkUpperRusianSymbol()) {
            if (countUp < 4) {
                countUp++;
                addTranslitHigh(alphabet.getRussianUpperCaseIndex(symbol));
            } else {
                addTranslitLow(alphabet.getRussianUpperCaseIndex(symbol));
            }
            return true;
        }
        return false;
    }

    private boolean checkUpperRusianSymbol() {
        return Character.isUpperCase(symbol) && !isEnglishChar;
    }

}
