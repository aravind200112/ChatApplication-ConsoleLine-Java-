import java.util.*;
import java.io.*;
import java.net.*;  

public class Client {

	private Socket socket;
	private BufferedWriter bufferedwriter;
	private BufferedReader bufferedreader;
	private String username;
	
	public Client(Socket socket, String username)
	{
		try {
			
			this.socket = socket;
			this.bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		
		}catch (IOException e)
		{
			closeEverything(socket, bufferedreader, bufferedwriter);
		}
	}
	
	public void sendMessage() {
		
		try {
			bufferedwriter.write(username);
			bufferedwriter.newLine();
			bufferedwriter.flush();
			
			Scanner scan = new Scanner(System.in);
			while(socket.isConnected())
			{
				String msgtosend = scan.nextLine();
				bufferedwriter.write(username+" : "+msgtosend);
				bufferedwriter.newLine();
				bufferedwriter.flush();
			}
			
		} catch(IOException e)
		{
			closeEverything(socket, bufferedreader, bufferedwriter);
			 
		}
	}
	
	public void listenforMsg()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				String msgfromGrpchat;
				
				while(socket.isConnected())
				{
					try {
						
						msgfromGrpchat = bufferedreader.readLine();
						System.out.println(msgfromGrpchat);
					} catch(IOException e)
					{
						closeEverything(socket, bufferedreader, bufferedwriter);
					}
				}
				
			}
			
			
		}).start();
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedreader, BufferedWriter bufferedwriter)
	{
		
		
		try {
			if(bufferedreader != null)
			{
				bufferedreader.close();
			}
			if(bufferedwriter != null)
			{
				bufferedwriter.close();
			}
			if(socket != null)
			{
				socket.close();
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the username for the chat");
		String username = scan.nextLine();
		Socket socket = new Socket("localhost",6666);
		Client client = new Client(socket, username);
		client.listenforMsg();
		client.sendMessage();
		
	}
}
