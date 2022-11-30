/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package serverchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Godwin
 */
public class BroadCast extends Thread{
    ArrayList<Conversation> clients = new ArrayList();
    
    int nbClient = 0;
    
    public static void main(String[] args) throws Exception{
          new BroadCast().start();  
    }
    @Override
    public void run(){
        System.out.println("Demarrage de serveur ...");
        
        try {
            ServerSocket ss = new ServerSocket(8080);
            
            while(true){
                Socket socket=ss.accept();
                nbClient++;
                Conversation conversation=new Conversation(socket,nbClient);
                clients.add(conversation);
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
        /*private void broadCastMessage(String message, Socket socket, int numClient){
          System.out.println();
          for (int i = 0; i < clients.size(); i++){
              try {
                  OutputStream ss = socket.getOutputStream();
              } catch (IOException ex) {
                  Logger.getLogger(BroadCast.class.getName()).log(Level.SEVERE, null, ex);
              }
           
          }
            
        }*/

        
        @Override
        public void run(){
           
            try {
                InputStream is = socket.getInputStream(); // pour les
                InputStreamReader isr=new InputStreamReader(is);// pour 
                BufferedReader br=new BufferedReader(isr);// pour les 
                
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true); // print writer pour envoyer le nessage au client
                
                
                System.out.println("Connexion du client numero" + numClient);
                String IP=socket.getRemoteSocketAddress().toString();
                pw.println("Connexion du client numero" + numClient + "pour adresse IP" +IP);
                
                while(true){
                  String req = br.readLine();
                  if(req!=null) {
                      pw.println("Le client numero" + numClient + " a envoye" + req);
                      int longChaine = req.length();
                      pw.println("Longueur : " + longChaine);
                    //  String message = null;
                      if (req.contains("=>")) {
                          String[] reqpar = req.split("=>");
                         // message = null;
                          if (reqpar.length == 2) ;
                          String messag = reqpar[1];
                          int numclient = Integer.parseInt(reqpar[0]);
                          broadCastMsge(messag, socket, numclient);

                      } else {
                          broadCastMsge(req, socket, -1);
                      //    broadCastMsge(message, socket, -1);
                      }
                  }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        private void broadCastMsge(String msge, Socket socket,int numClient) throws IOException {
            
            for (Conversation client : clients){
                /*
                OutputStream os=client.getOutputStream();
                PrintWriter pw=new PrintWriter(os, true);
                pw.println("Client num" +numClient+ ":" +msge);*/
                if(client.socket !=socket){
                    if (client.numClient==nbClient || nbClient==-1){
                    PrintWriter printWriter = new PrintWriter(client.socket.getOutputStream(),true);
                    printWriter.println(msge);
                    }
                }

            }
        }
    }
    
}
