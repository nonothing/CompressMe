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

public class Alphabet {
    private final String RUSSIAN_LOWER_CASE = "àáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    private final String RUSSIAN_UPPER_CASE = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞß";
    private final String TRANSLIT_LOWER_CASE[] = { "a", "b", "v", "g", "d", "e", "*", "z",
            "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "y", "f",
            "x", "c", "\\", "w", "$", "<", "u", "q", "[", "]", "¹"};
    private final String TRANSLIT_UPPER_CASE[] = { "A", "B", "V", "G", "D", "E", "#", "Z",
            "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "Y", "F",
            "X", "C", "/", "W", "|", ">", "U", "Q", "{", "}", "^"};
    
    
    public boolean isUpperCase(char symbol) {
        for (int i = 0; i < TRANSLIT_LOWER_CASE.length; i++) {
            if (TRANSLIT_UPPER_CASE[i].toCharArray()[0] == symbol) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLowerCase(char symbol) {
        for (int i = 0; i < TRANSLIT_LOWER_CASE.length; i++) {
            if (TRANSLIT_LOWER_CASE[i].toCharArray()[0] == symbol) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEnglish(char symbol) {
        return (symbol >= 'a' && symbol <= 'z')
                || (symbol >= 'A' && symbol <= 'Z');
    }

    public boolean isSymbolAll(char symbol) {
        return isUpperCase(symbol) || isLowerCase(symbol);
    }

    public int findSymbol(char symbol) {
        int result = 0;
        if (isUpperCase(symbol)) {
            for (int index = 0; index < TRANSLIT_LOWER_CASE.length; index++) {
                if (TRANSLIT_UPPER_CASE[index].toCharArray()[0] == symbol) {
                    result = index;
                }
            }
        } else {
            for (int index = 0; index < TRANSLIT_LOWER_CASE.length; index++) {
                if (TRANSLIT_LOWER_CASE[index].toCharArray()[0] == symbol) {
                    result = index;
                }
            }
        }
        return result;
    }

    public String getTranslitLow(int index){
        return TRANSLIT_LOWER_CASE[index];
    }
    
    public String getTranslitHigh(int index){
        return TRANSLIT_UPPER_CASE[index];
    }

    public char getRussianUpperCaseSymbol(char symbol){
        return RUSSIAN_UPPER_CASE.charAt(findSymbol(symbol));
    }
    
    public int getRussianUpperCaseIndex(char symbol){
        return RUSSIAN_UPPER_CASE.indexOf(symbol);
    }
    
    public char getRussianLowerCaseSymbol(char symbol){
        return RUSSIAN_LOWER_CASE.charAt(findSymbol(symbol));
    }
    
    public int getRussianLowerCaseIndex(char symbol){
        return RUSSIAN_LOWER_CASE.indexOf(symbol);
    }
    
    public boolean isRussian(char symbol) {
        return (symbol >= 'à' && symbol <= 'ÿ')
                || (symbol >= 'À' && symbol <= 'ÿ');
    }

}
