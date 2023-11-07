package aed;

import aed.PriorityQueueAcotada;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PriorityQueueAcotadaTests {

    @Test
    void nuevo_conjunto_vacio() {
        PriorityQueueAcotada<Integer> max_heap = new PriorityQueueAcotada<Integer>(16);

        assertEquals(0, max_heap.tamaño());
        assertEquals(16, max_heap.capacidad());
        
        //Integer[] arrayLoca = {5,4,3,8,9,10,4};
        //max_heap.array2Heap(arrayLoca);
        
        max_heap.encolar(10);
        max_heap.encolar(9);
        max_heap.encolar(4);
        max_heap.encolar(7);
        max_heap.encolar(12);

        assertEquals(5, max_heap.tamaño());
        assertEquals(16, max_heap.capacidad());

        assertEquals(12, max_heap.desencolar());
        assertEquals(10, max_heap.desencolar());
        assertEquals(9, max_heap.desencolar());
        assertEquals(7, max_heap.desencolar());
        assertEquals(4, max_heap.desencolar());

        assertEquals(0, max_heap.tamaño());
        assertEquals(16, max_heap.capacidad());

        Integer[] arrayLoca = {5,4,3,8,9,10,4};
        max_heap.array2Heap(arrayLoca);

        arrayLoca[0]=1;
        arrayLoca[1]=1;
        arrayLoca[2]=1;
        arrayLoca[3]=1;
        arrayLoca[4]=1;
        arrayLoca[5]=1;
        arrayLoca[6]=1;

        assertEquals(7, max_heap.tamaño());
        assertEquals(16, max_heap.capacidad());

        assertEquals(10, max_heap.desencolar());
        assertEquals(9, max_heap.desencolar());
        assertEquals(8, max_heap.desencolar());
    
    }

    @Test
    void pepe() {
        PriorityQueueAcotada<String> max_heap = new PriorityQueueAcotada<String>(8);

        assertEquals(0, max_heap.tamaño());
        assertEquals(8, max_heap.capacidad());
        
        //Integer[] arrayLoca = {5,4,3,8,9,10,4};
        //max_heap.array2Heap(arrayLoca);
        
        max_heap.encolar("a");
        max_heap.encolar("b");
        max_heap.encolar("z");
        max_heap.encolar("d");
        max_heap.encolar("c");

        assertEquals(5, max_heap.tamaño());
        assertEquals(8, max_heap.capacidad());

        assertEquals("z", max_heap.desencolar());
        assertEquals("d", max_heap.desencolar());
        assertEquals("c", max_heap.desencolar());
        assertEquals("b", max_heap.desencolar());
        assertEquals("a", max_heap.desencolar());

        assertEquals(0, max_heap.tamaño());
        assertEquals(8, max_heap.capacidad());

        String[] arrayLoca = {"A","B","F","G","Z","X","C"};
        max_heap.array2Heap(arrayLoca);

        assertEquals(7, max_heap.tamaño());
        assertEquals(8, max_heap.capacidad());

        assertEquals("Z", max_heap.desencolar());
        assertEquals("X", max_heap.desencolar());
        assertEquals("G", max_heap.desencolar());
    
    }


    @Test
    void testExtralol() {
        PriorityQueueAcotada<Integer> max_heap = new PriorityQueueAcotada<Integer>(6);

        assertEquals(0, max_heap.tamaño());
        assertEquals(6, max_heap.capacidad());
        
        //Integer[] arrayLoca = {5,4,3,8,9,10,4};
        //max_heap.array2Heap(arrayLoca);
        
        max_heap.encolar(1);
        max_heap.encolar(2);
        max_heap.encolar(4);
        max_heap.encolar(5);
        max_heap.encolar(3);

        assertEquals(5, max_heap.tamaño());
        assertEquals(6, max_heap.capacidad());

        assertEquals(5, max_heap.desencolar());
        max_heap.encolar(5);
        assertEquals(5, max_heap.desencolar());
        assertEquals(4, max_heap.desencolar());
        assertEquals(3, max_heap.desencolar());
        max_heap.encolar(7);
        max_heap.encolar(-2);
        assertEquals(7, max_heap.desencolar());
        assertEquals(2, max_heap.desencolar());
        assertEquals(1, max_heap.desencolar());
        assertEquals(-2, max_heap.desencolar());

        assertEquals(0, max_heap.tamaño());
        assertEquals(6, max_heap.capacidad());
    
    }

}