package Fonte;

import java.util.HashMap;
import java.util.Map;
//import java.io.File;
//import java.util.Scanner;
//import java.util.List;
//import java.util.ArrayList;

public class Elementar {
    
    Map<String, Object> variaveis = new HashMap <String, Object>();

    //example.put("K1", new String("V1"));
    Object variavelA = new Object();
    variaveis.put("nomeDaVariavel", variavelA);
    variaveis.get("nomeDaVariavel").nome = "novoNomeDaVariavel"; // retorna variavelA
    public static HashMap< String, HashMap<Integer,Integer>> vetoresArmazenados = new HashMap<String, HashMap<Integer,Integer>>();
}
