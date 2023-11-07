package aed;

/*
 * El tp consiste en dise;ar modulos para al Camara nacional Electoral.
 * P: Cantidad de partidos politicos
 * D: Cantidad de distritos
 * D_d: Cantidad de bancas de diputados en el distrito d. Cada distrito d puede
 * tener una cantidad D_d distinta.
 * 
 */

/*
 * 
 * Necesitamos modularizar. Asi que descompongamos la operaciones a implementar:
 * nuevoSistema O(P*D)
 * nombrePartido (a partir de id devuelve nombre) O(1)
 * nombreDistrito O(1)
 * diputadosEnDisputa (cantidad de bancas de diputados en disputa en un distrito
 * dado) O(1)
 * distritoDeMesa: Nombre de distrito al que pertenece una mesa O(Log(D))
 * registrarMesa: Votos de una mesa O(P+log(D))
 * votosPresidenciales: Devuelve la cantidad de votos que sobre su candidato a
 * presidente O(1)
 * resultadosDiputados: Cantidad de bancas de un distrito dado d. O(D_d *
 * log(P))
 * hayBallotage: Devuelve true si hay ballotage, false en caso contrario O(1)
 * 
 */
public class SistemaCNE {
    // Completar atributos privados

    String[] partidos;  // CHECK
    String[] distritos;  // CHECK
    int[] bancasPorDistrito; // Bancas por distrito segun id del dist.  // NO, LA MEMO
    int[] mesas;  // INNCESARIO
    int[][] rangoMesas; // Tenemos la lista de mesas por un lado y por otro el rango de id de las
                        // mismas marcadas por la id del distrito.  // REMPLAZADA CON ULTIMAMESAPORDISTRITO.
    Boolean[] mesasRegistradas;  // INNESARIO

    int[] votosPresidenciales; // Me interesa a nivel nacional  // CHECK
    int[][] votosDiputados; // Ordenado como idDistrito, idPartido  // CHECK

    int[] ganadores = new int[2];  // LO REMPLACE CON PRIMERO, SEGUNDO COPADO
    int totalVotos;  // CHECK
    

    public class VotosPartido {
        private int presidente;
        private int diputados;

        VotosPartido(int presidente, int diputados) {
            this.presidente = presidente;
            this.diputados = diputados;
        }

        public int votosPresidente() {
            return presidente;
        }

