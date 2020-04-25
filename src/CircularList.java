/**
 *
 * Circular list
 *
 * An implementation of a circular list with  key and info
 *
 */
 
public class CircularList{
	int maxLen;
	int len;
	Item[] arr;
	int start;
	 
	/**
	 *  CircularList constructor 
	 */
	public CircularList (int maxLen){
		this.maxLen = maxLen;
		this.len = 0;
		this.arr = new Item[maxLen];
		this.start = 0;	
	}
	
	
	/**
	 * public Item retrieve(int i)
	 *
	 * returns the item in the ith position if it exists in the list.
	 * otherwise, returns null
	 * complexity: O(1)
	 */
	public Item retrieve(int i){
		if (i < 0 || i >= this.len) {
			return null;  
		}
		
		return this.arr[(this.start + i) % this.maxLen];
		}
	  
	
	/**
	 * public int insert(int i, int k, String s) 
	 *
	 * inserts an item to the ith position in list  with key k and  info s.
	 * returns -1 if i<0 or i>n  or n=maxLen otherwise return 0.
	 * complexity: O(min{i+1, n-i+1})
	 */
	public int insert(int i, int k, String s) {
		if (i < 0 || i > this.len || this.len == this.maxLen) {
			return -1;  
		}
		
		Item item = new Item(k, s);
		
		/* i closer to list's start, shift all items from start to i by -1 , update starting position*/ 
		if (this.len - i > i) {			
			for(int j = 0; j < i ; j++) {
				this.arr[(this.start + j - 1) % this.maxLen] = this.arr[(this.start + j) % this.maxLen];
				start = (start - 1) % this.maxLen;
			}
		}
		  
		/* i closer to list's end, shift all items from end down to i by 1*/
		else {							 
			for(int j = this.len - 1; j >= i ; j--) {
				this.arr[((this.start) + j + 1) % this.len] = this.arr[((this.start) + j) % this.len];
			} 
		}
		  
		/* increase list size by 1, insert item in place */
		this.len += 1;												
		this.arr[(this.start + i) % this.len] = item;	
		
		return 0;	
	}

   
	/**
	 * public int delete(int i)
	 *
	 * deletes an item in the ith posittion from the list.
	 * returns -1 if i<0 or i>n-1 otherwise returns 0.
	 * complexity: O(min{i+1, n-i+1})
	 */
	public int delete(int i){
		if (i < 0 || i >= this.len) {
			return -1;  
		}
		   
		/* i closer to list's start, shift all items from i down to start by 1, update starting position*/
		if (this.len - i > i) {										
			for(int j = (i - 1); j >= 0 ; j--) {
				this.arr[(this.start + j + 1) % this.len] = this.arr[(this.start + j) % this.len];
				this.start = ((this.start + 1) % this.len);
			}
		}
		   
		/* i closer to list's end, shift all items from i to end by -1 */
		else {														
			for(int j = (i + 1); j < this.len ; j++) {
				this.arr[(this.start + j - 1) % this.len] = this.arr[(this.start + j) % this.len]; 
			}
		}
		   
		/* decrease list size by 1 */
		this.len -= 1;												
		
		return 0;
		}
	}
