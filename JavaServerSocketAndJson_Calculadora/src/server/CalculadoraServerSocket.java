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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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
				inicia(socket);
			}
		} finally {
			serverSocket.close();
		}
		
	}

	private static void inicia (final Socket socket) throws IOException{
		
		
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
					
					
					String line = reader.readLine();
					JSONObject jsonObject = new JSONObject(line);
					System.out.println(jsonObject.toString());
					
					
					
					String resultado = calcula(jsonObject);	
					System.out.println(resultado);
					
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
		
		List<String> listaElementosJson = new ArrayList();
		List<String> listaElementosJson2 = new ArrayList();
		List<String> chavesElementosJson = new ArrayList();
		
		Map<String, Object> mapaNomes = new HashMap<String, Object>();
		
		mapaNomes = jsonObject.toMap();
		
		String teste = mapaNomes.values().toString();
		
		String teste2 = teste.substring(1, teste.indexOf("]"));
		
		String str[] = teste2.split(",");
		
		listaElementosJson = Arrays.asList(str);
		
		//calculadora(listaElementosJson);
		
		for(String s: listaElementosJson){
            listaElementosJson2.add(s.trim());
        }
		
		System.out.println(listaElementosJson2.toString());
		
		return calculadora(listaElementosJson2);
	}

	
	public static String calculadora(List listaExpressao) {
		//Professor, não fiz a árvore de priorização dos elementos
		
		String expressao = "";
		
		for(int i=0; i<= listaExpressao.size()-1;i++) {
			System.out.println(listaExpressao.get(i));
			expressao += listaExpressao.get(i);
		}
		
        String resultado;
        List<Double> listaNumeros = new ArrayList<Double>();
        List<Character> listaOperadores = new ArrayList<Character>();
 
        System.out.println("Dentro calculadora:" + expressao);
        listaNumeros = obterNumeros(expressao);
        listaOperadores = obterOperadores(expressao);
 
        resultado = calcularValor(listaNumeros, listaOperadores);
 
        return resultado;
 
    }
	
	private static String calcularValor(List<Double> listaNumeros, List<Character> listaOperadores) {
		        String resultado = "";
		        double total = 0.0;
		        int j=0;
		        for (int i = 0; i < listaNumeros.size()-1; i++) {
		 
		            if (total==0.0) {
		                double numero1 = listaNumeros.get(i).doubleValue();
		                double numero2 = listaNumeros.get(i + 1).doubleValue();
		                char operador = listaOperadores.get(i).charValue();
		                total = executarOperacao(numero1, operador, numero2);
		            }
		            else if (total>0.0) {
		                 
		                double numero2 = listaNumeros.get(i).doubleValue();
		                char operador = listaOperadores.get(j).charValue();
		                total = executarOperacao(total, operador, numero2);
		                j++;
		            }

		        }
		        
		     resultado = Double.toString(total);
		     return resultado;
	}
	
	private static double executarOperacao(double numero1, char operador, double numero2) {
        double resultado = 0.0;
 
        if (operador == '+') {
            resultado = numero1 + numero2;
        } else if (operador == '-') {
            resultado = numero1 - numero2;
        } else if (operador == '/') {
            resultado = numero1 / numero2;
        } else if (operador == '*') {
            resultado = numero1 * numero2;
        }
        return resultado;
    }
	
	
	  private static List<Double> obterNumeros(String expressao) {
		  
	        List<Double> listaNumeros = new ArrayList<Double>();
	        
	        System.out.println("Dentro obter números: "+ expressao);
	 
	        String numeroEmString = "";
	        for (int i = 0; i < expressao.length(); i++) {
	 
	            if (!isOperador(expressao.charAt(i))) {
	            	System.out.println("Expressão charat:"+ expressao.charAt(i));
	                //Double numero = Double.parseDouble(expressao.charAt(i));
	            	double numero = Double.parseDouble(""+ expressao.charAt(i)+"");
	                System.out.println(numero);
	                listaNumeros.add(numero);
	                numeroEmString = "";
	            } else {
	                numeroEmString = numeroEmString.concat("" + expressao.charAt(i));
	            }
	        }
	        if(!numeroEmString.isEmpty())
	        {
	                Double numero = Double.valueOf(numeroEmString);
	                listaNumeros.add(numero);
	             
	        }
	        System.out.println(listaNumeros.toString());
	        return listaNumeros;
	    }
	
	private static List<Character> obterOperadores(String expressao) {
		 
        List<Character> listaOperadores = new ArrayList<Character>();
 
 
        for (int i = 0; i < expressao.length(); i++) {
 
            if (isOperador(expressao.charAt(i))) {
                listaOperadores.add(new Character(expressao.charAt(i)));
            }
        }
 
        return listaOperadores;
    }
	
	private static boolean isOperador(char caracter) {
        boolean isOperador = false;
        if (caracter == '-' || caracter == '+' || caracter == '/' || caracter == '*') {
            isOperador = true;
        }
        return isOperador;
    }
	
	
	
}
