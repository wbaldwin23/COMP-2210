import java.util.Iterator;
import java.util.NoSuchElementException;
public class DoubleEndedListImp<T> implements DoubleEndedList<T> {
   private Node front;
   private int size;
   private Node end;

   private class Node {
      private T element;
      private Node next;
      private Node prev;
      
      public Node(T e) {
         element = e;
         next = null;
         prev = null;
      }
      
      public Node(T elem, Node nex) {
         element = elem;
         next = nex;
      }
      
      public Node(T elem, Node nex, Node pre) {
         element = elem;
         next = nex;
         prev = pre;
      }
     
      public int length(Node n) {
         Node p = n;
         int len = 0;
         while (p != null) {
            len++;
            p = p.next;
         }
         return len;
      } 
   }
   
   public DoubleEndedListImp() {
      front = null;
      size = 0;
      end = null;
   }
   
   public int size(){
      return size;
   }
   
   public boolean isEmpty() {
      return size == 0;
   }
   
   public void addFirst(T element) {
      if (element == null) {
         throw new IllegalArgumentException();
      }
      Node n = new Node(element);
      if (front == null) {
         front = n;
         end = front;
      }
      else {
         n.prev = end;
         end.next = n;
         end = n;
      }
      size++;  
   }
   
   public void addLast(T element) {
      if (element == null) {
         throw new IllegalArgumentException();
      }
      Node n = new Node(element);
      if (front == null) {
         front = n;
         end = front;
      }
      else {
         n.prev = end;
         end.next = n;
         end = n;
      }
      size++;
   }
   
   public T removeFirst() {
   
      if (isEmpty())
      {
         return null;
      }
      T removed = front.element; 
      if (size == 1) {
         front = null;
         end = null;
      }
      else {
         front = front.next;
         front.prev = null;
      }
      size--;
      return removed;
   }
   
   public T removeLast() {
      if (isEmpty()) {
         return null;
      }
      T removed = end.element; 
      if (size == 1) {
         front = null;
         end = null;
      }
      else {
         end = end.prev;
         end.next = null;
      }
      size--;
      return removed;
   }
   
   public Iterator<T> iterator() {
      return new LinkedIterator();
   }
   
   private class LinkedIterator implements Iterator<T> {
      private Node current = front;
      
      public boolean hasNext() {
         return current != null;
      }
      
      public T next() {
         if (!hasNext()) {
            throw new NoSuchElementException();
         }
         T result = current.element;
         current = current.next;
         return result;
      }
      
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
   
 
}