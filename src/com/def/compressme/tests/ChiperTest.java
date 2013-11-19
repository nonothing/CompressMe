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
        assertChiper("������ ���!", "&PRivetMIr!");
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
        assertChiper("������", "&PRivet");
    }

    @Test
    public void test3() {
        assertChiper("    ������ ������ ���� �� ������� ����", "&SolnceSvetit^rkoNaGolybomNebe", "������ ������ ���� �� ������� ����");
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
        assertChiper("������ ������ ���� in the blue sky", "&solnceSvetit^rko'in the blue sky");
    }


    @Test   
    public void test7() { 
        assertChiper("������ shines ���� in the ������� sky", "&solnce'shines '^rko'in the 'Golybom'sky", "������ shines ���� in the ������� sky"); 
    }

    @Test 
    public void test8() {
        assertChiper("sun ������ brightly � blue ����", "&'sun 'Svetit'brightly ' v'blue 'Nebe", "sun ������ brightly � blue ����");
    }

    @Test 
    public void test9() {
        assertChiper("sun shines ���� � blue sky", "&'sun shines '^rko v'blue sky", "sun shines ���� � blue sky");
    }

    @Test
    public void test10() {
        assertChiper("������", "&PRIVEt", "������");
    }

    @Test
    public void test11() {
        assertChiper("��� ���", "&PRIVet", "������");
    }

    @Test
    public void test12() {
        assertChiper("���� ��", "&PRIVEt", "������");
    }

    @Test
    public void test13() {
        assertChiper(
                "� � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �",
                "&a b v g d e e * z i j k l m n o p r s t y f x \\ c w $ q u < [ ]^",
                "� � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �");
    }

    @Test 
    public void test14() {     
        assertChiper("�", "&e", "�");
    }

    @Test 
    public void test15() {
        assertChiper("� � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �",
                "&a b v g d e * z i j k l m n o p r s t y f x \\ c w $ q u < [ ]^");
    }

    @Test
    public void test16() {
        assertChiper("������ �� ������ ������", "&�blokoNa^bloneRastet");
    }

    @Test
    public void test17() {
        assertChiper("������ ������", "&�bloko^bloko");
    }

    @Test
    public void test18() {
        assertChiper("������ ������ ������", "&�bloko^bloko^bloko");
    }

    @Test
    public void test19() {
        assertChiper(" ������ ������ ������", "&^bloko^bloko^bloko", "������ ������ ������");
    }

    @Test
    public void test20() {
        assertChiper("�������", "&abrikos");
    }

    @Test
    public void test21() {
        assertChiper(" �������", "&Abrikos", "�������");
    }

    @Test
    public void test22() {
        assertChiper("�������", "&ABrikos");
    }

    @Test
    public void test23() {
        assertChiper(" �������", "&ABrikos", "�������");
    }

    @Test
    public void test24() {
        assertChiper("�������   ������� �������      ������� �������", "&ABrikosABrikosABrikosABrikosABrikos", "������� ������� ������� ������� �������");
    }

    @Test
    public void test25() {
        assertChiper("��������", "&*ivnostq");
    }

    @Test
    public void test26() {
        assertChiper("��������", "&#Ivnostq");
    }

    @Test
    public void test27() {
        assertChiper("���������", "&##ivnostq");
    }

    @Test
    public void test28() {
        assertChiper("�������", "&\\astota");
    }

    @Test
    public void test29() {
        assertChiper("�������", "&/Astota");
    }

    @Test
    public void test30() {
        assertChiper("�������", "&>Blabla");
    }

    @Test
    public void test31() {
        assertChiper("�������", "&<blabla");
    }

    @Test
    public void test32() {
        assertChiper("���", "&[]�");
    }

    @Test
    public void test33() {
        assertChiper("���", "&{}^");
    }

    @Test
    public void test34() {
        assertChiper("1234567890", "&1234567890");
    }

    @Test // TODO don't fix
    public void test35() {
        assertChiper("������9999���9�9�9�", "&^^^^��9999^^^9^9^9^", "������ 9999 ��� 9� 9� 9�" );
    }

    @Test   // TODO don't fix
    public void test36() {
        assertChiper("������9999���9�9�9�", "&������9999^��9^9^9^", "������ 9999 ��� 9 � 9� 9�");
    }

    @Test
    public void test37() {
        assertChiper("9999999999999", "&9999999999999");
    }

    @Test
    public void test39() {
        assertChiper("���������������", "&���������������");
    }

    @Test 
    public void test40() {
        assertChiper("\"���������\"", "&\"^�^�^�^�^\"");
    }

    @Test  
    public void test41() {
        assertChiper("\"�������������\"", "&\"AaaAaaAaaAaaA\"");
    }
    
    @Test  
    public void test42() {
        assertChiper("\"�����tyt����\"", "&\"AbvKg'tyt'dYje\"");
    }
    
    @Test  
    public void test43() {
        assertChiper("\"���������\"", "&\"AbvKgdYje\"");
    }
    
    @Test  
    public void test44() {
        assertChiper("����� ������� ������� \"�������\", but ����� ������!", "&KAka�Segodn�Xorowa�\"PoGoDDA\",'but 'MnogoRabotu!");
    }
    
    @Test  
    public void test45() {
        assertChiper("\"����� ������� ������� �������\", but ����� ������!", "&\"Kaka� segodn� xorowa� PoGoDDA\",'but 'MnogoRabotu!");
    }
    @Test  
    public void test46() {
        assertChiper("\"����� ������� 4342 ������� �������\", but ����� ������!", "&\"Kaka� segodn� 4342 xorowa� PoGoDDA\",'but 'MnogoRabotu!");
    }
}
