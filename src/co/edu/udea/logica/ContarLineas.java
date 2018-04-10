package co.edu.udea.logica;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContarLineas {

	public static void main(String[] args){
		System.out.println("Ingrese la ruta del archivo con los programas");
		Scanner entrada = new Scanner(System.in);
		String path = entrada.nextLine();
		entrada.close();
		generarInforme(path);		
		
	}	
	
	public static void generarInforme(String path){
			try {
				FileReader fr = new FileReader(path);
				BufferedReader bf = new BufferedReader(fr);
				List<Programa> listaProgramas = listarProgramas(bf);
				for(int i=0; i<listaProgramas.size(); i++){
					System.out.println("***PROGRAMA "+(i+1)+"***");					
					Programa programa = listaProgramas.get(i);
					System.out.println("Numero de partes: "+programa.getPartes().size());
					for(int j=0; j<programa.getPartes().size(); j++){
						Parte parte = programa.getPartes().get(j);
						System.out.println("    Parte "+(j+1));
						System.out.println("    Nombre: "+parte.getNombre());
						System.out.println("    Cantidad de ítems: "+parte.getItem().size());
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	public static List<Programa> listarProgramas(BufferedReader bf){
		List<Programa> listaProgramas = new ArrayList<Programa>();
		String linea;
		long nLineas = 0;
		long nProgramas = 0;
		long nPartes = 0;
		long nItems = 0;
		try {
			while ((linea = bf.readLine())!=null) {
				linea = linea.trim();
				if(linea==null){
					linea="";
				}
				/*Cuenta las lineas totales según una validación realizada 
				en base al estándar de conteo*/ 
				if(validar(linea)){
					nLineas++;
				}
				//Cuenta la cantidad de programas y sus elementos y construye los objetos para el programa
				if("/*****/".equals(linea.trim())){
					nProgramas++;
					Programa programa = new Programa();
					List<Parte> listaPartes = new ArrayList<Parte>();
					while ((linea = bf.readLine())!=null) {
						linea = linea.trim();
						if(linea==null){
							linea="";
						}
						Pattern patternClass = Pattern.compile("(public|private|protected) class ([a-zA-Z0-9{ ]{0,100})");
					    Matcher matchClass = patternClass.matcher(linea);
					    //Cuenta la cantidad de partes y sus elementos y construye los objetos para la parte o clase
					    if(matchClass.matches()) {
					    	nPartes++;
					    	String nombreParte = linea.substring(linea.lastIndexOf("class")+6, linea.length()-2).trim();
					    	Parte parte = new Parte();
					    	parte.setNombre(nombreParte);
					    	List<Item> listaItems = new ArrayList<Item>();
					    	
					    	while ((linea = bf.readLine())!=null) {
					    		linea = linea.trim();
								if(linea==null){
									linea="";
								}
					    		Pattern patternItem = Pattern.compile("(public|static|private|protected) ([a-zA-Z0-9\\s<>()"+Pattern.quote("[]")+"]{1,1000})[{]");
							    Matcher matchItem = patternItem.matcher(linea);
							    //Cuenta la cantidad de items o métodos y sus elementos y construye los objetos para el item o método
							    if(matchItem.matches()) {
							    	
							    	nItems++;
							    	Item item = new Item();
							    	item.setNombre(linea);
							    	listaItems.add(item);
							    }else if (patternClass.matcher(linea).matches()){//Si lo que sigue no es un item sino otra clase
							    	System.out.println("Encontró otra clase");
							    	//Se pone el reader en la linea anterior para que sea leida de nuevo esta linea
							    	bf.mark(100000);
							    	bf.reset();
							    	break;
							    }	
							    
					    	}
					    	parte.setItem(listaItems);
					    	listaPartes.add(parte);
					    }else if("/*****/".equals(linea)){//Si lo que sigue no es una clase sino otro programa
					    	//Se pone el reader en la linea anterior para que sea leida de nuevo esta linea
					    	bf.mark(100000);
					    	bf.reset();
					    	break;
					    }
					}
					programa.setPartes(listaPartes);
					listaProgramas.add(programa);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return listaProgramas;
	}
	
	public static boolean validar(String linea){
		if(
				//comentario de una linea
				linea.startsWith("//")||
				//importaciones de librerias o clases
				linea.startsWith("import")||
				//lineas vacías o con solo espacios
				"".equals(linea)){
			return false;
		}else{
			return true;
		}
	}

}