package sfa.nav.http.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Maree;

public class onLineTester {
	public  void toto(String[] args) {
		MyClientHttp mhc = new MyClientHttp();
		Map<String, String> headers = new HashMap<String, String>();
		mhc.Post ("https://reqbin.com/echo/post/json", headers);
		mhc.Get ("https://reqbin.com/echo/post/json", headers);
	}

	public static void main(String[] args) throws NavException {
		MyClientHttp mhc = new MyClientHttp();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "*/*");
		headers.put("Accept-Language", "en,fr-FR;q=0.9,fr;q=0.8,en-GB;q=0.7,en-US;q=0.6");
		headers.put("DNT", "1");
		headers.put("Origin", "https://maree.shom.fr");
		headers.put("Referer", "https://maree.shom.fr");
		headers.put("Sec-Fetch-Dest", "empty");
		headers.put("Sec-Fetch-Mode", "cors");
		headers.put("Sec-Fetch-Site", "same-site");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
		headers.put("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
		headers.put("sec-ch-ua-mobile", "?0");
		headers.put("sec-ch-ua-platform", "\"Windows\"");
		String reponse = null;
		reponse = mhc.Get ("https://services.data.shom.fr/b2q8lrcdl4s04cbabsj4nhcb/hdm/spm/hlt?harborName=SAINT-QUAY-PORTRIEUX&duration=7&date=2023-06-23&utc=standard&correlation=1", headers);
		
		ArrayList<Maree> retour = new ArrayList<>();
		// reponse = "{\"2023-06-21\":[[\"tide.low\",\"04:00\",\"2.56\",\"---\"],[\"tide.high\",\"09:48\",\"9.98\",\"69\"],[\"tide.low\",\"16:13\",\"2.80\",\"---\"],[\"tide.high\",\"21:56\",\"10.22\",\"66\"]],\"2023-06-22\":[[\"tide.low\",\"04:34\",\"2.78\",\"---\"],[\"tide.high\",\"10:23\",\"9.76\",\"64\"],[\"tide.low\",\"16:46\",\"3.07\",\"---\"],[\"tide.high\",\"22:30\",\"9.95\",\"61\"]],\"2023-06-23\":[[\"tide.low\",\"05:07\",\"3.07\",\"---\"],[\"tide.high\",\"10:57\",\"9.46\",\"58\"],[\"tide.low\",\"17:20\",\"3.40\",\"---\"],[\"tide.high\",\"23:05\",\"9.62\",\"56\"]],\"2023-06-24\":[[\"tide.low\",\"05:41\",\"3.40\",\"---\"],[\"tide.high\",\"11:32\",\"9.14\",\"53\"],[\"tide.low\",\"17:55\",\"3.74\",\"---\"],[\"tide.high\",\"23:42\",\"9.26\",\"50\"]],\"2023-06-25\":[[\"tide.low\",\"06:18\",\"3.71\",\"---\"],[\"tide.high\",\"12:12\",\"8.84\",\"47\"],[\"tide.low\",\"18:36\",\"4.05\",\"---\"],[\"tide.none\",\"--:--\",\"---\",\"---\"]],\"2023-06-26\":[[\"tide.high\",\"00:25\",\"8.93\",\"45\"],[\"tide.low\",\"07:02\",\"3.98\",\"---\"],[\"tide.high\",\"12:59\",\"8.59\",\"43\"],[\"tide.low\",\"19:26\",\"4.28\",\"---\"]],\"2023-06-27\":[[\"tide.high\",\"01:17\",\"8.69\",\"42\"],[\"tide.low\",\"07:56\",\"4.15\",\"---\"],[\"tide.high\",\"13:57\",\"8.48\",\"41\"],[\"tide.low\",\"20:29\",\"4.36\",\"---\"]]}";
		Maree.fromListeShomToListeMaree(reponse);
	}

}

