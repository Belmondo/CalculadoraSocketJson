package server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

import calc.Calculadora;




public class CalculadoraServerSocket {
	static ServerSocket serverSocket;
	static Calculadora calc;

	public static void main(String[] args) throws IOException {
		
		
		serverSocket = new ServerSocket(8080);
		calc = new Calculadora();
		

		try {
			while(true) {
				Socket socket = serverSocket.accept();
				startHandler(socket);
			}
		} finally {
			serverSocket.close();
		}
		
	}

	private static void startHandler (final Socket socket) throws IOException{
		
		
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
					
					
					String line = reader.readLine();
					JSONObject jsonObject = new JSONObject(line);
					
					
					
					String resultado = calcula(jsonObject);	
					
					JSONObject jsonObjectReturn = new JSONObject();
					jsonObjectReturn.put("resultado", resultado);
					
					//System.out.println(jsonObject.get("operacao"));
					writer.write(jsonObjectReturn.toString() + "\n");
					
					writer.flush();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					closeSocket();
				}
				
				
			}

			private void closeSocket() {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		
		
		
		thread.start();
		
		
		
		
	}

	protected static String calcula(JSONObject jsonObject) {
		
		 //Chamando a calculadora
        String result="";
        
        String operacao = jsonObject.getString("operacao");
        String oper1 = jsonObject.getString("operador1");
        String oper2 = jsonObject.getString("operador2");
        
        
        //avaliação da operação recebida e direcionamento para cada caso correspondente
        switch(Integer.parseInt(operacao)){
        case 1:
     	   result = ""+ calc.soma(Double.parseDouble(oper1),Double.parseDouble(oper2));
            break;
        case 2:
     	   result = ""+ calc.subtracao(Double.parseDouble(oper1),Double.parseDouble(oper2));
            break;
        case 3:
     	   result = ""+ calc.multiplicacao(Double.parseDouble(oper1),Double.parseDouble(oper2));
            break;
        case 4:
     	   result = ""+ calc.divisao(Double.parseDouble(oper1),Double.parseDouble(oper2));
            break;
            
        default:
            result = "Digite SOMENTE números entre 1 e 4";
            break;
		
        }
        return result; 
      }
}