        public int votosDiputados() {
            return diputados;
        }
    }

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos,
            int[] ultimasMesasDistritos) {

        /*
         * Por contrato:
         * - No hay nombres repetidos ni de partidos ni de distritos
         * - La misma cantidad de distritos que de diputados por distrito
         * - Primeras mesas distritos estrictamente creciente? no seria
         * ultimasMesasDistritos?
         * - Ultimo lugar en nombres de partidos = "Blanco"
         * 
         * En asegura:
         * - Tama;o de nombres partidos -> nombresPartidos, votospresidenciales
         * - Tama;o de nombres distritos -> nombresDistritos, diputados de distritos,
         * rango mesas distritos
         * 
         */

        partidos = nombresPartidos;
        distritos = nombresDistritos;

        bancasPorDistrito = diputadosPorDistrito;

        mesas = new int[ultimasMesasDistritos[ultimasMesasDistritos.length - 1]]; //
        rangoMesas = new int[distritos.length][2]; //

        for (int i = 0; i < ultimasMesasDistritos.length; i++) { // O(D)
            if (i == 0) {
                rangoMesas[i][0] = 0;
                rangoMesas[i][1] = ultimasMesasDistritos[0]; //
            } else {
                rangoMesas[i][0] = ultimasMesasDistritos[i - 1];
                rangoMesas[i][1] = ultimasMesasDistritos[i];
            }

        }
        mesasRegistradas = new Boolean[mesas.length]; // Por default en falso

        votosPresidenciales = new int[partidos.length];
        votosDiputados = new int[distritos.length][partidos.length]; //
        totalVotos = 0;
        for (int i = 0; i < partidos.length; i++) { // Esta es la operacion que le da O(P*D), tengo que inicializar todo
                                                    // en 0
            votosPresidenciales[i] = 0;
            for (int j = 0; j < distritos.length; j++) {
                votosDiputados[j][i] = 0;
            }
        }

    }

    public String nombrePartido(int idPartido) {
        return partidos[idPartido]; // O(1) Va a la posicion de memoria + indexacion
    }

    public String nombreDistrito(int idDistrito) {
        return distritos[idDistrito]; // O(1) Va a la posicion de memoria + indexacion
    }

    public int diputadosEnDisputa(int idDistrito) {
        return bancasPorDistrito[idDistrito]; // O(1) Va a la posicion de memoria + indexacion
    }

    public String distritoDeMesa(int idMesa) {
        /*
         * Aca necesitamos una complejidad de O(log(D))
         * Nos dan el id de la mesa y nosotros tenemos guardado el rango en rangoMesas
         * Podemos hacer busqueda binaria.
         */
        return distritos[indiceDistritoMesa(idMesa)];
    }

    private int indiceDistritoMesa(int idMesa) {
        int left = 0;
        int right = rangoMesas.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (rangoMesas[mid][0] <= idMesa && idMesa < rangoMesas[mid][1]) {
                return mid;
            } else if (idMesa < rangoMesas[mid][0]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        throw new UnsupportedOperationException("No deberia llegar aca :("); // En teoria si se cumple la especificacion no deberia llegar aca, estaria bueno
                       // hacer un buen error handling
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        /*
         * O(P + log(D))
         * Requiere nos da por contrato que:
         * - El id distrito esta en el rango de las mesas
         * - el largo de los resultados es igual a cant de partidos
         * - la mesa no esta aun registrada (mesasRegistradas[idMesa] == false)
         * 
         * Ahora tenemos que :
         * - No cambia:
         * ! partidos ! distritos ! bancasPorDistrito ! rangoMesas
         * - Por cada idPartido votosPresidenciales += resultado[idPartido].presidente
         * (mas detallado en enunciado)
         * - Por cada idDistrito in votosDiputados, hay idPartido (pasa por toda la
         * matriz), en donde si distritoMesa == idDistrito se += los votos
         * - Importante marcar que la mesa fue registrada, en nuestra implementacion
         * marcando el True en array de bools
         * 
         */
        
        
        int distMesa = indiceDistritoMesa(idMesa); // O(log(D))
        for (int i = 0; i < actaMesa.length; i++) { // O(P)
            votosDiputados[distMesa][i] += actaMesa[i].votosDiputados();
            votosPresidenciales[i] += actaMesa[i].votosPresidente();
        }
        for(int i = 0; i< partidos.length-1; i++){
            totalVotos += votosPresidenciales[i];
            if(votosPresidenciales[i] >= ganadores[0]){
                ganadores[1] = ganadores[0];
                ganadores[0] = votosPresidenciales[i];
            } else if (votosPresidenciales[i] >= ganadores[1]){
                ganadores[1] = votosPresidenciales[i];
            }
            
        }

        mesasRegistradas[idMesa] = true; // Complejidad O(P+log(D))
    }

    public int votosPresidenciales(int idPartido) {
        return votosPresidenciales[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados[idDistrito][idPartido];
    }

    public int[] resultadosDiputados(int idDistrito) { //O(D_d*log(D))
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage() { // O(1)
        return ballotageChecker(ganadores[0], ganadores[1]) ? true : false;
    }

    private boolean ballotageChecker(int p, int s){
        if (porcentaje(p, totalVotos) - porcentaje(s, totalVotos) > 10 && porcentaje(p,totalVotos) > 40){
            return true;
        } else if(porcentaje(p, totalVotos) > 45){
            return true;
        } else{
            return false;
        }
    }

    private int porcentaje(int p, int total) {
        int res =  p/total * 100;
        return res;
    }



    private class maxHeapAcotada {
        Nodo [] heap;
        int cota;
        int puntero;

        class Nodo {
            
            int partido;
            int valor;
            int div;
            int index;
            

            public Nodo(int n, int p){
                partido = p;
                valor = n;
                div = 1;
                index = -1; // CHECKEAR CUANDO ESTE EL FLOYD
            }
        }

        public maxHeapAcotada(int[] arr){
            heap = new Nodo[arr.length];
            for (int i = 0; i < arr.length; i++){
                Nodo curr = new Nodo(arr[i], i);
                curr.partido = i;
                heap[i] = curr;
            }       

            array2Heap(heap);
        }
        public void array2Heap(Nodo[] arreglo){
            this.heap = arreglo;
            this.puntero = arreglo.length;
            this.cota =arreglo.length;
            for(int i=this.puntero-1; i>0; i--){
                bajarElemento(heap[i]);
            }
        }


        public Nodo leftNode(Nodo n){
            return heap[n.index *2 + 1];
        }


        public Nodo rightNode(Nodo n){
            return heap[n.index *2 + 2];
        }

        public Nodo fatherNode(Nodo n){
            int father = (n.index-1)/2; // CHECKEAR 
            return heap[father];
        }

        public boolean hasLeft(Nodo n){
            return (n.index*2) +1 < heap.length;
        }
        public boolean hasRight(Nodo n){
            return (n.index*2) +2 > heap.length;
        }
        public boolean hasFather(Nodo n){
            return (n.index*2) +2 < heap.length;
        }

        public void desencolar(){
            heap[0].valor = heap[heap.length -1].valor;
            puntero--;
            heap[heap.length -1] = null; // Lo que tomemos como nulo 
            bajarElemento(heap[0]);
        }

        private void bajarElemento(Nodo n){
            if(hasRight(n)){ // Como el arbol es izquierdista sabemos que si tiene right tiene left
                if (n.valor < rightNode(n).valor && (leftNode(n).valor < rightNode(n).valor)) {
                    swap(n, rightNode(n));
                    bajarElemento(rightNode(n)); // continua recursion por der
                }
            } else if(hasLeft(n)){ // En caso que solo tenga left
                if(n.valor < leftNode(n).valor){
                swap(n, leftNode(n));
                    bajarElemento(leftNode(n)); // Continua recursion por izq
                }
            }
        }

        private void swap (Nodo x, Nodo y){
            int temp = x.valor;
            x.valor = y.valor;
            y.valor = temp;
        }

        public void encolar(Nodo n){
            if (puntero > cota -1){
                // agregar error handling
            }
            n.index = puntero+1;
            heap[puntero+1] = n;                
            subirElemento(n);
            puntero++;
        }

        private void subirElemento(Nodo n){
            if(hasFather(n)){
                if(fatherNode(n).valor < n.valor){
                    swap(n, fatherNode(n));
                    subirElemento(fatherNode(n));
                }
            }
        }
    }
}
