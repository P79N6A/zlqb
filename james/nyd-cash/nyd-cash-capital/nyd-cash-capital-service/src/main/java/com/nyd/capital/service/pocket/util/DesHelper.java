package com.nyd.capital.service.pocket.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liuqiu
 */
public class DesHelper {

    /**
     * des 加密
     *
     * @param key     加密key
     * @param message 加密前参数
     * @return
     */
    public static String desEncrypt(String key, String message) {
        String ciphertext = des(key, message, true,0, null, 0);
        return stringToHex(ciphertext);
    }

    /**
     * 解密
     *
     * @param key     加密key
     * @param hexString 加密后参数
     * @return
     */
    public static String desDecrypt(String key, String hexString) {
        String ciphertext = hexToString(hexString);
        return des(key,ciphertext,false,0,null,0);
    }


    private static String stringToHex (String s) {
        String r = "0x";
        String [] hexes = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        for (int i=0; i<s.length(); i++) {
            r += (hexes [(Character.codePointAt(s,i) >> 4)] + hexes [(Character.codePointAt(s,i) & 0xf)]);
        }
        return r;
    }

    private static String hexToString (String h) {
        String r = "";
//        int i = (h.substring (0,2)=="0x")?2:0;
        for (int i = 2; i<h.length(); i+=2) {
            r += Character.toChars(Integer.valueOf(h.substring(i,i+2),16));
        }
        return r;
    }

