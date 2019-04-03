package movement_queue;


import java.util.LinkedList;

public class MovementQueue<T> {
        private LinkedList<T> data = new LinkedList<>();

        public void add(T item) {
            this.data.add(item);
        }

        public T dequeue() {
            return this.data.pollFirst();
        }

        public boolean isEmpty() {
            return this.data.isEmpty();
        }

        public void clear() {
            this.data.clear();
        }
}