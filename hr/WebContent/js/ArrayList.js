/**
use as ArrayList for java programmer
*/
function testArrayList(){
 
 alert("ArrayList test begin:");
 try{
  var list=new ArrayList();
  assert(list.isEmpty());
  assert(list.size()==0);

  list.add(new Integer(100));
  list.add(new Integer(200));
  list.add(new Integer(33));

  assert(!list.isEmpty());
  assert(list.size()==3);
 
  var list2=new ArrayList();
  list2.add(new Integer(32));
  list2.addAll(list);
  assert(list2.size()==4);
  list2.add(new Integer(200));
  
  
  
  assert(list2.indexOf(new Integer(200))==2);
  assert(list2.lastIndexOf(new Integer(200))==4);
  
  
  assert(list2.contains(new Integer(200)));
  
  list2.removeAll(list);
  assert(list2.size()==2);
  assert(list2.contains(new Integer(200)));
  
  assert(list2.get(0).equals(new Integer(32)));
  
  list2.addAll(list);
  list2.retainAll(list);
  assert(list2.size()==4);
  assert(list2.get(0).intValue()==200);
  assert(list2.get(1).intValue()==100);
  assert(list2.get(2).intValue()==200);
  assert(list2.get(3).intValue()==33);

  assert(!list.equals(list2));
  list2.remove(0);
  assert(list.equals(list2));
  list2.set(0,new Integer(200));
  assert(!list.equals(list2));
  list2.set(1,new Integer(100));
  assert(list.equals(list2));

  assert(list.containsAll(list2));
  list2.clear();
  assert(list2.isEmpty());
  assert(list2.size()==0);
  assert(list.containsAll(list2));
  
  assert(!list2.iterator().hasNext());
  var it=list.iterator();
  assert(it.hasNext());
  it.next();
  assert(it.hasNext());
  it.next();
  assert(it.hasNext());
  it.next();
  assert(!it.hasNext());

 }
 catch(e){
  alert(e);
 }
   
 alert("ArrayList test end");
}

function ArrayList(){
 this.buffer=new Array();
 var args=ArrayList.arguments;
 if(args.length>0) this.buffer=args[0];
 this.length=this.buffer.length;


 function ListIterator(table,len){

        this.table=table;
  this.len=len;                          
        this.index=0;
  
  this.hasNext=hasNext;
  function hasNext() {
   return this.index< this.len;
        }

        this.next=next;
  function next() { 
   if(!this.hasNext())
    throw "No such Element!";
      return this.table[this.index++];
        }
    }
 
 this.hashCode=hashCode;
 function hashCode(){
  var h=0;
  for(var i=0;i<this.lengh;i++)
   h+=this.buffer[i].hashCode();
  return h;
 }
 
 this.size=size;
 function size(){
  return this.length;
 }

 
 this.clear=clear;
 function clear(){
  this.length=0;
 }

 
 this.isEmpty=isEmpty;
 function isEmpty(){
  return this.length==0;
 }
 
 
 this.toArray=toArray;
 function toArray(){
  var copy=new Array();
  for(var i=0;i<this.length;i++){
   copy[i]=this.buffer[i];
  }
  return copy;
 }

 this.get=get;
 function get(index){
  if(index>=0 && index<this.length)
   return this.buffer[index];
  return null;
 }

 
 this.remove=remove;
 function remove(param){
  var index=0;
  
  if(isNaN(param)){
   index=this.indexOf(param);
  }
  else index=param;
  
  
  if(index>=0 && index<this.length){
   for(var i=index;i<this.length-1;i++)
    this.buffer[i]=this.buffer[i+1];
   this.length-=1;
   return true;
  }
  else return false;
 }
 
 this.add=add;
 function add(){
  var args=add.arguments;
  if(args.length==1){
   this.buffer[this.length++]=args[0];
   return true;
  }
  else if(args.length==2){
   var index=args[0];
   var obj=args[1];
   if(index>=0 && index<=this.length){
    for(var i=this.length;i>index;i--)
     this.buffer[i]=this.buffer[i-1];
     this.buffer[i]=obj;
    this.length+=1;
    return true;
   }
  }
  return false;
 }

 this.indexOf=indexOf;
 function indexOf(obj){
  for(var i=0;i<this.length;i++){
   if(this.buffer[i] == obj) return i;
  }
  return -1;
 }

 
 this.lastIndexOf=lastIndexOf;
 function lastIndexOf(obj){
  for(var i=this.length-1;i>=0;i--){
   if(this.buffer[i] == obj) return i;
  }
  return -1;
 }

 this.contains=contains;
 function contains(obj){
  return this.indexOf(obj)!=-1;
 }

 this.equals=equals;
 function equals(obj){
  if(this.size()!=obj.size()) return false;
  for(var i=0;i<this.length;i++){
   if(!obj.contains(this.buffer[i])) return false;
  }
  return true;
 }


 this.addAll=addAll;
 function addAll(list){
  var mod=false;
  for(var it=list.iterator();it.hasNext();){
   var v=it.next();
   if(this.add(v)) mod=true;
  }
  return mod;  
 }
 
 this.containsAll=containsAll;
 function containsAll(list){
  for(var i=0;i<list.size();i++){
   if(!this.contains(list.get(i))) return false;
  }
  return true;
 }

 this.removeAll=removeAll;
 function removeAll(list){
  for(var i=0;i<list.size();i++){
   this.remove(this.indexOf(list.get(i)));
  }
 }
 
 
 this.retainAll=retainAll;
 function retainAll(list){
  for(var i=this.length-1;i>=0;i--){
   if(!list.contains(this.buffer[i])){
    this.remove(i);
   }
  }
 }

 this.subList=subList;
 function subList(begin,end){
  if(begin<0) begin=0;
  if(end>this.length) end=this.length;
  var newsize=end-begin;
  var newbuffer=new Array();
  for(var i=0;i<newsize;i++){
   newbuffer[i]=this.buffer[begin+i];
  }
  return new ArrayList(newbuffer);
 }
 
 this.set=set;
 function set(index,obj){
  if(index>=0 && index<this.length){
   temp=this.buffer[index];
   this.buffer[index]=obj;
   return temp;
  }
 }

 this.iterator=iterator;
 function iterator(){
  return new ListIterator(this.buffer,this.length);
 }
 
}
