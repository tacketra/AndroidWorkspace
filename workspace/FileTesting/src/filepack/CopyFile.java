package filepack;
import java.io.*;
import java.net.URL;



public class CopyFile {
   public static void main(String args[]) throws IOException
   {
      FileInputStream in = null;
      FileOutputStream out = null;
      //File file = new File(".");
      //String s = "C:" + " \"ROM\" ";
      String s = "C:\\Users\\rtack_000\\workspace\\FileTesting\\src\\filepack";
      // /C:/Users/rtack_000/workspace/FileTesting/bin/input.txt
      //getAbsolutePath();
      //File file = new File(s);
      URL url = CopyFile.class.getClassLoader().getResource("filepack"); 
      //System.out.println(url.getPath());  
      File file = new File(url.getPath());
     // File file = new File(s);
      for(String fileNames : file.list()) {
    	  //System.out.println(fileNames);
    	  if (fileNames.endsWith(".txt")){
    		  System.out.println("its a java filename and it = " + fileNames);
	      
		      try {
		         in = new FileInputStream(url.getPath() + "\\" + fileNames);
		         //in = new FileInputStream(s+ "\\" + fileNames);
		         System.out.println(url.getPath() + "\\" + fileNames + "/");
		         //String ab = "C:\\Users\\rtack_000\\Documents\\t-25 ab interval moves by second breakdown.txt";
		    	 //or you can use ./filename.txt if it is in binary of the package
		    	 //You can use YourClassName.class.getResourceAsStream("Filename.txt"), but your text file
		    	 //has to be in the same directory/package as your YourClassName file.
		    	 //in = (FileInputStream) CopyFile.class.getResourceAsStream(fileNames);
		    	 //System.out.println(in);
		    	  //System.out.println(System.getProperty("user.dir"));
		    	  //System.out.println(System.getProperty("java.class.path"));
		    	  
		         //in = new FileInputStream("./input.txt");
		         //in = new FileInputStream("C:\Users\rtack_000\workspace\FileTesting\src\filepack\input.txt");
		         out = new FileOutputStream("temporary.txt");
		         
		         /*
		         Reader rd = new InputStreamReader(in, "USASCII");  // or whatever encoding you use
		         while (true) {
		             String command = readLine(rd );
		             if (command .equals("RAW")) {
		                 int length = Integer.parseInt(readLine(rd ));
		                 byte[] data = readData(in , length);
		                 
		             }
		         }
		         */
		         String line = "";
		         String character = "";
		         int line_number = 1;
		         String str = "";
		         int c;
		         while ((c = in.read()) != -1) {
		        	//System.out.println(new String(new byte[]{ (byte)0x63 }, "US-ASCII"));
		        	//System.out.println(new String(new byte[]{ (byte)c }, "US-ASCII"));
		        	character = new String(new byte[]{ (byte)c }, "US-ASCII");
		        	if (c == 13){ // new line byte = 13
		        		//System.out.print( " this line is done");
		        		//line = "";
		        		line_number+=1;
		        		c = 98;
		        	}
		        	//line+=character;
		        	System.out.print(character);
		            out.write(c);
		            str+= character;
		         }
		         System.out.println(str);
		         String hey = "hey dog";
		         System.out.println("before changin the hey, it = " + hey);
		         hey.replace("hey", "the big hey yo");
		         System.out.println("after trying to replace with big hey" + hey);
		         hey = hey.replace("hey", " what is up with it?");
		         System.out.println("replacing hey, third time, hey = " + hey);
		         
		         /*
		         int alarm_number = 1; //the number of alarms
		         if(line_number >= alarm_number){
		        	 line_number = 0;// we have to write 0 new lines in the output file
		         }
		         else{
		        	 line_number = alarm_number - line_number; // how many new lines we have to do in output file
			         while ((c = in.read()) != -1) {
				        	character = new String(new byte[]{ (byte)c }, "US-ASCII");
				        	if (c == 13){ // new line byte = 13
				        		line_number+=1;
				        	}
				            out.write(c);
				            str+= c;
				     }
		        	 for (int i = 0; i < line_number; i++){
		        	 	out.write(13);
		        	 }
		         }
		         */
		      }finally {
		         if (in != null) {
		            in.close();
		         }
		         if (out != null) {
		            out.close();
		         }
		      }
    	  }  //end of if
      }//end of for
   }
}