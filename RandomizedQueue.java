/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int size;


    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        size = 0;
    }                 // construct an empty randomized queue


    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }                 // is the randomized queue empty?

    public int size() {
        return size;
    }                       // return the number of items on the randomized queue

    public void enqueue(Item item) {
        if (item == null) throw
                new java.lang.IllegalArgumentException();
        if (size == a.length) resize(2 * a.length);
        a[size++] = item;
    }           // add the item


    public Item dequeue() {
        if (isEmpty()) throw
                new java.util.NoSuchElementException();
        int randomIndex = StdRandom.uniform(size);
        Item item = a[randomIndex];
        a[randomIndex] = null;
        size--;
        if (size == 0) a = (Item[]) new Object[2];
        else {
            for (int i = randomIndex; i < size; i++) {
                a[i] = a[i + 1];
            }
            a[size] = null;
        }
        return item;
    }                   // remove and return a random item

    public Item sample() {
        if (isEmpty()) throw
                new java.util.NoSuchElementException();
        int randomIndex = StdRandom.uniform(size);
        Item item = a[randomIndex];
        return item;

    }                      // return a random item (but do not remove it)

    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator<Item> implements Iterator<Item> {
        private int i;

        public RandomQueueIterator() {
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i < size;

        }

        @Override
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return (Item) a[i++];
        }

        @Override
        public void remove() {
            throw
                    new java.lang.UnsupportedOperationException("remove() method is not supported");
        }
    } // return an independent iterator over items in random order

    public static void main(String[] args) {
        RandomizedQueue<String> stack = new RandomizedQueue<>();
        for (int i = 0; i < args.length; i++) {
            String item = args[i];
            if (!item.equals("-"))
                stack.enqueue(item);
            else if (!stack.isEmpty())
                StdOut.print(stack.dequeue() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }     // unit testing (optional)
}
