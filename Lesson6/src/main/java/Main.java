import javax.sound.midi.Soundbank;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // задание 1
        System.out.println(Arrays.toString(getArray(new int[]{1, 2, 3, 1, 1, 1, 1, 0})));

        // задание 2
        System.out.println(isFourAndOne(new int[]{1,1,1,4,4,1}));
    }

    public static int[] getArray(int[] arr) throws RuntimeException {
        int four = -1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                four = i;
                break;
            }
        }
        if (four == -1) {
            throw new RuntimeException("Нет четверок в массиве");
        } else {
            return Arrays.copyOfRange(arr, four+1, arr.length);
        }
    }

    public static boolean isFourAndOne(int[] arr) {
        boolean one = false;
        boolean four = false;
        for (int j : arr) {
            switch (j) {
                case 1: {
                    one = true;
                    break;
                }
                case 4: {
                    four = true;
                    break;
                }
                default: {
                    return false;
                }
            }
        }
        return (one && four);
    }
}
