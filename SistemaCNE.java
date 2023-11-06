package aed;
import aed.PriorityQueueAcotada;
import aed.SistemaCNE;

public class SistemaCNE {
    // Completar atributos privados

    String[] partidos;
    String[] distritos;
    int[] votosPresidenciales;
    int[][] votosDiputados;
    int[] ultimaMesaPorDistrito;
    int primero; // id del primero (presidencial)
    int segundo; // id del segundo (presidencial)
    int votosTotalesPresidenciales;
    PriorityQueueAcotada<Nodo>[] heapDiputados;
    int[] diputadosPorDistrito;  // Me dice la cantidad de diputados en disputa en cada distrito
    int[][] memoBancasPorDistrito;
    boolean[] memoEsValido;  // Me dice si en el distrito que estoy considerando el resultado de la memo es valido

    public class VotosPartido{
        private int presidente;
        private int diputados;
        VotosPartido(int presidente, int diputados){
            this.presidente = presidente;
            this.diputados = diputados;
        }
        public int votosPresidente(){
            return presidente;
        }
        public int votosDiputados(){
            return diputados;
        }
    }

    public class Nodo implements Comparable<Nodo> {
        private int votos;
        private int id;
        private int cociente;

        // Crear Constructor del nodo
        Nodo(int votos, int id) {
            this.votos = votos;
            this.id = id;
            this.cociente = 1;
        }

        @Override
        public int compareTo(Nodo otroNodo) {
            return this.votos - otroNodo.votos();
        }

        public int votos() {return this.votos;}
        public int id() {return this.id;}
        public int cociente() {return this.cociente;}
    }

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
        this.partidos = nombresPartidos;  // !REMINDER: Usar un metodo de copiado  de arrs para evitar alliasing
        this.distritos = nombresDistritos;
        this.diputadosPorDistrito = diputadosPorDistrito;
        this.ultimaMesaPorDistrito = ultimasMesasDistritos;
        // Java inicializa las arr de int con ceros y las arr de boolean con false.
        this.votosPresidenciales = new int[partidos.length];
        this.votosDiputados = new int[distritos.length][partidos.length];
        this.memoBancasPorDistrito = new int[distritos.length][partidos.length];
        this.memoEsValido = new boolean[distritos.length];
        this.votosTotalesPresidenciales = 0;
        this.primero = 0;
        this.segundo = 1;  // Supongo que hay al menos 2 partidos.

        // ME FALTA CREAR EL HEAPDIPUTADOS!!!
        
        //PriorityQueueAcotada<nodo>[] this.heapDiputados = new PriorityQueueAcotada[distritos.length];
        
        //for(int i=0; i < distritos.length; i++){
        //    for(int j=0; j < partidos.length; j++){

        //    }
        //}
        

        //PriorityQueueAcotada<Nodo> max_heap = new PriorityQueueAcotada<Nodo>(16);


        //heapDiputados = new PriorityQueueAcotada[5]; // Crea un array de PriorityQueueAcotada con longitud 5
        
        //for (int i = 0; i < 5; i++) {
            // Crea un nuevo nodo con votos 0 y un id creciente
        //    Nodo nuevoNodo = new Nodo(0, i + 1);
            
            // Inicializa la cola de prioridad y agrega el nodo
        //    PriorityQueueAcotada<Nodo> cola = new PriorityQueueAcotada<>(5);
        //    cola.encolar(nuevoNodo);
            
            // Asigna la cola de prioridad al array
        //    heapDiputados[i] = cola;
        //}


        //this.heapDiputados = new PriorityQueueAcotada<Nodo>[distrito.length];
    }

    public String nombrePartido(int idPartido) {
        return partidos[idPartido];
    }

    public String nombreDistrito(int idDistrito) {
        return distritos[idDistrito];
    }

    public int diputadosEnDisputa(int idDistrito) {
        return diputadosPorDistrito[idDistrito];
    }

    public String distritoDeMesa(int idMesa) {
        return distritos[indiceDistritoMesa(idMesa)];
    }

    private int indiceDistritoMesa(int idMesa) {
        return binarySearchEspecial(this.ultimaMesaPorDistrito, idMesa);
    }

    public int binarySearchEspecial(int[] arr, int elem){
        // Busca con busqueda binaria el elemento en la array, si lo encuentra devuelve el indice.
        // Si no lo encuentra, devuelve el indice del elemento mayor al buscado más cercano que pertenece a la array.
        // arr ordenada crecientemente.
        // Por el requiere sabemos que elem pertenece entre 0 y arr[arr.length - 1]
        // IMPORTANTE, NO MODULARIZAR ESTO, ES HORRIBLE
        int izq = -1;  // Todos los elementos a la izquierda de izq incluido son < al elemento
        int der = arr.length;  // Todos los elementos a la derecha de der incluido son >= al elemento
        while (der-izq > 1) {  // El rango activo es [izq+1 hasta der-1]
            int medio = (izq + der) / 2;
            if (arr[medio] < elem){
                izq = medio;
            } else {
                der = medio;
            }
        }
        if (arr[der] == elem){  // Notar que sin el requiere se rompe aca!
            return der + 1;
        } else {
            return der;
        }
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        int indiceDistritoMesa = indiceDistritoMesa(idMesa);
        for(int i=0; i < this.partidos.length; i++){  // por requiere sé que VotosPartido.length == this.partido.lenght
            this.votosPresidenciales[i] += actaMesa[i].votosPresidente();
            this.votosTotalesPresidenciales += actaMesa[i].votosPresidente();
            this.votosDiputados[indiceDistritoMesa][i] += actaMesa[i].votosDiputados();
        }
        actualizarPrimeroSegundo();
        memoEsValido[indiceDistritoMesa] = false;
        // FALTA: usar floyd para crear el heap del distrito.
    }

    private void actualizarPrimeroSegundo(){
        this.primero = 0;
        this.segundo = 1;
        for(int i=0; i < this.partidos.length; i++){
            if (votosPresidenciales[i] > votosPresidenciales[primero]){
                this.segundo = this.primero;
                this.primero = i;
            } else if (votosPresidenciales[i] > votosPresidenciales[segundo]){
                this.segundo = i;
            }
        }
    }

    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];
    }

    public int[] resultadosDiputados(int idDistrito){
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        return ballotageChecker(this.votosPresidenciales[this.primero], this.votosPresidenciales[this.segundo]) ? true : false;
    }

    private boolean ballotageChecker(int p, int s){
        if (porcentaje(p, this.votosTotalesPresidenciales) - porcentaje(s, this.votosTotalesPresidenciales) > 10 && porcentaje(p,this.votosTotalesPresidenciales) > 40){
            return true;
        } else if(porcentaje(p, this.votosTotalesPresidenciales) > 45){
            return true;
        } else{
            return false;
        }
    }

    private float porcentaje(int p, int total) {
        int res =  p/total * 100;
        return res;
    }

}

