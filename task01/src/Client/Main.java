package Client;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws UnknownHostException, IOException
    {
        //variables
        String msgRecv = "";
        Float fMean;
        Float fStddev;

        String serverName = args[0];
        Integer port = Integer.parseInt(args[1]);

        //opening a socket to connect to server on port xxxx
        Socket socket = new Socket(serverName,port);
        
        try(OutputStream os = socket.getOutputStream()){
            //read using objectOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            try (InputStream is = socket.getInputStream()){
                ObjectInputStream ois = new ObjectInputStream(is);
                List<String> numList = new ArrayList<String>();

                //to read msg using ObjectInputStream
                msgRecv = ois.readUTF();
                
                //System.out.println("From server: " + msgRecv);
                //Message comes in as 456.00, 654.054
                numList = Arrays.asList(msgRecv.split(","));
                //call separate functions to calc mean and std dev
                fMean = calcMean(numList);
                fStddev = calcStdDev(numList, fMean);

                //write back to server
                oos.writeUTF("Seow Sze Howe, Stanley");
                oos.writeUTF("seowszehowestanley@gmail.com");
                oos.writeFloat(fMean);
                oos.writeFloat(fStddev);
                oos.flush();
                
                //close after finish
                ois.close();
                is.close();
                os.close();
                socket.close();


            } catch (EOFException e){
                e.printStackTrace();
                socket.close();
            }
        } catch (EOFException ex){
            ex.printStackTrace();
            socket.close();
        }
    }

    //calculate mean function taking in list of string of numbers
    public static float calcMean(List<String> numList){
        float sum = 0.0f;
        float mean = 0.0f;

        //sum up double
        for(int i = 0; i<numList.size(); i++){
            sum += Double.parseDouble(numList.get(i).trim());
        }

        //calc mean of number lists
        mean = sum/numList.size();

        return mean;
    }

    //calculate stddev function taking in list of string of numbers and average
    public static float calcStdDev(List<String> numList, float avg){
        //variables
        double num;
        //stddev sum
        double dstddev = 0.0;
        double davg = (double)avg;

        for(int i = 0; i<numList.size(); i++){
            num = Double.parseDouble(numList.get(i).trim());
            dstddev = dstddev + Math.pow(num-davg, 2);
        }
        double stdev = Math.sqrt(dstddev/(numList.size()));

        return (float)stdev;
    }
}