    private static String des(String key, String message, boolean encrypt, int mode, String iv, int padding) {
        int[] spfunction1 = {0x1010400, 0, 0x10000, 0x1010404, 0x1010004, 0x10404, 0x4, 0x10000, 0x400, 0x1010400, 0x1010404, 0x400, 0x1000404, 0x1010004, 0x1000000, 0x4, 0x404, 0x1000400, 0x1000400, 0x10400, 0x10400, 0x1010000, 0x1010000, 0x1000404, 0x10004, 0x1000004, 0x1000004, 0x10004, 0, 0x404, 0x10404, 0x1000000, 0x10000, 0x1010404, 0x4, 0x1010000, 0x1010400, 0x1000000, 0x1000000, 0x400, 0x1010004, 0x10000, 0x10400, 0x1000004, 0x400, 0x4, 0x1000404, 0x10404, 0x1010404, 0x10004, 0x1010000, 0x1000404, 0x1000004, 0x404, 0x10404, 0x1010400, 0x404, 0x1000400, 0x1000400, 0, 0x10004, 0x10400, 0, 0x1010004};
        int[] spfunction2 = {-0x7fef7fe0, -0x7fff8000, 0x8000, 0x108020, 0x100000, 0x20, -0x7fefffe0, -0x7fff7fe0, -0x7fffffe0, -0x7fef7fe0, -0x7fef8000, -0x80000000, -0x7fff8000, 0x100000, 0x20, -0x7fefffe0, 0x108000, 0x100020, -0x7fff7fe0, 0, -0x80000000, 0x8000, 0x108020, -0x7ff00000, 0x100020, -0x7fffffe0, 0, 0x108000, 0x8020, -0x7fef8000, -0x7ff00000, 0x8020, 0, 0x108020, -0x7fefffe0, 0x100000, -0x7fff7fe0, -0x7ff00000, -0x7fef8000, 0x8000, -0x7ff00000, -0x7fff8000, 0x20, -0x7fef7fe0, 0x108020, 0x20, 0x8000, -0x80000000, 0x8020, -0x7fef8000, 0x100000, -0x7fffffe0, 0x100020, -0x7fff7fe0, -0x7fffffe0, 0x100020, 0x108000, 0, -0x7fff8000, 0x8020, -0x80000000, -0x7fefffe0, -0x7fef7fe0, 0x108000};
        int[] spfunction3 = {0x208, 0x8020200, 0, 0x8020008, 0x8000200, 0, 0x20208, 0x8000200, 0x20008, 0x8000008, 0x8000008, 0x20000, 0x8020208, 0x20008, 0x8020000, 0x208, 0x8000000, 0x8, 0x8020200, 0x200, 0x20200, 0x8020000, 0x8020008, 0x20208, 0x8000208, 0x20200, 0x20000, 0x8000208, 0x8, 0x8020208, 0x200, 0x8000000, 0x8020200, 0x8000000, 0x20008, 0x208, 0x20000, 0x8020200, 0x8000200, 0, 0x200, 0x20008, 0x8020208, 0x8000200, 0x8000008, 0x200, 0, 0x8020008, 0x8000208, 0x20000, 0x8000000, 0x8020208, 0x8, 0x20208, 0x20200, 0x8000008, 0x8020000, 0x8000208, 0x208, 0x8020000, 0x20208, 0x8, 0x8020008, 0x20200};
        int[] spfunction4 = {0x802001, 0x2081, 0x2081, 0x80, 0x802080, 0x800081, 0x800001, 0x2001, 0, 0x802000, 0x802000, 0x802081, 0x81, 0, 0x800080, 0x800001, 0x1, 0x2000, 0x800000, 0x802001, 0x80, 0x800000, 0x2001, 0x2080, 0x800081, 0x1, 0x2080, 0x800080, 0x2000, 0x802080, 0x802081, 0x81, 0x800080, 0x800001, 0x802000, 0x802081, 0x81, 0, 0, 0x802000, 0x2080, 0x800080, 0x800081, 0x1, 0x802001, 0x2081, 0x2081, 0x80, 0x802081, 0x81, 0x1, 0x2000, 0x800001, 0x2001, 0x802080, 0x800081, 0x2001, 0x2080, 0x800000, 0x802001, 0x80, 0x800000, 0x2000, 0x802080};
        int[] spfunction5 = {0x100, 0x2080100, 0x2080000, 0x42000100, 0x80000, 0x100, 0x40000000, 0x2080000, 0x40080100, 0x80000, 0x2000100, 0x40080100, 0x42000100, 0x42080000, 0x80100, 0x40000000, 0x2000000, 0x40080000, 0x40080000, 0, 0x40000100, 0x42080100, 0x42080100, 0x2000100, 0x42080000, 0x40000100, 0, 0x42000000, 0x2080100, 0x2000000, 0x42000000, 0x80100, 0x80000, 0x42000100, 0x100, 0x2000000, 0x40000000, 0x2080000, 0x42000100, 0x40080100, 0x2000100, 0x40000000, 0x42080000, 0x2080100, 0x40080100, 0x100, 0x2000000, 0x42080000, 0x42080100, 0x80100, 0x42000000, 0x42080100, 0x2080000, 0, 0x40080000, 0x42000000, 0x80100, 0x2000100, 0x40000100, 0x80000, 0, 0x40080000, 0x2080100, 0x40000100};
        int[] spfunction6 = {0x20000010, 0x20400000, 0x4000, 0x20404010, 0x20400000, 0x10, 0x20404010, 0x400000, 0x20004000, 0x404010, 0x400000, 0x20000010, 0x400010, 0x20004000, 0x20000000, 0x4010, 0, 0x400010, 0x20004010, 0x4000, 0x404000, 0x20004010, 0x10, 0x20400010, 0x20400010, 0, 0x404010, 0x20404000, 0x4010, 0x404000, 0x20404000, 0x20000000, 0x20004000, 0x10, 0x20400010, 0x404000, 0x20404010, 0x400000, 0x4010, 0x20000010, 0x400000, 0x20004000, 0x20000000, 0x4010, 0x20000010, 0x20404010, 0x404000, 0x20400000, 0x404010, 0x20404000, 0, 0x20400010, 0x10, 0x4000, 0x20400000, 0x404010, 0x4000, 0x400010, 0x20004010, 0, 0x20404000, 0x20000000, 0x400010, 0x20004010};
        int[] spfunction7 = {0x200000, 0x4200002, 0x4000802, 0, 0x800, 0x4000802, 0x200802, 0x4200800, 0x4200802, 0x200000, 0, 0x4000002, 0x2, 0x4000000, 0x4200002, 0x802, 0x4000800, 0x200802, 0x200002, 0x4000800, 0x4000002, 0x4200000, 0x4200800, 0x200002, 0x4200000, 0x800, 0x802, 0x4200802, 0x200800, 0x2, 0x4000000, 0x200800, 0x4000000, 0x200800, 0x200000, 0x4000802, 0x4000802, 0x4200002, 0x4200002, 0x2, 0x200002, 0x4000000, 0x4000800, 0x200000, 0x4200800, 0x802, 0x200802, 0x4200800, 0x802, 0x4000002, 0x4200802, 0x4200000, 0x200800, 0, 0x2, 0x4200802, 0, 0x200802, 0x4200000, 0x800, 0x4000002, 0x4000800, 0x800, 0x200002};
        int[] spfunction8 = {0x10001040, 0x1000, 0x40000, 0x10041040, 0x10000000, 0x10001040, 0x40, 0x10000000, 0x40040, 0x10040000, 0x10041040, 0x41000, 0x10041000, 0x41040, 0x1000, 0x40, 0x10040000, 0x10000040, 0x10001000, 0x1040, 0x41000, 0x40040, 0x10040040, 0x10041000, 0x1040, 0, 0, 0x10040040, 0x10000040, 0x10001000, 0x41040, 0x40000, 0x41040, 0x40000, 0x10041000, 0x1000, 0x40, 0x10040040, 0x1000, 0x41040, 0x10001000, 0x40, 0x10000040, 0x10040000, 0x10040040, 0x10000000, 0x40000, 0x10001040, 0, 0x10041040, 0x40040, 0x10000040, 0x10040000, 0x10001000, 0x10001040, 0, 0x10041040, 0x41000, 0x41000, 0x1040, 0x1040, 0x40040, 0x10000000, 0x10041000};
        List<Long> keys = desCreateKeys(key);
        int m = 0;
        int len = message.length();
        int chunk = 0;
        int iterations = keys.size() == 32 ? 3 : 9;
        int[] looping = null;
        int temp=0;
        if (iterations == 3) {
            looping = encrypt ? new int[]{0, 32, 2} : new int[]{30, -2, -2};
        } else {
            looping = encrypt ? new int[]{0, 32, 2, 62, 30, -2, 64, 96, 2} : new int[]{94, 62, -2, 32, 64, 2, 30, -2, -2};
        }
        if (padding == 2) {
            message += "        ";
        } else if (padding == 1) {
            temp = (char) (8 - (len % 8));
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < 8; i++) {
                s.append(new Character((char) temp));
            }
            message += s.toString();
            if (temp == 8) {
                len += 8;
            }

        } else if (padding == 0) {
            message += "\0\0\0\0\0\0\0\0";
        }
        String result = "";
        StringBuffer tempresult = new StringBuffer();
        long cbcleft=0;
        long cbcright=0;
        if (mode == 1) { //CBC mode
            cbcleft = (Character.codePointAt(iv, m++) << 24) | (Character.codePointAt(iv, m++) << 16) | (Character.codePointAt(iv, m++) << 8) | ord(iv, m++);
            cbcright = (Character.codePointAt(iv, m++) << 24) | (Character.codePointAt(iv, m++) << 16) | (Character.codePointAt(iv, m++) << 8) | ord(iv, m++);
            m = 0;
        }
        while (m < len) {
            int left = (Character.codePointAt(message, m++) << 24) | (Character.codePointAt(message, m++) << 16) | (Character.codePointAt(message, m++) << 8) | Character.codePointAt(message, m++);
            int right = (Character.codePointAt(message, m++) << 24) | (Character.codePointAt(message, m++) << 16) | (Character.codePointAt(message, m++) << 8) | Character.codePointAt(message, m++);
            long cbcleft2=0,cbcright2=0;
            if (mode == 1) {
                if (encrypt) {
                    left ^= cbcleft; right ^= cbcright;
                } else {
                    cbcleft2 = cbcleft;
                    cbcright2 = cbcright;
                    cbcleft = left;
                    cbcright = right;
                }
            }
//          tempresult .= (chr(left>>24 & masks[24]) . chr((left>>16 & masks[16]) & 0xff) . chr((left>>8 & masks[8]) & 0xff) . chr(left & 0xff) .
//         chr(right>>24 & masks[24]) . chr((right>>16 & masks[16]) & 0xff) . chr((right>>8 & masks[8]) & 0xff) . chr(right & 0xff));
//            long aaa=( ((left >> 4 & masks[4]) ^ right) & 0x0f0f0f0f);

            temp = (((left >>> 4) ^ right) & 0x0f0f0f0f);right ^= temp; left ^= (temp << 4);
            temp = (((left >>> 16) ^ right) & 0x0000ffff); right ^= temp; left ^= (temp << 16);
            temp = (((right >>> 2) ^ left) & 0x33333333); left ^= temp; right ^= (temp << 2);
            temp = (((right >>> 8) ^ left) & 0x00ff00ff); left ^= temp; right ^= (temp << 8);
            temp = (((left >>> 1) ^ right) & 0x55555555); right ^= temp; left ^= (temp << 1);

            left = ((left << 1) | (left >>> 31));
            right = ((right << 1) | (right >>> 31));
            int endloop,loopinc;
            for (int j=0; j<iterations; j+=3) {
                endloop = looping[j+1];
                loopinc = looping[j+2];
                int right1,right2;
                for (int i=looping[j]; i!=endloop; i+=loopinc) {
                    right1 = (int) (right ^ keys.get(i));
                    right2 = (int) (((right >>> 4) | (right << 28)) ^ keys.get(i+1));
                    temp = left;
                    left = right;
                    right = temp ^ (spfunction2[((right1 >>> 24) & 0x3f)] | spfunction4[((right1 >>> 16) & 0x3f)]
                            | spfunction6[((right1 >>>  8) & 0x3f)] | spfunction8[right1 & 0x3f]
                            | spfunction1[((right2 >>> 24) & 0x3f)] | spfunction3[((right2 >>> 16) & 0x3f)]
                            | spfunction5[((right2 >>>  8) & 0x3f)] | spfunction7[right2 & 0x3f]);
                }
                temp = left;
                left = right;
                right = temp;
            }
            left = ((left >>> 1) | (left << 31));
            right = ((right >>> 1) | (right << 31));


            temp = (int) (((left >>> 1) ^ right) & 0x55555555); right ^= temp; left ^= (temp << 1);
            temp = (int) (((right >>> 8) ^ left) & 0x00ff00ff); left ^= temp; right ^= (temp << 8);
            temp = (int) (((right >>> 2) ^ left) & 0x33333333); left ^= temp; right ^= (temp << 2);
            temp = (int) (((left >>> 16) ^ right) & 0x0000ffff); right ^= temp; left ^= (temp << 16);
            temp = (int) (((left >>> 4) ^ right) & 0x0f0f0f0f); right ^= temp; left ^= (temp << 4);
            if (mode == 1) {
                if (encrypt) {
                    cbcleft = left; cbcright = right;
                } else {
                    left ^= cbcleft2; right ^= cbcright2;
                }
            }
            tempresult.append(Character.toChars(left>>>24));
            tempresult.append(Character.toChars(left>>>16 & 0xff));
            tempresult.append(Character.toChars(left>>>8 & 0xff));
            tempresult.append(Character.toChars(left & 0xff));
            tempresult.append(Character.toChars(right>>>24));
            tempresult.append(Character.toChars(right>>>16 & 0xff));
            tempresult.append(Character.toChars(right>>>8 & 0xff));
            tempresult.append(Character.toChars(right & 0xff));
            chunk += 8;
            if (chunk == 512) {
                result += tempresult;
                tempresult.setLength(0);
                chunk = 0;
            }
        }


