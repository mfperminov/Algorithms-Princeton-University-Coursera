/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private Node sentinelBegin;
    private Node sentinelEnd;
    private int n;

    public Deque() {
        sentinelBegin = new Node();
        sentinelEnd = new Node();
        n = 0;
    }                        // construct an empty deque

    public boolean isEmpty() {
        return n == 0;
    }                // is the deque empty?

    public int size() {
        return n;
    }                        // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException("Cannot add null "
                                                                 + "element!");
        Node newNode = new Node();
        newNode.item = item;
        if (n != 0) {
            Node oldFirst = sentinelBegin.next;
            oldFirst.prev = newNode;
            newNode.next = oldFirst;
            newNode.prev = sentinelBegin;
            sentinelBegin.next = newNode;
        }
        else {
            sentinelBegin.next = newNode;
            newNode.next = sentinelEnd;
            newNode.prev = sentinelBegin;
            sentinelEnd.prev = newNode;
        }
        n++;
    }          // add the item to the front

    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException("Cannot add null "
                                                                 + "element!");
        Node newNode = new Node();
        newNode.item = item;
        if (n != 0) {
            Node oldLast = sentinelEnd.prev;
            oldLast.next = newNode;
            newNode.prev = oldLast;
            newNode.next = sentinelEnd;
            sentinelEnd.prev = newNode;
        }
        else {
            sentinelEnd.prev = newNode;
            newNode.next = sentinelEnd;
            newNode.prev = sentinelBegin;
            sentinelBegin.next = newNode;
        }
        n++;
    }           // add the item to the end

    public Item removeFirst() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("Cannot remove from the empty deque");
        n--;
        if (n == 0) {
            Item item = sentinelBegin.next.item;
            sentinelBegin = new Node();
            sentinelEnd = new Node();
            return item;
        }
        else {
            Item item = sentinelBegin.next.item;
            sentinelBegin.next = sentinelBegin.next.next;
            sentinelBegin.next.prev = sentinelBegin;
            return item;
        }
    }                // remove and return the item from the front

    public Item removeLast() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("Cannot remove from the empty deque");
        n--;
        if (n == 0) {
            Item item = sentinelEnd.prev.item;
            sentinelBegin = new Node();
            sentinelEnd = new Node();
            return item;
        }
        else {
            Item item = sentinelEnd.prev.item;
            sentinelEnd.prev = sentinelEnd.prev.prev;
            sentinelEnd.prev.next = sentinelEnd;
            return item;
        }
    }                // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new DequeIterator();
    } // return an iterator over items in order from front to end

    private class DequeIterator implements Iterator<Item> {
        private Node current = sentinelBegin.next;

        @Override
        public boolean hasNext() {
            return !current.equals(sentinelEnd);
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException
                    ("remove() method is not supported");
        }
    }

    public static void main(String[] args) {
        Deque<String> stack = new Deque<>();
        for(int i = 0; i < args.length; i++) {
            String item = args[i];
            if (!item.equals("-"))
                stack.addFirst(item);
            else if (!stack.isEmpty())
                StdOut.print(stack.removeFirst() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }  // unit testing (optional)
}
