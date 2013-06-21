package com.liuxinglanyue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.impl.client.DefaultHttpClient;  
  
/**
 * 版权所有人：liuxinglanyue
 * 英文名：Herry
 * @author Jiao JianFeng
 *2013-6-19
 */
public class ClientAbortMethod { 
	
	private HttpClient httpClient = new DefaultHttpClient();
	private static int goldYear = 4;
	private static int pageNum = 1;
	private static int pageNumFrom = 1;
	
	private String getPage(String url) throws ClientProtocolException, IOException {
		StringBuilder stringBuilder = new StringBuilder();
		HttpGet httpget = new HttpGet(url); 
		HttpResponse response = httpClient.execute(httpget);  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));  
           String str = null;  
           while(null != (str = reader.readLine()) ){  
               stringBuilder.append(str);
            }  
        }  
        httpget.abort();
		return stringBuilder.toString();
	}
	
	private List<String> getPageUrl(String pageString) {
		//System.out.println(pageString);
		List<String> pageUrlList = new ArrayList<String>();
		int address = -1;
		int to = -1;
		//address = pageString.indexOf("Do");
		//System.out.println(address);
		boolean flag = true;
		while(-1 != (address = pageString.indexOf("product-gs"))) {
			pageString = pageString.substring(address);
			if(-1 != (to = pageString.indexOf("\""))) {
				String url = pageString.substring(0, to);
				//System.out.println(url);
				if(flag) {
					pageUrlList.add("http://www.alibaba.com/" + url);
					flag = false;
				} else {
					flag = true;
				}
				pageString = pageString.substring(to);
			}
			//System.out.println(pageString);
		}
		return pageUrlList;
	}
	
	private static int gold(String detailString) {
		int to = -1;
		if(-1 != (to = detailString.indexOf("title=\"Gold Supplier "))) {
			String detail = detailString.substring(to + 21, to + 31);
			int th = -1;
			if(-1 != (th = detail.indexOf("th"))) {
				String number = detail.substring(0, th);
				try {
					int year = Integer.parseInt(number);
					return year;
				} catch (Exception e) {
					
				}
				//System.out.println(number);
			}
			//System.out.println(detail);
		}
		return 1000;
	}
	
	private static String getProValue(String detailString) {
		String proValue = "";
		int from = -1;
		if(-1 != (from = detailString.indexOf("Payment Terms:"))) {
			String proString = detailString.substring(from + 55, from + 150);
			int to = -1;
			if(-1 != (to = proString.indexOf("<"))) {
				return proString.substring(0, to);
			}
			//System.out.println(proString);
		}
		return proValue;
	}
	
	private static String getContactUrl(String detailString) {
		String contactString = "";
		int from = -1;
		if(-1 != (from = detailString.indexOf("Contact Details"))) {
			String string = detailString.substring(from - 200, from - 24);
			int to = -1;
			if(-1 != (to = string.lastIndexOf("\""))) {
				//System.out.println(string.substring(to));
				return string.substring(to + 1);
			}
			//System.out.println(string);
		}
		return contactString;
	}
	
	private static String getCity(String contactString) {
		String cityString = "";
		int from = -1;
		if(-1 != (from = contactString.indexOf("City:"))) {
			String proString = contactString.substring(from + 17, from + 50);
			int to = -1;
			if(-1 != (to = proString.indexOf("<"))) {
				//System.out.println(proString.substring(0, to));
				if(proString.startsWith(">")) {
					return proString.substring(1, to);
				}
				return proString.substring(0, to);
			}
			//System.out.println(proString);
		}
		return cityString;
	}
	
	private static String getMobile_Phone(String contactString) {
		String mobile_PhoneString = "";
		int from = -1;
		if(-1 != (from = contactString.indexOf("Mobile Phone:"))) {
			String proString = contactString.substring(from + 25, from + 50);
			int to = -1;
			if(-1 != (to = proString.indexOf("<"))) {
				//System.out.println(proString.substring(0, to));
				if(proString.startsWith(">")) {
					//System.out.println(proString.substring(1, to));
					return proString.substring(1, to);
				}
				return proString.substring(0, to);
			}
			System.out.println(proString);
		}
		return mobile_PhoneString;
	}
	
	private static String getName(String contactString) {
		String nameString = "";
		int from = -1;
		if(-1 != (from = contactString.indexOf("<h1 class=\"name\">"))) {
			String proString = contactString.substring(from + 21, from + 100);
			int to = -1;
			if(-1 != (to = proString.indexOf("	"))) {
				//System.out.println(proString.substring(0, to));
				nameString = proString.substring(0, to);
			}
			//System.out.println(proString);
		}
		
		if("".equals(nameString)) {
			if(-1 != (from = contactString.indexOf("<h5>"))) {
				String proString = contactString.substring(from + 9, from + 50);
				int to = -1;
				if(-1 != (to = proString.indexOf("	"))) {
					//System.out.println(proString.substring(0, to));
					nameString = proString.substring(0, to);
				}
				//System.out.println(proString);
			}
		}
		return nameString;
	}
	
	private static String getTelephone(String contactString) {
		String telephoneString = "";
		int from = -1;
		if(-1 != (from = contactString.indexOf("Telephone:"))) {
			String proString = contactString.substring(from + 22, from + 50);
			int to = -1;
			if(-1 != (to = proString.indexOf("<"))) {
				//System.out.println(proString.substring(0, to));
				if(proString.startsWith(">")) {
					return proString.substring(1, to);
				}
				return proString.substring(0, to);
			}
			//System.out.println(proString);
		}
		return telephoneString;
	}
	
	private static String getProvince(String contactString) {
		String provinceString = "";
		int from = -1;
		if(-1 != (from = contactString.indexOf("Province/State:"))) {
			String proString = contactString.substring(from + 27, from + 50);
			int to = -1;
			if(-1 != (to = proString.indexOf("<"))) {
				//System.out.println(proString.substring(0, to));
				if(proString.startsWith(">")) {
					return proString.substring(1, to);
				}
				return proString.substring(0, to);
			}
			//System.out.println(proString);
		}
		return provinceString;
	}

	
	private static User getUserInfo(String proValue, String url, String contactString, int year) {
		User user = new User();
		user.setPayment_Terms(proValue);
		user.setUrl(url);
		user.setCity(getCity(contactString));
		user.setMobile_Phone(getMobile_Phone(contactString));
		user.setName(getName(contactString));
		user.setTelephone(getTelephone(contactString));
		user.setProvince(getProvince(contactString));
		user.setGold(year + "");
		return user;
	}
	
	private static void exportExcel(List<User> userList, String key) throws IOException, RowsExceededException, WriteException {
		System.out.println("导出到Excel文件。。。。");
		String title[]={"姓名","手机","固定电话","支付方式","供应商年限","省份","城市","链接"};

		SimpleDateFormat myFormat=new SimpleDateFormat("yyyy年MM月dd日");
		String path = "data//" + myFormat.format(new Date());
		File direct = new File(path);
		direct.mkdirs();
		File file = new File(path + "//" + key + "_" + System.currentTimeMillis() + "_" + goldYear + "年_从" + pageNumFrom + "页到" + pageNum + "页.xls");
		WritableWorkbook book= Workbook.createWorkbook(file); 
        WritableSheet sheet=book.createSheet("第一页",0); 
        sheet.setColumnView(0, 25);
        sheet.setColumnView(1, 20);
        sheet.setColumnView(2, 20);
        sheet.setColumnView(3, 30);
        sheet.setColumnView(4, 15);
        sheet.setColumnView(5, 15);
        sheet.setColumnView(6, 15);
        sheet.setColumnView(7, 100);
        for(int i=0;i<8;i++) {
            sheet.addCell(new Label(i,0,title[i])); 
        }
        for(int j=0; j<userList.size(); j++) {
    		sheet.addCell(new Label(0, j+1, userList.get(j).getName()));
    		sheet.addCell(new Label(1, j+1, userList.get(j).getMobile_Phone()));
    		sheet.addCell(new Label(2, j+1, userList.get(j).getTelephone()));
    		sheet.addCell(new Label(3, j+1, userList.get(j).getPayment_Terms()));
    		sheet.addCell(new Label(4, j+1, userList.get(j).getGold()));
    		sheet.addCell(new Label(5, j+1, userList.get(j).getProvince()));
    		sheet.addCell(new Label(6, j+1, userList.get(j).getCity()));
    		WritableHyperlink link = new WritableHyperlink(7, j+1, new URL(userList.get(j).getUrl()));
    		sheet.addHyperlink(link);
    		//sheet.addCell(new Label(6, j+1, userList.get(j).getUrl()));
    	}
        book.write();
        book.close();
        System.out.println("导出到Excel文件成功，文件路径为：" + file.getAbsolutePath());
	}

    public static void main(String[] args){  
    	//String key = "bandage dress";
    	String key = args[0];
    	try {
	    	goldYear = Integer.parseInt(args[1]);
	    	pageNumFrom = Integer.parseInt(args[2]);
	    	pageNum = Integer.parseInt(args[3]);
    	} catch (Exception e) {
    		
    	}
    	key = key.replaceAll(" ", "_");
        ClientAbortMethod clientAbortMethod = new ClientAbortMethod();
        List<String> pageUrlList = new ArrayList<String>();
        List<User> userList = new ArrayList<User>();
        List<User> cloneUserList = new ArrayList<User>();
        for(int i=pageNumFrom; i<= pageNum; i++) {
        	System.out.println("下载第" + i + "个页面。。。。。");
        	String pageString = "";
			try {
				pageString = clientAbortMethod.getPage("http://www.alibaba.com/products/F0/" + key + "/--CN/" + i +".html");
			} catch (Exception e) {
				e.printStackTrace();
			}
        	//System.out.println(pageString);
        	if(!"".equals(pageString)) {
        		pageUrlList.addAll(clientAbortMethod.getPageUrl(pageString));
        	}
        }
        int pagepage = 0;
        if(pageUrlList.size() > 0) {
        	System.out.println(pageUrlList.size());
        	for(String url : pageUrlList) {
        		pagepage++;
        		System.out.println("处理第" + pagepage + "个链接：" + url);
        		String detailString = "";
        		try {
					detailString = clientAbortMethod.getPage(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
        		int year = 100;
        		if(!"".equals(detailString) && (year = gold(detailString)) <= goldYear && -1 == detailString.toLowerCase().indexOf("paypal")) {
        			String contactString = "";
        			try {
        				//getContactUrl(detailString);
    					contactString = clientAbortMethod.getPage(getContactUrl(detailString));
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
        			cloneUserList.add(getUserInfo(getProValue(detailString), url, contactString, year));
        		}
        	}
        }
        clientAbortMethod.httpClient.getConnectionManager().shutdown();   
        
        Collections.sort(cloneUserList);
        User clone = null;
        for(User user : cloneUserList) {
        	if(null != clone) {
        		if(!user.getTelephone().equals(clone.getTelephone()) || !user.getMobile_Phone().equals(clone.getMobile_Phone())) {
        			userList.add(user);
        			clone = user;
        		}
        	} else {
        		userList.add(user);
        		clone = user;
        	}
        }
        
        for(User user : userList) {
        	System.out.println(user.getName() + "  " + user.getMobile_Phone() + "  " + user.getTelephone() + "  " + user.getPayment_Terms());
        }
        try {
			exportExcel(userList, key);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }  
  
}  

class User implements Comparable<User> {
	private String name;
	private String url;
	private String payment_Terms;
	private String telephone;
	private String mobile_Phone;
	private String city;
	private String province;
	private String gold;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPayment_Terms() {
		return payment_Terms;
	}

	public void setPayment_Terms(String payment_Terms) {
		this.payment_Terms = payment_Terms;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile_Phone() {
		return mobile_Phone;
	}

	public void setMobile_Phone(String mobile_Phone) {
		this.mobile_Phone = mobile_Phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int compareTo(User o) {
		if(this.mobile_Phone.equals(o.getMobile_Phone())) {
			if(this.telephone.equals(o.getTelephone())) {
				return 0;
			}
		}
		return 1;
	}

	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}
}