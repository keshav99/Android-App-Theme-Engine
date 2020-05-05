import java.io.IOException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
public class themer {
	
	public static String appname = "flipboard";
	public static String primary = "#ff7986CB";
	public static String primaryApp = "ffffffff";
	
	public static void decompileAPK() {
		  try {
			  	System.out.println("Decompiling the APK");
	            runProcess("java -jar apktool.jar d ../Android/"+appname+".apk ");
	            System.out.println("Successfully Decompiled");
	    		String directory = getDirectory("colors");
	            docFactory = DocumentBuilderFactory.newInstance();
	            docBuilder = docFactory.newDocumentBuilder();
	            doc = docBuilder.parse(directory);
	            entries = doc.getElementsByTagName("*");
	            
	            getColors();
	            changeStrings();
	            recompile();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

		

		static DocumentBuilderFactory docFactory ;
		static DocumentBuilder docBuilder;
		static Document doc ;
		static NodeList entries;
		
		static DocumentBuilderFactory docFactory1 ;
		static DocumentBuilder docBuilder1;
		static Document doc1 ;
		static NodeList entries1;
		static String names1[], values1[];
		
		private static void changeStrings() {
			// TODO Auto-generated method stub
			try {
    		 String directory = getDirectory("styles");

			 docFactory1 = DocumentBuilderFactory.newInstance();
	         docBuilder1 = docFactory1.newDocumentBuilder();
	         doc1 = docBuilder1.parse(directory);
	         entries1 = doc1.getElementsByTagName("item");
	         
	         names1 = new String[entries1.getLength()+1];
	         values1 = new String[entries1.getLength()+1];

	         for (int i=0; i<entries1.getLength(); i++) {
	        	
	     		Element element = (Element) entries1.item(i);
	     		Element parent = (Element) element.getParentNode();
   	            String Color = element.getFirstChild().toString();

	     		names1[i] = element.getAttribute("name");
	     		values1[i] = Color.substring(7,Color.length()-1);
//	     		System.out.println(values1[i]);
	         }
	         System.out.println(entries1.getLength()+" "+Arrays.asList(names1).indexOf("cardBackgroundColor"));
	         setWhiteTexts();
	         setBlackBg();
			}catch(Exception e) {
				
			}
		}
	

	private static void setBlackBg() {
			// TODO Auto-generated method stub
			for(int i=0; i<3028; i++) {
//				System.out.println(i);
				String white[] = {"Background", "background", "BG","bg","Bg","backGround"};
				String black[] = {"title","btn","button","Title","BTN","Button","Btn"};
				if(hasAny(names1[i], white)) {
					String str=names1[i].substring(2);
					if(str.length()==6) {
				     int r = Integer.valueOf( str.substring( 0, 2 ), 16 );
				     int g = Integer.valueOf( str.substring( 2, 4 ), 16 );
				     int b = Integer.valueOf( str.substring( 4, 6 ), 16 );
				     if(r==g&&g==b) {
				    	 entries1.item((int) i).setTextContent("@android:color/black");
							changeVals("styles",doc1);
				     }
					}
						if(values1[(int) i].contains("drawable")||values1[(int) i].contains("color/white")) {
//							System.out.println(names1[i]);
							entries1.item((int) i).setTextContent("@android:color/black");
							changeVals("styles",doc1);
						}
					
					
				}
			}
		}
	private static boolean hasAny(String str, String containers[]) {
			for(String i:containers) {
				if(str.contains(i)) {
					return true;
				}
			}
		return false;
	}
	
	private static boolean hasAll(String str, String containers[]) {
		for(String i:containers) {
			if(!str.contains(i)) {
				return false;
			}
		}
	return true;
}
	private static void setWhiteTexts() {
		// TODO Auto-generated method stub
		String white[] = {"textColor","TextColor"};
		String black[] = {"selected","Selected","disable","Disable","Inverse","inverse","primary","Primary","Secondary","secondary"
				,"tertiary","Tertiary"};
		String textTypes[] = {"text","Text","title","Title"};
		for(int i=0; i<entries1.getLength(); i++) {
			if(hasAny(names1[i],white)) {
				
				if(!hasAny(names1[i],black)) {
					if(!checkLightGray(values1[i])) {
						entries1.item(i).setTextContent("@android:color/white");
						changeVals("styles",doc1);
					}
				}
			}
			
			if(hasAny(names1[i],textTypes)&&(values1[i].contains("color")||values1[i].substring(0,1).equals("#"))) {
				entries1.item(i).setTextContent("@android:color/white");
				changeVals("styles",doc1);
			}
		}
		
//		for (int i=0; i<entries1.getLength(); i++) {
//			Element element = (Element) entries1.item(i);
//			Element textEl[] = new Element[entries1.getLength()];
////			System.out.println("he");
//			if(element.getAttribute("name").contains("text")) {
//				if(element.getAttribute("name").contains("color")||element.getAttribute("name").contains("Color")) {
//					int x = Arrays.asList(names1).indexOf(element.getAttribute("name").toString().replace("text", ""));
//					if(x!=-1) {
//						System.out.println("FOUND JA");
//						element.setTextContent("@android:color/white");
//						entries1.item(x).setTextContent("@android:color/black");
//						changeVals("styles",doc1);
//					}
//				}
//			}
//			if(element.getAttribute("name").contains("Text")) {
//				int x = Arrays.asList(names1).indexOf(element.getAttribute("name").toString().replace("Text", ""));
//				if(x!=-1) {
//					System.out.println("FOUND JA");
//					element.setTextContent("@android:color/white");
//					entries1.item(x).setTextContent("@android:color/black");
//					changeVals("styles",doc1);
//				}
//			}
//			
//		}
	}

	private static int contains(NodeList entry, String str) {
		// TODO Auto-generated method stub
		for(int i=0; i<entry.getLength(); i++) {
			Element ele = (Element) entry.item(i);
			if(ele.getAttribute("name").equals(str)) {
				return i;
			}
		}
		return -1;
	}

	private static void getColors(){
		// TODO Auto-generated method stub
		
		
		
			String names[] = new String[entries.getLength()];
			String values[] = new String[entries.getLength()];
	         
	        for (int i=0; i<entries.getLength(); i++) {
	            Element element = (Element) entries.item(i);
	            String Color = element.getFirstChild().toString();
//	            System.out.println("Found element " + element.getAttribute("name")+" "+ Color.substring(9,Color.length()-1));
	            names[i] = element.getAttribute("name");
	            values[i] = Color.substring(9,Color.length()-1);
//	           System.out.println(values[i]);
	        }
	        if(Arrays.asList(names).indexOf("colorPrimary")!=-1)
	        	primaryApp = values[Arrays.asList(names).indexOf("colorPrimary")].substring(2);
	        else if(Arrays.asList(names).indexOf("primary")!=-1)
	        primaryApp = values[Arrays.asList(names).indexOf("primary")].substring(2);
	        else if(Arrays.asList(names).indexOf("Primary")!=-1)
	        primaryApp = values[Arrays.asList(names).indexOf("Primary")].substring(2);
	        changeColorVals(names, values);
		
	}

	private static void changeColorVals(String[] names, String[] values) {
		// TODO Auto-generated method stub
	for(int i=0; i<(names.length); i++){
            Element element = (Element) entries.item(i);

			if(names[i].contains("light")&&Arrays.asList(names).indexOf(names[i].toString().replace("light", "dark"))!=-1
					&&!names[i].contains("dark")) {
				//System.out.println(names[i]+" "+values[i]);
				element.setAttribute("name", element.getAttribute("name").toString().replace("light", "dark"));
				element = (Element) entries.item(Arrays.asList(names).indexOf(names[i].toString().replace("light", "dark")));
				element.setAttribute("name", element.getAttribute("name").toString().replace("dark", "light"));
				changeVals("colors",doc);
			}
			else if(names[i].contains("Light")&&Arrays.asList(names).indexOf(names[i].toString().replace("Light", "Dark"))!=-1
					&&!names[i].contains("Dark")) {
				element.setAttribute("name", element.getAttribute("name").toString().replace("Light", "Dark"));
				element = (Element) entries.item(Arrays.asList(names).indexOf(names[i].toString().replace("Light", "Dark")));
				element.setAttribute("name", element.getAttribute("name").toString().replace("Dark", "Light"));
				changeVals("colors",doc);
			}
//			System.out.println(colorDiff(primaryApp,values[i].substring(2)));
			else if(colorDiff(primaryApp,values[i].substring(2))<50) {
//				System.out.println("YGDFIUVNDFNJIDFBFKJDGBKJ0");
				element.setTextContent(primary);
				changeVals("colors",doc);
			}
			
			String black[] = {"author", "title", "text","Text","Author","Title","btn","BTN","Btn","subtitle","selected","Selected","disable","Disable","Inverse","inverse","primary","Primary","Secondary","secondary"
					,"tertiary","Tertiary","icon","Icon","status","Status", "separat", "Separat"};
			
			if(checkDarkGray(values[i])&&!hasAny(names[i],black)) {
				element.setTextContent("@android:color/black");
				changeVals("colors",doc);
			}
			
			if(checkLightGray(values[i])&&!hasAny(names[i],black)) {
				System.out.println("Here too bruh");
				element.setTextContent("@android:color/black");
				changeVals("colors",doc);
			}
			
			if(names[i].contains("grey")&&!names[i].contains("text")) {
				if(names[i].contains("600")||names[i].contains("650")||names[i].contains("700")||names[i].contains("750")||names[i].contains("800")||names[i].contains("850")||names[i].contains("900")||names[i].contains("950")) {
//					System.out.println(names[i]+" "+values[i]);
					if(values[i].substring(2,4).equals(values[i].substring(4,6))&&values[i].substring(6,8).equals(values[i].substring(4,6))) {
						element.setTextContent("@android:color/black");
					}
				}
			}
			
			if(names[i].contains("text")) {
				//System.out.println(names[i]);
				element.setTextContent("@android:color/white");
				changeVals("colors",doc);		

			}
			
			
			if(names[i].contains("background")||names[i].contains("Background")||names[i].contains("foreground")||
					names[i].contains("Foreground")||names[i].contains("fore")||names[i].contains("Fore")||names[i].contains("header")) {
				if(checkLightGray(values[i])||checkDarkGray(values[i])) {
					element.setTextContent("@android:color/black");
					changeVals("colors",doc);	
				}
			
		}
		
		

	}

	}

	private static boolean checkLightGray(String str) {
		// TODO Auto-generated method stub
		
		if(str.contains("color/white"))
			return true;
		else if(str.length()!=8)
			return false;
			
			str=str.substring(2);
		     int r = Integer.valueOf( str.substring( 0, 2 ), 16 );
		     int g = Integer.valueOf( str.substring( 2, 4 ), 16 );
		     int b = Integer.valueOf( str.substring( 4, 6 ), 16 );
		     if(r==g&&r==b&&r>200)
		    	 return true;
			
		return false;
	}

	private static double colorDiff(String primary, String str) {
		// TODO Auto-generated method stub
	if(str.length()==6) {
		int r1 = Integer.valueOf( str.substring( 0, 2 ), 16 );
		    int g1 = Integer.valueOf( str.substring( 2, 4 ), 16 );
		    int b1 = Integer.valueOf( str.substring( 4, 6 ), 16 );
		    int r2 = Integer.valueOf( primary.substring( 0, 2 ), 16 );
		    int g2 = Integer.valueOf( primary.substring( 2, 4 ), 16 );
		    int b2 = Integer.valueOf( primary.substring( 4, 6 ), 16 );
		    
		    double diff = Math.sqrt(Math.pow(r2-r1,2)+Math.pow(g2-g1,2)+Math.pow(b2-b1,2));
//		    System.out.println(diff);
		    return diff;

	}
		return 10000;
	}

	private static boolean checkDarkGray(String str) {
		// TODO Auto-generated method stub
		
		if(str.contains("color/black"))
			return true;
		else if(str.length()!=8)
			return false;
		
		str=str.substring(2);
	     int r = Integer.valueOf( str.substring( 0, 2 ), 16 );
	     int g = Integer.valueOf( str.substring( 2, 4 ), 16 );
	     int b = Integer.valueOf( str.substring( 4, 6 ), 16 );
	     if(r==g&&r==b&&r<100)
	    	 return true;
		
		return false;
	}

	private static void recompile() {
		// TODO Auto-generated method stub
		try {
			System.out.println("REcompiling APK");
            runProcess("java -jar apktool.jar b "+ appname);
            System.out.println("Successfully recompiled");
		}catch(Exception e) {
			System.out.println(e);
		}
		signAPK();
		System.out.println("Your app is ready with name "+appname+"-signed.apk");
	}

	private static void signAPK() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Signing APK");
			runProcess("java -jar Sign//signapk.jar Sign//certificate.pem Sign//key.pk8 "+appname+"//dist//"+appname+".apk"+" "+appname+"-signed.apk");
			System.out.println("Succesfully signed APK");
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	private static void changeVals(String dir, Document d) {
		// TODO Auto-generated method stub
		String directory = getDirectory(dir);
	
        		
        		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        		try {
        		Transformer transformer = transformerFactory.newTransformer();
        		DOMSource source = new DOMSource(d);
        		StreamResult result = new StreamResult(new File(directory));
        		
				transformer.transform(source, result);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//        		System.out.println("Values changed");
    }
        
	

	private static String getDirectory(String string) {
		// TODO Auto-generated method stub
		return System.getProperty("user.dir")+'\\'+appname+"\\res\\values\\"+string+".xml";
	}

	public static void main(String[] args) {
//		System.out.print(System.getProperty("user.dir"));
		
        decompileAPK();
    }

    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
            new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
      }

      private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
      }

}