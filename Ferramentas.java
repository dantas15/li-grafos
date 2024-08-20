import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Ferramentas {
    Grafo grafo;
    Boolean ciclo;
    public Ferramentas(Grafo grafo){
        this.grafo = grafo;
        ciclo = false;
    }
    public boolean isConexo(){
        List<String> visitadasA = new ArrayList<>();
        visitadasA = grafo.BFS("0");
        List<String> verticesV = new ArrayList<>();
        String[] verticesT = grafo.Vertices().split(" ");
        for(int i=0;i < visitadasA.size();i++){
            verticesV.add(grafo.getGrafo().get(visitadasA.get(i)).get(0).getOrigin()); 
            verticesV.add(grafo.getGrafo().get(visitadasA.get(i)).get(0).getDestiny());
        }
        for(String a : verticesT){
            if(!verticesV.contains(a)){
                return false;
            }
        }
        return true;
    }
    public boolean isBipartido(){
        Map<String, Integer> cores = new HashMap<>();
        Queue<String> fila = new LinkedList<>();
        String[] vertices = grafo.Vertices().split(" ");
        for(String vertice : vertices){
            cores.put(vertice, -1);
        }
        fila.add("0");
        cores.put("0", 0);
        while(!fila.isEmpty()){
            String atual = fila.poll();
            for(Pair<String, Integer, String> par : grafo.getGrafo().get(atual)){
                if(cores.get(par.getDestiny()) == -1){
                    cores.put(par.getDestiny(), 1 - cores.get(atual));
                    fila.add(par.getDestiny());
                } else if(cores.get(par.getDestiny()) == cores.get(atual)){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isEuleriano(){
        if (!isConexo()) {
            return false;
        }
        int oddDegreeCount = 0;
        String[] valores = grafo.Vertices().split(" ");
        for (String vertice : valores) {
            if (grafo.getGrau(vertice) % 2 != 0) {
                System.out.println(vertice + " Possui grau: " + grafo.getGrau(vertice));
                oddDegreeCount++;
                return false;
            }
        }
        
        if (oddDegreeCount > 2) {
            return false;
        }
        ciclo = true;
        return true;
    }
    public boolean possuiCiclo(){
        if(ciclo){
            return true;
        }
        Set<String> visitados = new HashSet<>();
        Stack<String> pilha = new Stack<>();
        pilha.push("0");
        while(!pilha.isEmpty()){
            String atual = pilha.pop();
            if(!visitados.contains(atual)){
                visitados.add(atual);
                for(Pair<String, Integer, String> par : grafo.getGrafo().get(atual)){
                    pilha.push(par.getDestiny());
                }
            } else {
                return true;
            }
        }
        return false;
    }
    public void componentesConexas(){
        Set<String> visitados = new HashSet<>();
        Stack<String> pilha = new Stack<>();
        pilha.push("0");
        while(!pilha.isEmpty()){
            String atual = pilha.pop();
            if(!visitados.contains(atual)){
                visitados.add(atual);
                for(Pair<String, Integer, String> par : grafo.getGrafo().get(atual)){
                    pilha.push(par.getDestiny());
                }
            }
        }
        List<String> sortedList = new ArrayList<>(visitados);
        Collections.sort(sortedList);
        System.out.println(sortedList);
    }
    public void componentesFortementeConexas(){
        HashMap<String, ArrayList<Pair<String, Integer, String>>> grafoT = new HashMap<>();
        for(Map.Entry<String, ArrayList<Pair<String, Integer, String>>> entry : grafo.getGrafo().entrySet()){
            if(entry.getValue() != null){
                for(Pair<String, Integer, String> par : entry.getValue()){
                    if(grafoT.containsKey(par.getDestiny())){
                        grafoT.get(par.getDestiny()).add(new Pair<>(par.getDestiny(), par.getValue(), par.getOrigin()));
                    } else {
                        ArrayList<Pair<String, Integer, String>> lista = new ArrayList<>();
                        lista.add(new Pair<>(par.getDestiny(), par.getValue(), par.getOrigin()));
                        grafoT.put(par.getDestiny(), lista);
                    }
                }
            }
        }
        Set<String> visitados = new HashSet<>();
        Stack<String> pilha = new Stack<>();
        pilha.push("0");
        while(!pilha.isEmpty()){
            String atual = pilha.pop();
            if(!visitados.contains(atual)){
                visitados.add(atual);
                for(Pair<String, Integer, String> par : grafoT.get(atual)){
                    pilha.push(par.getDestiny());
                }
            }
        }
        List<String> sortedList = new ArrayList<>(visitados);
        Collections.sort(sortedList);
        if(grafo.isDirecionado() == false){
            System.out.println("Grafo não possui componentes fortemente conexas");
        } else {
            System.out.println(sortedList);
        }
        
    }
    public List<String> trilhaEuleriana(String initial){
        Stack<String> pilha = new Stack<>();
        Set<String> verticesVisitados = new HashSet<>();
        Set<String> arestasVisitadas = new HashSet<>();
        List<String> visitados = new ArrayList<>();


        pilha.add(initial);
        verticesVisitados.add(grafo.getGrafo().get(initial).get(0).getDestiny());
        verticesVisitados.add(grafo.getGrafo().get(initial).get(0).getOrigin());
        arestasVisitadas.add("0");
        while(!pilha.isEmpty()){
            String aresta = pilha.pop();

            if (grafo.getGrafo().get(aresta) != null) {
                for (Map.Entry<String, ArrayList<Pair<String, Integer, String>>> entry : grafo.getGrafo().entrySet()){
                    if(entry.getValue() != null){
                        for (Pair<String, Integer, String> par : entry.getValue()) {
                            if(par.getOrigin().equals(grafo.getGrafo().get(aresta).get(0).getDestiny())){
                                pilha.add(entry.getKey());
                                arestasVisitadas.add(entry.getKey());
                                verticesVisitados.add(par.getOrigin());
                                verticesVisitados.add(par.getDestiny());
                                if(!visitados.contains(par.getOrigin())){
                                    visitados.add(entry.getKey());
                                }
                                if(!visitados.contains(par.getDestiny())){
                                    visitados.add(par.getDestiny());
                                }
                                aresta = pilha.pop();
                            }
                        }
                    }
                }
                for(Map.Entry<String, ArrayList<Pair<String, Integer, String>>> entry : grafo.getGrafo().entrySet()){
                    if(visitados.get(visitados.size()-1).equals(entry.getValue().get(0).getDestiny())){
                        visitados.add(entry.getValue().get(0).getOrigin());
                    }
                }
            }
        }
        return visitados;
    }
    public void verticesDeArticulacao(){
        String[] vertices = grafo.Vertices().split(" ");
        List<String> articulados = new ArrayList<>();
        String[] consulta = new String[vertices.length*2];
        for(int i=0;i<vertices.length;i++){ 
            consulta = trilhaEuleriana(vertices[i]).toArray(new String[0]);
            if(consulta[0].equals(consulta[consulta.length-1])){
                articulados.add(vertices[i]);
            }
        }
        System.out.println(articulados);
    }
    public void arestasDePonte(){
        
    }
}
