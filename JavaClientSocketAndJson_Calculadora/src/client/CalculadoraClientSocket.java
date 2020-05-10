package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Socket;

import org.json.JSONObject;

public class CalculadoraClientSocket {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Socket clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), 8080);
		System.out.println("Conectado ao Servidor");
		OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("operacao", "1");
		jsonObject.put("operador1", "3");
		jsonObject.put("operador2", "5");
		
		writer.write(jsonObject.toString() + "\n");
		writer.flush();
		
		String line = reader.readLine();
		jsonObject = new JSONObject(line);
		
		System.out.println(jsonObject.get("resultado").toString());
		
		
		clientSocket.close();
		
	

	}

}
