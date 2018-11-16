package test;

import com.jeeplus.common.security.Digests;
import com.jeeplus.common.utils.Base64;

public class TestSigin {
	public static void main(String[] args) {
//		//获取参数并验证签名
//		String key = "3ew8t1btY8LBg38iCiBdKqLlR9NGAnGDdCftXd7v6NSLdapzsOKdLwvgggjMUjOc";
//		String rstOrderCode = "1516241690565465102";
//		String timestamp = "20171112080101";
//		String resCode = "0001";
//		String sign = "";
//		//验签
//		System.out.println(genSign(timestamp, rstOrderCode, resCode, key));
		//{"parm":{"officeId":"100000002005","timestamp":"180118140120","orderCode":"1516255267644387249","resCode":"0000","sign":"4LpdpWDyEtzd0kAm+dANYA=="}
		//{"parm":{"officeId":"100000002005","timestamp":"180118134917","orderCode":"1516254530591604700","resCode":"0000","sign":"AGMI82o5W6m73L4CdGYCGg=="}}
		//获取参数并验证签名
//		String key = "3ew8t1btY8LBg38iCiBdKqLlR9NGAnGDdCftXd7v6NSLdapzsOKdLwvzkgjMUjOc";
//		String rstOrderCode = "1516255267644387249";
//		String timestamp = "180118140120";
//		String resCode = "0000";
//		String sign = "";
//		//验签
//		System.out.println(genSign(timestamp, rstOrderCode, resCode, key));
	
		System.out.println(new String(Digests.md5(("1111").getBytes())));
	}
	
	public static String genSign(String timestamp, String orderCode, String resCode, String key){
		return Base64.encode(Digests.md5((timestamp+orderCode+resCode+key).getBytes()));
	}
}
