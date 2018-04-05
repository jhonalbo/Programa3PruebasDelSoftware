package co.edu.udea.logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ContarLineas {

	public static void main(String[] args) {
		try{
			FileReader fr = new FileReader("fichero.txt");
			BufferedReader bf = new BufferedReader(fr);
			long nLineas = 0;		 
			String linea;
			while ((linea = bf.readLine())!=null) {
				if(validar(linea)){
					nLineas++;
				}				
			}
			System.out.println(nLineas);
			bf.close();
		} catch (FileNotFoundException fnfe){
			fnfe.printStackTrace();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
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