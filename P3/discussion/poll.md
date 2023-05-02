```java
/*@ public normal_behavior
  @ requires (Queue.size() > 0);
  @ \assignable nothing
  @ ensures \result == Queue.remove();
  @ also
  @ public normal_behavior
  @ requires (Queue.size() == 0);
  @ \assignable nothing
  @ ensures \result == null;
  @*/
public E poll();
```

```java
/*@ public normal_behavior
  @ requires queue.length > 0;
  @ ensures (\forall int i; 0 <= i && i < queue.length; queue[i] == \old(queue[i]));
  @ ensures \result == queue[0];
  @
  @ also
  @ public normal_behavior
  @ requires queue.length == 0;
  @ ensures \result == null;
  @*/
public /*@ pure @*/ E peek();
```





```java
/*@ public normal_behavior
  @ requires \old(queue.length) < capacity;
  @ assignable queue;
  @ ensures queue.length == \old(queue.length) + 1;
  @ ensures (\forall int i; 0 <= i && i < \old(queue.length);
  @          (\exists int j; 0 <= j && j < queue.length; queue[j].equals(\old(messages[i]))));
  @ ensures (\exists int i; 0 <= i && i < queue.length; queue[i].equals(e));
  @ also
  @ public exceptional_behavior
  @ signals (IllegalStateException e) (\old(queue.length) >= capacity);
 */
public void add(E e);
```



```java
/*@ public normal_behavior
    @ requires queue.length > 0;
    @ assignable queue;
	@ ensures (\forall int i; 0 <= i && i < queue.length; queue[i] == \old(queue[i + 1]));
	@ ensures !(\exists int i; 0 <= i && i < queue.length; queue[i] == \old(queue[0]));
	@ ensures \old(queue.length) == queue.length + 1;
	@ public exceptional_behavior
	@ signals (NoSuchElementException e) (queue.length == 0);
 @*/
 Queue.remove();
```

