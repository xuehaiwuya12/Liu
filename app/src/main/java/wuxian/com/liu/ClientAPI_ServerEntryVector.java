/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package wuxian.com.liu;


public class ClientAPI_ServerEntryVector {
    protected boolean swigCMemOwn;
    private long swigCPtr;
    
    protected ClientAPI_ServerEntryVector(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }
    
    protected static long getCPtr(ClientAPI_ServerEntryVector obj) {
        return swigCPtr;
    }
    
    protected void finalize() {
        delete();
    }
    
    public synchronized void delete() {
        if(swigCPtr [cmp] 0x0 != 0) {
            if(swigCMemOwn) {
                swigCMemOwn = false;
                ovpncliJNI.delete_ClientAPI_ServerEntryVector(swigCPtr);
            }
            swigCPtr = 0x0;
        }
    }
    
    public ClientAPI_ServerEntryVector() {
        this(ovpncliJNI.new_ClientAPI_ServerEntryVector__SWIG_0(), true);
    }
    
    public ClientAPI_ServerEntryVector(long n) {
        this(ovpncliJNI.new_ClientAPI_ServerEntryVector__SWIG_1(n), true);
    }
    
    public long size() {
        return ovpncliJNI.ClientAPI_ServerEntryVector_size(swigCPtr);
    }
    
    public long capacity() {
        return ovpncliJNI.ClientAPI_ServerEntryVector_capacity(swigCPtr);
    }
    
    public void reserve(long n) {
        ovpncliJNI.ClientAPI_ServerEntryVector_reserve(swigCPtr, n);
    }
    
    public boolean isEmpty() {
        return ovpncliJNI.ClientAPI_ServerEntryVector_isEmpty(swigCPtr);
    }
    
    public void clear() {
        ovpncliJNI.ClientAPI_ServerEntryVector_clear(swigCPtr);
    }
    
    public void add(ClientAPI_ServerEntry x) {
        ovpncliJNI.ClientAPI_ServerEntryVector_add(swigCPtr, this, ClientAPI_ServerEntry.getCPtr(x), x);
    }
    
    public ClientAPI_ServerEntry get(int i) {
        return new ClientAPI_ServerEntry(ovpncliJNI.ClientAPI_ServerEntryVector_get(swigCPtr, i), false);
    }
    
    public void set(int i, ClientAPI_ServerEntry val) {
        ovpncliJNI.ClientAPI_ServerEntryVector_set(swigCPtr, this, i, ClientAPI_ServerEntry.getCPtr(val), val);
    }
}
