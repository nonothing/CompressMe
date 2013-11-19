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
package com.def.compressme.tests;
import org.junit.Test;
import com.def.compressme.model.Chiper;
import static org.junit.Assert.assertEquals;

public class ChiperTest {

    @Test
    public void test() {
        assertChiper("ѕривет ћир!", "&PRivetMIr!");
    }

    private void assertChiper(String text, String expectedCode) {
        assertChiper(text, expectedCode, text);
    }

    private void assertChiper(String text, String expectedCode, String expectedDecode) {
        Chiper chiper = new Chiper();
        String actual = chiper.translateToTranslit(text);
        assertEquals(expectedCode, actual);
        assertEquals(expectedDecode, chiper.translateToRus(actual));
    }

    @Test
    public void test2() {
        assertChiper("ѕривет", "&PRivet");
    }

    @Test
    public void test3() {
        assertChiper("    солнце светит €рко на голубом небе", "&SolnceSvetit^rkoNaGolybomNebe", "солнце светит €рко на голубом небе");
    }

    @Test
    public void test4() {
        assertChiper("    the sun shines brightly in the blue sky",
                "&'the sun shines brightly in the blue sky",
                "the sun shines brightly in the blue sky");
    }

    @Test
    public void test5() {
        assertChiper("the sun shines brightly in the blue sky", "&'the sun shines brightly in the blue sky");
    }

    @Test
    public void test6() {
        assertChiper("солнце светит €рко in the blue sky", "&solnceSvetit^rko'in the blue sky");
    }


    @Test   
    public void test7() { 
        assertChiper("солнце shines €рко in the голубом sky", "&solnce'shines '^rko'in the 'Golybom'sky", "солнце shines €рко in the голубом sky"); 
    }

    @Test 
    public void test8() {
        assertChiper("sun светит brightly в blue небе", "&'sun 'Svetit'brightly ' v'blue 'Nebe", "sun светит brightly в blue небе");
    }

    @Test 
    public void test9() {
        assertChiper("sun shines €рко в blue sky", "&'sun shines '^rko v'blue sky", "sun shines €рко в blue sky");
    }

    @Test
    public void test10() {
        assertChiper("ѕ–»вет", "&PRIVEt", "ѕ–»¬≈“");
    }

    @Test
    public void test11() {
        assertChiper("ѕ–» вет", "&PRIVet", "ѕ–»¬≈“");
    }

    @Test
    public void test12() {
        assertChiper("ѕ–»¬ ет", "&PRIVEt", "ѕ–»¬≈“");
    }

    @Test
    public void test13() {
        assertChiper(
                "а б в г д е Є ж з и й к л м н о п р с т у ф х ч ц ш щ ь ы ъ э ю €",
                "&a b v g d e e * z i j k l m n o p r s t y f x \\ c w $ q u < [ ]^",
                "а б в г д е е ж з и й к л м н о п р с т у ф х ч ц ш щ ь ы ъ э ю €");
    }

    @Test 
    public void test14() {     
        assertChiper("Є", "&e", "е");
    }

    @Test 
    public void test15() {
        assertChiper("а б в г д е ж з и й к л м н о п р с т у ф х ч ц ш щ ь ы ъ э ю €",
                "&a b v g d e * z i j k l m n o p r s t y f x \\ c w $ q u < [ ]^");
    }

    @Test
    public void test16() {
        assertChiper("€блоко на €блоне растет", "&єblokoNa^bloneRastet");
    }

    @Test
    public void test17() {
        assertChiper("€блоко €блоко", "&єbloko^bloko");
    }

    @Test
    public void test18() {
        assertChiper("€блоко €блоко €блоко", "&єbloko^bloko^bloko");
    }

    @Test
    public void test19() {
        assertChiper(" €блоко €блоко €блоко", "&^bloko^bloko^bloko", "€блоко €блоко €блоко");
    }

    @Test
    public void test20() {
        assertChiper("абрикос", "&abrikos");
    }

    @Test
    public void test21() {
        assertChiper(" абрикос", "&Abrikos", "абрикос");
    }

    @Test
    public void test22() {
        assertChiper("јбрикос", "&ABrikos");
    }

    @Test
    public void test23() {
        assertChiper(" јбрикос", "&ABrikos", "јбрикос");
    }

    @Test
    public void test24() {
        assertChiper("јбрикос   јбрикос јбрикос      јбрикос аЅрикос", "&ABrikosABrikosABrikosABrikosABrikos", "јбрикос јбрикос јбрикос јбрикос јбрикос");
    }

    @Test
    public void test25() {
        assertChiper("живность", "&*ivnostq");
    }

    @Test
    public void test26() {
        assertChiper("∆ивность", "&#Ivnostq");
    }

    @Test
    public void test27() {
        assertChiper("∆живность", "&##ivnostq");
    }

    @Test
    public void test28() {
        assertChiper("частота", "&\\astota");
    }

    @Test
    public void test29() {
        assertChiper("„астота", "&/Astota");
    }

    @Test
    public void test30() {
        assertChiper("Џблабла", "&>Blabla");
    }

    @Test
    public void test31() {
        assertChiper("ъблабла", "&<blabla");
    }

    @Test
    public void test32() {
        assertChiper("эю€", "&[]є");
    }

    @Test
    public void test33() {
        assertChiper("Ёёя", "&{}^");
    }

    @Test
    public void test34() {
        assertChiper("1234567890", "&1234567890");
    }

    @Test // TODO don't fix
    public void test35() {
        assertChiper("яяяяяя9999яяя9я9я9я", "&^^^^єє9999^^^9^9^9^", "яяяяяя 9999 яяя 9€ 9€ 9€" );
    }

    @Test   // TODO don't fix
    public void test36() {
        assertChiper("€€€€€€9999€€€9€9€9€", "&єєєєєє9999^єє9^9^9^", "€€€€€€ 9999 €€€ 9 € 9€ 9€");
    }

    @Test
    public void test37() {
        assertChiper("9999999999999", "&9999999999999");
    }

    @Test
    public void test39() {
        assertChiper("€€€€€€€€€€€€€€€", "&єєєєєєєєєєєєєєє");
    }

    @Test 
    public void test40() {
        assertChiper("\"я€я€я€я€я\"", "&\"^є^є^є^є^\"");
    }

    @Test  
    public void test41() {
        assertChiper("\"јаајаајаајаај\"", "&\"AaaAaaAaaAaaA\"");
    }
    
    @Test  
    public void test42() {
        assertChiper("\"јбв гtytд”йе\"", "&\"AbvKg'tyt'dYje\"");
    }
    
    @Test  
    public void test43() {
        assertChiper("\"јбв гд”йе\"", "&\"AbvKgdYje\"");
    }
    
    @Test  
    public void test44() {
        assertChiper(" ака€ сегодн€ хороша€ \"ѕо√оƒƒј\", but много работы!", "&KAkaєSegodnєXorowaє\"PoGoDDA\",'but 'MnogoRabotu!");
    }
    
    @Test  
    public void test45() {
        assertChiper("\" ака€ сегодн€ хороша€ ѕо√оƒƒј\", but много работы!", "&\"Kakaє segodnє xorowaє PoGoDDA\",'but 'MnogoRabotu!");
    }
    @Test  
    public void test46() {
        assertChiper("\" ака€ сегодн€ 4342 хороша€ ѕо√оƒƒј\", but много работы!", "&\"Kakaє segodnє 4342 xorowaє PoGoDDA\",'but 'MnogoRabotu!");
    }
}
