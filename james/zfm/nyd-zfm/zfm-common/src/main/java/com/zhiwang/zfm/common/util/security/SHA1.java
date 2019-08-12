package com.zhiwang.zfm.common.util.security;


/** *sha鍔犲瘑绠楁硶 * */
public class SHA1 {

	private final int[] abcde = { 0x67452301, 0xefcdab89, 0x98badcfe,
			0x10325476, 0xc3d2e1f0 };

	// 鎽樿鏁版嵁瀛樺偍鏁扮粍
	private int[] digestInt = new int[5];
	// 璁＄畻杩囩▼涓殑涓存椂鏁版嵁瀛樺偍鏁扮粍
	private int[] tmpData = new int[80];

	// 璁＄畻sha-1鎽樿
	private int process_input_bytes(byte[] bytedata) {
		// 鍒濊瘯鍖栧父閲�
		System.arraycopy(abcde, 0, digestInt, 0, abcde.length);
		// 鏍煎紡鍖栬緭鍏ュ瓧鑺傛暟缁勶紝琛�10鍙婇暱搴︽暟鎹�
		byte[] newbyte = byteArrayFormatData(bytedata);
		// 鑾峰彇鏁版嵁鎽樿璁＄畻鐨勬暟鎹崟鍏冧釜鏁�
		int MCount = newbyte.length / 64;
		// 寰幆瀵规瘡涓暟鎹崟鍏冭繘琛屾憳瑕佽绠�
		for (int pos = 0; pos < MCount; pos++) {
			// 灏嗘瘡涓崟鍏冪殑鏁版嵁杞崲鎴�16涓暣鍨嬫暟鎹紝骞朵繚瀛樺埌tmpData鐨勫墠16涓暟缁勫厓绱犱腑
			for (int j = 0; j < 16; j++) {
				tmpData[j] = byteArrayToInt(newbyte, (pos * 64) + (j * 4));
			}
			// 鎽樿璁＄畻鍑芥暟
			encrypt();
		}
		return 20;
	}

	// 鏍煎紡鍖栬緭鍏ュ瓧鑺傛暟缁勬牸寮�
	private byte[] byteArrayFormatData(byte[] bytedata) {
		// 琛�0鏁伴噺
		int zeros = 0;
		// 琛ヤ綅鍚庢�讳綅鏁�
		int size = 0;
		// 鍘熷鏁版嵁闀垮害
		int n = bytedata.length;
		// 妯�64鍚庣殑鍓╀綑浣嶆暟
		int m = n % 64;
		// 璁＄畻娣诲姞0鐨勪釜鏁颁互鍙婃坊鍔�10鍚庣殑鎬婚暱搴�
		if (m < 56) {
			zeros = 55 - m;
			size = n - m + 64;
		} else if (m == 56) {
			zeros = 63;
			size = n + 8 + 64;
		} else {
			zeros = 63 - m + 56;
			size = (n + 64) - m + 64;
		}
		// 琛ヤ綅鍚庣敓鎴愮殑鏂版暟缁勫唴瀹�
		byte[] newbyte = new byte[size];
		// 澶嶅埗鏁扮粍鐨勫墠闈㈤儴鍒�
		System.arraycopy(bytedata, 0, newbyte, 0, n);
		// 鑾峰緱鏁扮粍Append鏁版嵁鍏冪礌鐨勪綅缃�
		int l = n;
		// 琛�1鎿嶄綔
		newbyte[l++] = (byte) 0x80;
		// 琛�0鎿嶄綔
		for (int i = 0; i < zeros; i++) {
			newbyte[l++] = (byte) 0x00;
		}
		// 璁＄畻鏁版嵁闀垮害锛岃ˉ鏁版嵁闀垮害浣嶅叡8瀛楄妭锛岄暱鏁村瀷
		long N = (long) n * 8;
		byte h8 = (byte) (N & 0xFF);
		byte h7 = (byte) ((N >> 8) & 0xFF);
		byte h6 = (byte) ((N >> 16) & 0xFF);
		byte h5 = (byte) ((N >> 24) & 0xFF);
		byte h4 = (byte) ((N >> 32) & 0xFF);
		byte h3 = (byte) ((N >> 40) & 0xFF);
		byte h2 = (byte) ((N >> 48) & 0xFF);
		byte h1 = (byte) (N >> 56);
		newbyte[l++] = h1;
		newbyte[l++] = h2;
		newbyte[l++] = h3;
		newbyte[l++] = h4;
		newbyte[l++] = h5;
		newbyte[l++] = h6;
		newbyte[l++] = h7;
		newbyte[l++] = h8;
		return newbyte;
	}

