import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;

public class RandomizedListImp<T> implements RandomizedList<T>{
   private T[] array;
   private int size;
   private static final int CAPACITY = 5;
   
   public RandomizedListImp() {
      this(CAPACITY);
   }
   
   
   public RandomizedListImp(int capacity) {
      array = (T[]) new Object[capacity];
      size = 0;
   }
   
   public int size() {
      return size;
   }
   
   public boolean isEmpty() {
      return size == 0;
   }
   
   public T remove() {
      if (this.isEmpty()) {
         return null;
      }
      Random ran = new Random();
      int value = ran.nextInt(size);
      T removed = array[value];
      array[value] = null;
      if (value != (size-1)) { 
         array[value] = array[size-1];
         array[size-1] = null;
      }
      size--;
      
      if (size > 0 && size < array.length / 4) {
         resize(array.length/2);
      }
      
      return removed;
   }
   
   public void add(T element) {
      if (element == null) {
         throw new IllegalArgumentException();
      }
      if (size == array.length) {
         resize(array.length*2); 
      }
      array[size] = element;
      size++;
   }
   
   
   public T sample() {
      if (this.isEmpty()) {
         return null;
      }
      Random ran = new Random();
      int value = ran.nextInt(size);
      return array[value];
   }
   
   private void resize(int capacity) {
      T[] a = (T[]) new Object[capacity];
      for (int i = 0; i < size(); i++) {
         a[i] = array[i];
      }
      array = a;
   }
   
   public Iterator<T> iterator() {
      return new ArrayIterator(array, size);
   }
   
   
   
   
   
   public class ArrayIterator<T> implements Iterator<T> {
      private T[] a;
      private int length;
      
      
      public ArrayIterator(T[] array, int sizeIn) {
         a = array;
         length = sizeIn; 
      }
   
      public boolean hasNext() {
         return (length > 0);
      }
      
      public void remove() {
         throw new UnsupportedOperationException();
      }
     
      public T next() {
         if (!hasNext()) {
            throw new NoSuchElementException();
         }
         Random rand = new Random();
         int value = rand.nextInt(length);
         T next = a[value]; 
         if (value != (length-1)) { 
            a[value] = a[length-1];
            a[length-1] = next;
         }
         length--;
         return next;
      }
   }
}