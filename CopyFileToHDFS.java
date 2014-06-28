import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.sql.Timestamp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.IOUtils;

public class CopyFileToHDFS {
  public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException 
   {
      //get configuration
      Configuration configuration = new Configuration();
      
      //open file for reading
      FileInputStream fstream = new FileInputStream(args[0]);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      
      //open file for writing
      FileSystem hdfs = FileSystem.get( configuration);
      OutputStream outputStream = hdfs.create(new Path(args[1]));
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
      
      //loop through file, read and write each line
      int NumReps = 1;
      String strLine;
      while ((strLine = br.readLine()) != null)  {
      bw.write(strLine);
      bw.write("\n");
      //write timestamp and pause every 5000 lines
      if ((NumReps % 5000) == 0)  {
      bw.write("SNAPTEST_TIMESTAMP: ");
      Date date = new Date();
      bw.write(date.toString());
      bw.write("\n");
      Thread.sleep(1500);
      }
      NumReps++;
}
br.close();
bw.close();

 }
}

