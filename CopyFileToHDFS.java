import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.Date;

public class CopyFileToHDFS {
  public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
    //get configuration
    Configuration configuration = new Configuration();

    //open file for reading
    FileInputStream inputFileStream = new FileInputStream(args[0]);
    DataInputStream in = new DataInputStream(inputFileStream);
    BufferedReader input = new BufferedReader(new InputStreamReader(in));

    //open file for writing
    FileSystem hdfs = FileSystem.get( configuration);
    OutputStream outputStream = hdfs.create(new Path(args[1]));
    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

    //loop through file, read and write each line
    int NumReps = 1;
    String strLine;
    while ((strLine = input.readLine()) != null)  {
      output.write(strLine);
      output.newLine();
      //write timestamp and pause every 5000 lines
      if ((NumReps % 5000) == 0)  {
        output.write("SNAPTEST_TIMESTAMP: ");
        Date date = new Date();
        output.write(date.toString());
        output.newLine();
        Thread.sleep(1500);
      }
      NumReps++;
    }
    input.close();
    output.close();
  }
}

