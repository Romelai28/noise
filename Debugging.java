package aed;

class Debugging {
    boolean xor(boolean a, boolean b) {
        return ((a || b) && !(a && b));
    }

    boolean iguales(int[] xs, int[] ys) {
        boolean res = true;

        if (xs.length != ys.length) {
            return false;  // Al poner return false en vez de cambiar el valor de res, se evita que se recorra la array en el for loop
        }

        for (int i = 0; i < xs.length; i++) {
            if (xs[i] != ys[i]) {
                res = false;
            }
        }
        return res;
    }

    boolean ordenado(int[] xs) {
        boolean res = true;
        for (int i = 0; i < xs.length - 1; i++) {
            if (xs[i] > xs [i+1]) {
                res = false;
            }
        }
        return res;
    }

    int maximo(int[] xs) {
        int res = xs[0];
        for (int i = 1; i < xs.length; i++) {
            if (xs[i] > res) res = xs[i];
        }
        return res;
    }

    boolean todosPositivos(int[] xs) {
        boolean res = true;
        for (int x : xs) {
            if (x <= 0) {  // Si x es no positivo, res es falso. (Podría evitar que siga recorriendo la array al poner return false en el if)
                res = false;
            }
        }
        return res;
    }
}
