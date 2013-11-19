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
/**
 * This class is intended for use by seeking or receiving the symbols of the alphabet.
 */
public class Alphabet {
    private final static String RUSSIAN_LOWER_CASE = "àáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    private final static String RUSSIAN_UPPER_CASE = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞß";
    private final static String TRANSLIT_LOWER_CASE = "abvgde*zijklmnoprstyfxc\\w$<uq[]¹";
    private final static String TRANSLIT_UPPER_CASE = "ABVGDE#ZIJKLMNOPRSTYFXC/W|>UQ{}^";
    
    /**Indicates whether the specified character is an upper case translit alphabet.
     * @param symbol - the character to check.
     * @return true if symbol is an upper case translit; false otherwise. 
     */
    public boolean isUpperCase(char symbol) {
        if (TRANSLIT_UPPER_CASE.indexOf(symbol) >= 0) {
            return true;
        }
        return false;
    }
    
    /**Indicates whether the specified character is a lower case translit alphabet.
     * @param symbol - the character to check.
     * @return true if symbol is a lower case translit; false otherwise. 
     */
    public boolean isLowerCase(char symbol) {
        if (TRANSLIT_LOWER_CASE.indexOf(symbol) >= 0) {
            return true;
        }
        return false;
    }
    
    /**Indicates whether the specified character is a latin alphabet.
     * @param symbol - the character to check.
     * @return true if symbol is a latin; false otherwise. 
     */
    public boolean isEnglish(char symbol) {
        return (symbol >= 'a' && symbol <= 'z')
                || (symbol >= 'A' && symbol <= 'Z');
    }

    /**Check if symbol is in translit alphabet.
     * @param symbol - the char value to find.
     * @return true if specified symbol was found; false otherwise.
     */
    public boolean isSymbolAll(char symbol) {
        return isUpperCase(symbol) || isLowerCase(symbol);
    }

    /**Returns the index symbol in translit alphabet.
     * @param symbol - the char value to find.
     * @return index if specified symbol was found; else -1.     
     */
    public int findSymbol(char symbol) {
        if (isUpperCase(symbol)) {
            return TRANSLIT_UPPER_CASE.indexOf(symbol);
        } else {
            return TRANSLIT_LOWER_CASE.indexOf(symbol);
        }
    }
    
    /**Returns the character symbol in lower case translit alphabet.
     * @param index - the char value to find.
     * @return the character symbol.
     */
    public char getTranslitLow(int index){
        return TRANSLIT_LOWER_CASE.charAt(index);
    }
    
    /**Returns the character symbol in upper case translit alphabet.
     * @param index - the int value to find.
     * @return the character symbol.
     */
    public char getTranslitHigh(int index){
        return TRANSLIT_UPPER_CASE.charAt(index);
    }
    
    /**Returns the character symbol in upper case Russian alphabet.
     * @param symbol - the char value to find.
     * @return the character symbol.
     */
    public char getRussianUpperCaseSymbol(char symbol){
        return RUSSIAN_UPPER_CASE.charAt(findSymbol(symbol));
    }
    
    /**Returns the index symbol in upper case Russian alphabet.
     * @param symbol - the char value to find.
     * @return the character at the index.
     */
    public int getRussianUpperCaseIndex(char symbol){
        return RUSSIAN_UPPER_CASE.indexOf(symbol);
    }
    
    /**Returns the character symbol in lower case Russian alphabet.
    * @param symbol - the char value to find.
    * @return the character symbol.
    */
    public char getRussianLowerCaseSymbol(char symbol){
        return RUSSIAN_LOWER_CASE.charAt(findSymbol(symbol));
    }
    
    /**Returns the index symbol in lower case Russian alphabet.
    * @param symbol - the char value to find.
    * @return the character at the index.
    */
    public int getRussianLowerCaseIndex(char symbol){
        return RUSSIAN_LOWER_CASE.indexOf(symbol);
    }
    
     /**Indicates whether the specified character is a cyrillic alphabet.
     * @param symbol - the character to check.
     * @return true if symbol is a Cyrillic symbol; false otherwise. 
     */
    public boolean isRussian(char symbol) {
        return (symbol >= 'à' && symbol <= 'ÿ')
                || (symbol >= 'À' && symbol <= 'ÿ');
    }

}
