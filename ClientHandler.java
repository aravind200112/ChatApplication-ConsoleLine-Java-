import java.util.*;
import java.io.*;
import java.net.*;  

public class ClientHandler implements Runnable{

	public static ArrayList<ClientHandler> clienthandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedreader;
	private BufferedWriter bufferedwriter;
	private String clientUsername;
	
	public ClientHandler(Socket socket) {
		
		try {
			this.socket = socket;
			this.bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedreader.readLine();
			clienthandlers.add(this);
			broadcastMessage("SERVER: "+clientUsername+" has entered the Chat");
		
		}catch (IOException e)
		{
			closeEverything(socket, bufferedreader, bufferedwriter);
		}

	}
	
	
	
	@Override
	public void run()
	{
	   String msgFromClient;
	   
	   while(socket.isConnected())
	   {
		   try {
			   
			   msgFromClient = bufferedreader.readLine();
			   broadcastMessage(msgFromClient);
			   
		   }catch (IOException e) {
			   closeEverything(socket, bufferedreader, bufferedwriter);
			   break;
		   }
		   
	   }
	}
	
	public void broadcastMessage(String msgtoSend)
	{
		for(ClientHandler clienthandler: clienthandlers)
		{
			try {
			if(!clienthandler.clientUsername.equals(clientUsername))
			{
				clienthandler.bufferedwriter.write(msgtoSend);
				clienthandler.bufferedwriter.newLine();
				clienthandler.bufferedwriter.flush();
			}
			}catch (IOException e)
			{
				closeEverything(socket, bufferedreader, bufferedwriter);
			}
		}
	}
	
	public void removeClientHandler()
	{
		clienthandlers.remove(this);
		broadcastMessage("SERVER: "+clientUsername+" has left the chat");
		
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedreader, BufferedWriter bufferedwriter)
	{
		removeClientHandler();
		
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
	
}