	private int f1(int x, int y, int z) {
		return (x & y) | (~x & z);
	}

	private int f2(int x, int y, int z) {
		return x ^ y ^ z;
	}

	private int f3(int x, int y, int z) {
		return (x & y) | (x & z) | (y & z);
	}

	private int f4(int x, int y) {
		return (x << y) | x >>> (32 - y);
	}

	// 鍗曞厓鎽樿璁＄畻鍑芥暟
	private void encrypt() {
		for (int i = 16; i <= 79; i++) {
			tmpData[i] = f4(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14]
					^ tmpData[i - 16], 1);
		}
		int[] tmpabcde = new int[5];
		for (int i1 = 0; i1 < tmpabcde.length; i1++) {
			tmpabcde[i1] = digestInt[i1];
		}
		for (int j = 0; j <= 19; j++) {
			int tmp = f4(tmpabcde[0], 5)
					+ f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4]
					+ tmpData[j] + 0x5a827999;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int k = 20; k <= 39; k++) {
			int tmp = f4(tmpabcde[0], 5)
					+ f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4]
					+ tmpData[k] + 0x6ed9eba1;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int l = 40; l <= 59; l++) {
			int tmp = f4(tmpabcde[0], 5)
					+ f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4]
					+ tmpData[l] + 0x8f1bbcdc;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int m = 60; m <= 79; m++) {
			int tmp = f4(tmpabcde[0], 5)
					+ f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4]
					+ tmpData[m] + 0xca62c1d6;
			tmpabcde[4] = tmpabcde[3];
			tmpabcde[3] = tmpabcde[2];
			tmpabcde[2] = f4(tmpabcde[1], 30);
			tmpabcde[1] = tmpabcde[0];
			tmpabcde[0] = tmp;
		}
		for (int i2 = 0; i2 < tmpabcde.length; i2++) {
			digestInt[i2] = digestInt[i2] + tmpabcde[i2];
		}
		for (int n = 0; n < tmpData.length; n++) {
			tmpData[n] = 0;
		}
	}

	// 4瀛楄妭鏁扮粍杞崲涓烘暣鏁�
	private int byteArrayToInt(byte[] bytedata, int i) {
		return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16)
				| ((bytedata[i + 2] & 0xff) << 8) | (bytedata[i + 3] & 0xff);
	}

	// 鏁存暟杞崲涓�4瀛楄妭鏁扮粍
	private void intToByteArray(int intValue, byte[] byteData, int i) {
		byteData[i] = (byte) (intValue >>> 24);
		byteData[i + 1] = (byte) (intValue >>> 16);
		byteData[i + 2] = (byte) (intValue >>> 8);
		byteData[i + 3] = (byte) intValue;
	}

	// 灏嗗瓧鑺傝浆鎹负鍗佸叚杩涘埗瀛楃涓�
	private static String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	// 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃涓�
	private static String byteArrayToHexString(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexString(bytearray[i]);
		}
		return strDigest;
	}

	// 璁＄畻sha-1鎽樿锛岃繑鍥炵浉搴旂殑瀛楄妭鏁扮粍
	public byte[] getDigestOfBytes(byte[] byteData) {
		process_input_bytes(byteData);
		byte[] digest = new byte[20];
		for (int i = 0; i < digestInt.length; i++) {
			intToByteArray(digestInt[i], digest, i * 4);
		}
		return digest;
	}

	// 璁＄畻sha-1鎽樿锛岃繑鍥炵浉搴旂殑鍗佸叚杩涘埗瀛楃涓�
	public String getDigestOfString(byte[] byteData) {
		return byteArrayToHexString(getDigestOfBytes(byteData));
	}
	
	public static void main(String[] args) {
		System.out.println(new SHA1().getDigestOfString("abc123".getBytes()));
	}
	
}