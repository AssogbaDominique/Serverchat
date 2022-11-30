/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package serverchat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Godwin
 */
public class ServerChat extends Thread{
    int nbClient = 0;
    public static void main(String[] args) throws Exception{
          new ServerChat().start();  
    }
    @Override
    public void run(){
        System.out.println("Demarrage de serveur ...");
        try {
            ServerSocket ss = new ServerSocket(1235);
            while(true){
                Socket socket=ss.accept();
                nbClient++;
                Conversation conversation=new Conversation(socket,nbClient);
                conversation.start();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    private class Conversation extends Thread{
        
        private Socket socket;
        
        private int numClient;
        
        public Conversation(Socket socket,int numClient){
            super();
            this.socket = socket;
            this.numClient = numClient;
        }

        
        @Override
        public void run(){
           
            try {
                InputStream is = socket.getInputStream(); // pour les
                InputStreamReader isr=new InputStreamReader(is);// pour 
                BufferedReader br=new BufferedReader(isr);// pour les 
                
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                
                
                System.out.println("Connexion du client numero" + numClient);
                String IP=socket.getRemoteSocketAddress().toString();
                pw.println("Connexion du client numero" + numClient + "pour adresse IP" +IP);
                
                while(true){
                  String req = br.readLine();
                  if(req!=null){
                      pw.println("Le client numero" + numClient + " a envoye" + req);
                     int longChaine=req.length();
                     pw.println("Longueur : " + longChaine);
                  }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    
}
