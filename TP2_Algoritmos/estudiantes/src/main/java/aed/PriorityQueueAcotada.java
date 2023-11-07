package aed;

public class PriorityQueueAcotada<T extends Comparable<T>> {
    private T[] arr;
    private int tamaño;  // Tamaño actual.  Apunta a la primera posición "vacia"
    private int capacidad;  // Capacidad máxima.  Requiere > 0.

    public PriorityQueueAcotada(int capacidad) {
        this.capacidad = capacidad;
        this.tamaño = 0;
        this.arr = (T[]) new Comparable[capacidad];
    }

    public T[] arr() {return this.arr;}

    public int tamaño() {return this.tamaño;}

    public int capacidad() {return this.capacidad;}

    public int hijo_izq(int i) {return 2*i;}

    public int hijo_der(int i) {return 2*i+1;}

    public int padre(int i) {return  i/2;}  // Devuelve el piso de la división.

    public void encolar(T elem){
        this.arr[tamaño] = elem;
        int indice_actual = tamaño;
        this.tamaño += 1;
        subir(indice_actual);
    }

    private void subir(int indice_actual){
        while (indice_actual != 0 && arr[indice_actual].compareTo(arr[padre(indice_actual)]) > 0){
            int indice_padre = padre(indice_actual);
            swap(indice_actual, indice_padre);
            indice_actual = indice_padre;
        }
    }

    private void swap(int i, int j){
        T temporal = arr[j];
        arr[j] = arr[i];
        arr[i] = temporal;
    }

    public T mirarMaximo(){
        // Requiere tamaño != 0;
        return arr[0];
    }

    public T desencolar(){
        T maximo = mirarMaximo();
        swap(0, tamaño-1);
        this.tamaño -= 1;
        bajar(0);
        return maximo;
    }

    private void bajar(int i){
        while (!esHoja(i) && tieneAlgunHijoMayor(i)){
            int indiceHijoMayor = hijoMayor(i);
            swap(i, indiceHijoMayor);
            i = indiceHijoMayor;
        }
    }

    private boolean esHoja(int i){
        return hijo_izq(i) >= this.tamaño;  // Como el heap es izquierdista esto es equivalente a ser una hoja.
    }

    private boolean tieneHijoDerecho(int i){
        return hijo_der(i) < this.tamaño;
    }

    private boolean tieneAlgunHijoMayor(int i){
        return ((arr[i].compareTo(arr[hijo_izq(i)])) < 0 || (tieneHijoDerecho(i) && (arr[i].compareTo(arr[hijo_der(i)])) < 0));
    }

    private int hijoMayor(int i){
        if ((arr[i].compareTo(arr[hijo_izq(i)]) < 0 && !tieneHijoDerecho(i)) || (arr[hijo_izq(i)].compareTo(arr[hijo_der(i)])) > 0){
            return hijo_izq(i);
        } else {
            return hijo_der(i);
        }
    }

    public void array2Heap(T[] arreglo){
        this.arr = copiarArray(arreglo);
        this.tamaño = arreglo.length;
        for(int i=(this.tamaño/2)-1; i>=0; i--){
            bajar(i);
        }
    }


    private T[] copiarArray(T[] arr){
        T[] nueva_arr = (T[]) new Comparable[arr.length];
        for (int i=0; i<arr.length; i++){
            nueva_arr[i] = arr[i];
        }
        return nueva_arr;
    }
    
}