        return result + tempresult.toString();
    }
    private static Character chr(long v){
        return (char)(int) v;
    }
    private static List<Long> desCreateKeys(String key) {
        int[] pc2bytes0 = {0, 0x4, 0x20000000, 0x20000004, 0x10000, 0x10004, 0x20010000, 0x20010004, 0x200, 0x204, 0x20000200, 0x20000204, 0x10200, 0x10204, 0x20010200, 0x20010204};
        int[] pc2bytes1 = {0, 0x1, 0x100000, 0x100001, 0x4000000, 0x4000001, 0x4100000, 0x4100001, 0x100, 0x101, 0x100100, 0x100101, 0x4000100, 0x4000101, 0x4100100, 0x4100101};
        int[] pc2bytes2 = {0, 0x8, 0x800, 0x808, 0x1000000, 0x1000008, 0x1000800, 0x1000808, 0, 0x8, 0x800, 0x808, 0x1000000, 0x1000008, 0x1000800, 0x1000808};
        int[] pc2bytes3 = {0, 0x200000, 0x8000000, 0x8200000, 0x2000, 0x202000, 0x8002000, 0x8202000, 0x20000, 0x220000, 0x8020000, 0x8220000, 0x22000, 0x222000, 0x8022000, 0x8222000};
        int[] pc2bytes4 = {0, 0x40000, 0x10, 0x40010, 0, 0x40000, 0x10, 0x40010, 0x1000, 0x41000, 0x1010, 0x41010, 0x1000, 0x41000, 0x1010, 0x41010};
        int[] pc2bytes5 = {0, 0x400, 0x20, 0x420, 0, 0x400, 0x20, 0x420, 0x2000000, 0x2000400, 0x2000020, 0x2000420, 0x2000000, 0x2000400, 0x2000020, 0x2000420};
        int[] pc2bytes6 = {0, 0x10000000, 0x80000, 0x10080000, 0x2, 0x10000002, 0x80002, 0x10080002, 0, 0x10000000, 0x80000, 0x10080000, 0x2, 0x10000002, 0x80002, 0x10080002};
        int[] pc2bytes7 = {0, 0x10000, 0x800, 0x10800, 0x20000000, 0x20010000, 0x20000800, 0x20010800, 0x20000, 0x30000, 0x20800, 0x30800, 0x20020000, 0x20030000, 0x20020800, 0x20030800};
        int[] pc2bytes8 = {0, 0x40000, 0, 0x40000, 0x2, 0x40002, 0x2, 0x40002, 0x2000000, 0x2040000, 0x2000000, 0x2040000, 0x2000002, 0x2040002, 0x2000002, 0x2040002};
        int[] pc2bytes9 = {0, 0x10000000, 0x8, 0x10000008, 0, 0x10000000, 0x8, 0x10000008, 0x400, 0x10000400, 0x408, 0x10000408, 0x400, 0x10000400, 0x408, 0x10000408};
        int[] pc2bytes10 = {0, 0x20, 0, 0x20, 0x100000, 0x100020, 0x100000, 0x100020, 0x2000, 0x2020, 0x2000, 0x2020, 0x102000, 0x102020, 0x102000, 0x102020};
        int[] pc2bytes11 = {0, 0x1000000, 0x200, 0x1000200, 0x200000, 0x1200000, 0x200200, 0x1200200, 0x4000000, 0x5000000, 0x4000200, 0x5000200, 0x4200000, 0x5200000, 0x4200200, 0x5200200};
        int[] pc2bytes12 = {0, 0x1000, 0x8000000, 0x8001000, 0x80000, 0x81000, 0x8080000, 0x8081000, 0x10, 0x1010, 0x8000010, 0x8001010, 0x80010, 0x81010, 0x8080010, 0x8081010};
        int[] pc2bytes13 = {0, 0x4, 0x100, 0x104, 0, 0x4, 0x100, 0x104, 0x1, 0x5, 0x101, 0x105, 0x1, 0x5, 0x101, 0x105};
        long[] masks = {4294967295L, 2147483647, 1073741823, 536870911, 268435455, 134217727, 67108863, 33554431, 16777215, 8388607, 4194303, 2097151, 1048575, 524287, 262143, 131071, 65535, 32767, 16383, 8191, 4095, 2047, 1023, 511, 255, 127, 63, 31, 15, 7, 3, 1, 0};
        int iterations = key.length() > 8 ? 3 : 1;
        List<Long> keys = new ArrayList<>();
        int[] shifts = {0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0};
        int m = 0, n = 0;
        for (int j = 0; j < iterations; j++) {
//            System.out.println(Character.codePointAt(key, m++) << 24);
//            System.out.println(Character.codePointAt(key, m++) << 16);
//            System.out.println(Character.codePointAt(key, m++) << 8);
//            System.out.println(Character.codePointAt(key, m++));
            long left = (Character.codePointAt(key, m++) << 24) | (Character.codePointAt(key, m++) << 16) | (Character.codePointAt(key, m++) << 8) | (Character.codePointAt(key, m++));
            long right = (Character.codePointAt(key, m++) << 24) | (Character.codePointAt(key, m++) << 16) | (Character.codePointAt(key, m++) << 8) | (Character.codePointAt(key, m++));
            long temp = ((left >> 4 & masks[4]) ^ right) & 0x0f0f0f0f;
            right ^= temp;
            left ^= (temp << 4);
            temp = ((right >> 16 & masks[16]) ^ left) & 0x0000ffff;
            left ^= temp;
            right ^= (temp << 16);
            temp = ((left >> 2 & masks[2]) ^ right) & 0x33333333;
            right ^= temp;
            left ^= (temp << 2);
            temp = ((right >> 16 & masks[16]) ^ left) & 0x0000ffff;
            left ^= temp;
            right ^= (temp << 16);
            temp = ((left >> 1 & masks[1]) ^ right) & 0x55555555;
            right ^= temp;
            left ^= (temp << 1);
            temp = ((right >> 8 & masks[8]) ^ left) & 0x00ff00ff;
            left ^= temp;
            right ^= (temp << 8);
            temp = ((left >> 1 & masks[1]) ^ right) & 0x55555555;
            right ^= temp;
            left ^= (temp << 1);
            temp = (left << 8) | ((right >> 20 & masks[20]) & 0x000000f0);
            left = (right << 24) | ((right << 8) & 0xff0000) | ((right >> 8 & masks[8]) & 0xff00) | ((right >> 24 & masks[24]) & 0xf0);
            right = temp;
            for (int i = 0; i < shifts.length; i++) {
                if (shifts[i] > 0) {
                    left = ((left << 2) | (left >> 26 & masks[26]));
                    right = ((right << 2) | (right >> 26 & masks[26]));
                } else {
                    left = ((left << 1) | (left >> 27 & masks[27]));
                    right = ((right << 1) | (right >> 27 & masks[27]));
                }
                left = left & -0xf;
                right = right & -0xf;
                long lefttemp = pc2bytes0[(int) (left >> 28 & masks[28])] | pc2bytes1[(int) ((left >> 24 & masks[24]) & 0xf)]
                        | pc2bytes2[(int) ((left >> 20 & masks[20]) & 0xf)] | pc2bytes3[(int) ((left >> 16 & masks[16]) & 0xf)]
                        | pc2bytes4[(int) ((left >> 12 & masks[12]) & 0xf)] | pc2bytes5[(int) ((left >> 8 & masks[8]) & 0xf)]
                        | pc2bytes6[(int) ((left >> 4 & masks[4]) & 0xf)];
                long righttemp = pc2bytes7[(int) (right >> 28 & masks[28])] | pc2bytes8[(int) ((right >> 24 & masks[24]) & 0xf)]
                        | pc2bytes9[(int) ((right >> 20 & masks[20]) & 0xf)] | pc2bytes10[(int) ((right >> 16 & masks[16]) & 0xf)]
                        | pc2bytes11[(int) ((right >> 12 & masks[12]) & 0xf)] | pc2bytes12[(int) ((right >> 8 & masks[8]) & 0xf)]
                        | pc2bytes13[(int) ((right >> 4 & masks[4]) & 0xf)];
                temp = ((righttemp >> 16 & masks[16]) ^ lefttemp) & 0x0000ffff;
                keys.add(lefttemp ^ temp);
                keys.add(righttemp ^ (temp << 16));
            }
        }
        return keys;
    }

    private static int ord(String key, int index) {
        if (key == null || key == "") {
            return '\u0000';
        }
        return ordd(key.charAt(index));
    }

    public static int ordd(char s) {
        return s & 0xff;
    }
    /**
     * Convert the characters to ASCII value
     * @param character character
     * @return ASCII value
     */
    public static int CharToASCII(final char character){
        return (int)character;
    }
    public static int orrd(String s) {
        return s.length() > 0 ? (s.getBytes(StandardCharsets.UTF_8)[0] & 0xff) : 0;
    }

    public static void main(String[] args) {



        String privateKey = "8cfcacd995116e0afada7a37ca2dbff6b376f47d504810b7d6726df7f781310221da66b946f846bf1f68116ff78a792d105567c79f27a1bc6a389a5c4d617c9f6c4e28cc0996742428bfa7fddc35efee594d8458ff3d77da5386b0d8bb8a72f725dcb53c87a3dbca875baae576bc96956f4addc809bf157c6db1d9f5da949ddc";
        //实际生产环境用这种加密方式
        String des_encrypt = desEncrypt(privateKey, "{\"out_trade_no\":\"1510912835kdlc_xjk\",\"money\":3475100,\"repay_time\":1511511270}");

        //testing 加密
        String des_encrypt1 = des(privateKey, "{\"out_trade_no\":\"1510912835kdlc_xjk\",\"money\":3475100,\"repay_time\":1511511270}", true,0, null, 0);

        System.out.println(des_encrypt1);
        //testing 解密
        System.out.println(des(privateKey, des_encrypt1, false, 0, null, 0));
//        String hkey = desDecrypt(privateKey,des_encrypt);
//        System.out.println(hkey);

    }
}