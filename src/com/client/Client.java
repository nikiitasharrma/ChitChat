package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	public Client() {
		
		try {
			
			System.out.println("Sending request to server");
			socket = new Socket("000.000.00.000",7777); //takes two parameters- IP address, port number
			System.out.println("connection done.");
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
			
		} catch (IOException e) {
			
			System.out.println("Connection closed!");
		}
	}
	
public void startReading() {
		
		Runnable r1 = () -> {
			System.out.println("Reader started...");
			
			try {
			    while(!socket.isClosed()) {
					String msg = br.readLine();
					
					if(msg.equals("exit")) {
						System.out.println("Server terminated the chat");
						socket.close();
						break;
					}
					
					System.out.println("Server: " + msg);
				} 
			}catch (Exception e) {
				
				System.out.println("Connection closed!");
				
			}
		};
		
		new Thread(r1).start();
	}
	
    public void startWriting() {
		
        Runnable r2 = () -> {
			System.out.println("Writer started...");
    		
    		try {
        	    while(!socket.isClosed()) {
        			
        			BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            		String content = br1.readLine();
            		out.println(content);
            		out.flush();
            		
            		if(content.equals("exit")) {
            			br1.close();
            			socket.close();
            		}
            		
        		}
        		
        	}catch (IOException e) {
    			
        		System.out.println("Connection closed!");
    			
    		}
		};
		
		new Thread(r2).start();
	}
	
	public static void main(String[] args) {
		
		System.out.println("This is client");
		new Client();
		
	}
}
