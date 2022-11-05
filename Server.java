import java.util.*;
import java.io.*;
import java.net.*;  

public class Server {
	
	private ServerSocket serversocket;
	

	public Server(ServerSocket serversocket) {
		this.serversocket = serversocket;
	}
	
	public void startServer()
	{
		
		try
		{
			while(!serversocket.isClosed())
			{
				Socket socket = serversocket.accept();
				System.out.println("A new client has been connected");
				ClientHandler clienthandler = new ClientHandler(socket);
				
				Thread thread = new Thread(clienthandler);
			    thread.start();
			}
		}catch (Exception e)
		{
			
		}
	}
	
	public void closeServerSocket() {
		
		try {
			if(serversocket != null){
				serversocket.close();
			}
			
			}catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws IOException {
		
		ServerSocket serversocket = new ServerSocket(6666);
		Server server = new Server(serversocket);
		server.startServer();

	}

}
