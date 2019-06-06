package group14.movement_queue;


import java.util.LinkedList;

public class MovementQueue<T> {
        private LinkedList<T> data = new LinkedList<>();

        void add(T item) {
            this.data.add(item);
        }

        T dequeue() {
            return this.data.pollFirst();
        }

        boolean isEmpty() {
            return this.data.isEmpty();
        }

        public void clear() {
            this.data.clear();
        }
}