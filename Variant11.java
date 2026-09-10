import java.io.IOException;

public class Variant11 {

    private static final int TOTAL_STATES = 4;
    private static final int ALPHABET_CHARACTERS = 2;

    private enum State {
        q0, q1, q2, q3
    }

    private enum Input {
        _0, _1
    }

    private static final char[] ALPHABET = {'0', '1'};


    /*
     * Таблица переходов НКА.
     *
     * transitionTable[from][symbol][to]
     *
     * from   - из какого состояния
     * symbol - по какому символу
     * to     - в какое состояние
     *
     * true  - переход существует
     * false - переход отсутствует
     */
    private static final boolean[][][] transitionTable =
            new boolean[TOTAL_STATES]
                       [ALPHABET_CHARACTERS]
                       [TOTAL_STATES];


    /*
     * Допускающие состояния.
     */
    private static final boolean[] finalStates = {
            true,
            true,
            true,
            true
    };


    private static boolean[] currentStates =
            new boolean[TOTAL_STATES];


    /*
     * Заполнение таблицы переходов.
     *
     * Таблица соответствует автомату,
     * построенному в JFLAP.
     */
    private static void setTransitions() {

        // q0 по 0 -> q0 и q3
        transitionTable
                [State.q0.ordinal()]
                [Input._0.ordinal()]
                [State.q0.ordinal()] = true;

        transitionTable
                [State.q0.ordinal()]
                [Input._0.ordinal()]
                [State.q3.ordinal()] = true;


        // q0 по 1 -> q1 и q2
        transitionTable
                [State.q0.ordinal()]
                [Input._1.ordinal()]
                [State.q1.ordinal()] = true;

        transitionTable
                [State.q0.ordinal()]
                [Input._1.ordinal()]
                [State.q2.ordinal()] = true;


        // q1 по 0 -> q0 и q3
        transitionTable
                [State.q1.ordinal()]
                [Input._0.ordinal()]
                [State.q0.ordinal()] = true;

        transitionTable
                [State.q1.ordinal()]
                [Input._0.ordinal()]
                [State.q3.ordinal()] = true;


        // q1 по 1 -> q2
        transitionTable
                [State.q1.ordinal()]
                [Input._1.ordinal()]
                [State.q2.ordinal()] = true;


        // q2 по 0 -> q3
        transitionTable
                [State.q2.ordinal()]
                [Input._0.ordinal()]
                [State.q3.ordinal()] = true;


        // q2 по 1 -> q2
        transitionTable
                [State.q2.ordinal()]
                [Input._1.ordinal()]
                [State.q2.ordinal()] = true;


        // q3 по 1 -> q2
        transitionTable
                [State.q3.ordinal()]
                [Input._1.ordinal()]
                [State.q2.ordinal()] = true;
    }


    /*
     * Возвращает автомат
     * в начальное состояние.
     */
    private static void reset() {

        for (int i = 0; i < TOTAL_STATES; i++) {
            currentStates[i] = false;
        }

        currentStates[State.q0.ordinal()] = true;
    }


    /*
     * Определяем индекс входного символа.
     *
     * 0 -> индекс 0
     * 1 -> индекс 1
     *
     * Если символ не принадлежит алфавиту,
     * возвращаем -1.
     */
    private static int getSymbolIndex(char symbol) {

        for (int i = 0; i < ALPHABET_CHARACTERS; i++) {

            if (symbol == ALPHABET[i]) {
                return i;
            }
        }

        return -1;
    }


    /*
     * Обработка одного входного символа.
     */
    private static boolean processSymbol(char symbol) {

        // Определяем номер символа в алфавите
        int symbolIndex = getSymbolIndex(symbol);

        // Символ не принадлежит {0, 1}
        if (symbolIndex == -1) {
            return false;
        }


        /*
         * Здесь будут храниться состояния,
         * в которые можно попасть после
         * обработки текущего символа.
         */
        boolean[] nextStates =
                new boolean[TOTAL_STATES];


        /*
         * Перебираем все возможные
         * текущие состояния.
         */
        for (int from = 0; from < TOTAL_STATES; from++) {

            if (!currentStates[from]) {
                continue;
            }


            /*
             * Проверяем все возможные
             * переходы из активного состояния.
             */
            for (int to = 0; to < TOTAL_STATES; to++) {

                if (transitionTable[from][symbolIndex][to]) {

                    nextStates[to] = true;
                }
            }
        }


        /*
         * После обработки символа
         * новый набор состояний
         * становится текущим.
         */
        currentStates = nextStates;

        return true;
    }


    /*
     * Проверка: принимает ли НКА цепочку.
     *
     * Для НКА достаточно, чтобы
     * хотя бы одно активное состояние
     * было допускающим.
     */
    private static boolean isAccepted() {

        for (int i = 0; i < TOTAL_STATES; i++) {

            if (currentStates[i] && finalStates[i]) {
                return true;
            }
        }

        return false;
    }


    public static void main(String[] args) throws IOException {

        setTransitions();

        reset();


        System.out.println("Введите цепочку из 0 и 1:");


        int code;

        boolean correctInput = true;


        /*
         * Считываем ввод ПО ОДНОМУ СИМВОЛУ.
         */
        while ((code = System.in.read()) != -1) {

            char symbol = (char) code;

            if (symbol == '\n' || symbol == '\r') {
                break;
            }

            if (!processSymbol(symbol)) {

                correctInput = false;
                break;
            }
        }


        // Вывод результата
        if (!correctInput) {

            System.out.println(
                    "Reject (допустимы только символы 0 и 1)"
            );

        } else if (isAccepted()) {

            System.out.println("Accept");

        } else {

            System.out.println("Reject");
        }
    }
}